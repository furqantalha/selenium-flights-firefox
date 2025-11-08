package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;

    private By flightsTab = By.xpath("/html/body/div[1]/div/div[1]/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/div[1]/div/div/div/div/div[1]/a[4]/div/div/div/div");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void goToFlights() {
        driver.findElement(flightsTab).click();
    }
}
