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
            System.out.println("✅ From City entered: " + city);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to enter From City: " + e.getMessage());
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
            System.out.println("✅ To City entered: " + city);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to enter To City: " + e.getMessage());
        }
    }


    public void selectLowestFareDate() {
        try {
            // 1️⃣ Open the calendar
            WebElement calInput = wait.until(ExpectedConditions.elementToBeClickable(calendarInput));
            calInput.click();

            // 2️⃣ Click the Next Month button (arrow)
            By nextMonthButton = By.xpath("/html/body/div/div/div[2]/div[1]/div/div[2]/div/div[2]/form/div/div[3]/div[1]/div[2]/div/div/div/div/table[2]/thead/tr[1]/th[3]/div/button");
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextMonthButton));
            nextBtn.click();

            // 3️⃣ Wait for next month to load
            Thread.sleep(1500);
            // Wait until some date buttons are visible

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("button.pl8ttv")));
            List<WebElement> buttons = driver.findElements(By.cssSelector("button.pl8ttv"));

            int lowestFare = Integer.MAX_VALUE;
            WebElement lowestButton = null;

            for (WebElement btn : buttons) {
                try {
                    WebElement priceDiv = btn.findElement(By.cssSelector("div.tmT\\+ZQ"));
                    String text = priceDiv.getText().replaceAll("[^0-9]", "");
                    if (!text.isEmpty()) {
                        int fare = Integer.parseInt(text);
                        if (fare < lowestFare) {
                            lowestFare = fare;
                            lowestButton = btn;
                        }
                    }
                } catch (Exception e) {
                    // ignore invalid buttons
                }
            }

            if (lowestButton != null) {
                // Scroll into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", lowestButton);
                Thread.sleep(1000);

                // Try normal click first
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(lowestButton));
                    lowestButton.click();
                } catch (Exception e) {
                    // Fallback to JS click if normal click fails due to overlay
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", lowestButton);
                }

                System.out.println("✅ Clicked date with lowest fare: ₹" + lowestFare);
            } else {
                System.out.println("⚠️ No fare button found to click.");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error selecting lowest fare date: " + e.getMessage());
        }
    }



    public void clickSearch() {
        try {
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
            searchBtn.click();
            System.out.println("✅ Search button clicked.");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to click Search: " + e.getMessage());
        }
    }
    public void printTopTwoFlights() {
        try {
            // Wait for all flight cards to load
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("div.eivht0"))); // main container for each flight

            List<WebElement> flights = driver.findElements(By.cssSelector("div.eivht0"));

            if (flights.isEmpty()) {
                System.out.println("⚠️ No flight results found!");
                return;
            }

            // Store each flight with its price
            List<Map.Entry<Integer, WebElement>> flightList = new ArrayList<>();

            for (WebElement flight : flights) {
                try {
                    // Extract price
                    WebElement priceEl = flight.findElement(By.cssSelector("div.O\\+irE2"));
                    String priceText = priceEl.getText().replaceAll("[^0-9]", "");
                    if (!priceText.isEmpty()) {
                        int price = Integer.parseInt(priceText);
                        flightList.add(Map.entry(price, flight));
                    }
                } catch (Exception e) {
                    // skip invalid ones
                }
            }

            if (flightList.isEmpty()) {
                System.out.println("⚠️ Could not find any valid flight prices!");
                return;
            }

            // Sort by price ascending
            flightList.sort(Map.Entry.comparingByKey());

            // Top 1st and 2nd flights
            WebElement firstFlight = flightList.get(0).getValue();
            WebElement secondFlight = flightList.size() > 1 ? flightList.get(1).getValue() : null;

            System.out.println("✈️ First Cheapest Flight:");
            printFlightDetails(firstFlight);

            if (secondFlight != null) {
                System.out.println("\n✈️ Second Cheapest Flight:");
                printFlightDetails(secondFlight);
            } else {
                System.out.println("\n⚠️ Only one flight result found.");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error finding top two flights: " + e.getMessage());
        }
    }

    // Helper: extract and print details of one flight
    private void printFlightDetails(WebElement flight) {
        try {
            String airline = flight.findElement(By.cssSelector("div.jvoo4s span")).getText();
            String flightCode = flight.findElement(By.cssSelector("div.jvoo4s span:nth-of-type(2)")).getText();
            String departTime = flight.findElement(By.cssSelector("span.tMdhpV")).getText();
            String arriveTime = flight.findElement(By.cssSelector("span.t3y8cp")).getText();
            String price = flight.findElement(By.cssSelector("div.O\\+irE2")).getText();

            System.out.println("Airline: " + airline + " " + flightCode);
            System.out.println("Departure: " + departTime);
            System.out.println("Arrival: " + arriveTime);
            System.out.println("Price: " + price);

        } catch (Exception e) {
            System.out.println("⚠️ Could not extract details for one flight: " + e.getMessage());
        }
    }


}
