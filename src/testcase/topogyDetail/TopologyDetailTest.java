package testcase.topogyDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeTest;

import testcase.topologySummary.TopologySummaryData;
import utility.Utils;
import common.Navigator;

public class TopologyDetailTest {

	//@Test(priority = 1)
	public void testTopoDetails() {		

		try {
			WebDriver driver = Navigator.driver;

			try {
				TopologyDetailsData.logger.info("Testing of tables of topology details:default begins");
				checkTables();
			} catch (Exception e) {
				TopologyDetailsData.logger.error("Testing of tables of topology details page failed", e);
				Assert.assertFalse(true, "Topology Details test failed \n Reason :"+e.getCause());
			}finally{
				TopologyDetailsData.logger.info("Testing of tables of topology details:default ends");
			}

			String sys = "";
			String win = "";

			try {
				TopologyDetailsData.logger.info("Testing of tables of topology details:change time window begins");
				sys = "0";
				win = "10800";
				Select dropdown = new Select(driver.findElement(By.id("tFrame")));
				dropdown.selectByValue(win);
				//dropdown.selectByVisibleText(win);
				TopologyDetailsData.buildTopoURL(TopologyDetailsData.topoid,sys,win);
				checkTables();
			} catch (Exception e) {
				Assert.assertFalse(true, "Topology Details test failed \n Reason :"+e.getCause());
			}finally{
				TopologyDetailsData.logger.info("Testing of tables of topology details:change time window ends");
			}

			try {
				TopologyDetailsData.logger.info("Testing of tables of topology details:with system bolts begins");
				sys = "1";
				TopologyDetailsData.buildTopoURL(TopologyDetailsData.topoid,sys,win);
				driver.findElement(By.id("sysBolt")).click();
				checkTables();
			} catch (Exception e) {
				Assert.assertFalse(true, "Topology Details test failed \n Reason :"+e.getCause());
			}finally{
				TopologyDetailsData.logger.info("Testing of tables of topology details:with system bolts ends");
			}
		} catch (Exception e) {
			Assert.assertFalse(true, "Topology Details test failed \n Reason :"+e.getCause());
		}
	}

	@DataProvider
	public Object[][] dataProvider() {
		return new Object[][] {
				new Object[] { new TopologyDetailsData() }
		};
	}

	public void checkSummary(String topoTitle, String selValue, String boltChBox){
		try {
			WebDriver driver = Navigator.driver;
			String TopologyTitle = driver.findElement(By.xpath("//h3[@class='topology-title']")).getText();

			Select dropDownList = new Select((new WebDriverWait(driver, 60))
					.until(ExpectedConditions.presenceOfElementLocated(By.tagName("select"))));
			String selectedValue = dropDownList.getFirstSelectedOption().getText();

			WebElement sysBoltCheckBox = driver.findElement(By.id("sysBolt"));

			if(topoTitle.equalsIgnoreCase(TopologyTitle) && selValue.equalsIgnoreCase(selectedValue) && boltChBox.equals(sysBoltCheckBox)){
				Assert.assertFalse(true, "Topology summary test failed");
			}
		} catch (Exception e) {
			Assert.assertFalse(true, "Topology summary test failed \n Reason :"+e.getCause());
		}
	}

