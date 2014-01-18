package com.huizhi.dass.model.operation;

import com.huizhi.dass.model.BaseEntity;

public class RetentionChartInfo extends BaseEntity {
	
	private String[] dates;
	
	private Double[] tmrRetentions;
	
	private Double[] day7Retentions;
	
	private Double[] day30Retentions;
	
	private String startDate;
	
	private String endDate;
	
	private String gameName;
	

	public String[] getDates() {
		return dates;
	}

	public void setDates(String[] dates) {
		this.dates = dates;
	}

	public Double[] getTmrRetentions() {
		return tmrRetentions;
	}

	public void setTmrRetentions(Double[] tmrRetentions) {
		this.tmrRetentions = tmrRetentions;
	}

	public Double[] getDay7Retentions() {
		return day7Retentions;
	}

	public void setDay7Retentions(Double[] day7Retentions) {
		this.day7Retentions = day7Retentions;
	}

	public Double[] getDay30Retentions() {
		return day30Retentions;
	}

	public void setDay30Retentions(Double[] day30Retentions) {
		this.day30Retentions = day30Retentions;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	

}
