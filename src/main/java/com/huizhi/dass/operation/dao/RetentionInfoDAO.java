package com.huizhi.dass.operation.dao;

import java.util.List;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.RetentionInfo;

/**
 * 留存率信息DAO
 * 
 * @author Administrator
 *
 */
public interface RetentionInfoDAO {

	public int insert(RetentionInfo retentionInfo);
	
	public List<RetentionInfo> selectByExample(Criteria criteria);

	public int selectCountByExample(Criteria criteria);

	public RetentionInfo selectByPrimaryKey(Long id);

	public int updateAuditByPrimaryKey(RetentionInfo retentionInfo);

	public int updateByPrimaryKey(RetentionInfo retentionInfo);
	
}
