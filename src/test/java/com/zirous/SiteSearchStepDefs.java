package com.zirous;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * Cucumber Step Definitions for the SiteSearch.feature.
 */
public class SiteSearchStepDefs implements En {

    static final String ZIROUS_URL = "https://www.zirous.com/";

    WebDriver driver;

    private String searchInput;

    public SiteSearchStepDefs() {
        Given("^I use the Chrome browser$", () ->
                driver = SiteSearchTest.getDriver(SeleniumTest.Browser.CHROME));

        Given("^I navigate to the Zirous home page$", () ->
                driver.get(ZIROUS_URL));

        When("^I click the magnifying glass in the header$", () -> {
            By locator = By.cssSelector("#menu-main-menu > li.search-toggle-li.wpex-menu-extra > a");
            _wait(2L).until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
        });

        When("^I type \"([^\"]*)\"$", (String input) -> {
            searchInput = input;

            By locator = By.cssSelector("#searchform-header-replace > form > label > input");
            _wait(2L).until(ExpectedConditions.elementToBeClickable(locator));

            Actions builder = new Actions(driver);
            Action inputAction = builder
                    .click(driver.findElement(locator))
                    .sendKeys(input)
                    .pause(Duration.ofMillis(250L))
                    .sendKeys(Keys.ENTER)
                    .build();
            inputAction.perform();
        });

        Then("^the search results should include:$", (DataTable expected) -> {
            Map<String, String> mapOfExpected = expected.asMap(String.class, String.class);
            List<String> expectedArticleTitleList = mapOfExpected.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            By locator = By.className("search-entry-header-title");
            _wait(10L).until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, 0));

            List<String> actualArticleTitleList = driver.findElements(locator)
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            expectedArticleTitleList.forEach(expectedArticleTitle -> {
                assertThat(actualArticleTitleList, hasItem(expectedArticleTitle));
            });
        });

        Then("^the page title should contain the search input$", () ->
                assertEquals("You searched for " + searchInput + " | Zirous", driver.getTitle()));

    }

    // Convenience method for better code readability.
    private WebDriverWait _wait(long timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
}
