package com.zirous;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A helper class to manage Selenium WebDriver setup.
 * <p>
 * Every class that runs tests with Selenium should:
 * - extend this class
 * - add an @AfterClass method with getDriver().quit() to perform driver cleanup at the end of the test
 */
public class SeleniumTest {

    /*
     Make sure you install the drivers in the appropriate locations before running any tests.
     Alternatively, you can install them where you want and just set the System properties before
     running the tests.
     */
    private static final String CHROME_DRIVER_PATH = "C:\\Program Files (x86)\\Google\\Chrome\\chromedriver.exe";
    private static boolean CHROME_DRIVER_INITIATED = false;
    private static final String FIREFOX_DRIVER_PATH = "C:\\Program Files\\Mozilla Firefox\\geckodriver.exe";
    private static boolean FIREFOX_DRIVER_INITIATED = false;
    private static final String EDGE_DRIVER_PATH = "C:\\Program Files (x86)\\Microsoft\\Edge\\msedgedriver.exe";
    private static boolean EDGE_DRIVER_INITIATED = false;

    /**
     * Browser options available.
     */
    enum Browser {CHROME, FIREFOX, EDGE}

    private static WebDriver driver;
    private static Browser currentBrowser;

    /**
     * @return the current WebDriver. If there is no current driver, it returns a new ChromeDriver.
     */
    public static WebDriver getDriver() {
        if (null == driver) {
            return getDriver(Browser.CHROME);
        } else {
            return driver;
        }
    }

    /**
     * Manages creation and switching between different browser drivers.
     *
     * @return a WebDriver corresponding to the given browser.
     */
    public static WebDriver getDriver(Browser browser) {
        if (null != driver) {
            if (browser == currentBrowser) {
                return driver;
            }
            driver.quit();
        }

        currentBrowser = browser;

        switch (browser) {
            case CHROME:
                if (!CHROME_DRIVER_INITIATED) {
                    initChromeDriverProperties();
                }
                // https://stackoverflow.com/questions/48450594/selenium-timed-out-receiving-message-from-renderer
                ChromeOptions options = new ChromeOptions();
                options.addArguments("start-maximized");
                options.addArguments("enable-automation");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-extensions");
                options.addArguments("--dns-prefetch-disable");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-browser-side-navigation");
                options.addArguments("--disable-gpu");
                //options.addArguments("--headless"); // only if you are ACTUALLY running headless
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                if (!FIREFOX_DRIVER_INITIATED) {
                    initFirefoxDriverProperties();
                }
                driver = new FirefoxDriver();
                break;
            case EDGE:
                if (!EDGE_DRIVER_INITIATED) {
                    initEdgeDriverProperties();
                }
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Must supply a supported Browser type!");
        }
        return driver;
    }

    private static void initChromeDriverProperties() {
        if (System.getProperty("webdriver.chrome.driver") == null && new File(CHROME_DRIVER_PATH).exists()) {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
            CHROME_DRIVER_INITIATED = true;
        }
        if (System.getProperty("webdriver.chrome.driver") != null) {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        } else {
            System.err.println("WARNING: Cannot locate Chrome WebDriver!");
        }
    }

    private static void initFirefoxDriverProperties() {
        if (System.getProperty("webdriver.gecko.driver") == null && new File(FIREFOX_DRIVER_PATH).exists()) {
            System.setProperty("webdriver.gecko.driver", FIREFOX_DRIVER_PATH);
            FIREFOX_DRIVER_INITIATED = true;
        }
        if (System.getProperty("webdriver.gecko.driver") == null) {
            System.err.println("WARNING: Cannot locate Firefox (gecko) WebDriver!");
        }
    }

    private static void initEdgeDriverProperties() {
        if (System.getProperty("webdriver.edge.driver") == null && new File(EDGE_DRIVER_PATH).exists()) {
            System.setProperty("webdriver.edge.driver", EDGE_DRIVER_PATH);
            EDGE_DRIVER_INITIATED = true;
        }
        if (System.getProperty("webdriver.edge.driver") == null) {
            System.err.println("WARNING: Cannot locate Edge WebDriver!");
        }

    }

}
