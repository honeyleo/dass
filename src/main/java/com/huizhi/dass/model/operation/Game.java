package com.huizhi.dass.model.operation;

import com.huizhi.dass.model.BaseEntity;

public class Game extends BaseEntity {
	
	private Integer gameId;
	private String gameName;
	private String appSecret;

	
	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	
	

}
