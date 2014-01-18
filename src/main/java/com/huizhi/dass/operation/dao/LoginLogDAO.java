package com.huizhi.dass.operation.dao;

import java.util.List;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.LoginLog;

/**
 * 玩家登陆注册退出日志DAO
 * 
 * @author Administrator
 */
public interface LoginLogDAO {
	
	public int insert(LoginLog loginLog);

	public void insertList(List<LoginLog> list);

	public LoginLog selectLastLoginLog(LoginLog loginLog);
	
	public List<LoginLog> selectByExample(Criteria criteria);
	
	public int updateByPrimaryKey(LoginLog loginLog);
	
	public int countByExample(Criteria criteria);
	
	public int deleteByPrimaryKey(Long id);
	
	public LoginLog selectByPrimaryKey(Long id);

}
