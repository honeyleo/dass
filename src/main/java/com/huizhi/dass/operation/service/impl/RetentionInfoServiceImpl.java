package com.huizhi.dass.operation.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huizhi.dass.common.util.DateUtils;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.Channel;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.model.operation.RetentionChartInfo;
import com.huizhi.dass.model.operation.RetentionInfo;
import com.huizhi.dass.model.type.LoginLogType;
import com.huizhi.dass.model.type.RetentionType;
import com.huizhi.dass.operation.cache.RetentionInfoCache;
import com.huizhi.dass.operation.dao.ChannelDAO;
import com.huizhi.dass.operation.dao.GameDAO;
import com.huizhi.dass.operation.dao.LoginLogDAO;
import com.huizhi.dass.operation.dao.RetentionInfoDAO;
import com.huizhi.dass.operation.service.RetentionInfoService;

@Service
public class RetentionInfoServiceImpl implements RetentionInfoService {

	@Autowired
	private RetentionInfoDAO retentionDAO;
	
	@Autowired
	private LoginLogDAO loginLogDAO;

	@Autowired
	private GameDAO gameDAO;
	
	@Autowired 
	private RetentionInfoCache riCache;
	
	/* 计算留存率 开始 */

//	@Override
	public boolean addRetentionInfo(RetentionInfo info) {
		retentionDAO.insert(info);
		
		return info.getId() > 0;
	}

	@Override
	public boolean addAllRetentionInfoByDate(String date) {
		DateTime targetDate = new DateTime(date);
		
		//初始化缓存中当前日期的数据
		riCache.insertTargetLoginLogList(targetDate.toString("yyyy-MM-dd"));
		
		//缓存交集活跃用户信息Key
		Set<String> interKeys = new HashSet<String>();
		//缓存留存率指定日期注册用户信息key
		Set<String> oriKeys = new HashSet<String>();
		
		List<Game> games = gameDAO.selectAllGame();
		
		for (RetentionType type : RetentionType.values()) {
			RetentionInfo retention = new RetentionInfo();
			switch (type) {
			case TOMORROW_RETENTION:
				DateTime yesterday = targetDate.minusDays(1);
				initCacheData(yesterday);
				interKeys = riCache.checkLoginLogInCache();
				oriKeys = riCache.queryOriLoginLogKeys();
				for (Game game : games) {
					int gameId = game.getGameId();
					retention = calcRetention(yesterday, targetDate, type, gameId, interKeys, oriKeys);
					if (retention == null) continue; 
					addRetentionInfo(retention);
				}
				break;
			case SEVEN_RETENTION:
				DateTime day7ago = targetDate.minusDays(7);
				initCacheData(day7ago);
				interKeys = riCache.checkLoginLogInCache();
				oriKeys = riCache.queryOriLoginLogKeys();
				for (Game game : games) {
					int gameId = game.getGameId();
					retention = calcRetention(day7ago, targetDate, type, gameId, interKeys, oriKeys);
					if (retention == null) continue; 
					addRetentionInfo(retention);
				}
				break;
			case THIRTY_RETENTION:
				DateTime day30ago = targetDate.minusDays(30);
				initCacheData(day30ago);
				interKeys = riCache.checkLoginLogInCache();
				oriKeys = riCache.queryOriLoginLogKeys();
				for (Game game : games) {
					int gameId = game.getGameId();
					retention = calcRetention(day30ago, targetDate, type, gameId, interKeys, oriKeys);
					if (retention == null) continue; 
					addRetentionInfo(retention);
				}
				break;
				
			default:
				continue;
			}

		}
		riCache.clearCache();		
		
		return true;
	}
	
	/**
	 * 初始化缓存数据
	 * @param oriDate 留存率指定日期
	 * @param targetDate 当前日期
	 * @param interKeys 交集keys
	 * @param oriKeys 原始keys
	 * @return
	 */
	private boolean initCacheData(DateTime oriDate) {
		riCache.insertOriginalLoginLogList(oriDate.toString("yyyy-MM-dd"));
//		interKeys = riCache.checkLoginLogInCache();
//		oriKeys = riCache.queryOriLoginLogKeys();
		
		return true;
	}
	
