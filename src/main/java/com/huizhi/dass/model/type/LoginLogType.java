package com.huizhi.dass.model.type;

public enum LoginLogType {
	
	REGIST(1, "注册"),
	LOGIN(2, "登陆"),
	LOGOUT(3, "退出");
	
	private Integer id;
	private String name;

	private LoginLogType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
