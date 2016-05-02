package com.storm.common;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.storm.common.PropertiesUtil;
import com.storm.common.UserProperty;

public class DriverMgr {

	public static WebDriver driver ;
	
	public static WebDriver getDriver() {
		PropertiesUtil propertyUtils = PropertiesUtil.getInstance();
		
//		 read property and create driver at application level;
		if(driver == null) {
			
			URL url = Thread.currentThread().getContextClassLoader().getResource(propertyUtils.getProperty(UserProperty.CHROME_DRIVER_PATH, "driver/chromedriver"));
			
			System.setProperty("webdriver.chrome.driver", url.getPath());
			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("--kiosk");
			driver=new ChromeDriver(chromeOptions);
			
//			driver = new FirefoxDriver();
			
			System.out.println(driver.manage().window().getSize());
			
			driver.manage().window().maximize();
			driver.get(propertyUtils.getProperty(UserProperty.AMBARI_HOST_URL, "http://localhost:8080"));
		}
		
		
		return driver;
	}

}
