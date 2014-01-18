package com.huizhi.dass.operation.cache;

import java.util.List;
import java.util.Set;

import com.huizhi.dass.model.operation.LoginLog;

public interface RetentionInfoCache {
	
	/**
	 * 向redis中批量插入新增用户数据
	 * @param loginLogList
	 * @return
	 */
	public boolean insertOriginalLoginLogList(String date);

	/**
	 * 向redis中批量插入活跃用户数据
	 * @param date
	 * @return
	 */
	public boolean insertTargetLoginLogList(String date);
	
	/**
	 * 验证缓存中是否有符合条件的数据
	 * 留存日期注册的用户在当前日期仍活跃
	 * @param loginLog
	 * @return 交集Key
	 */
	public Set<String> checkLoginLogInCache();

	/**
	 * 验证缓存中是否有符合条件的数据
	 * 留存日期注册的用户在当前日期仍活跃
	 * @param loginLog
	 * @return 注册集Key
	 */
	public Set<String> queryOriLoginLogKeys();
	
	/**
	 * 根据key的前缀清空cache
	 * @param perfix
	 * @return
	 */
	public boolean clearCache();
	

}
