package com.huizhi.dass.operation.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huizhi.dass.common.util.DateUtils;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.model.operation.OnlineInfo;
import com.huizhi.dass.model.type.LoginLogType;
import com.huizhi.dass.operation.dao.LoginLogDAO;
import com.huizhi.dass.operation.dao.OnlineInfoDAO;
import com.huizhi.dass.operation.service.OnlineInfoService;

@Service
public class OnlineInfoServiceImpl implements OnlineInfoService {
	
	@Autowired
	private OnlineInfoDAO onlineInfoDAO;
	
	@Autowired
	private LoginLogDAO loginLogDAO;
	
	
	/* 在线时长相关 开始 */
	
	@Override
	public boolean addAllOnlineInfo(String date) {
		//查询指定日期的新增用户
		Criteria criteria = new Criteria();
		Timestamp startTime = new Timestamp(DateUtils.getDateStartStamp(new DateTime(date).toDate()));
		Timestamp endTime = new Timestamp(DateUtils.getDateStartStamp(new DateTime(date).plusDays(1).toDate()));
		criteria.put("startTime", startTime);
		criteria.put("endTime", endTime);
		criteria.put("type", LoginLogType.LOGOUT.getId());
		List<LoginLog> list = loginLogDAO.selectByExample(criteria);
		
		for (LoginLog logoutLog : list) {
			addOnlineInfo(logoutLog);
		}
		
		return true;
	}
	
	@Override
	public boolean addOnlineInfo(LoginLog logoutLog) {
		//查询最后登陆日志
		LoginLog loginLog = queryLastLoginLog(logoutLog);
		//计算在线时长
		OnlineInfo onlineInfo = calcOnlineInfo(loginLog, logoutLog);
		
		if (onlineInfo == null) return false;
		
		//插入在线时长信息
		long addId = onlineInfoDAO.insert(onlineInfo);
		
		return addId > 0;
	}
	

	@Override
	public LoginLog queryLastLoginLog(LoginLog logoutLog) {

		return loginLogDAO.selectLastLoginLog(logoutLog);
	}

	/**
	 * 计算一个用户一次完整登陆的在线时长
	 * @param loginLog
	 * @param logoutLog
	 * @return
	 */
	private OnlineInfo calcOnlineInfo(LoginLog loginLog, LoginLog logoutLog) {
		Timestamp loginTime = loginLog.getOperateTime();
		Timestamp logoutTime = logoutLog.getOperateTime();
		long onlineTime = logoutTime.getTime() - loginTime.getTime();
		
		if (onlineTime <= 0) return null;
		
		OnlineInfo info = new OnlineInfo();
		info.setUserId(logoutLog.getUserId());
		info.setGameId(logoutLog.getGameId());
		info.setChannelId(logoutLog.getChannelId());
		info.setDeviceId(logoutLog.getDeviceId());
		info.setStartTime(loginTime);
		info.setEndTime(logoutTime);
		info.setOnlineTime(onlineTime);
		
		return info;
	}
	
	/* 在线时长相关 结束 */

}
