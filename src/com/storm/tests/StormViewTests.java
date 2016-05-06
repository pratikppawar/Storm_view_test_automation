package com.storm.tests;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.storm.common.UserProperty;
import com.storm.pages.AmbariDashboardPage;
import com.storm.pages.AmbariLoginPage;
import com.storm.pages.StormViewPage;

public class StormViewTests extends BaseTest{
	
	protected AmbariLoginPage loginPage = new AmbariLoginPage();
	protected AmbariDashboardPage ambariDashboardPage = new AmbariDashboardPage();
	protected StormViewPage stormPage = new StormViewPage();
	
	
	@BeforeTest
	public void setupTest() {
		try {
			// login navigate to the view
			String username = propertiesUtil.getProperty(UserProperty.AMBARI_LOGIN, "admin");
			String password = propertiesUtil.getProperty(UserProperty.AMBARI_PASSWORD, "admin");
			loginPage.doLogin(username, password);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			Assert.assertEquals(loginPage.dashboardMetricsTab.getText(), "Metrics");
			// Make sure the view is present, if not present then add the view
			
			
			// Go to the view 
			System.out.println("test executed");
		} catch(Exception e) {
			logger.error("Problem while setting up DashboardTest, "+e.getMessage(), new Throwable(e));
			
			// TODO find a better way to exit
			System.exit(1);
		}
	}
	
