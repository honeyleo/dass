package com.huizhi.dass.operation.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huizhi.dass.operation.service.RechargeInfoService;
import com.huizhi.dass.operation.service.RetentionInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/business-config.xml")
public class RechargeInfoServiceImplTest {
	
	@Autowired
	RechargeInfoService riService;

	@Test
	public void testAddDayRechargeInfo() {
		riService.addDayRechargeInfo("2013-10-09");
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/business-config.xml"); 
		RechargeInfoService riService = context.getBean(RechargeInfoService.class); 
	}

}
