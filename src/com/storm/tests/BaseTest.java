package com.storm.tests;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.storm.common.DriverMgr;
import com.storm.common.PropertiesUtil;

public abstract class BaseTest {
	
	public static Logger logger = Logger.getLogger(BaseTest.class);
	
	public static String STORM_VIEW_NAME = "Storm View";

	protected PropertiesUtil propertiesUtil;
	protected WebDriver driver;
	public BaseTest() 
	{
		try {
			propertiesUtil = PropertiesUtil.getInstance();
			driver = DriverMgr.getDriver();
		} catch(Exception e){
			logger.error("Error while creating driver "+e.getMessage(), new Throwable(e));
		}
	}
}
