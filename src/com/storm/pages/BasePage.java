package com.storm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.storm.common.DriverMgr;
import com.storm.common.PropertiesUtil;

public abstract class BasePage {

	protected WebDriver driver;
	
	protected PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
	
	public BasePage() {
		driver = DriverMgr.getDriver();
		PageFactory.initElements(driver, this);
	}
}
