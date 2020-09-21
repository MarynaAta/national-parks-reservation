package com.models.JDBC;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.models.Campground;
import com.models.Park;
import com.models.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
		List<Park> allParks = new ArrayList<Park>();
		String sqlAllParks = "SELECT * from park";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlAllParks);
		while (sqlRowSet.next()) {
			allParks.add(mapToPark(sqlRowSet));
		}

		return allParks;
	}

	@Override
	public Park getParkById(long parkId) {
		Park park = new Park();
		String sqlFindParkById = "SELECT * from park WHERE park_id = ? ";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlFindParkById, parkId);
		if (sqlRowSet.next()) {
			park = mapToPark(sqlRowSet);
		}
		return park;
	}
	
	@Override
	public Park getParkByName(String name) {
		Park park = new Park();
		String sqlFindParkByName = "SELECT * from park WHERE name = ? ";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlFindParkByName, name);
		if (sqlRowSet.next()) {
			park = mapToPark(sqlRowSet);
		}
		return park;
	}

	private Park mapToPark(SqlRowSet sqlRowSet) {
		Park tempPark = new Park();
		
		LocalDate estDate = sqlRowSet.getDate("establish_date").toLocalDate(); 
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
		
		long visitors = sqlRowSet.getLong("visitors");
		long area = sqlRowSet.getLong("area");
		DecimalFormat numberFormatter = new DecimalFormat("###,###.###");
		
        // Function call 
         
   		tempPark.setParkId(sqlRowSet.getLong("park_id"));
		tempPark.setName(sqlRowSet.getString("name"));
		tempPark.setLocation(sqlRowSet.getString("location"));
		tempPark.setEstablishDate(dateFormatter.format(estDate));
		tempPark.setArea(numberFormatter.format(area));
		tempPark.setVisitors(numberFormatter.format(visitors));
		tempPark.setDescription(sqlRowSet.getString("description"));
		return tempPark;
	}
}
