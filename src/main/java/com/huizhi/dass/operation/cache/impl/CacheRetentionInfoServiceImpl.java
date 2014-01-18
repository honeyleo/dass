package com.huizhi.dass.operation.cache.impl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.cache.RedisPool;
import com.huizhi.dass.common.util.JsonUtils;
import com.huizhi.dass.common.util.LogFileCallback;
import com.huizhi.dass.common.util.LogFileUtils;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.cache.RetentionInfoCache;

@Component
public class CacheRetentionInfoServiceImpl implements RetentionInfoCache {
	
	private static final String LOG_PATH = Constants.LOG_PATH;
	private static final String LOGIN_LOG_FLAG = Constants.LOGIN_LOG_FLAG;
	
	//指定日期的新增用户数据集合hash
	private static final String ORIGINAL_HASH_KEY = "ori.loginLog.hash";
	//指定日期的新增用户key set
	private static final String ORIGINAL_SET_KEY = "ori.loginLog.key";
	//当前日期的活跃用户key set
	private static final String TARGET_SET_KEY = "tar.loginLog.key";

	private static final String INTER_SET_KEY = "inter.loginLog.key";
	
	private JedisPool pool = RedisPool.getPool();
	LogFileUtils<LoginLog> logFileUtils = new LogFileUtils<LoginLog>(){};
	@Override
	public boolean insertOriginalLoginLogList(String date) {
		clearOriginalSetCache();
		List<String> paths = logFileUtils.traverseLogFile(LOG_PATH, date, LOGIN_LOG_FLAG);
		try {
			for (String path : paths) {
				System.out.println("== read file start: " + DateTime.now());
				logFileUtils.copyDataFromLogFile(logFileUtils, new File(path), new LogFileCallback<LoginLog, Boolean>() {
					@Override
					public Boolean callBack(List<LoginLog> list) {
//						boolean isAdd = insertIntoHash(list, ORIGINAL_HASH_KEY);
						return insertIntoSet(list, ORIGINAL_SET_KEY);
					}
				});
				System.out.println("== read file end: " + DateTime.now());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;  
	}

	@Override
	public boolean insertTargetLoginLogList(String date) {
		clearTargetSetCache();
		List<String> paths = logFileUtils.traverseLogFile(LOG_PATH, date, LOGIN_LOG_FLAG);
		try {
			for (String path : paths) {
				System.out.println("== read file start: " + DateTime.now());
				logFileUtils.copyDataFromLogFile(logFileUtils, new File(path), new LogFileCallback<LoginLog, Boolean>() {
					@Override
					public Boolean callBack(List<LoginLog> list) {
						return insertIntoSet(list, TARGET_SET_KEY);
					}
				});
				System.out.println("== read file end: " + DateTime.now());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;  
	}
	
	/**
	 * 把登陆日志信息写入指定Hash中
	 * redis:hash:key(key):field(query condition):value(loginLog json)
	 * @param loginLogList
	 * @param key
	 * @return
	 */
	private boolean insertIntoHash(List<LoginLog> loginLogList, String key) {
		boolean isAdd = false;
		Jedis jedis = pool.getResource();
		try {
			Map<String, String> hash = new HashMap<String, String>();
			for (LoginLog loginLog : loginLogList) {
				hash.put(buildKey(loginLog), JsonUtils.beanToJson(loginLog));
			}
			String isSet = jedis.hmset(key, hash);
			isAdd = "OK".equalsIgnoreCase(isSet);
		} finally {
			pool.returnResource(jedis);
		}
		
		return isAdd;
	}

	/**
	 * 把登陆日志信息key写入指定SET中
	 * redis:set:key(key):value(set)
	 * @param loginLogList
	 * @param key
	 * @return
	 */
	private boolean insertIntoSet(List<LoginLog> loginLogList, String key) {
		boolean isAdd = false;
		Jedis jedis = pool.getResource();
		try {
			Set<String> set = new HashSet<String>();
			for (LoginLog loginLog : loginLogList) {
				set.add(buildKey(loginLog));
			}
			long addCount = jedis.sadd(key, set.toArray(new String[0]));
			isAdd = addCount > 0;
		} finally {
			pool.returnResource(jedis);
		}
		
		return isAdd;
	}
	
	


	//获得交集key集合
	@Override
	public Set<String> checkLoginLogInCache() {
		Set<String> keys = new HashSet<String>();
		
		Jedis jedis = pool.getResource();
		try {
			long interKeyCount = jedis.sinterstore(INTER_SET_KEY, ORIGINAL_SET_KEY, TARGET_SET_KEY);
			keys = jedis.smembers(INTER_SET_KEY);
			
			System.out.println(INTER_SET_KEY + ":" + jedis.scard(INTER_SET_KEY));
			System.out.println(ORIGINAL_SET_KEY + ":" + jedis.scard(ORIGINAL_SET_KEY));
			System.out.println(TARGET_SET_KEY + ":" + jedis.scard(TARGET_SET_KEY));
			
		} finally {
			pool.returnResource(jedis);
		}
		
		return keys;
	}
	
	@Override
	public Set<String> queryOriLoginLogKeys() {
		Set<String> keys = new HashSet<String>();
		
		Jedis jedis = pool.getResource();
		try {
			keys = jedis.smembers(ORIGINAL_SET_KEY);
		} finally {
			pool.returnResource(jedis);
		}
		
		return keys;
	}

	/**
	 * 生成cache key || field
	 * @param loginLog
	 * @return
	 */
	private String buildKey(LoginLog loginLog) {
		String key = loginLog.getGameId() + "." + loginLog.getUserId() + "." + loginLog.getDeviceId();
		
		return key;
	}

	@Override
	public boolean clearCache() {
		boolean isClear = false;
		Jedis jedis = pool.getResource();
		try {
			long delCount = jedis.del(ORIGINAL_HASH_KEY, ORIGINAL_SET_KEY, TARGET_SET_KEY, INTER_SET_KEY);
			isClear = delCount > 0;
		} finally {
			pool.returnResource(jedis);
		}
		return isClear;
	}
	
	public boolean clearOriginalSetCache() {
		boolean isClear = false;
		Jedis jedis = pool.getResource();
		try {
			long delCount = jedis.del(ORIGINAL_SET_KEY);
			isClear = delCount > 0;
		} finally {
			pool.returnResource(jedis);
		}
		return isClear;
	}
	
	public boolean clearOriginalHashCache() {
		boolean isClear = false;
		Jedis jedis = pool.getResource();
		try {
			long delCount = jedis.del(ORIGINAL_HASH_KEY);
			isClear = delCount > 0;
		} finally {
			pool.returnResource(jedis);
		}
		return isClear;
	}
	
	public boolean clearTargetSetCache() {
		boolean isClear = false;
		Jedis jedis = pool.getResource();
		try {
			long delCount = jedis.del(TARGET_SET_KEY);
			isClear = delCount > 0;
		} finally {
			pool.returnResource(jedis);
		}
		return isClear;
	}

}
