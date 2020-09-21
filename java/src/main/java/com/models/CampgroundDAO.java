package com.models;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds(long parkId);

	public Campground getCampgroundById(long campgroundId);

	public Campground getCampgroundByName(String selectedCampground);
	
	public String toString(Campground campground);

	public long getCampgroundIdBySiteId();
}


