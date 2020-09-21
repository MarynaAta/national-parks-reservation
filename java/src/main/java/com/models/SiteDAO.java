package com.models;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {

	public List<Site> getAvaliableSitesByDate();

	List<Site> getAvaliableSitesByDate(long siteChoice, LocalDate arrival, LocalDate departure);

	public List<Site> availSitesByCampground(long campgroundId, String arrivalString, String departureString);

	public String toString(Site site);

	public List<Site> getAllSitesLimit5();

	public List<Site> getPopularSites(long campgroundId);

	public Site getSiteById(long siteId);

}