	/**
	 * 缓存中计算留存率
	 * @param oriDate 留存率指定日期
	 * @param targetDate 当前日期
	 * @param type
	 * @param gameId
	 * @return
	 */
	private RetentionInfo calcRetention(DateTime oriDate, DateTime targetDate, RetentionType type, int gameId, Set<String> interKeys, Set<String> oriKeys) {
		int[] count = new int[2];
		
		//统计指定游戏id的活跃用户数量
		for (Iterator<String> itor = interKeys.iterator(); itor.hasNext();) {
			String key = itor.next();
			if (key.startsWith(gameId + ".")) {
				count[0] += 1;
				itor.remove();
			}
		}
		
		//统计留存率日期的指定游戏id的注册用户数量
		for (Iterator<String> itor = oriKeys.iterator(); itor.hasNext();) {
			String key = itor.next();
			if (key.startsWith(gameId + ".")) {
				count[1] += 1;
				itor.remove();
			}
		}
		
		RetentionInfo r = new RetentionInfo();
		r.setDate(oriDate.toDate());
		r.setGameId(gameId);
		double rtn = 0;
		if (count[1] != 0) {
			rtn = new BigDecimal(count[0]).divide(new BigDecimal(count[1]), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue(); 
		}
		r.setRetention(rtn);
		r.setType(type.getId());
		
		return r;
	}
	
	
	/**
	 * 根据留存率类型计算当天留存率 (DB)
	 * @param targetDate 和留存率类型对应的日期
	 * @return
	 */
	@Deprecated
	private RetentionInfo calcRetention(DateTime targetDate, DateTime oriDate, int gameId, int channelId, RetentionType type) {
		RetentionInfo retention = new RetentionInfo();
		retention.setGameId(gameId);
		retention.setChannelId(channelId);
		retention.setType(type.getId());
		retention.setDate(targetDate.toDate());
		
		//查询指定日期的新增用户
		Criteria criteria = new Criteria();
		Timestamp startTime = new Timestamp(DateUtils.getDateStartStamp(targetDate.toDate()));
		Timestamp endTime = new Timestamp(DateUtils.getDateStartStamp(targetDate.plusDays(1).toDate()));
		criteria.put("startTime", startTime);
		criteria.put("endTime", endTime);
		criteria.put("gameId", gameId);
		criteria.put("channelId", channelId);
		criteria.put("type", LoginLogType.REGIST.getId());
		List<LoginLog> list = loginLogDAO.selectByExample(criteria);
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		int totalNewUserCount = list.size();
		int totalActiveCount = 0;
		for (LoginLog loginLog : list) {
			boolean isActive = queryLoginLog(loginLog, oriDate);
			if (isActive) {
				totalActiveCount ++;
			}
		}
		double rtn = new BigDecimal(totalActiveCount).multiply(new BigDecimal(100)).divide(new BigDecimal(totalNewUserCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		retention.setRetention(rtn);
		
		return retention;
	}
	
	/**
	 * 根据用户判断指定留存时间后是否活跃 (DB)
	 * @param loginLog 查询的用户
	 * @param date 目标时间
	 * @return
	 */
	@Deprecated
	private boolean queryLoginLog(LoginLog loginLog, DateTime date) {
		Timestamp loginTime = loginLog.getOperateTime();
		DateTime oldTime = new DateTime(loginTime.getTime());
		Timestamp startTime = new Timestamp(DateUtils.getDateStartStamp(date.toDate()));
		Timestamp endTime = new Timestamp(DateUtils.getDateStartStamp(date.plusDays(1).toDate()));
		
		Criteria criteria = new Criteria();
		criteria.put("userId", loginLog.getUserId());
		criteria.put("gameId", loginLog.getGameId());
		criteria.put("channelId", loginLog.getChannelId());
		criteria.put("deviceId", loginLog.getDeviceId());
		criteria.put("startTime", startTime);
		criteria.put("endTime", endTime);
		criteria.setOffset(1);
		
		List<LoginLog> list = loginLogDAO.selectByExample(criteria);
		if (list == null || list.size() == 0) {
			return false;
		}
		
		return true;
	}

	/* 计算留存率 结束 */

	@Override
	public PageInfo<RetentionInfo> queryRetentionInfoByDateAndGame(Criteria criteria) {
		PageInfo<RetentionInfo> pager = new PageInfo<RetentionInfo>();
		List<RetentionInfo> data = retentionDAO.selectByExample(criteria);
		pager.setData(data);
		
		int total = retentionDAO.selectCountByExample(criteria);
		pager.setTotal(total);
		
		return pager;
	}

	@Override
	public boolean updateAudit(RetentionInfo info) {
		long id = retentionDAO.updateAuditByPrimaryKey(info);
		
		return id > 0;
	}

	@Override
	public boolean updateById(RetentionInfo info) {
		long id = retentionDAO.updateByPrimaryKey(info);
		
		return id > 0;
	}

	@Override
	public RetentionInfo queryById(Long id) {

		return retentionDAO.selectByPrimaryKey(id);
	}
	
	/*
	 * 留存率json图表 开始
	 */

	@Override
	public RetentionChartInfo queryRetentionChartInfo(Criteria criteria) {
		Criteria tmrCta = buildCriteria(criteria, RetentionType.TOMORROW_RETENTION.getId());
		List<RetentionInfo> tmrRtn = retentionDAO.selectByExample(tmrCta);
		
		Criteria day7Cta = buildCriteria(criteria, RetentionType.SEVEN_RETENTION.getId());
		List<RetentionInfo> day7Rtn = retentionDAO.selectByExample(day7Cta);
		
		Criteria day30Cta = buildCriteria(criteria, RetentionType.THIRTY_RETENTION.getId());
		List<RetentionInfo> day30Rtn = retentionDAO.selectByExample(day30Cta);
		
		RetentionChartInfo rci = buildRetentionChartInfo(tmrRtn, day7Rtn, day30Rtn, criteria);
		
		return rci;
	}
	
	/**
	 * 构造留存率json对象
	 * @param tmrCta
	 * @param day7Cta
	 * @param day30Cta
	 * @param criteria
	 * @return
	 */
	private RetentionChartInfo buildRetentionChartInfo(List<RetentionInfo> tmrRtn, List<RetentionInfo> day7Rtn, List<RetentionInfo> day30Rtn, Criteria criteria) {
		RetentionChartInfo rci = new RetentionChartInfo();
		
		//日期
		Map<String, Object> condition = criteria.getCondition();
		
		String startTime = "";
		String endTime = "";
		
		Object start = condition.get("startTime");
		if (start == null) {
			startTime = DateTime.now().plusDays(-31).toString("yyyy-MM-dd");
		} else {
			startTime = new DateTime(start).toString("yyyy-MM-dd");
		}
		Object end = condition.get("endTime");
		if (end == null) {
			endTime = DateTime.now().plusDays(-1).toString("yyyy-MM-dd");
		} else {
			endTime = new DateTime(end).toString("yyyy-MM-dd");
		}
		
		rci.setStartDate(startTime);
		rci.setEndDate(endTime);
		
		//日期横坐标
		List<String> dates = new ArrayList<String>();
		List<String> copyDates = new ArrayList<String>();
		LocalDate startLocalDate = new DateTime(startTime).toLocalDate();
		LocalDate endLocalDate = new DateTime(endTime).toLocalDate();
		int days = Days.daysBetween(startLocalDate, endLocalDate).getDays();
		dates.add(new DateTime(startTime).toString("MM-dd"));
		copyDates.add(startTime);
		DateTime time = new DateTime(startTime);
		for (int i = 0; i < days; i++) {
			time = time.plusDays(1);
			dates.add(time.toString("MM-dd"));
			copyDates.add(time.toString("yyyy-MM-dd"));
		}
		rci.setDates(dates.toArray(new String[]{}));
		
		
		//留存率
		rci.setTmrRetentions(buildRetentionData(tmrRtn, copyDates));
		rci.setDay7Retentions(buildRetentionData(day7Rtn, copyDates));
		rci.setDay30Retentions(buildRetentionData(day30Rtn, copyDates));
		
		
		return rci;
	}
	
	/**
	 * 整理留存率具体数据
	 * @param rtns
	 * @return
	 */
	private Double[] buildRetentionData(List<RetentionInfo> rtns, List<String> dates) {
		List<Double> rtnData = new ArrayList<Double>();
		
		if (rtns == null || rtns.size() == 0) {
			for (String date : dates) {
				rtnData.add(0d);
			}
			return rtnData.toArray(new Double[]{});
		}
		
		for (String date : dates) {
			for (RetentionInfo rtn : rtns) {
				DateTime rtnDate = new DateTime(rtn.getDate());
				if (rtnDate.isEqual(new DateTime(date))) {
					if (rtn.getAudit() == 0) {
						rtnData.add(0d);
					} else {
						rtnData.add(rtn.getRetention());
					}
					continue;
				} else {
					rtnData.add(0d);
				}
			}
		}
		
		return rtnData.toArray(new Double[]{});
	}
	
	/**
	 * 构造留存率具体数据的查询条件
	 * @param criteria
	 * @param type
	 * @return
	 */
	private Criteria buildCriteria(Criteria criteria, Integer type) {
		Criteria newCta = new Criteria();
		Map<String, Object> condition = criteria.getCondition();
		newCta.put("gameId", condition.get("gameId"));
		newCta.put("type", type);
		newCta.putOrder("date", true);
		
		String startTime = "";
		String endTime = "";
		
		Object start = condition.get("startTime");
		if (start == null) {
			startTime = DateTime.now().plusDays(-31).toString("yyyy-MM-dd");
		} else {
			startTime = new DateTime(start).toString("yyyy-MM-dd");
		}
		Object end = condition.get("endTime");
		if (end == null) {
			endTime = DateTime.now().plusDays(-1).toString("yyyy-MM-dd");
		} else {
			endTime = new DateTime(end).toString("yyyy-MM-dd");
		}
		
		newCta.put("startTime", startTime);
		newCta.put("endTime", endTime);
		
		return newCta;
	}
	
	/*
	 * 留存率json图表 开始
	 */
}
