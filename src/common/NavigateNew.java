package common;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

public class NavigateNew {

	public static Logger logger = Logger.getLogger(NavigateNew.class);
	public static String PLATFORM_NAME;
	public static WebDriver driver = getDriversLoaded();
	private static String driverURL;

	public static Map<String, String> systemPropertiesMap;
	public static Map<String, String> userPropertiesMap;

	public static String uiURL = systemPropertiesMap.get("test.ui.url");

	public static boolean isYarnServiceUp = true;
	public static boolean isYarnViewAvailable = false;
	public static String baseURL = userPropertiesMap
			.get("test.browser.set.url");

	//@BeforeSuite
	//public void beforeSuite() {
	static{	
	logger.info("Start of method beforeTest");
		WebDriver driver = NavigateNew.driver;
		driver.navigate().to(systemPropertiesMap.get("HOST"));
		List<WebElement> elementList;
		WebElement element;
		WebDriverWait wait = new WebDriverWait(driver, 200L);

		try {
			logger.info("Logging into Ambari UI");

			element = (new WebDriverWait(driver, 200)).until(ExpectedConditions
					.presenceOfElementLocated(By.tagName("input")));
			elementList = driver.findElements(By.tagName("input"));
			elementList.get(0).sendKeys(userPropertiesMap.get("userName"));
			elementList.get(1).sendKeys(userPropertiesMap.get("password"));
			element = driver.findElement(By.tagName("button"));
			element.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By
					.xpath("//div[@class='progress progress-striped active']"))));
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("screenshotLogin.png"));
			logger.info("Loggin into Ambari UI succeded");
			
		} catch (Exception e) {
			throw new SkipException(
					"Ambari Login page not available \nReason : "
							+ e.getMessage());
		}

		try {
			if (!isYarnViewAvailable) {
				logger.info("Loggin into Ambari UI succeded");
				logger.info("Check if Yarn View instance is already created");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath("//i[@class='icon-th']")));
				element = driver.findElement(By.xpath("//i[@class='icon-th']"));
				Actions builder = new Actions(driver);
				Action mouseOver = builder.moveToElement(element).build();
				mouseOver.perform();

				List<WebElement> el = driver
						.findElements(
								By.xpath("//li[@class='ember-view top-nav-dropdown']"))
						.get(4).findElements(By.tagName("li"));
				for (WebElement webElement : el) {
					WebElement listElt = webElement
							.findElement(By.tagName("a"));
					if (listElt.getText().equals("Yarn View")) {
						logger.info("Yarn View is available, going to Yarn view page");
						listElt.click();
					}
				}
				wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By
						.xpath("//div[@class='progress progress-striped active']"))));

				(new WebDriverWait(driver, 200)).until(ExpectedConditions
						.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
				isYarnViewAvailable = true;
			}

		} catch (Exception e) {

		}
		try {
			if (!isYarnViewAvailable) {
				logger.info("Creating  Yarn view instance");
				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//li[@id='dashboard-view-tab-metrics']")));

				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//button[@class='btn dropdown-toggle']")));
				element.click();
				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//a[@id='manage-ambari']")));
				element.click();

				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//a[@class='viewslist-link ng-isolate-scope']")));
				element.click();
				Thread.sleep(2000);
				List<WebElement> viewList = driver
						.findElements(By
								.xpath("//div[@class='panel panel-default ng-isolate-scope']"));
				for (WebElement webElement : viewList) {
					if (webElement.getText().contains("yarn-Views")) {
						webElement.findElement(By.tagName("i")).click();
					}
				}
				Thread.sleep(3);

				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//a[@class='instance-link ng-binding']")));
				element.click();
				Thread.sleep(3);

				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//input[@name='instanceNameInput']")));
				element.sendKeys("yarnView");
				Thread.sleep(3);
				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//input[@name='displayLabel']")));
				element.sendKeys("Yarn Views");
				Thread.sleep(3);
				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//input[@name='description']")));
				element.sendKeys("Yarn View instance for testing");
				Thread.sleep(3);
				element = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath("//button[@class='btn btn-primary pull-right left-margin save-button']")));
				element.click();
				Actions builder = new Actions(driver);
				element = driver.findElement(By.xpath("//i[@class='icon-th']"));
				Action mouseOver = builder.moveToElement(element).build();
				mouseOver.perform();

				List<WebElement> el = driver
						.findElements(
								By.xpath("//li[@class='ember-view top-nav-dropdown']"))
						.get(4).findElements(By.tagName("li"));
				for (WebElement webElement : el) {
					WebElement listElt = webElement
							.findElement(By.tagName("a"));
					if (listElt.getText().equals("Yarn Views")) {
						listElt.click();
					}
				}
				wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By
						.xpath("//div[@class='progress progress-striped active']"))));

				(new WebDriverWait(driver, 200)).until(ExpectedConditions
						.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
				isYarnViewAvailable = true;
				logger.info("Creation of Yarn view instance succeded");
			}
		} catch (Exception e) {
		}

	}

	private static WebDriver getDriversLoaded() {
		logger.info("Loading drivers required for selenium testing");
		WebDriver browserDriver = null;
		try {
			String browserName = systemPropertiesMap.get("test.browser");
			String osName = System.getProperty("os.name");
			PLATFORM_NAME = osName;
			String osArchitechture = System.getProperty("os.arch");
			DesiredCapabilities capabilities = null;

			if (("firefox").equalsIgnoreCase(browserName)) {
				capabilities = DesiredCapabilities.firefox();
				browserDriver = new FirefoxDriver(capabilities);
			} else if (("chrome").equalsIgnoreCase(browserName)) {
				if (osName.contains("win")
						&& osArchitechture.equalsIgnoreCase("i386")) {
					systemPropertiesMap.get("DRIVER_WIN32");
					driverURL = systemPropertiesMap.get("URL_DRIVER_WIN32");
					System.setProperty("webdriver.chrome.driver",
							systemPropertiesMap.get("driver.chrome.win32.path"));
				}
				capabilities = DesiredCapabilities.chrome();
				browserDriver = new ChromeDriver(capabilities);
			} else if (("internetexplorer").equalsIgnoreCase(browserName)) {
				capabilities = DesiredCapabilities.internetExplorer();
				browserDriver = new InternetExplorerDriver(capabilities);
			} else if (("opera").equalsIgnoreCase(browserName)) {
				System.out.println("Opera is currently not supported");
				System.exit(0);
			}
			logger.info("Loading of driver succeded, now loading Ambari login page");
			browserDriver.navigate().to(systemPropertiesMap.get("HOST"));
			uiURL = systemPropertiesMap.get("test.ui.url");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return browserDriver;
	}

	@AfterSuite
	public void afterSuite() {/*
		try {
			logger.info("Starting post test suite processing : remove Yarn view created.");
			NavigateToYarnView.driver.get(NavigateToYarnView.baseURL);
			Thread.sleep(10000);
			WebElement element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath("//li[@id='dashboard-view-tab-metrics']")));

			element = (new WebDriverWait(driver, 30)).until(ExpectedConditions
					.presenceOfElementLocated(By
							.xpath("//button[@class='btn dropdown-toggle']")));
			element.click();
			element = (new WebDriverWait(driver, 30)).until(ExpectedConditions
					.presenceOfElementLocated(By
							.xpath("//a[@id='manage-ambari']")));
			element.click();

			element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath("//a[@class='viewslist-link ng-isolate-scope']")));
			element.click();
			List<WebElement> viewList = driver
					.findElements(By
							.xpath("//div[@class='panel panel-default ng-isolate-scope']"));
			for (WebElement webElement : viewList) {
				if (webElement.getText().contains("Yarn View")) {
					webElement.findElement(By.tagName("i")).click();
					driver.findElement(
							By.xpath("//a[@href='#/views/Storm_Monitoring/versions/1.0.0/instances/INSTANCE_1/edit']"))
							.click();
				}
			}
			driver.findElement(
					By.xpath("//button[@class='btn disabled btn-default btn-delete-instance ng-scope']"))
					.click();

		} catch (Exception e) {
			// TODO: handle exception
		}
		NavigateToYarnView.driver.close();
	*/}

	
}