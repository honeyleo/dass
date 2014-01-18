package com.huizhi.dass.operation.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huizhi.dass.common.util.MessageDigestUtil;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.operation.dao.GameDAO;
import com.huizhi.dass.operation.service.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameDAO gameDAO;
	
	@Override
	@Cacheable(value = "commonCache", key = "'gameService_allGame'")
	public List<Game> queryAllGame() {
		return gameDAO.selectAllGame();
	}

	@Override
	public int queryCountByCriteria(Criteria criteria) {
		return gameDAO.selectCountByExample(criteria);
	}

	@Override
	public PageInfo<Game> queryByCriteria(Criteria criteria) {
		PageInfo<Game> pager = new PageInfo<Game>();
		List<Game> data = gameDAO.selectByExample(criteria);
		
		int total = gameDAO.selectCountByExample(criteria);
		pager.setTotal(total);
		pager.setData(data);
		return pager;
	}

	@Override
	public boolean add(Game game) {
		String appSecret = buildAppSecret(game);
		game.setAppSecret(appSecret);
		int id = gameDAO.insertGame(game);
		
		return id > 0;
	}

	@Override
	public boolean edit(Game game) {
		
		return gameDAO.updateByPrimaryKey(game) > 0;
	}

	@Override
	public Game queryById(long id) {

		return gameDAO.selectByPrimaryKey(id);
	}

	@Override
	public boolean remove(long id) {
		return gameDAO.deleteByPrimaryKey(id) > 0;
	}

	@Override
	public Game queryByGameId(Integer gameId) {
		
		return gameDAO.selectByGameId(gameId);
	}
	
	private String buildAppSecret(Game game) {
		return MessageDigestUtil.getSHA1("huizhi_game_" + game.getGameId() + "_" + DateTime.now().getMillis());
	}

	
	
	
	
	
	
	
	public static void main(String[] args) {
		GameServiceImpl gs = new GameServiceImpl();
		Game game = new Game();
		game.setGameId(1);
		System.out.println(gs.buildAppSecret(game));
	}
	
}
