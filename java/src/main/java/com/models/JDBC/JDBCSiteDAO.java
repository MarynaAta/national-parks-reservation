package com.models.JDBC;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.models.Campground;
import com.models.Park;
import com.models.Reservation;
import com.models.Site;
import com.models.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcSpecial;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcSpecial = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Site getSiteById(long siteId) {
		String sqlSearch = "SELECT * FROM site WHERE site_id = ?";
		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlSearch, siteId);

		if (result.next()) {
			return mapRowToSite(result);
		}
		return null;
	}

	@Override
	public List<Site> getAvaliableSitesByDate(long siteChoice, LocalDate arrival, LocalDate departure) {
		List<Site> availableSites = new ArrayList<Site>();
		String sqlGetAvailableSites = "SELECT * FROM reservation A \r\n" + "JOIN site B ON A.site_id = B.site_id \r\n"
				+ "JOIN campground C on B.campground_id = C.campground_id \r\n"
				+ "WHERE B.site_id = ? AND (from_date, to_date) OVERLAPS (?, ?)";

		String sqlSelect = "SELECT * FROM site WHERE campground_id = :id AND site_id = :siteId "
				+ "NOT IN (SELECT site_id FROM reservation WHERE (from_date, to_date) OVERLAPS ( :dates ) )";

		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlSelect, siteChoice, arrival, departure);

		while (result.next()) {

			Site newSite = mapRowToSite(result);
			availableSites.add(newSite);
		}
		return availableSites;
	}

	@Override
	public List<Site> getPopularSites(long campgroundId) {
		List<Site> top5 = new ArrayList<Site>();
		String sqlSelect = "SELECT site.*, COUNT(reservation.site_id) from reservation "
				+ "JOIN site on reservation.site_id = site.site_id "
				+ "WHERE site.campground_id = ? "
				+ "GROUP BY site.site_id, reservation.site_id "
				+ "ORDER BY COUNT desc LIMIT 5";

		SqlRowSet top5Sites = jdbcTemplate.queryForRowSet(sqlSelect, campgroundId);
		while (top5Sites.next()) {
			Site tempSite = mapRowToSite(top5Sites);
			top5.add(tempSite);
		}
		
		if (top5.size() > 0) {
			return top5;
		} else if (top5.size() == 0) {
			top5 = getAllSitesLimit5();
		}

		return top5;
	}

	public List<Site> availSitesByCampground(long campgroundId, String arrival, String departure) {
		int arrivalYear = Integer.parseInt(arrival.substring(0, 4));
		int arrivalMonth = Integer.parseInt(arrival.substring(4, 6));
		int arrivalDay = Integer.parseInt(arrival.substring(6));

		int departureYear = Integer.parseInt(departure.substring(0, 4));
		int departureMonth = Integer.parseInt(departure.substring(4, 6));
		int departureDay = Integer.parseInt(departure.substring(6));

		Set<LocalDate> dates = new HashSet<LocalDate>();
		dates.add(LocalDate.of(arrivalYear, arrivalMonth, arrivalDay));
		dates.add(LocalDate.of(departureYear, departureMonth, departureDay));

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("dates", dates);
		parameters.addValue("campId", campgroundId);

		String sqlSelect = "SELECT s.*, (COUNT(r.site_id)) from site s "
				+ "LEFT OUTER JOIN reservation r on r.site_id = s.site_id "
				+ "JOIN campground c on s.campground_id = c.campground_id "
				+ "WHERE s.campground_id = :campId AND (s.site_id NOT IN " + 
					"(SELECT site_id FROM reservation WHERE (from_date, to_date) OVERLAPS ( :dates ))) " 
				+ "GROUP BY r.site_id, s.site_id " 
				+ "ORDER BY COUNT desc LIMIT 5";

		SqlRowSet sqlRowSet = jdbcSpecial.queryForRowSet(sqlSelect, parameters);

		List<Site> availSiteList = new ArrayList<Site>();
		while (sqlRowSet.next()) {
			availSiteList.add(mapRowToSite(sqlRowSet));
		}

		return availSiteList;
	}

	private Site mapRowToSite(SqlRowSet sqlRowSet) {
		long siteId = sqlRowSet.getLong("site_id");
		long campgroundId = sqlRowSet.getLong("campground_id");
		long siteNum = sqlRowSet.getLong("site_number");
		long maxOccupancy = sqlRowSet.getLong("max_occupancy");
		long maxRvLength = sqlRowSet.getLong("max_rv_length");

		boolean utilities = sqlRowSet.getBoolean("utilities");
		boolean accessible = sqlRowSet.getBoolean("accessible");

		return new Site(siteId, campgroundId, siteNum, maxOccupancy, accessible, maxRvLength, utilities);
	}

	public List<Site> getAllSites() {
		String sqlAllSites = "SELECT * FROM site";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlAllSites);
		
		List<Site> allSites = new ArrayList<Site>();
		while (sqlRowSet.next()) {
			allSites.add(mapRowToSite(sqlRowSet));
		}
		return allSites;
	}

	public List<Site> getAllSitesLimit5ById(long campgroundID) {
		String sqlAllSites = "SELECT * FROM site WHERE campground_id = ? LIMIT 5";
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlAllSites, campgroundID);

		List<Site> allSites = new ArrayList<Site>();
		while (sqlRowSet.next()) {
			allSites.add(mapRowToSite(sqlRowSet));
		}
		return allSites;
	}

	@Override
	public List<Site> getAvaliableSitesByDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getAllSitesLimit5() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(Site site) {
		// TODO Auto-generated method stub
		return null;
	}
}