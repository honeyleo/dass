package com.huizhi.dass.model.type;

public enum RetentionType {
	
	TOMORROW_RETENTION(1, "次日留存"),
	SEVEN_RETENTION(2, "7日留存"),
	THIRTY_RETENTION(3, "30日留存");
	
	
	
	private Integer id;
	
	private String name;

	private RetentionType(Integer id, String name) {
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
