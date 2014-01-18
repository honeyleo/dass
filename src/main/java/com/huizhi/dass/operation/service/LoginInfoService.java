package com.huizhi.dass.operation.service;

import com.huizhi.dass.model.operation.LoginLog;

public interface LoginInfoService {
	
	/**
	 * 接口调用 新增登陆日志 (即时新增数据)
	 * @param log
	 * @return
	 */
	public boolean addLoginLog(LoginLog log);
	
	
	/**
	 * 从文件中读取登陆日志，写入数据库
	 * @return
	 */
//	public boolean addLoginLogToDBFromLog();
	


}
