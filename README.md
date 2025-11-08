# ✈️ Selenium Flights Automation Framework (Infrrd QA Assignment)

## 📘 Problem Statement

Create an automation framework following the **Page Object Model (POM)** design pattern for a travel website.  
The framework automates the following flow:

1. Navigate to the application.
2. Go to the Flights section.
3. Enter source and destination locations.
4. Select a date for the next month.
5. Click on **Search**.
6. Identify and print the **cheapest** and **second cheapest** flight details.
7. Open a new browser tab within the same session, switch to it, and navigate to **Google**.
8. Add an additional custom scenario of your choice.

---

## 🧠 Solution Overview

This framework is built using **Java**, **Maven**, **TestNG**, and **Selenium WebDriver** (Firefox).  
It demonstrates clean coding practices and modularity using **Page Object Model (POM)** design.

---

## 🧩 Tech Stack

| Component | Description |
|------------|-------------|
| **Language** | Java 23 |
| **Automation Tool** | Selenium WebDriver |
| **Test Framework** | TestNG |
| **Build Tool** | Maven |
| **Browser** | Mozilla Firefox |
| **Design Pattern** | Page Object Model (POM) |

---

## 🗂️ Project Structure

selenium-flights-firefox/
│
├── base/
│ └── BaseTest.java # Common WebDriver setup and teardown
│
├── pages/
│ ├── HomePage.java # Handles navigation to Flights section
│ └── FlightsPage.java # Handles flight search, date selection, price extraction
│
├── tests/
│ └── FlightBookingTest.java # Test case implementing the full flow
│
├── pom.xml # Maven dependencies and build configuration
└── README.md # Project documentation


---

## 🚀 Features Implemented

✅ Launches Firefox browser using WebDriverManager  
✅ Navigates to [Flipkart Flights](https://www.flipkart.com/travel/flights)  
✅ Enters **source** and **destination** cities  
✅ Selects a **date from the next month** dynamically  
✅ Clicks on **Search** and waits for results  
✅ Extracts, sorts, and prints the **cheapest and second cheapest** flights  
✅ Opens **Google in a new browser tab**, fetches and prints the page title  
✅ Switches back to the original Flights tab successfully

---

## 🧪 How to Run

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/furqantalha/selenium-flights-firefox.git
cd selenium-flights-firefox

directly from your IDE (IntelliJ / Eclipse) by running
➡️ FlightBookingTest.java under tests package.

Sample Console Output
Firefox launched successfully!
From City entered: Chennai
To City entered: Bangalore
Selected date index 17 with fare: ₹ 2499
Search button clicked.

Cheapest Flight:
Airline: SpiceJet SG-672
Departure: 07:05
Arrival: 08:05
Price: ₹2,815

Second Cheapest Flight:
Airline: Air India Express IX-2662
Departure: 19:25
Arrival: 20:45
Price: ₹3,149

Opened Google successfully
Page Title: Google
Back to Flights page: Flight bookings, Cheap flights, Lowest Air tickets at Flipkart.com

⚙️ Additional Scenario Added

After fetching flight details,
➡️ the script opens Google in a new tab, prints its page title, and switches back to the Flipkart Flights tab — validating multi-tab session handling.

👨‍💻 Author
**Furqan Talha C**
