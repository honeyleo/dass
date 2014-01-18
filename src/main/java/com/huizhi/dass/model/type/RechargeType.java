package com.huizhi.dass.model.type;

public enum RechargeType {
	
	RECHARGE(1, "充值");
//	CONSUME(2, "消费");
	
	private Integer id;
	private String name;

	private RechargeType(Integer id, String name) {
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
