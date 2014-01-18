package com.huizhi.dass.operation.service;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.DayRechargeInfo;
import com.huizhi.dass.model.operation.RechargeLog;
import com.huizhi.dass.model.operation.RetentionInfo;

public interface RechargeInfoService {
	
	/**
	 * 新增充值花费日志
	 * @param rechargeLog
	 * @return
	 */
	public boolean addRechargeLog(RechargeLog rechargeLog);
	
	/**
	 * 新增每日充值花费数据
	 * @return
	 */
	public boolean addDayRechargeInfo(String date);
	
	/**
	 *  分页查询
	 * @param criteria
	 * @return
	 */
	public PageInfo<DayRechargeInfo> queryByDateAndGame(Criteria criteria);
	
	/**
	 * 审核 取消审核
	 * @param info
	 * @return
	 */
	public boolean updateAudit(DayRechargeInfo info);

	/**
	 * 修改留存率
	 * @param info
	 * @return
	 */
	public boolean updateById(DayRechargeInfo info);
	
	/**
	 * 查询一条
	 * @param id
	 * @return
	 */
	public DayRechargeInfo queryById(Long id);

}
