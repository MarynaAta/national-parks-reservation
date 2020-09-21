package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.models.Reservation;
import com.models.JDBC.JDBCReservationDAO;

public class JDBCReservationDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO reservationDAO;
	private JdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		this.reservationDAO = new JDBCReservationDAO(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	@Test
	public void test_get_all_reservations() {
		String sqlCount = "SELECT COUNT(*) FROM reservation";

		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlCount);
		while (result.next()) {
			int numberOfReservations = result.getInt("count");
			assertEquals(numberOfReservations, this.reservationDAO.getAllReservations().size());
		}
	}

	@Test
	public void test_make_reservation() {

		Reservation testReserv = new Reservation();
		LocalDate startDate = LocalDate.of(2020, 8, 12);
		LocalDate endDate = LocalDate.of(2020, 8, 28);
		reservationDAO.makeReservation(2, "Adams Family Reservation", startDate, endDate);
		Reservation actualReserv = reservationDAO.getReservationByDetails(2, "Adams Family Reservation", startDate,
				endDate);
		assertEquals("Adams Family Reservation", actualReserv.getName());
	}

	@Test

	public void test_get_reservation_by_id() {

		String sqlSearch = "SELECT * FROM reservation WHERE reservation_id = ?";
		List<Reservation> reservationById = this.reservationDAO.getAllReservations();
		long reservationId = reservationById.get(0).getReservationId();
		List<Reservation> result = this.reservationDAO.getAllReservations();

		assertEquals(reservationById.get(0).getReservationId(), result.get(0).getReservationId());

	}
}
