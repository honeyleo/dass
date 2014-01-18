package com.huizhi.dass.common.cache;

import java.util.ResourceBundle;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
	
	private static ResourceBundle bundle;
	private static JedisPoolConfig config;
	private static JedisPool pool;
	
	static {
		bundle = ResourceBundle.getBundle("spring/redis-access");
		if (bundle == null) {
			throw new IllegalArgumentException("spring/redis-pool.properties is not found.");
		}
		config = new JedisPoolConfig();
		//最大分配的对象数
		config.setMaxActive(Integer.valueOf(bundle.getString("redis.pool.maxActive")));  
		//最大能够保持idel状态的对象数
	    config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));  
	    //当池内没有返回对象时，最大等待时间
	    config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")));  
	    //#当调用borrow Object方法时，是否进行有效性检查
	    //当maxActive到达最大数，获取连接时的操作  0.抛异常 1.阻塞等待 2.创建新的（maxActive将失效）
	    config.setWhenExhaustedAction(Byte.valueOf(bundle.getString("redis.pool.whenExhaustedAction")));
	    //#在获取连接时，是否验证有效性
	    config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
	    //#在归还连接时，是否验证有效性
	    config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn"))); 
	    //#设定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。过期的对象将被回收。如果这个值不是正数，那么对休眠时间没有特别的约束。
	    config.setMinEvictableIdleTimeMillis(Long.valueOf(bundle.getString("redis.pool.minEvictableIdleTimeMillis")));
	    //#每次检查的连接数
	    config.setNumTestsPerEvictionRun(Integer.valueOf(bundle.getString("redis.pool.numTestsPerEvictionRun")));
	    //#设定间隔没过多少毫秒进行一次后台连接清理的行动
	    config.setTimeBetweenEvictionRunsMillis(Long.valueOf(bundle.getString("redis.pool.timeBetweenEvictionRunsMillis")));
	    //#当连接空闲时，是否验证有效性
	    config.setTestWhileIdle(Boolean.valueOf(bundle.getString("redis.pool.testWhileIdle")));
	    
	    pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port"))); 
		
	}

	private RedisPool() {
	}
	
	private static class InstanceHolder {
		private static final RedisPool POOL = new RedisPool();
	}
	
	public static JedisPool getPool() {
		return InstanceHolder.POOL.pool;
	}
	
	
	
	
}
