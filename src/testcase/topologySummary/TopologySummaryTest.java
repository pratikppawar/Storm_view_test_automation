package testcase.topologySummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utility.Utils;
import common.Navigator;

public class TopologySummaryTest {


	@BeforeTest
	public void beforeTest(){
		TopologySummaryData.logger.info("Inside beforeTest method for pre test processing");
		if(!Navigator.isStormViewAvailable){
			TopologySummaryData.logger.info("Leaving beforeTest method after pre test processing : Storm monitoring not available");
			throw new SkipException("Storm Monitoring page not available : skipping Topology summary test");
		}
		TopologySummaryData.logger.info("Leaving beforeTest method after pre test processing");	
	}

	@Test(priority=1)
	void checkSummaryTable(){
		try {
			
			TopologySummaryData.logger.info("Check summary test begins");
			WebDriver driver = Navigator.driver;
			
			Utils.loadingWait();
			WebElement table = (new WebDriverWait(driver, 100))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("summaryTable")));
			TopologySummaryData.topoLogyListView = Utils.uiTableToArrayList(table);

			if(TopologySummaryData.topoLogyListView.size() == 1 && TopologySummaryData.topoLogyListUI.isEmpty()){
				if(!TopologySummaryData.topoLogyListView.get(0).containsValue("No topology found")){
					Assert.assertFalse(true, "Summary table test failed");
				}
			}else{
				@SuppressWarnings("unchecked")
				boolean test = Utils.testTable(TopologySummaryData.topoLogyListView, (ArrayList<HashMap<String, Object>>) TopologySummaryData.topoLogyListUI.get(0).get("topologies"), TopologySummaryData.searchIdTopology);

				if(!test){
					Assert.assertFalse(true, "Summary table test failed");
				}
			}
			Utils.checkTopologySummaryInfo();

		} catch (Exception e) {
			TopologySummaryData.logger.error("Check summary test : Summary table test failed");
			Assert.assertFalse(true, "Summary table test failed \nReason :"+e.getMessage());
		}
		TopologySummaryData.logger.info("Check summary test begins");
	}
	@Test(priority=3)
	public void testPopUp(){
		try {			
			TopologySummaryData.logger.info("Tests deploy topology popup test begins");
			WebDriver driver = Navigator.driver;
			WebDriverWait wait = (new WebDriverWait(driver, 10));
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("button")));
			element.click();
			WebElement popUp = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("modal-dialog")));
			List<WebElement> textFields = popUp.findElements(By.tagName("label"));

			if(textFields.size() == 5){

				List<WebElement> btns = popUp.findElements(By.tagName("button"));
				btns.get(2).click();
				List<WebElement> errorFields = popUp.findElements(By.xpath("//div[@data-error='']"));
				int erCount = 0;
				for (WebElement webElement : errorFields) {
					if((webElement.getText()).equals("This field is required")){
						erCount++;
					}
				}
				if(erCount != 3){
					Assert.assertFalse(true, "Deploy topology pop up test failed");
				}			
				btns.get(1).click();

				element = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("button")));
				element.click();

				popUp = driver.findElement(By.className("modal-dialog"));
				textFields = popUp.findElements(By.className("form-group"));
				for (WebElement webElement : textFields) {

					switch (webElement.findElement(By.tagName("label")).getText()) {
					case "Name*":
						webElement.findElement(By.tagName("input")).sendKeys("Topology1");
						break;
					case "Jar*":
						webElement.findElement(By.tagName("input")).sendKeys("/resources/storm-starter-0.0.1-storm-0.9.0.1.jar");
						break;
					case "Topology Class*":
						webElement.findElement(By.tagName("input")).sendKeys("storm.starter.WordCountTopology");
						break;
					}				
				}
				btns = popUp.findElements(By.tagName("button"));
				btns.get(2).click();
				Utils.loadingWait();
				Assert.assertFalse(Utils.testAlertText("Please wait till topology is being deployed"),
						"Deploy topology pop up alert message test failed");
			}else{
				TopologySummaryData.logger.error("Tests deploy topology popup test : Deploy topology pop up test failed");
				Assert.assertFalse(true, "Deploy topology pop up test failed");
			}
			Utils.checkTopologySummaryInfo();
		} catch (Exception e) {
			//TopologySummaryData.logger.info("Tests deploy topology popup test : Deploy topology pop up test failed", e);
			//Assert.assertFalse(true, "Deploy topology pop up test failed \n Reason : "+e.getMessage());
		}
		TopologySummaryData.logger.info("Tests deploy topology popup test ends");
	}

	@DataProvider
	public Object[][] dataProvider() {
		return new Object[][] {
				new Object[] { new TopologySummaryData() }
		};
	}
}
