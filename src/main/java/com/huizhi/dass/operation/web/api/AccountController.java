package com.huizhi.dass.operation.web.api;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huizhi.dass.common.util.MessageDigestUtil;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.service.GameService;
import com.huizhi.dass.operation.service.LoginInfoService;

@Controller
public class AccountController {
	
	@Autowired
	LoginInfoService loginInfoService;
	
	@Autowired
	GameService gameService;
	
	@RequestMapping(value = "/account", method = {RequestMethod.GET, RequestMethod.POST})
	public Object createLoginLog(LoginLogForm loginLogForm) {
		if (!validateAccount(loginLogForm)) {
			//参数错误
			return new ResponseEntity<String>("{'code': 3}", HttpStatus.OK);
		}
		
		if (!checkSign(loginLogForm)) {
			return new ResponseEntity<String>("{'code': 4}", HttpStatus.OK);
		}
		
		loginLogForm.setOperateTime(new Timestamp(DateTime.now().getMillis()));
		boolean isAdd = loginInfoService.addLoginLog(loginLogForm.toLoginLog());
		if (isAdd) {
			//成功
			return new ResponseEntity<String>("{'code': 1}", HttpStatus.OK);
		}
		//失败
		return new ResponseEntity<String>("{'code': 2}", HttpStatus.OK);
	}
	
	/**
	 * 参数验证
	 * @param loginLog
	 * @return
	 */
	private boolean validateAccount(LoginLogForm loginLog) {
		Integer gameId = loginLog.getGameId();
		if (gameId == null || gameId.intValue() == 0) {
			return false;
		}
		
		Integer type = loginLog.getType();
		if (type == null || type.intValue() == 0) {
			return false;
		}
		
		String userId = loginLog.getUserId();
		String deviceId = loginLog.getDeviceId();
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(deviceId)) {
			return false;
		}
		
		String sign = loginLog.getSign();
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		
		return true;
	}
	
	private boolean checkSign(LoginLogForm params) {
		Game game = gameService.queryByGameId(params.getGameId());
		if (game == null) {
			return false;
		}
		String appSecret = game.getAppSecret();
		
		StringBuffer realSign = new StringBuffer();
		realSign.append("appSecret=" + appSecret);
		if (StringUtils.isNotBlank(params.getDeviceId())) {
			realSign.append("&deviceId=" + params.getDeviceId());
		}
		realSign.append("&gameId=" + params.getGameId());
		realSign.append("&type=" + params.getType());
		if (StringUtils.isNotBlank(params.getUserId())) {
			realSign.append("&userId=" + params.getUserId());
		}
		if (params.getSign().equalsIgnoreCase(MessageDigestUtil.getMD5(realSign.toString()))) {
			return true;
		}
		
		return false;
	}
	
	public static class LoginLogForm {
		private String userId;
		private String deviceId;
		private Integer gameId;
		private Integer channelId;
		private Integer type;
		private String sign;
		private Timestamp operateTime;
		
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getDeviceId() {
			return deviceId;
		}
		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}
		public Integer getGameId() {
			return gameId;
		}
		public void setGameId(Integer gameId) {
			this.gameId = gameId;
		}
		public Integer getChannelId() {
			return channelId;
		}
		public void setChannelId(Integer channelId) {
			this.channelId = channelId;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		public Timestamp getOperateTime() {
			return operateTime;
		}
		public void setOperateTime(Timestamp operateTime) {
			this.operateTime = operateTime;
		}
		
		public LoginLog toLoginLog() {
			LoginLog log = new LoginLog();
			log.setChannelId(this.channelId);
			log.setDeviceId(this.deviceId);
			log.setGameId(this.gameId);
			log.setType(this.type);
			log.setUserId(this.userId);
			log.setOperateTime(this.operateTime);
			
			return log;
		}
		
	}
	
	
	

}
