package com.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class Campground {

	private long campgroundId;
	private String name;
	private String startDate;
	private String endDate;
	private BigDecimal dailyFee;
	private long parkId;
	private String details;

	public long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String month) {
		this.startDate = month;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getDailyFee() {
		return dailyFee;
	}

	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}

	public long getParkId() {
		return parkId;
	}

	public void setParkId(long parkId) {
		this.parkId = parkId;
	}

	@Override
	public String toString() {
		return String.format("%-25s %-10s %-15s$%-15s", this.getName(),
				this.getStartDate(), this.getEndDate(), this.getDailyFee());
	}
}
