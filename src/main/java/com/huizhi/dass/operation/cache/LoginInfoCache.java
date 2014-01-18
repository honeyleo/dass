package com.huizhi.dass.operation.cache;

import java.util.List;

import com.huizhi.dass.model.operation.LoginLog;

public interface LoginInfoCache {
	
	public boolean addLoginLog(LoginLog loginLog);
	
	public boolean selectAndInsertCurrentLoginLogListIntoDB();

}
