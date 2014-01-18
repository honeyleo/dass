package com.huizhi.dass.operation.service.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.util.DateUtils;
import com.huizhi.dass.common.util.JsonUtils;
import com.huizhi.dass.common.util.LogFileCallback;
import com.huizhi.dass.common.util.LogFileUtils;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.DayRechargeInfo;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.model.operation.RechargeLog;
import com.huizhi.dass.model.type.RechargeType;
import com.huizhi.dass.operation.dao.DayRechargeInfoDAO;
import com.huizhi.dass.operation.dao.GameDAO;
import com.huizhi.dass.operation.dao.RechargeLogDAO;
import com.huizhi.dass.operation.service.RechargeInfoService;

@Service
public class RechargeInfoServiceImpl implements RechargeInfoService {

	private static final Logger logger = LoggerFactory.getLogger("rechargeLog");
	
	private static final String LOG_PATH = Constants.LOG_PATH;
	private static final String RECHARGE_LOG_FLAG = Constants.RECHARGE_LOG_FLAG;
	
	@Autowired
	private RechargeLogDAO rlDAO;
	
	@Autowired
	private DayRechargeInfoDAO driDAO;
	
	@Autowired
	private GameDAO gameDAO;
	
	LogFileUtils<RechargeLog> logFileUtils = new LogFileUtils<RechargeLog>(){};
	
	@Override
	public boolean addRechargeLog(RechargeLog rechargeLog) {
		logger.info(JsonUtils.beanToJson(rechargeLog));
		
		return true;
	}
	
	@Override
	public boolean addDayRechargeInfo(String date) {
		boolean isAdd = calDayRechargeInfoWithDB(date);
		
		return isAdd;
	}
	
	/* 每日充值计算 开始 */
	
	/**
	 * 在数据库中计算每日充值花费
	 * @return
	 */
	private boolean calDayRechargeInfoWithDB(String date) {
		insertRechargeLogList(date);
		List<Game> games = gameDAO.selectAllGame();
		for (Game game : games) {
			for (RechargeType type : RechargeType.values()) {
				DayRechargeInfo info = sumRechargeInfo(game.getGameId(), type.getId(), date);
				info.setDate(new DateTime(date).toDate());
				driDAO.insert(info);
			}
		}
		rlDAO.delete();
		return true;
	}
	
	private DayRechargeInfo sumRechargeInfo(int gameId, int type, String date) {
		Criteria criteria = new Criteria();
		criteria.put("gameId", gameId);
		criteria.put("type", type);
		
		Timestamp startTime = new Timestamp(DateUtils.getDateStartStamp(new DateTime(date).toDate()));
		Timestamp endTime = new Timestamp(DateUtils.getDateStartStamp(new DateTime(date).plusDays(1).toDate()));
		
		criteria.put("startTime", startTime);
		criteria.put("endTime", endTime);
		
		DayRechargeInfo info = driDAO.sumByGame(criteria);
		
		return info;
	}
	
	/**
	 * 充值日志插入数据库
	 * @param date
	 * @return
	 */
	private boolean insertRechargeLogList(String date) {
		List<String> paths = logFileUtils.traverseLogFile(LOG_PATH, date, RECHARGE_LOG_FLAG);
		try {
			for (String path : paths) {
				System.out.println("== read file start: " + DateTime.now());
				logFileUtils.copyDataFromLogFile(logFileUtils, new File(path), new LogFileCallback<RechargeLog, Boolean>() {
					@Override
					public Boolean callBack(List<RechargeLog> list) {
						rlDAO.insertList(list);
						return true;
					}
				});
				System.out.println("== read file end: " + DateTime.now());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;  
	}
	
	/* 每日充值计算 结束 */
	

	@Override
	public PageInfo<DayRechargeInfo> queryByDateAndGame(Criteria criteria) {
		PageInfo<DayRechargeInfo> pager = new PageInfo<DayRechargeInfo>();
		
		List<DayRechargeInfo> data = driDAO.selectByExample(criteria);
		pager.setData(data);
		
		int total = driDAO.selectCountByExample(criteria);
		pager.setTotal(total);
		
		return pager;
	}

	@Override
	public boolean updateAudit(DayRechargeInfo info) {
		long id = driDAO.updateAuditByPrimaryKey(info);
		
		return id > 0;
	}

	@Override
	public boolean updateById(DayRechargeInfo info) {
		long id = driDAO.updateByPrimaryKey(info);
		
		return id > 0;
	}

	@Override
	public DayRechargeInfo queryById(Long id) {
		DayRechargeInfo info = driDAO.selectByPrimaryKey(id);
		
		return info;
	}
	


}
