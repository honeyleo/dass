package com.huizhi.dass.operation.dao;

import java.util.List;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.Game;

public interface GameDAO {
	
	public List<Game> selectAllGame();
	
	public int insertGame(Game game);
	
	public int deleteByPrimaryKey(Long id);
	
	public int updateByPrimaryKey(Game game);
	
	public Game selectByPrimaryKey(Long id);
	
	public List<Game> selectByExample(Criteria criteria);
	
	public int selectCountByExample(Criteria criteria);
	
	public Game selectByGameId(Integer gameId);
	
	

}
