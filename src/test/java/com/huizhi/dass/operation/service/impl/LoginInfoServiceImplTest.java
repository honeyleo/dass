package com.huizhi.dass.operation.service.impl;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.service.LoginInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/business-config.xml")
public class LoginInfoServiceImplTest {

	@Autowired
	private LoginInfoService service;
	
//	@Test
	public void testAddLoginLog() {
		
		for (int i = 0; i < 1000000; i++) {
			LoginLog log = new LoginLog();
			log.setChannelId(i + 3000000);
			log.setDeviceId("aaa");
			log.setGameId(2);
			log.setOperateTime(new Timestamp(DateTime.now().getMillis()));
			log.setType(1);
			log.setUserId("222");
			service.addLoginLog(log);
		}
	}
	
	@Test
	public void testAddLoginLogToDBFromLog() {
//		service.addLoginLogToDBFromLog();
	}

}
