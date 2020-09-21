package com.models;

import java.util.List;

public interface ParkDAO {

	public List<Park> getAllParks();
	public Park getParkById(long parkId);
	Park getParkByName(String name);
}
