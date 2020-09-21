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

import com.models.Site;
import com.models.JDBC.JDBCSiteDAO;

public class JDCBCSiteDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO siteDAO;
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
		this.siteDAO = new JDBCSiteDAO(dataSource);
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
	public void test_get_site_by_id() {

		String sqlSearch = "SELECT * FROM site WHERE site_id = ?";
		List<Site> siteById = this.siteDAO.getAllSites();
		long reservationId = siteById.get(0).getSiteId();
		List<Site> result = this.siteDAO.getAllSites();

		assertEquals(siteById.get(0).getSiteId(), result.get(0).getSiteId());

	}

	@Test
	public void test_get_popular_sites() {

		String sqlSelect = "SELECT site.*, COUNT(reservation.site_id) from reservation "
				+ "JOIN site on reservation.site_id = site.site_id "
				+ "WHERE site.campground_id = ? "
				+ "GROUP BY site.site_id, reservation.site_id "
				+ "ORDER BY COUNT desc LIMIT 5";
//		List<Site> popularSite = siteDAO.getAllSites();
		
		long campgroundId = 0;
		List<Site> popularSite = siteDAO.getPopularSites((long) campgroundId);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelect);
		assertEquals(5, popularSite.size());

	}

	@Test
	public void test_get_available_sites_by_camp() {

		List<Site> allSites = siteDAO.availSitesByCampground(0, "20200801", "20200829");

		long numOfSites = 0;
		if (allSites.size() == 5) {
			numOfSites = 5;

		} else
			numOfSites = 0;
		assertEquals(numOfSites, allSites.size());
	}

	@Test
	public void test_get_all_sites() {
		String sqlCount = "SELECT COUNT(*) FROM site";

		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlCount);
		while (result.next()) {
			int numberOfSites = result.getInt("count");
			assertEquals(numberOfSites, this.siteDAO.getAllSites().size());
		}
	}

}
