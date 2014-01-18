package com.huizhi.dass.operation.dao;

import java.util.List;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.DayRechargeInfo;

/**
 * 每日充值消费信息DAO
 * 
 * @author Administrator
 *
 */
public interface DayRechargeInfoDAO {
	
	public int insert(DayRechargeInfo info);
	
	public List<DayRechargeInfo> selectByExample(Criteria criteria);
	
	public int selectCountByExample(Criteria criteria);
	
	/**
	 * 根据游戏、时间和充值类型求和
	 * @param criteria
	 * @return
	 */
	public DayRechargeInfo sumByGame(Criteria criteria);
	
	public int updateAuditByPrimaryKey(DayRechargeInfo info);
	
	public int updateByPrimaryKey(DayRechargeInfo info);
	
	public DayRechargeInfo selectByPrimaryKey(Long id);

}
