package com.huizhi.dass.operation.dao;

import java.util.List;

import com.huizhi.dass.model.operation.RechargeLog;

/**
 * 充值消费日志DAO
 * 
 * @author Administrator
 *
 */
public interface RechargeLogDAO {
	
	public int insert(RechargeLog log);
	
	public int insertList(List<RechargeLog> list);
	
	public int delete();
	
	
}
