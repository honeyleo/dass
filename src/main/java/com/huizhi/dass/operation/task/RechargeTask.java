package com.huizhi.dass.operation.task;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.huizhi.dass.operation.service.RechargeInfoService;

public class RechargeTask {
	
	@Autowired
	RechargeInfoService riService;
	
	public void startEveryDay() {
		String date = DateTime.now().plusDays(-1).toString("yyyy-MM-dd");
		riService.addDayRechargeInfo(date);
	}
	
}
