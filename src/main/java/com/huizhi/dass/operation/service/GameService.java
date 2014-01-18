package com.huizhi.dass.operation.service;

import java.util.List;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.Game;

public interface GameService {

	public List<Game> queryAllGame();
	
	public int queryCountByCriteria(Criteria criteria);
	
	public PageInfo<Game> queryByCriteria(Criteria criteria);
	
	public boolean add(Game game);
	
	public boolean edit(Game game);
	
	public Game queryById(long id);

	public Game queryByGameId(Integer gameId);
	
	public boolean remove(long id);
	
}
