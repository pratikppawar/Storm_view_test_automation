package com.storm.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.storm.common.UserProperty;
import com.storm.pages.AmbariDashboardPage;
import com.storm.pages.AmbariLoginPage;

public class DashboardTest extends BaseTest {

	protected AmbariLoginPage loginPage = new AmbariLoginPage();
	
	protected AmbariDashboardPage ambariDashboardPage = new AmbariDashboardPage();
	
	@BeforeTest
	public void setupTest() {
		try {
			// login navigate to the view
			String username = propertiesUtil.getProperty(UserProperty.AMBARI_LOGIN, "admin");
			String password = propertiesUtil.getProperty(UserProperty.AMBARI_PASSWORD, "admin");
			loginPage.doLogin(username, password);
			Assert.assertEquals(loginPage.dashboardMetricsTab.getText(), "Metrics");
			// Make sure the view is present, if not present then add the view
			
			
			// Go to the view 
			
		} catch(Exception e) {
			logger.error("Problem while setting up DashboardTest, "+e.getMessage(), new Throwable(e));
			
			// TODO find a better way to exit
			System.exit(1);
		}
	}
	
	@Test(priority=1)
	public void navigateToStormView() {
		System.out.println("test executed");
		
	}
	
	@AfterMethod
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException  
	{ 
		if (testResult.getStatus() == ITestResult.FAILURE) 
		{ 
			System.out.println(testResult.getStatus()); 
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
			FileUtils.copyFile(scrFile, new File("./stormUI/Screenshots/"+testResult.getName()+".png"));
		}
	}
	
//	@AfterTest
//	public void tearDownTest() {
//		// TODO Need to see if view needs to be removed
//		driver.quit();
//		// logout
//	}
	
}
