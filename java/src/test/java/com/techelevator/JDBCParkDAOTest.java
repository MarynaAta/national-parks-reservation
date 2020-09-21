package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
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

import com.models.Park;
import com.models.JDBC.JDBCParkDAO;



public class JDBCParkDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO ParkDAO;
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
		this.ParkDAO = new JDBCParkDAO(dataSource);
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
	public void test_get_all_parks() {

		String sqlCount = "SELECT COUNT(*) FROM park";

		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlCount);
		while (result.next()) {
			int numberOfParks = result.getInt("count");
			assertEquals(numberOfParks, this.ParkDAO.getAllParks().size());
		}
	}
		@Test
		public void test_get_park_by_id() {
			String sqlSearch = "SELECT * FROM park WHERE park_id = ?";
			List<Park> parkById = this.ParkDAO.getAllParks();
			long parkId = parkById.get(0).getParkId();
			List<Park> result = this.ParkDAO.getAllParks();

			assertEquals(parkById.get(0).getParkId(), result.get(0).getParkId());

	}
		
		@Test
		public void test_get_park_by_name() {
			List<Park> park = this.ParkDAO.getAllParks();
			String parkName = park.get(0).getName();
			Park newList = ParkDAO.getParkByName(parkName);

			park.get(0).getName();
			assertEquals(park.get(0).getName(), newList.getName());
		
		
		}

}
