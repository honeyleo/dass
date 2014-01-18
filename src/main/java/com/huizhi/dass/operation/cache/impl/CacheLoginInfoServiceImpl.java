package com.huizhi.dass.operation.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.huizhi.dass.common.util.JsonUtils;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.cache.LoginInfoCache;
import com.huizhi.dass.operation.dao.LoginLogDAO;
import com.huizhi.dass.operation.service.LoginInfoService;

@Component
public class CacheLoginInfoServiceImpl implements LoginInfoCache {
	
	private Lock lock = new ReentrantLock();
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
	
	@Autowired
	private LoginLogDAO loginLogDAO;
	
	private static final String TEMP_INSERT_KEY = "login.log.list";
	
	@Override
	public boolean addLoginLog(LoginLog loginLog) {
		lock.lock();
		try {
			long id = listOps.leftPush(TEMP_INSERT_KEY, JsonUtils.beanToJson(loginLog));
			return id > 0;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean selectAndInsertCurrentLoginLogListIntoDB() {
		List<String> loginLogJsonList = new ArrayList<String>();
		lock.lock();
		try {
			loginLogJsonList = listOps.range(TEMP_INSERT_KEY, 1, listOps.size(TEMP_INSERT_KEY));
			redisTemplate.delete(TEMP_INSERT_KEY);
		} finally {
			lock.unlock();
		}
		
		List<LoginLog> list = new ArrayList<LoginLog>();
		for (int i = 0; i < loginLogJsonList.size(); i++) {
			String json = loginLogJsonList.get(i);
			LoginLog loginLog = JsonUtils.jsonToBean(json, LoginLog.class);
			list.add(loginLog);
			
			if (i % 1000 == 0) {
				loginLogDAO.insertList(list);
				list.clear();
			} else if ((i + 1) == loginLogJsonList.size()) {
				loginLogDAO.insertList(list);
			}
		}
		
		return true;
	}
	
}
