package com.storm.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class AmbariDashboardPage extends BasePage {
	public static Logger logger = Logger.getLogger(AmbariDashboardPage.class);
	
	@FindBy(xpath = "html/body/div[1]/div/div/div[1]/div/div/div/ul/li[6]/a/i")
	protected WebElement navBarViewMenu;
	
	@FindBy(xpath = "")
	protected WebElement viewMenuItems;
	
	public void goToViewWithName(String viewName) throws InterruptedException {
		Actions action = new Actions(driver);
		action.moveToElement(navBarViewMenu).perform();
		Thread.sleep(6000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		List<WebElement> elementList = driver.findElements(By.xpath("//div/div/div/ul/li[6]/ul/li"));
		WebElement viewMenuElement = null;
		for(WebElement e : elementList) {
			if(e.getText().equals(viewName)) {
				viewMenuElement = e;
				break;
			}
		}
		
		if(viewMenuElement == null) {
			logger.error("No menu item found with name:"+viewName);
		} else {
			viewMenuElement.click();
		}
		
		
	}
}
