package com.models.JDBC;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.models.Campground;
import com.models.CampgroundDAO;
import com.models.Park;
import com.models.Reservation;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgrounds(long parkId) {
		List<Campground> campgrounds = new ArrayList<Campground>();
		String sqlAllCampInPark = "SELECT * from campground WHERE park_id = ? ";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlAllCampInPark, parkId);

		while (sqlRowSet.next()) {
			campgrounds.add(mapToCampground(sqlRowSet));
		}
		return campgrounds;
	}
	
	public Campground getCampgroundById(long campgroundId) {
		Campground campground = new Campground();
		String sqlFindCampgroundById = "SELECT * from campground WHERE campground_id = ? ";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlFindCampgroundById, campgroundId);
		if (sqlRowSet.next()) {
			campground = mapToCampground(sqlRowSet);
		}
		return campground;
		
	}
		
		@Override
		public Campground getCampgroundByName(String name) {
			Campground campground = new Campground();
			String sqlFindCampgroundByName = "SELECT * from campground WHERE name = ? ";
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlFindCampgroundByName, name);
			if (sqlRowSet.next()) {
				campground = mapToCampground(sqlRowSet);
			}
			return campground;
		

	}
		
	public long getCampgroundIdBySiteId(long siteId) {
		Campground campground = null;
		long campgroundId;
		String sqlFindCampgroundId = "SELECT campground_id from site where site_id = ?";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlFindCampgroundId, siteId);
		if (sqlRowSet.next()) {
			campground = mapToCampground(sqlRowSet);
		}
		
		return campground.getCampgroundId();
		
	}

		
	public void searchCampgroundReservations(long siteChoice, LocalDate arrival, LocalDate departure) {
		
		
	}

	private Campground mapToCampground(SqlRowSet sqlRowSet) {
		Campground tempCamp = new Campground();
		int openMonth = sqlRowSet.getInt("open_from_mm");
		int endMonth = sqlRowSet.getInt("open_to_mm");
		String openMonthWord = Month.of(openMonth).toString();
		String endMonthWord = Month.of(endMonth).toString();

		tempCamp.setCampgroundId(sqlRowSet.getLong("campground_id"));
		tempCamp.setName(sqlRowSet.getString("name"));
		tempCamp.setStartDate(openMonthWord);
		tempCamp.setEndDate(endMonthWord);
		tempCamp.setDailyFee(new BigDecimal(sqlRowSet.getString("daily_fee")).setScale(2));
		tempCamp.setParkId(sqlRowSet.getLong("park_id"));
		

		return tempCamp;
		//TODO: figure out how to get the month  name to return from the month code
	}
	
	public String toString(Campground campground) {
		return String.format("%-25s %-10s %-15s$%-15s", campground.getName(),
				campground.getStartDate(), campground.getEndDate(), campground.getDailyFee());
	}

	@Override
	public long getCampgroundIdBySiteId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
