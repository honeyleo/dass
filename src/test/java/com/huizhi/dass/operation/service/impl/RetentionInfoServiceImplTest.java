package com.huizhi.dass.operation.service.impl;

import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huizhi.dass.operation.service.LoginInfoService;
import com.huizhi.dass.operation.service.RetentionInfoService;
import com.huizhi.dass.operation.service.impl.CreateLoginLogData.UserTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/business-config.xml")
public class RetentionInfoServiceImplTest {

	@Resource
	private RetentionInfoService rService;

	@Test
	public void test() {
		rService.addAllRetentionInfoByDate("2013-10-01");
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/business-config.xml"); 
		RetentionInfoService rservice = context.getBean(RetentionInfoService.class); 
		
		ExecutorService exec = Executors.newFixedThreadPool(2000);
		
		for (int i = 0; i < 2000; i++) {
			exec.execute(new UserTask(rservice));
		}
		
		exec.shutdown();
		
	}
	
	public static class UserTask implements Runnable {

		private RetentionInfoService rservice;
		
		public UserTask(RetentionInfoService rservice) {
			this.rservice = rservice;
		}

		@Override
		public void run() {
			rservice.addAllRetentionInfoByDate("2013-10-01");
		}
		
	}

}
