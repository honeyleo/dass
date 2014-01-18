package com.huizhi.dass.operation.web.api;

import java.math.BigDecimal;
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
import com.huizhi.dass.model.operation.RechargeLog;
import com.huizhi.dass.operation.service.GameService;
import com.huizhi.dass.operation.service.RechargeInfoService;

@Controller
public class RechargeController {
	
	@Autowired
	RechargeInfoService rechargeInfoService;
	
	@Autowired
	GameService gameService;
	
	@RequestMapping(value = "/recharge", method = {RequestMethod.GET, RequestMethod.POST})
	public Object createRechargeInfo(RechargeForm rechargeForm) {
		if (!validateRecharge(rechargeForm)) {
			//参数错误
			return new ResponseEntity<String>("{'code': 3}", HttpStatus.OK);
		}
		
		rechargeForm.setOperateTime(new Timestamp(DateTime.now().getMillis()));
		
		//验证sign
		boolean isLegal = checkSign(rechargeForm);
		if (!isLegal) {
			//失败
			return new ResponseEntity<String>("{'code': 4}", HttpStatus.OK);
			
		}
		
		boolean isAdd = rechargeInfoService.addRechargeLog(rechargeForm.toRechargeLog());
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
	private boolean validateRecharge(RechargeForm info) {
		
		int gameId = info.getGameId();
		if (gameId == 0) {
			return false;
		}
		
		long money = info.getMoney();
		if (money <= 0) {
			return false;
		}
		
		String userId = info.getUserId();
		String deviceId = info.getDeviceId();
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(deviceId)) {
			return false;
		}
		
		String sign = info.getSign();
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		
		return true;
	}
	
	public boolean checkSign(RechargeForm params) {
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
		realSign.append("&money=" + params.getMoney());
		if (StringUtils.isNotBlank(params.getUserId())) {
			realSign.append("&userId=" + params.getUserId());
		}
		if (params.getSign().equalsIgnoreCase(MessageDigestUtil.getMD5(realSign.toString()))) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 参数实体类
	 * @author Administrator
	 *
	 */
	public static class RechargeForm {
		private String userId;
		private String deviceId;
		private int gameId;
		private int channelId;
		private int type;
		private int money;
		private Timestamp operateTime;
		private String sign;
		
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
		public int getGameId() {
			return gameId;
		}
		public void setGameId(int gameId) {
			this.gameId = gameId;
		}
		public int getChannelId() {
			return channelId;
		}
		public void setChannelId(int channelId) {
			this.channelId = channelId;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getMoney() {
			return money;
		}
		public void setMoney(int money) {
			this.money = money;
		}
		public Timestamp getOperateTime() {
			return operateTime;
		}
		public void setOperateTime(Timestamp operateTime) {
			this.operateTime = operateTime;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		
		public RechargeLog toRechargeLog() {
			RechargeLog log = new RechargeLog();
			log.setChannelId(this.channelId);
			log.setDeviceId(this.deviceId);
			log.setGameId(this.gameId);
			log.setMoney(new BigDecimal(this.money / 100));
			log.setType(this.type);
			log.setUserId(this.userId);
			log.setOperateTime(this.operateTime);
			
			return log;
		}
		
	}
}
