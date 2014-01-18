package com.huizhi.dass.operation.service.impl;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.service.LoginInfoService;

public class CreateLoginLogData {
	
	public static class UserTask implements Runnable {
		LoginInfoService lservice;
		int i = 0;

		int channelId = new Random().nextInt(100);
		int gameId = new Random().nextInt(100);
		int userId = 0;
		
		int type = 1;

		int munite = new Random().nextInt(60);
		
		DateTime regTime = new DateTime("2013-09-01T00:" + munite + ":" + munite);
		
		public UserTask(LoginInfoService lservice, int i) {
			this.lservice = lservice;
			this.i = i;
			this.userId = i + 3000 * 5;
			this.gameId = i % 100;
		}

		@Override
		public void run() {
			try {
				for (int j = 0; j < 10; j++) {
					LoginLog log = new LoginLog();
					
					log.setChannelId(channelId);
					log.setGameId(gameId);
					log.setOperateTime(new Timestamp(regTime.getMillis()));
					log.setType(type);
					log.setUserId(userId + "");
					
//					System.out.println("user: " + userId + " type: " + type + " | " +  i + " " + j + " | " + this);
					boolean isAdd = lservice.addLoginLog(log);
					if (isAdd) {
						if (type != 1) {
							regTime = regTime.plusSeconds(new Random().nextInt(300));
						} else {
							regTime = regTime.plusSeconds(1);
						}
						if (type == 3) {
							type = 2;
						} else {
							type ++;
						}
					}
					
//					Thread.sleep(100);
					Thread.yield();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			
		}
		
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/business-config.xml"); 
		LoginInfoService lservice = context.getBean(LoginInfoService.class); 
		
		ExecutorService exec = Executors.newFixedThreadPool(2000);
		for (int i = 0; i < 2000; i++) {
			exec.execute(new UserTask(lservice, i));
		}
		exec.shutdown();
		
	}
}
