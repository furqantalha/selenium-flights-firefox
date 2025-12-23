package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlightsPage {

    WebDriver driver;
    WebDriverWait wait;

    // Flipkart Flights locators
    private By fromInput = By.xpath("//input[@name='0-departcity']");
    private By toInput = By.xpath("//input[@name='0-arrivalcity']");
    private By calendarInput = By.xpath("//input[@name='0-datefrom']");
    private By searchButton = By.xpath("/html/body/div/div/div[2]/div[1]/div/div[2]/div/div[2]/form/div/button");

    public FlightsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void enterFromCity(String city) {
        try {
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(fromInput));
            input.clear();
            input.sendKeys(city);
            Thread.sleep(1000);
            input.sendKeys(Keys.ARROW_DOWN);
            input.sendKeys(Keys.ENTER);
            System.out.println("From City entered: " + city);
        } catch (Exception e) {
            System.out.println("Failed to enter From City: " + e.getMessage());
        }
    }

    public void enterToCity(String city) {
        try {
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(toInput));
            input.clear();
            input.sendKeys(city);
            Thread.sleep(1000);
            input.sendKeys(Keys.ARROW_DOWN);
            input.sendKeys(Keys.ENTER);
            System.out.println("To City entered: " + city);
        } catch (Exception e) {
            System.out.println(" Failed to enter To City: " + e.getMessage());
        }
    }


    public void selectDateByIndex(int index) {
        try {
            // Open the calendar
            WebElement calInput = wait.until(ExpectedConditions.elementToBeClickable(calendarInput));
            calInput.click();

            // Click next month
            By nextMonthButton = By.xpath("/html/body/div/div/div[2]/div[1]/div/div[2]/div/div[2]/form/div/div[3]/div[1]/div[2]/div/div/div/div/table[2]/thead/tr[1]/th[3]/div/button");
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextMonthButton));
            nextBtn.click();

            //  Wait for calendar update
            Thread.sleep(1500);

            //  Collect all date buttons
            List<WebElement> dateButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("Yuvowl")));

            //  Validate index range
            if (index <= 0 || index > dateButtons.size()) {
                System.out.println("Invalid index. Calendar only has " + dateButtons.size() + " days.");
                return;
            }

            // Select the given index (e.g., 3 → 3rd date)
            WebElement selectedDate = dateButtons.get(index - 1);

            // Get price *before clicking* to avoid stale element
            String priceText = "";
            try {
                WebElement price = selectedDate.findElement(By.cssSelector("div.tmT\\+ZQ"));
                priceText = price.getText();
            } catch (Exception ignored) {}

            // Scroll and click safely
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedDate);
            Thread.sleep(800);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(selectedDate));
                selectedDate.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedDate);
            }

            // Print confirmation after click
            System.out.println("Selected date index " + index + (priceText.isEmpty() ? "" : " with fare: " + priceText));

        } catch (Exception e) {
            System.out.println("Failed to select date by index: " + e.getMessage());
        }
    }

    public void clickSearch() {
        try {
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
            searchBtn.click();
            System.out.println("Search button clicked.");
        } catch (Exception e) {
            System.out.println(" Failed to click Search: " + e.getMessage());
        }
    }
    public void printCheapestTwoFlights() {
        try {
            // Wait for flight cards to load
            List<WebElement> flights = new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.cssSelector("div.Sjl9aT")
                    ));

            if (flights.isEmpty()) {
                System.out.println("No flight results found!");
                return;
            }

            // Map flights with their prices
            List<Map.Entry<Integer, WebElement>> flightList = new ArrayList<>();

            for (WebElement flight : flights) {
                try {
                    WebElement priceEl = flight.findElement(By.cssSelector("div.TstEqy"));
                    String priceText = priceEl.getText().replaceAll("[^0-9]", "");
                    if (!priceText.isEmpty()) {
                        int price = Integer.parseInt(priceText);
                        flightList.add(Map.entry(price, flight));
                    }
                } catch (Exception ignored) {}
            }

            if (flightList.isEmpty()) {
                System.out.println("No valid flight prices found!");
                return;
            }

            // Sort by price ascending
            flightList.sort(Map.Entry.comparingByKey());

            WebElement cheapestFlight = flightList.get(0).getValue();
            WebElement secondCheapestFlight = flightList.size() > 1 ? flightList.get(1).getValue() : null;

            System.out.println("Cheapest Flight:");
            printFlightDetails(cheapestFlight);

            if (secondCheapestFlight != null) {
                System.out.println("\nSecond Cheapest Flight:");
                printFlightDetails(secondCheapestFlight);
            } else {
                System.out.println("\nOnly one flight result found.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching flight prices: " + e.getMessage());
        }
    }

    public void printFlightDetails(WebElement flight) {
        try {
            String airline = flight.findElement(By.cssSelector("div.BIa_xg span:nth-of-type(1)")).getText();
            String flightNo = flight.findElement(By.cssSelector("div.BIa_xg span:nth-of-type(2)")).getText();

            String departure = flight.findElement(By.cssSelector("span.e39CO_")).getText();
            String arrival = flight.findElement(By.cssSelector("span.h8wxuH")).getText();

            String price = flight.findElement(By.cssSelector("div.TstEqy")).getText();

            System.out.println("Airline    : " + airline);
            System.out.println("Flight No  : " + flightNo);
            System.out.println("Departure  : " + departure);
            System.out.println("Arrival    : " + arrival);
            System.out.println("Price      : " + price);

        } catch (NoSuchElementException e) {
            System.out.println("Could not extract flight details: " + e.getMessage());
        }
    }
}
