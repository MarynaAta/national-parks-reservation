package com.models.JDBC;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.models.Park;
import com.models.Reservation;
import com.models.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Reservation> getAllReservations() {
		List<Reservation> allReservations = new ArrayList<Reservation>();
		String sqlAllReservations = "SELECT * FROM reservation";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlAllReservations);
		while (sqlRowSet.next()) {
			allReservations.add(mapToReservations(sqlRowSet));
		}

		return allReservations;

	}

	@Override
	public long makeReservation(long siteId, String name, LocalDate startDate, LocalDate endDate) {
		String sqlMakeReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES (?, ?, ?, ?, ?)";
		LocalDate bookingDate = LocalDate.now();
		jdbcTemplate.update(sqlMakeReservation, siteId, name, startDate,
				endDate, bookingDate);

		Reservation reservation = getReservationByDetails(siteId, name, startDate, endDate);
		long reservationId = reservation.getReservationId();
		return reservationId;
	}
	

	
	public Reservation getReservationByDetails(long siteId, String name, LocalDate startDate, LocalDate endDate) {
		Reservation reservation = null;
		
		String sqlFindID = " SELECT * from reservation WHERE site_id = ? AND name = ? AND from_date = ? AND to_date = ? ";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlFindID, siteId, name, startDate, endDate);
		while (sqlRowSet.next()) {
			reservation = mapToReservations(sqlRowSet);
		}
		
		return reservation;
	}
	
	
	@Override
	public Reservation getReservationById(long reservationId) {
		String sqlSearch = "SELECT * FROM reservation WHERE reservation_id = ?";
		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlSearch, reservationId);
		Reservation tempReservation = new Reservation();

		while (result.next()) {

			tempReservation = mapToReservations(result);

		}
		return tempReservation;
	}

	private Reservation mapToReservations(SqlRowSet sqlRowSet) {

		Reservation tempReservation = new Reservation();
		tempReservation.setSiteId(sqlRowSet.getLong("site_id"));
		tempReservation.setName(sqlRowSet.getString("name"));
		tempReservation.setStartDate(sqlRowSet.getDate("from_date").toLocalDate());
		tempReservation.setEndDate(sqlRowSet.getDate("to_date").toLocalDate());
		tempReservation.setReservationId(sqlRowSet.getLong("reservation_id"));
		tempReservation.setBookingDate(sqlRowSet.getDate("create_date").toLocalDate());
		return tempReservation;

	}

	@Override
	public void searchResById(long resId) {
		// TODO Auto-generated method stub
		
	}

}
