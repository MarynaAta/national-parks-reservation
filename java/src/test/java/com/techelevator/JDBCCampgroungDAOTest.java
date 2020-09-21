package com.techelevator;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
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

import com.models.Campground;
import com.models.Park;
import com.models.JDBC.JDBCCampgroundDAO;

public class JDBCCampgroungDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO campgroundDAO;
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
		this.campgroundDAO = new JDBCCampgroundDAO(dataSource);
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
	public void test_get_all_campgrounds() {

		long parkId = 1L;
		List<Campground> campgrounds = this.campgroundDAO.getAllCampgrounds(parkId);
		long parkId2 = campgrounds.get(0).getParkId();
		long campgroungId = campgrounds.get(0).getCampgroundId();
		List<Campground> result = this.campgroundDAO.getAllCampgrounds(parkId2);
		assertEquals(campgrounds.get(0).getCampgroundId(), result.get(0).getCampgroundId());

	}

	@Test

	public void test_get_campground_by_id() {

		String sqlSearch = "SELECT * FROM campground WHERE campground_id = ?";
		long campId = 1L;
		List<Campground> campById = this.campgroundDAO.getAllCampgrounds(campId);
		long campgroundId = campById.get(0).getCampgroundId();
		List<Campground> result = this.campgroundDAO.getAllCampgrounds(campId);

		assertEquals(campById.get(0).getCampgroundId(), result.get(0).getCampgroundId());

	}
	
	@Test
	public void test_get_camground_by_name() {
		List<Campground> campground = this.campgroundDAO.getAllCampgrounds(1L);
		String campName = campground.get(0).getName();
		Campground campByName = campgroundDAO.getCampgroundByName(campName);

		campground.get(0).getName();
		assertEquals(campground.get(0).getName(), campByName.getName());
	}
}
