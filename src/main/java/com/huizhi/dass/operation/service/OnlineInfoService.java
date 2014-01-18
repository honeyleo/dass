package com.huizhi.dass.operation.service;

import com.huizhi.dass.model.operation.LoginLog;

public interface OnlineInfoService {
	
	
	/**
	 * 新增所有在线时长信息
	 * @param date
	 * @return
	 */
	public boolean addAllOnlineInfo(String date);
	
	/**
	 * 根据退出日志查询此玩家最近登陆日志
	 * @param logoutLog
	 * @return
	 */
	public LoginLog queryLastLoginLog(LoginLog logoutLog);
	
	
	/**
	 * 新增在线信息数据
	 * @param info
	 * @return
	 */
	public boolean addOnlineInfo(LoginLog logoutLog);

}
