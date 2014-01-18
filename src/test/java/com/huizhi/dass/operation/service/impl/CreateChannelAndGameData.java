package com.huizhi.dass.operation.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.huizhi.dass.model.operation.Channel;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.operation.dao.ChannelDAO;
import com.huizhi.dass.operation.dao.GameDAO;
import com.huizhi.dass.operation.service.GameService;

public class CreateChannelAndGameData {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/business-config.xml"); 
		
		GameService gameService = context.getBean(GameService.class);
		for (int i = 1; i <= 100; i++) {
			Game game = new Game();
			game.setGameId(i);
			game.setGameName("game_" + i);
			gameService.add(game);
		}

		ChannelDAO channelDAO = context.getBean(ChannelDAO.class);
		for (int i = 1; i <= 100; i++) {
			Channel channel = new Channel();
			channel.setChannelId(i);
			channel.setChannelName("channel_" + i);
			channelDAO.insertChannel(channel);
		}
		
	}
}
