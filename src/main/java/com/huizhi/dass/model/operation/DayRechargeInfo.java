package com.huizhi.dass.model.operation;

import java.math.BigDecimal;
import java.util.Date;

import com.huizhi.dass.model.BaseEntity;

public class DayRechargeInfo extends BaseEntity {
	
	private Integer channelId;
	private Integer gameId;
	private Integer type;
	private BigDecimal money;
	private Date date;
	private Integer audit;
	private String gameName;

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
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAudit() {
		return audit;
	}
	public void setAudit(Integer audit) {
		this.audit = audit;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	

}