	@Test(priority=1)
	public void navigateToStormView() {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			ambariDashboardPage.goToViewWithName(STORM_VIEW_NAME);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
			String actual=stormPage.topologyListingHeader.getText();
			Assert.assertEquals(actual, "Topology Listing");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=2)
	public void openTopologyView()
	{
		try{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			stormPage.firstTopologyLink.click();
			Thread.sleep(5000);
			Assert.assertEquals(stormPage.topologySummaryHeader.getText(), "TOPOLOGY SUMMARY");
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=3)
	public void deactivateTopology()
	{
		try{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if(stormPage.topologyDeactivateButton.isEnabled())
			{
			stormPage.topologyDeactivateButton.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement deactivateModal = driver.findElement(By.cssSelector("div.modal-footer"));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
			deactivateModal.findElement(By.xpath("./button[2]")).click();
//			stormPage.confirmationMessageYesButton.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
			boolean b = driver.getPageSource().contains("Topology deactivated successfully.");
			Assert.assertTrue(b);
			}else{
				System.out.println("Topology already deactivated");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=4)
	public void activateTopology()
	{
		try{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(7000);
			if(stormPage.topologyActivateButton.isEnabled())
			{
			stormPage.topologyActivateButton.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement deactivateModal = driver.findElement(By.cssSelector("div.modal-footer"));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(2000);
			deactivateModal.findElement(By.xpath("./button[2]")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(4000);
			boolean b = driver.getPageSource().contains("Topology activated successfully.");
			Assert.assertTrue(b);
			}else{
				System.out.println("Topology already Active");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=5)
	public void activatingSystemSummary()
	{
		try{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if(stormPage.systemSummaryButton.getText().equals("OFF"))
			{
			stormPage.systemSummaryButton.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String actual=stormPage.systemSummaryButton.getText();
			Assert.assertEquals(actual, "ON");
			}else{
				System.out.println("System Summary already ON");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=6)
	public void activatingDebugMode()
	{
		try{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if(stormPage.debugButton.getText().equals("OFF"))
			{
			stormPage.debugButton.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("html/body/div[4]/div/div/div[3]/button[2]"));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String actual=stormPage.debugButton.getText();
			Assert.assertEquals(actual, "ON");
			}else{
				System.out.println("Debug Mode already ON");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=7)
	public void navigatetoTopologyListing()
	{
		try{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if(stormPage.topologySummaryHeader.isDisplayed())
			{
			stormPage.breadCrumTopologyListingBtn.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String actual=stormPage.topologyListPageHeader.getText();
			Assert.assertEquals(actual, "Topology Listing");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=8)
	public void verifyStatusofTopology()
	{
		try{
			String actualStatus=stormPage.getTopologyStatus("WordCount");
			if(actualStatus.equals("ACTIVE"))
			{
				boolean bol=driver.findElement(By.xpath(".//*[@id='container']/div/div[1]/div/div/div/div[1]/div[4]/div/button[1]")).getCssValue("disabled").equals("");
				Assert.assertTrue(bol);
			}else{
				boolean bol=driver.findElement(By.xpath(".//*[@id='container']/div/div[1]/div/div/div/div[1]/div[4]/div/button[2]")).isSelected();
				Assert.assertTrue(bol);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=9)
	public void navigateToStormViewHome()
	{
		try{
			stormPage.breadcrumHomeIcon.click();
//			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(4000);
			boolean bol=driver.findElement(By.xpath(".//*[@id='container']/div[1]/div[1]/div[2]/div/div[1]/div/div/div[2]/div/table/tbody/tr[1]/td[1]")).getText().equals("No topology found !");
			Assert.assertFalse(bol);
			Assert.assertEquals(stormPage.topologyListingHeader.getText(), "Topology Listing");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=10)
	public void openRebalanceTopologyWindow()
	{
		try{
		stormPage.firstTopologyLink.click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		stormPage.topologyRebalanceButton.click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
//		Assert.assertTrue(driver.findElement(By.id("ex1")).isDisplayed());
//		Assert.assertTrue(condition);
		Assert.assertEquals(stormPage.rebalanceTopologyWindowHeader.getText(), "Rebalance Topology");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=11)
	public void allRebalancingFieldsDisplayed()
	{
		boolean bol=driver.findElement(By.xpath(".//*[@id='modal-rebalance']/div/div/div[2]/form/div/label")).isDisplayed();
		Assert.assertTrue(bol);
	}
	
	@Test(priority=12)
	public void closingRebalanceTopologyWindow()
	{
		try{
		driver.findElement(By.xpath(".//*[@id='modal-rebalance']/div/div/div[3]/button[1]")).click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		String actual=stormPage.breadCrumTopologyListingBtn.getText();
		Assert.assertEquals(actual, "Topology Listing");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=13)
	public void displayChangeLogLevelList()
	{
		try{
		stormPage.changeLogLevelButton.click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(4000);
		Assert.assertTrue(driver.findElement(By.xpath(".//*[@id='slideContent']/div/div/table/tbody/tr/td[1]/a")).isDisplayed());
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=14)
	public void minimisingContainers()
	{
		try{
		stormPage.minimizeWordCount.click();
		Thread.sleep(2000);
		stormPage.minimizeSpouts.click();
		Thread.sleep(2000);
		stormPage.minimizeBolts.click();
		Thread.sleep(2000);
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=15)
	public void openSpoutStats()
	{
		try{
		stormPage.minimizeSpouts.click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		stormPage.firstSpoutId.click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		Assert.assertEquals(stormPage.spoutStatsHeader.getText(), "SPOUT STATS");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=16)
	public void openBoltStats()
	{
		try{
		stormPage.breadcrumTopologyName.click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		stormPage.firstBoltId.click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		Assert.assertEquals(stormPage.boltStatsHeader.getText(), "BOLT STATS");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(priority=17)
	public void verifyWindowDropDown()
	{
		List<WebElement> element=driver.findElements(By.xpath(".//*[@id='container']/div/div[2]/div[2]/div/div[2]/table/tbody/tr/td[1]"));
		for (int i=1;i<=element.size();i++)
		{
			String displayedWindowName=driver.findElement(By.xpath(".//*[@id='container']/div/div[2]/div[2]/div/div[2]/table/tbody/tr["+i+"]/td[1]")).getText();
			String dropdownWindowName=driver.findElement(By.xpath(".//*[@id='container']/div/div[1]/div/div/div/div[1]/div[1]/select/option["+i+"]")).getText();
			Assert.assertEquals(displayedWindowName, dropdownWindowName);
		}
	}
	
//	@Test(priority=16)
//	public void verifyWindowDropDown()
//	{
//		stormPage.breadcrumTopologyName.click();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		List<WebElement> element=driver.findElements(By.xpath(".//*[@id='container']/div/div[1]/div/div/div/div[1]/div[1]/select"));
//		for(int i=0;i<=element.size();i++)
//		{
//		Select sel=new Select(elem);
//		sel.selectByIndex(arg0);
//		}
//	}
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
