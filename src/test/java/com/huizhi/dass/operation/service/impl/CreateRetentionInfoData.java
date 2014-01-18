package com.huizhi.dass.operation.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.huizhi.dass.operation.service.RetentionInfoService;

public class CreateRetentionInfoData {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/business-config.xml"); 
		RetentionInfoService riService = context.getBean(RetentionInfoService.class);
		
		for (int i = 0; i < 100; i++) {
			
		}
	}
	
	
}
