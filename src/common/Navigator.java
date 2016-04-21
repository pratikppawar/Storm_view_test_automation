package common;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public final class Navigator {

	public static Logger logger = Logger.getLogger(Navigator.class);
	private static Properties userConfigProperties = getPropertiesLoaded();
	public static String PLATFORM_NAME;
	public static WebDriver driver = getDriversLoaded();
	private static String driverURL;

	public static String uiURL = userConfigProperties.getProperty("test.ui.url") ;
	public static boolean isStormUIUp = true;
	public static boolean isStormViewAvailable = false;
	public static boolean isStormViewCreated = false;
	public static String stormViewName = "Storm_Monitoring";
	public static String baseURL = userConfigProperties.getProperty("test.browser.set.url");

	@BeforeSuite
	public void beforeSuite() {

		logger.info("Start of method beforeTest");
		WebDriver driver = Navigator.driver;
		logger.info("URL - "+baseURL);
		driver.get(baseURL);
		List<WebElement> elementList;
		WebElement element;
		WebDriverWait wait = new WebDriverWait(driver,200L);

		try {
			logger.info("Logging into Ambari UI");

			element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
			elementList = driver.findElements(By.tagName("input"));	
			elementList.get(0).sendKeys((String)userConfigProperties.get("username"));
			elementList.get(1).sendKeys((String)userConfigProperties.get("password"));
			element = driver.findElement(By.tagName("button"));
			element.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='progress progress-striped active']"))));
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("screenshotLogin.png"));
			logger.info("Loggin into Ambari UI succeded");
			/*	driver.navigate().to("ec2-54-86-54-234.compute-1.amazonaws.com:8080/#/main/views/Storm_Monitoring/0.1.0/Storm_Monitoring");
			isStormViewAvailable = true;
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='progress progress-striped active']"))));
			(new WebDriverWait(driver, 200))
			.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));*/
		} catch (Exception e) {
			throw new SkipException("Ambari Login page not available \nReason : "+e.getMessage());
		}

		try {
			try {
				if(!isStormViewAvailable){
					logger.info("Check if storm monitoring view instance is already created and move to that page");
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='icon-th']")));

					element =  driver.findElement(By.xpath("//i[@class='icon-th']"));
					Actions builder = new Actions(driver);
					Action mouseOver =builder.moveToElement(element).build();
					mouseOver.perform(); 

					List<WebElement> el = driver.findElements(By.xpath("//li[@class='ember-view top-nav-dropdown']")).get(4).findElements(By.tagName("li"));
					for (WebElement webElement : el) {
						WebElement listElt = webElement.findElement(By.tagName("a"));
						if(listElt.getText().equals(stormViewName)){
							logger.info("Storm monitoring view is available, going to storm monitoring view page");
							listElt.click();
							wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='progress progress-striped active']"))));
							(new WebDriverWait(driver, 200))
							.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
							isStormViewAvailable = true;
							logger.info("Storm monitoring view instance is created");
							break;
						}						
					}
					logger.info("Storm monitoring view instance is not available, create a new instance");
				}

			} catch (Exception e) {
				logger.error("Loading of storm monitoring view instance failed :\n", e);
			}
			logger.info("isStormViewAvailable="+isStormViewAvailable);
			try {
				if(!isStormViewAvailable){
					logger.info("Creating  Storm monitoring view instance");
					logger.info("Go to admin view to create Storm monitoring view instance");

					element = (new WebDriverWait(driver, 30))
							.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@id='dashboard-view-tab-metrics']")));

					element = (new WebDriverWait(driver, 30))
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn dropdown-toggle']")));
					element.click();
					element = (new WebDriverWait(driver, 30))
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='manage-ambari']")));
					element.click();

					element = (new WebDriverWait(driver, 30))
							.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='viewslist-link ng-isolate-scope']")));
					element.click();
					Thread.sleep(2000);
					List<WebElement> viewList = driver.findElements(By.xpath("//div[@class='panel panel-default ng-isolate-scope']"));
					for (WebElement view : viewList) {
						if(view.getText().contains(stormViewName)){
							view.findElement(By.tagName("i")).click();
							view.findElement(By.tagName("tfoot")).findElement(By.tagName("tr")).findElement(By.tagName("a")).click();
							Thread.sleep(5000);
							element = (new WebDriverWait(driver, 30))
									.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='instanceNameInput']")));
							element.sendKeys("stormViewTest");

							element = (new WebDriverWait(driver, 30))
									.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='displayLabel']")));
							element.sendKeys(stormViewName);

							element = (new WebDriverWait(driver, 30))
									.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='description']")));
							element.sendKeys("View instance for automated testing");

							element = (new WebDriverWait(driver, 30))
									.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn btn-primary pull-right left-margin save-button']")));
							element.click();

							Thread.sleep(5000);

							Actions builder = new Actions(driver); 
							element =  driver.findElement(By.xpath("//i[@class='fa fa-th']"));
							Action mouseOver =builder.moveToElement(element).build();
							mouseOver.perform(); 

							logger.info("Check if creation of storm ambari view instance succeded");

							List<WebElement> el = driver.findElements(By.xpath("//ul[@class='nav navbar-nav navbar-right']")).get(0).findElements(By.tagName("li"));
							for (WebElement webElement : el) {
								WebElement listElt = webElement.findElement(By.tagName("a"));
								if(listElt.getText().equals(stormViewName)){
									listElt.click();
									wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='progress progress-striped active']"))));

									(new WebDriverWait(driver, 200))
									.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
									isStormViewAvailable = true;
									logger.info("Creation of storm ambari view instance succeded");
									break;
								}								
							}
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("Creation of storm ambari view instance failed"+e.getMessage());
				File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File("resources/screenshots/adminview.png"));
			}
		} catch (Exception e) {
			throw new SkipException("Storm Monitoring page not available : skipping Topology summary test\n Reason :"+e.getMessage());
		}
	}

	private static Properties getPropertiesLoaded() {
		logger.info("Loading properties from file config.properties");
		Properties propertyLoader = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream(
					"resources/config.properties");
			propertyLoader.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertyLoader;
	}

	private static WebDriver getDriversLoaded() {
		logger.info("Loading drivers required for selenium testing");
		WebDriver browserDriver = null;

		try {
			String browserName = userConfigProperties.getProperty("test.browser");
			String osName = System.getProperty("os.name");
			PLATFORM_NAME = osName;
			String osArchitechture = System.getProperty("os.arch");
			DesiredCapabilities capabilities = null;

			if (("firefox").equalsIgnoreCase(browserName)) {
				capabilities = DesiredCapabilities.firefox();
				browserDriver = new FirefoxDriver(capabilities);
				//browserDriver = new HtmlUnitDriver();
			} else if (("chrome").equalsIgnoreCase(browserName)) {
				if (osName.contains("win")
						&& osArchitechture.equalsIgnoreCase("i386")) {
					userConfigProperties
					.getProperty("DRIVER_WIN32");
					driverURL = userConfigProperties
							.getProperty("URL_DRIVER_WIN32");
					System.setProperty("webdriver.chrome.driver",
							userConfigProperties
							.getProperty("driver.chrome.win32.path"));
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
			uiURL = userConfigProperties
					.getProperty("test.ui.url");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return browserDriver;
	}

	@AfterSuite
	public void afterSuite() {
		try {
			logger.info("Starting post test suite processing : remove storm monitoring view created.");
			Navigator.driver.get(Navigator.baseURL);
			Thread.sleep(10000);
			WebElement element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@id='dashboard-view-tab-metrics']")));

			element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn dropdown-toggle']")));
			element.click();
			element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='manage-ambari']")));
			element.click();

			element = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='viewslist-link ng-isolate-scope']")));
			element.click();
			List<WebElement> viewList = driver.findElements(By.xpath("//div[@class='panel panel-default ng-isolate-scope']"));
			for (WebElement webElement : viewList) {
				if(webElement.getText().contains(stormViewName)){
					webElement.findElement(By.tagName("i")).click();
					try {
						webElement.findElement(By.tagName("tbody")).findElement(By.tagName("tr"))
						.findElement(By.tagName("a")).click();
						driver.findElement(By.xpath("//button[@class='btn btn-danger ng-scope']")).click();
						driver.findElement(By.className("modal-dialog")).findElements(By.tagName("button")).get(1).click();
					} catch (Exception e) {
						driver.navigate().to(webElement.findElement(By.tagName("tbody")).findElement(By.tagName("tr"))
								.findElement(By.tagName("a")).getAttribute("href"));
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}	
		Navigator.driver.close();		
	}
}