	@Test(priority = 2)
	public void checkButtons(){
		try {
			ArrayList<String> failedCases = new ArrayList<String>();
			WebDriver driver = Navigator.driver;
			//checkButtonsState(false, true, true, true);
			try {

				driver.findElement(By.xpath("//button[@id='btnDeactivate']")).click();
				driver.findElement(By.className("modal-dialog")).findElements(By.tagName("button")).get(2).click();
				Utils.loadingWait();
				Utils.testAlertText("Topology deactivated successfully");

				//checkButtonsState(true, false, true, true);
			} catch (Exception e) {
				failedCases.add("Topology deactivation popup test failed");
			}

			try {
				driver.findElement(By.xpath("//button[@id='btnActivate']")).click();
				driver.findElement(By.className("modal-dialog")).findElements(By.tagName("button")).get(2).click();
				Utils.loadingWait();
				Utils.testAlertText("Topology deactivated successfully");			
				//checkButtonsState(false, true, true, true);
			} catch (Exception e) {
				failedCases.add("Topology deactivation popup test failed");
			}			

			try {
				driver.findElement(By.xpath("//button[@id='btnRebalance']")).click();
				WebElement slider = driver.findElement(By.xpath("//div[@name='workers']"));
				int width = slider.getSize().getWidth();
				Thread.sleep(3000);
				Actions move = new Actions(driver);
				Action action = move.dragAndDropBy(slider, width, 0).build();
				action.perform();
				Thread.sleep(3000);
				driver.findElement(By.xpath("//input[@name='waitTime']")).clear();
				driver.findElement(By.xpath("//input[@name='waitTime']")).sendKeys("5");
				driver.findElement(By.className("modal-dialog")).findElements(By.tagName("button")).get(2).click();
				Utils.loadingWait();
				Utils.testAlertText("Topology rebalaanced successfully");
				//checkButtonsState(true, true, false, true);
			} catch (Exception e) {
				failedCases.add("Topology rebalance popup test failed"+e.getMessage());
			}

			try {
				testKillTopology();
				//checkButtonsState(true, true, true, false);
			} catch (Exception e) {
				failedCases.add("Topology kill popup test failed");
			}

			if(!failedCases.isEmpty()){
				Assert.assertFalse(true, "Topology buttons test failed \n Reason :"+failedCases.toString());
			}
		} catch (Exception e) {
			Assert.assertFalse(true, "Topology buttons test failed \n Reason :"+e.getCause());
		}
	}

/*	public void checkButtonsState(boolean activate, boolean deactivate, boolean rebalance, boolean kill){
		WebDriver driver = Navigator.driver;
		TopologyDetailsData.logger.info("Testing of check buttons state begins");

		try {
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			if(!(driver.findElement(By.xpath("//button[@id='btnActivate']")).isEnabled() == activate &&
					driver.findElement(By.xpath("//button[@id='btnDeactivate']")).isEnabled() == deactivate &&
					driver.findElement(By.xpath("//button[@id='btnRebalance']")).isEnabled() == rebalance &&
					driver.findElement(By.xpath("//button[@id='btnKill']")).isEnabled() == kill))
			{
				TopologyDetailsData.logger.info("Testing of check buttons state failed");
				Assert.assertFalse(true, "Topology details page buttons test failed");
			}			
		}
		catch (Exception e) {
			Assert.assertFalse(true, "Topology details page buttons test failed \n Reason :"+e.getCause());
		}finally{
			TopologyDetailsData.logger.info("Testing of check buttons state ends");
		}
	}*/

