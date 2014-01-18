package com.huizhi.dass.operation.task;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.huizhi.dass.operation.service.RetentionInfoService;

public class RetentionTask {
	
	@Autowired
	RetentionInfoService riService;
	
	public void startEveryday() {
		String date = DateTime.now().plusDays(-1).toString("yyyy-MM-dd");
		riService.addAllRetentionInfoByDate(date);
	}

}
