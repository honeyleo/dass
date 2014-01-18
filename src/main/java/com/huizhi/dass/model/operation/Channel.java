package com.huizhi.dass.model.operation;

import com.huizhi.dass.model.BaseEntity;

public class Channel extends BaseEntity {
	
	private Integer channelId;
	private String channelName;

	
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	
	

}
