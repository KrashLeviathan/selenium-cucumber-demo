package com.zirous;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"classpath:features/SiteSearch.feature"})
public class SiteSearchTest extends SeleniumTest {
    @AfterClass
    public static void afterAll() {
        getDriver().quit();
    }
}
