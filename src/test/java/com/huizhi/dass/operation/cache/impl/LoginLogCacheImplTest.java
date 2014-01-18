package com.huizhi.dass.operation.cache.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.cache.LoginInfoCache;
import com.huizhi.dass.operation.dao.LoginLogDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/business-config.xml")
public class LoginLogCacheImplTest {
	
	@Autowired
	private LoginInfoCache llc;
	
	@Autowired
	private LoginLogDAO llDAO;

//	@Test
	public void testInsert() {
		List<LoginLog> list = llDAO.selectByExample(new Criteria());
		System.out.println(list.size());
		System.out.println(DateTime.now().toString());
		
		for (LoginLog loginLog : list) {
			llc.addLoginLog(loginLog);
		}
		System.out.println(DateTime.now().toString());
	}
	
	@Test
	public void testSelectAndInsertCurrentLoginLogListIntoDB() {
		System.out.println(DateTime.now().toString());
		llc.selectAndInsertCurrentLoginLogListIntoDB();
		System.out.println(DateTime.now().toString());
	}

}
