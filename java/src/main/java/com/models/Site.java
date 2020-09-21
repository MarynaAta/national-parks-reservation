package com.models;

public class Site {

	private long siteId;
	private long campgroundId;
	private long siteNum;
	private long maxOccupancy;
	private boolean accessible;
	private long maxRvLength;
	private boolean utilities;

	public Site(long siteId, long campgroundId, long siteNum, long maxOccupancy, boolean accessible, long maxRvLength,
			boolean utilities) {
		super();
		this.siteId = siteId;
		this.campgroundId = campgroundId;
		this.siteNum = siteNum;
		this.maxOccupancy = maxOccupancy;
		this.accessible = accessible;
		this.maxRvLength = maxRvLength;
		this.utilities = utilities;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public long getCampgorundId() {
		return campgroundId;
	}

	public void setCampgorundId(long campgorundId) {
		this.campgroundId = campgorundId;
	}

	public long getSiteNum() {
		return siteNum;
	}

	public void setSiteNum(long siteNum) {
		this.siteNum = siteNum;
	}

	public long getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(long maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public boolean getAccessible() {
		return accessible;
	}

	public void setAccessible(boolean string) {
		this.accessible = string;
	}

	public long getMaxRvLength() {
		return maxRvLength;
	}

	public void setMaxRvLength(long maxRvLength) {
		this.maxRvLength = maxRvLength;
	}

	public boolean getUtilities() {
		return utilities;
	}

	public void setUtilities(boolean string) {
		this.utilities = string;
	}

	@Override
	public String toString() {
		return String.format("%-10s %-12s %-15s %-15s %-15s", this.getSiteNum(),
				this.getMaxOccupancy(), this.getAccessible(), this.getMaxRvLength(), this.getUtilities());
			
	}
	

}