	public void checkTables(){

		try {
			ArrayList<String> failedCases = new ArrayList<String>();
			WebDriver driver = Navigator.driver;
			boolean test = true;

			try {
				TopologyDetailsData.logger.info("Testing of topology details table begins");
				Utils.loadingWait();
				WebElement topologyDetail = (new WebDriverWait(Navigator.driver, 100))
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='topologyDetail']")));

				TopologyDetailsData.topologyDetailView = Utils.uiTableToArrayList(topologyDetail);

				test = Utils.testTable(TopologyDetailsData.topologyDetailView, TopologyDetailsData.topologyDetailUi, "");

				if(!test){
					TopologyDetailsData.logger.error("Testing of topology details table failed");
					failedCases.add("topology detail test failed");
				}

			} catch (Exception e) {
				TopologyDetailsData.logger.error("Testing of topology details table failed", e);
				failedCases.add("topology detail test failed\n Reason :"+e.getMessage());
			}finally{
				TopologyDetailsData.logger.info("Testing of topology details table ends");
			}

			try {
				TopologyDetailsData.logger.info("Testing of Spout container table begins");
				WebElement spoutAcc = driver.findElement(By.xpath("//a[@href='#collapseOne']"));
				spoutAcc.click();
				spoutAcc.sendKeys(Keys.PAGE_DOWN);				

				List<WebElement> SpoutTable = driver.findElements(By.xpath("//div[@class='statistics-container']"));

				for (WebElement webElement : SpoutTable) {
					try {
						TopologyDetailsData.logger.info("Testing of Spouts summary table begins");
						WebElement spoutsSummaryTable = webElement.findElement(By.xpath("//div[@data-id='SpoutsSummaryTable']"));
						TopologyDetailsData.SpoutsSummaryView = Utils.uiTableToArrayList(spoutsSummaryTable);

						test = Utils.testTable(TopologyDetailsData.SpoutsSummaryView,  TopologyDetailsData.SpoutsSummaryUi, TopologyDetailsData.spoutId);

						if(!test){
							failedCases.add("Spouts summary table test failed");
							TopologyDetailsData.logger.error("Testing of Spouts summary table failed");
						}
					} catch (Exception e) {
						TopologyDetailsData.logger.error("Spouts summary table test failed", e);
						failedCases.add("Spouts summary table test failed"+e.getMessage());
					}finally{
						TopologyDetailsData.logger.info("Testing of Spouts summary table ends");
					}

					try {
						TopologyDetailsData.logger.info("Testing of output stats summary table begins");

						WebElement opstSummaryTable = webElement.findElement(By.xpath("//div[@data-id='OpstSummaryTable']"));
						TopologyDetailsData.optsSummaryView = Utils.uiTableToArrayList(opstSummaryTable);

						test = Utils.testTable(TopologyDetailsData.optsSummaryView,  TopologyDetailsData.optsSummaryUi, TopologyDetailsData.outStatsId);

						if(!test){
							TopologyDetailsData.logger.error("Output stats summary table test failed");
							failedCases.add("Output stats table test failed");
						}
					} catch (Exception e) {
						TopologyDetailsData.logger.error("Output stats summary table test failed", e);
						failedCases.add("Output stats table test failed"+e.getMessage());
					}finally{
						TopologyDetailsData.logger.info("Output stats summary table test ends");
					}

					try {
						TopologyDetailsData.logger.info("Testing of Executor summary table begins");
						WebElement extrSummaryTable = webElement.findElement(By.xpath("//div[@data-id='ExtrSummaryTable']"));
						TopologyDetailsData.exrtSummaryView = Utils.uiTableToArrayList(extrSummaryTable);

						test = Utils.testTable(TopologyDetailsData.exrtSummaryView, TopologyDetailsData.exrtSummaryUi,  TopologyDetailsData.executorId);

						if(!test){
							TopologyDetailsData.logger.error("Executor summary table test failed");
							failedCases.add("Executor summary table test failed");
						}
					} catch (Exception e) {
						TopologyDetailsData.logger.error("Executor summary table test failed", e);
						failedCases.add("Executor summary table test failed"+e.getMessage());
					}finally{
						TopologyDetailsData.logger.info("Executor summary table test ends");
					}

					try {
						TopologyDetailsData.logger.info("Testing of Error summary table begins");
						WebElement errorSummaryTable = webElement.findElement(By.xpath("//div[@data-id='ErrorSummaryTable']"));
						TopologyDetailsData.errorSummaryView = Utils.uiTableToArrayList(errorSummaryTable);
						test = Utils.testTable(TopologyDetailsData.errorSummaryView, TopologyDetailsData.errorSummaryUi,  TopologyDetailsData.errorSummaryId);

						if(!test){
							TopologyDetailsData.logger.error("Testing of Error summary table failed");
							failedCases.add("Error summary table test failed");
						}
					} catch (Exception e) {
						TopologyDetailsData.logger.error("Testing of Error summary table failed", e);
						failedCases.add("Error summary table test failed"+e.getMessage());
					}finally{
						TopologyDetailsData.logger.info("Testing of Error summary table ends");
					}
					spoutAcc.click();
				}

			} catch (Exception e) {
				TopologyDetailsData.logger.error("Spouts summary table test failed",e);
				failedCases.add("Spouts summary table test failed\n Reason :"+e.getMessage());
			}finally{
				TopologyDetailsData.logger.info("Spouts summary table test ends");
			}

			try {
				TopologyDetailsData.logger.info("Testing of bolts summary table begins");
				WebElement boutsAcc = driver.findElement(By.xpath("//a[@href='#collapseTwo']"));
				boutsAcc.sendKeys(Keys.PAGE_DOWN);
				boutsAcc.click();

				WebElement boltsSummaryTable = driver.findElement(By.xpath("//div[@id='BoltsSummaryTable']"));
				TopologyDetailsData.boltsSummaryView = Utils.uiTableToArrayList(boltsSummaryTable);

				test = Utils.testTable(TopologyDetailsData.boltsSummaryView, TopologyDetailsData.boltsSummaryUi,  TopologyDetailsData.boltId);
				boutsAcc.click();

				if(!test){
					TopologyDetailsData.logger.error("Testing of bolts summary table failed");
					failedCases.add("Bolts summary table test failed");
				}	
			} catch (Exception e) {
				TopologyDetailsData.logger.error("Testing of bolts summary table failed", e);
				failedCases.add("Bolts summary table test failed\n Reason :"+e.getMessage());
			}finally{
				TopologyDetailsData.logger.info("Testing of bolts summary table ends");
			}

			try {
				TopologyDetailsData.logger.info("Testing of topology config summary table begins");
				WebElement topoConfigacc = driver.findElement(By.xpath("//a[@href='#collapseThree']"));
				topoConfigacc.sendKeys(Keys.PAGE_DOWN);
				topoConfigacc.click();

				WebElement topologyConfigTable = driver.findElement(By.xpath("//div[@id='TopologyConfigTable']"));
				TopologyDetailsData.topoConfigView = Utils.uiTableToArrayList(topologyConfigTable);

				test = Utils.testTable(TopologyDetailsData.topoConfigView, TopologyDetailsData.topoConfigUi, "");
				topoConfigacc.click();
				if(!test){
					failedCases.add("Topology config table test failed");
					TopologyDetailsData.logger.error("Testing of topology config summary table failed");
				}
			} catch (Exception e) {
				failedCases.add("Topology config table test failed \nReason :"+e.getMessage());
				TopologyDetailsData.logger.error("Testing of topology config summary table failed");
			}finally{
				TopologyDetailsData.logger.info("Testing of topology config summary table ends");
			}

			if (!failedCases.isEmpty()) {
				Assert.assertFalse(true, "Topology table testing failed \n Reason :"+failedCases.toString());
			}
		} catch (Exception e) {
			TopologyDetailsData.logger.error("Topology table testing failed", e);
			Assert.assertFalse(true, "Topology table testing failed \n Reason :"+e.getCause());

		}finally{
			TopologyDetailsData.logger.info("Topology table testing ends");
		}
	}

	public void testBreadcrumbs(){
		try {
			WebElement element = (new WebDriverWait(Navigator.driver, 10))
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='#!/topology']")));
			element.click();
		} catch (Exception e) {
			Assert.assertFalse(true, "BreadCrumb test failed \n Reason :"+e.getCause());
		}
	}

	@Test(priority = 5)
	public void killAll(){
		try {
			TopologyDetailsData.logger.info("Test kill all topologies begins");
			WebDriver driver = Navigator.driver;
			testBreadcrumbs();
			WebElement table = (new WebDriverWait(driver, 100))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("summaryTable")));
			List<WebElement> topologies = table.findElement(By.tagName("tbody")).findElements(By.tagName("a"));

			for (int i = topologies.size(); i >= 0; i--) {
				table = (new WebDriverWait(driver, 100))
						.until(ExpectedConditions.presenceOfElementLocated(By.id("summaryTable")));
				topologies = table.findElement(By.tagName("tbody")).findElements(By.tagName("a"));
				if(!topologies.isEmpty()){
					topologies.get(0).click();
					try {
						testKillTopology();
					} catch (Exception e) {
						TopologyDetailsData.logger.error("Test kill topology failed\nReason :", e);

					}finally{
						testBreadcrumbs();
					}					
				}
			}
		} catch (Exception e) {
			TopologyDetailsData.logger.error("Test kill all topologies failed", e);
			Assert.assertFalse(true, "Kill all topologies test failed \n Reason :"+e.getCause());
		}finally{
			TopologyDetailsData.logger.info("Test kill all topologies ends");
		}
	}

	public void testKillTopology() throws Exception{
		TopologyDetailsData.logger.info("Test kill topology begins");
		WebDriver driver = Navigator.driver;
		WebElement killButton = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("btnKill")));
		Integer killWaitTime = 5;
		killButton.click();
		WebElement el = driver.findElement(By.className("modal-dialog")).findElement(By.tagName("input"));
		el.clear();
		el.sendKeys(killWaitTime.toString());
		driver.findElement(By.className("modal-dialog")).findElements(By.tagName("button")).get(2).click();

		Assert.assertFalse(!Utils.testAlertText("Topology killed successfully"), "Topology killed successfully alert test fail");

		TopologyDetailsData.logger.info("Test kill topology ends");
	}

	@BeforeTest
	public void beforeTest() {
		TopologyDetailsData.logger.info("Inside TopologyDetailTest.beforeTest Method");
		TopologyDetailsData.logger.info("Check if Storm monitoring view is up");
		if(!Navigator.isStormViewAvailable)
			throw new SkipException("Storm Monitoring page not available : skipping Topology details test");

		TopologyDetailsData.logger.info("Check if Storm UI is up");
		if(!Navigator.isStormUIUp)
			throw new SkipException("Storm UI page not available : skipping Topology details test");

		try {
			WebDriver driver = Navigator.driver;
			TopologyDetailsData.logger.info("Go to topology details page");
			WebElement table = (new WebDriverWait(driver, 100))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("summaryTable")));
			if(Navigator.isStormUIUp){
				TopologySummaryData.topoLogyListView = Utils.uiTableToArrayList(table);
				TopologyDetailsData.topoName = table.findElement(By.tagName("tbody")).findElements(By.tagName("a")).get(0).getText();
				TopologyDetailsData.topoid = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).get(0).findElements(By.tagName("td")).get(1).getText();
				table.findElement(By.tagName("tbody")).findElements(By.tagName("a")).get(0).click();
				TopologyDetailsData.logger.info("Loading of topology details page successfull");				
			}
		} catch (Exception e) {
			TopologyDetailsData.logger.error("Loading of topology details page failed\n Reason :"+e.getCause());
			Assert.assertFalse(true, "Loading topology details page failed : skipping the test \n Reason :"+e.getCause());
		}
	}
}
