package com.huizhi.dass.operation.dao;

import java.util.List;

import com.huizhi.dass.model.operation.Channel;

public interface ChannelDAO {
	
	public List<Channel> selectAllChannel();
	
	public int insertChannel(Channel channel);

}
