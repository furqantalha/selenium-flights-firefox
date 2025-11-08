package tests;

import base.BaseTest;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;
import pages.FlightsPage;
import pages.HomePage;

import java.util.Iterator;
import java.util.Set;

public class FlightBookingTest extends BaseTest {

    @Test
    public void bookFlightFlow() throws Exception {
        // Open Flipkart Flights
        driver.get("https://www.flipkart.com/");
        HomePage home = new HomePage(driver);

        FlightsPage flights = new FlightsPage(driver);
        home.goToFlights();
        flights.enterFromCity("Chennai");
        flights.enterToCity("Bangalore");
        flights.selectDateByIndex(17);
        flights.clickSearch();
        flights.printCheapestTwoFlights();


        // open Google in a new tab
        ((JavascriptExecutor) driver).executeScript("window.open('https://www.google.com','_blank');");

        //  Switch to new tab
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> it = handles.iterator();
        String originalTab = it.next();
        String newTab = it.next();
        driver.switchTo().window(newTab);

        System.out.println("Opened Google successfully");
        System.out.println("Page Title: " + driver.getTitle());

        // Switch back to Flipkart Flights tab
        driver.switchTo().window(originalTab);
        System.out.println("Back to Flights page: " + driver.getTitle());
    }
}
