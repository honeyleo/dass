package com.huizhi.dass.operation.service;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.RetentionChartInfo;
import com.huizhi.dass.model.operation.RetentionInfo;

public interface RetentionInfoService {
	
	/**
	 * 新增留存率信息
	 * @param info
	 * @return
	 */
//	public boolean addRetentionInfo(RetentionInfo info);
	
	
	/**
	 * 根据日期新增对应日期的留存率
	 * @param date 坐标日期 yyyy-MM-dd (一般为当前日期)
	 * @return
	 */
	public boolean addAllRetentionInfoByDate(String date);
	
	/**
	 * 分页条件查询留存率
	 * @param criteria
	 * @return
	 */
	public PageInfo<RetentionInfo> queryRetentionInfoByDateAndGame(Criteria criteria);
	
	/**
	 * 审核 取消审核
	 * @param info
	 * @return
	 */
	public boolean updateAudit(RetentionInfo info);

	/**
	 * 修改留存率
	 * @param info
	 * @return
	 */
	public boolean updateById(RetentionInfo info);
	
	/**
	 * 查询一条
	 * @param id
	 * @return
	 */
	public RetentionInfo queryById(Long id);

	/**
	 * 查询图表数据
	 * @param criteria
	 * @return
	 */
	public RetentionChartInfo queryRetentionChartInfo(Criteria criteria);
	
}
