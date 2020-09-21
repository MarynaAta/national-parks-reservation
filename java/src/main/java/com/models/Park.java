package com.models;

import java.time.LocalDate;

public class Park {

	private long parkId;
	private String name;
	private String location;
	private String establishDate;
	private String area;
	private String visitors;
	private String description;

	public long getParkId() {
		return parkId;
	}

	public void setParkId(long parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEstablishDate() {
		return establishDate;
	}

	public void setEstablishDate(String string) {
		this.establishDate = string;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String string) {
		this.area = string;
	}

	public String getVisitors() {
		return visitors;
	}

	public void setVisitors(String string) {
		this.visitors = string;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
