package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.davidmoten.text.utils.WordWrap;
import org.springframework.jdbc.core.JdbcTemplate;

import com.models.Campground;
import com.models.CampgroundDAO;
import com.models.Park;
import com.models.ParkDAO;
import com.models.Reservation;
import com.models.ReservationDAO;
import com.models.Site;
import com.models.SiteDAO;
import com.models.JDBC.JDBCCampgroundDAO;
import com.models.JDBC.JDBCParkDAO;
import com.models.JDBC.JDBCReservationDAO;
import com.models.JDBC.JDBCSiteDAO;
import com.techelevator.view.Menu;

public class CampgroundCLI {

	public static final String RESERVATION_MENU_SPACING = "%-10s %-10s %-30s %-15s %-18s %-1s";
	private static final String BACK_TO_ALL_PARKS = "Back to All Parks";
	private static final String MAIN_MENU_OPTION_PARKS = "View Parks";
	private static final String MAIN_MENU_OPTION_RES_SEARCH = "Search For Reservation";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS,
			MAIN_MENU_OPTION_RES_SEARCH, "Quit" };

	private static final String PARK_MENU_OPTIONS_PARKS = "View Parks";
	private static final String PARK_MENU_OPTIONS_CAMPGROUNDS = "View Campgrounds at this Park";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTIONS_PARKS,
			PARK_MENU_OPTIONS_CAMPGROUNDS, "Back" };
	private static final String CAMPGROUND_MENU_OPTIONS_ALL_CAMPGROUND = null;
	private static final String CAMPGROUND_MENU_OPTIONS_AVAILABLE_RESERVATION = null;
	private static final String CAMPGROUND_MENU_OPTIONS_ALL_CAMPGROUND_BACK = null;
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTIONS_ALL_CAMPGROUND,
			CAMPGROUND_MENU_OPTIONS_AVAILABLE_RESERVATION, CAMPGROUND_MENU_OPTIONS_ALL_CAMPGROUND_BACK };

	private Scanner userInput = new Scanner(System.in);
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;
	private SiteDAO siteDAO;
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		header();
		application.mainMenu();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);

		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
	}

	public void mainMenu() {

		String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

		while (!choice.equals("Quit")) {
			if (choice.equals(MAIN_MENU_OPTION_PARKS)) {
				handleParks();
			} else if (choice.equals(MAIN_MENU_OPTION_RES_SEARCH)) {
				findExistingRes();
			}
		}
		System.exit(0);
	}

	private void handleParks() {

		while (!MAIN_MENU_OPTIONS.equals("Back")) {

			List<Park> allParks = parkDAO.getAllParks();

			String[] parkNames = new String[allParks.size() + 1];
			// listCampgrounds(allCampgrounds);
			for (int i = 0; i < allParks.size(); i++) {
				parkNames[i] = allParks.get(i).getName();
			}
			parkNames[parkNames.length - 1] = "Back";

			System.out.println("*** Select a Park for Details ***");

			String selectedPark = (String) menu.getChoiceFromOptions(parkNames);

			while (!selectedPark.equals("Back")) {
				Park parkDetails = parkDAO.getParkByName(selectedPark);
				parkDetails(parkDetails);

				System.out.println("*** Select a Command ***");

				String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);

				while (!choice.equals("Back")) {
					if (choice.equals(PARK_MENU_OPTIONS_CAMPGROUNDS)) {
						handleCampgrounds(parkDetails.getParkId(), parkDetails.getName());

					}
					mainMenu();
				}

			}
			mainMenu();
		}
	}

	private void handleCampgrounds(long parkId, String name) {
		System.out.println("\nCampgrounds at " + name + " National Park");
		System.out.println("\nWhich campground would you like to search for available sites?");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds(parkId);

		Object[] campDetails = new Object[allCampgrounds.size() + 1];
		for (int i = 0; i < allCampgrounds.size(); i++) {
			campDetails[i] = allCampgrounds.get(i);
		}
		campDetails[campDetails.length - 1] = BACK_TO_ALL_PARKS;
		System.out.printf("\n %-1s %-25s %-10s %-15s%-15s", "", "Campground Name", "Open from", "Through", "Daily Fee");
		Object selectedCampground = menu.getChoiceFromOptions(campDetails);

		while (!selectedCampground.equals(BACK_TO_ALL_PARKS)) {
			searchReservations(campgroundDAO.getCampgroundByName(((Campground) selectedCampground).getName())); // searches
																												// for
																												// reservations
		}
		handleParks();
	}

	private void searchReservations(Campground campground) {
		System.out.print("\nWhat is your arrival date? (YYYYMMDD) ");
		String arrivalString = userInput.next();

		System.out.print("What is your departure date? (YYYYMMDD) ");
		String departureString = userInput.next();

		List<Site> availSites = siteDAO.availSitesByCampground(campground.getCampgroundId(), arrivalString,
				departureString);

		while (true) {
			if (availSites.size() > 0) {
				handleReservations(availSites, arrivalString, departureString);
			} else {
				System.out.print("No available sites for " + campground.getName());
				mainMenu();
			}
		}

	}

	private void handleReservations(List<Site> availSites, String arrival, String departure) {
		System.out.println("\nAvailable Sites");
		System.out.printf("\n%-2s %-10s %-12s %-15s %-15s %-15s", "", "Site No.", "Max Occup.", "Accessible?",
				"Max RV Length", "Utilities?");

		Site[] siteDetails = new Site[availSites.size()];
		for (int i = 0; i < availSites.size(); i++) {
			siteDetails[i] = availSites.get(i);
		}

		Site selectedSite = (Site) menu.getChoiceFromOptions(siteDetails);

		int arrivalYear = Integer.parseInt(arrival.substring(0, 4));
		int arrivalMonth = Integer.parseInt(arrival.substring(4, 6));
		int arrivalDay = Integer.parseInt(arrival.substring(6));

		int depYear = Integer.parseInt(departure.substring(0, 4));
		int depMonth = Integer.parseInt(departure.substring(4, 6));
		int depDay = Integer.parseInt(departure.substring(6));

		LocalDate arrivalDate = LocalDate.of(arrivalYear, arrivalMonth, arrivalDay);

		LocalDate departureDate = LocalDate.of(depYear, depMonth, depDay);

		System.out.print("Please enter your name for the reservation: ");
		String reservationName = userInput.next();

		Long resId = reservationDAO.makeReservation(selectedSite.getSiteId(), reservationName, arrivalDate,
				departureDate);
		System.out.println("Hooray! Your resrvation is made. Your reservation ID is " + resId);
		reservationDetails(reservationDAO.getReservationById(resId), selectedSite.getSiteNum());
		mainMenu();
	}

	private void listParks(List<Park> parks) {
		System.out.println();
		if (parks.size() > 0) {
			for (Park park : parks) {
				System.out.println(park.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void listCampgrounds(List<Campground> campgrounds) {
		System.out.println();
		if (campgrounds.size() > 0) {
			for (Campground campground : campgrounds) {
				System.out.println(campground.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void listReservations(List<Reservation> reservations) {
		System.out.println();
		if (reservations.size() > 0) {
			for (Reservation reservation : reservations) {
				System.out.println(reservation.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void listSites(List<Site> sites) {
		System.out.println();
		if (sites.size() > 0) {
			for (Site site : sites) {
				siteDetails(site);
			}
		} else if (sites.size() == 0) {
			System.out.println("\n*** No results ***");
		}
	}

	private void parkDetails(Park park) {

		System.out.println();
		if (park != null) {
			System.out.println(park.getName() + " National Park\nLocation:\t\t" + park.getLocation()
					+ "\nEstablished:\t\t" + park.getEstablishDate() + "\nArea:\t\t\t" + park.getArea() + " sq km "
					+ "\nAnnual Visitors:\t" + park.getVisitors() + "\n\n"
					+ WordWrap.from(park.getDescription()).maxWidth(90).insertHyphens(true).wrap());

		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void campgroundDetails(Campground campground) {
		System.out.println();
		if (campground != null) {
			System.out.println(campground.getCampgroundId() + "\t" + campground.getName() + "\t"
					+ campground.getStartDate() + "\t" + campground.getEndDate() + "\t$" + campground.getDailyFee());
		} else {
			System.out.println("\n*** No results ***");
		}

	}

	private void reservationDetails(Reservation reservation, long siteNum) {

		System.out.printf(RESERVATION_MENU_SPACING + "\n", "Res ID", "Site No.", "Reservation Name", "Arrival Date",
				"Departure Date", "Date Booked");
		if (reservation != null) {

			System.out.printf(RESERVATION_MENU_SPACING, reservation.getReservationId(), siteNum, reservation.getName(),
					reservation.getStartDate(), reservation.getEndDate(), reservation.getBookingDate());
		}
	}

	private void siteDetails(Site site) {
		if (site != null) {
			System.out.printf("%-10s %-12s %-15s %-15s %-15s \n", site.getSiteNum(), site.getMaxOccupancy(),
					site.getAccessible(), site.getMaxRvLength(), site.getUtilities());

		} else {
			System.out.println("\n *** No results ***");
		}
	}

	private void findExistingRes() {
		System.out.println("Please input your existing reservation number for details on your reservation");
		String reservationId = userInput.next();
		long resId = Integer.parseInt(reservationId);
		Reservation reservation = reservationDAO.getReservationById(resId);
		Site site = siteDAO.getSiteById(reservation.getSiteId());
		System.out.println("\nYour reservation details: ");
		reservationDetails(reservation, site.getSiteNum());
		System.out.println("\n");
		mainMenu();
	}

	private static void header() {
		System.out.println();

		System.out.println("##   ##  ######    #####                                              ");
		System.out.println("###  ##  ##   ##  ##   ##                                             ");
		System.out.println("###  ##  ##   ##  ##                 #####    ######  ### ##   ###### ");
		System.out.println("## # ##  ######    #####            ##       ##   ##  ## # ##  ##   ##");
		System.out.println("## # ##  ##            ##           ##       ##   ##  ## # ##  ##   ##");
		System.out.println("##  ###  ##       ##   ##           ##       ##  ###  ## # ##  ##   ##");
		System.out.println("##   ##  ##        #####     ##      #####    ### ##  ##   ##  ###### ");
		System.out.println("                                                               ##     ");
		System.out.println("                                                               ##     ");
	}

}
