package com.huizhi.dass.model;

import java.io.Serializable;

public class BaseEntity implements Serializable {
	
	protected Long id;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean thisIsNew() {
		return (id == null);
	}
	

}
