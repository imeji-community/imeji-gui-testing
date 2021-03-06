package spot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

public class SeleniumTestSuite {
	
	private static WebDriver driver;
	
	/** properties file with required login information for test user and admin **/
	private static Properties properties;	
	public static final String propertiesFileName = "testData.properties";
	
	public static final String qaEdmond = "http://qa-edmond.mpdl.mpg.de/imeji/";
	public static final String qaImeji = "http://qa-imeji.mpdl.mpg.de/";
	
	public static final String testEnvironmentURL = "http://qa-edmond.mpdl.mpg.de/imeji/"; 	
//	public static final String testEnvironmentURL = "http://qa-imeji.mpdl.mpg.de/";
//	public static final String testEnvironmentURL = "http://dev-faces.mpdl.mpg.de/";

	private static final Logger log4j = LogManager.getLogger(SeleniumTestSuite.class.getName());
	
	@Parameters("browserType")
	@BeforeSuite
	public void launchDriver(String browserType) throws MalformedURLException {

		
		log4j.info("Launching driver..");
		log4j.info("Browser is " + browserType);

		setDriver(browserType);
		loadPropertiesFile();
		
		driver.navigate().to(testEnvironmentURL);
		System.out.println("test environment url loaded");
	}

	private void loadPropertiesFile() {
		properties = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

		try {	
			properties.load(input);
		} catch (IOException e) {
			log4j.error("Properties file with login data couldn't be loaded");
			e.printStackTrace();
		} finally {
        	if(input != null) {
        		try {
        			input.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
	}

	/**
	 * Dependent on the specified browserType, the corresponding browser will be launched.
	 * 
	 * @param browserType
	 */
	private void setDriver(String browserType) {
		switch (browserType) {
			case "chrome":
				ChromeOptions options = new ChromeOptions();
				System.setProperty("webdriver.chrome.driver", "/Users/kocar/WebDrivers/chromedriver.exe");
				DesiredCapabilities chrome = DesiredCapabilities.chrome();
				chrome.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new ChromeDriver(); 
				break;
			case "firefox":
				driver = initFirefoxDriver();
				break;
			default:
				log4j.warn("browser : " + browserType
						+ " is invalid. Launching default browser instead (Firefox)..");
				driver = initFirefoxDriver();
		}
		// maximize browser window; only works for firefox & internet explorer; but not chrome
		driver.manage().window().maximize();
		System.out.println("window maximized");
	}

	/**---
	 * Launching Firefox.
	 * @return
	 */
	private WebDriver initFirefoxDriver() {
		log4j.info("Launching Firefox browser..");
		WebDriver driver = new FirefoxDriver();
		
		return driver;
	}

	@AfterSuite
	public static void quitDriver() {
		log4j.info("Quitting driver..");
		driver.quit();
	}	

	public static WebDriver getDriver() {
		return driver;
	}

	public static Properties getProperties() {
		return properties;
	}	

	@BeforeTest
	public void testBeforeTest() {
	}

	@AfterTest
	public void testAfterTest() {
	}
}