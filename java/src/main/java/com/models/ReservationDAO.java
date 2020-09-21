package com.models;

import java.time.LocalDate;

public interface ReservationDAO {

	public long makeReservation(long siteId, String name, LocalDate startDate, LocalDate endDate);
	
	public Reservation getReservationById(long reservationId);

	public void searchResById(long resId);
}
