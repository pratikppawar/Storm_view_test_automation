package testcase.ClusterSummary;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeTest;

import testcase.topogyDetail.TopologyDetailsData;
import utility.Utils;
import common.Navigator;

public class ClusterSummaryTest {

	@SuppressWarnings("unchecked")
	@Test(dataProvider = "dataProvider")
	public void test(ClusterSummaryData data) {

		ArrayList<String> failedCases = new ArrayList<String>();
		WebDriver driver = Navigator.driver;
		boolean test;
		try {
			Thread.sleep(5000);
			ClusterSummaryData.logger.info("Cluster summary page test begins");
			WebElement table;
			try {
				ClusterSummaryData.logger.info("Cluster summary table test begins");
				table = driver.findElement(By.id("clusterSummaryTable"));
				ClusterSummaryData.clusterSummaryView = Utils.uiTableToArrayList(table);

				if(ClusterSummaryData.clusterSummaryView.size() == 1 && ClusterSummaryData.clusterSummaryUI.isEmpty()){
					if(!ClusterSummaryData.clusterSummaryView.get(0).containsValue("No cluster found")){
						ClusterSummaryData.logger.error("Cluster summary table test failed");
						failedCases.add("Cluster summary table test failed");
					}
				}else{
					test = Utils.testTable(ClusterSummaryData.clusterSummaryView,  ClusterSummaryData.clusterSummaryUI, "");
					if(!test){
						ClusterSummaryData.logger.error("Cluster summary table test failed");
						failedCases.add("Cluster summary table test failed");
					}
				}
			} catch (Exception e) {
				ClusterSummaryData.logger.error("Cluster summary table test failed", e);
				failedCases.add("Cluster summary table test failed"+e.getCause());
			}finally{
				ClusterSummaryData.logger.info("Cluster summary table test ends");
			}

			try {
				ClusterSummaryData.logger.info("Nimbus summary table test begins");
				table = driver.findElement(By.id("nbsSummaryTable"));
				ClusterSummaryData.nbsSummaryView = Utils.uiTableToArrayList(table);

				if(ClusterSummaryData.nbsSummaryView.size() == 1 && ClusterSummaryData.nbsSummaryUI.isEmpty()){
					if(!ClusterSummaryData.nbsSummaryView.get(0).containsValue("No nimbus found")){
						ClusterSummaryData.logger.error("Nimbus summary table test failed");
						failedCases.add("Nimbus summary table test failed");
					}
				}else{
					test = Utils.testTable(ClusterSummaryData.nbsSummaryView,  (ArrayList<HashMap<String, Object>>) ClusterSummaryData.nbsSummaryUI.get(0).get("nimbuses"), "");
					if(!test){
						ClusterSummaryData.logger.error("Nimbus summary table test failed");
						failedCases.add("Nimbus summary table test failed");
					}
				}
			} catch (Exception e) {
				ClusterSummaryData.logger.error("Nimbus summary table test failed", e);
				failedCases.add("Nimbus summary table test failed"+e.getCause());
			}finally{
				ClusterSummaryData.logger.info("Nimbus summary table test ends");
			}

			try {
				ClusterSummaryData.logger.info("Supervisor summary table test begins");
				table = driver.findElement(By.xpath("//div[@id='sprSummaryTable']"));
				ClusterSummaryData.sprSummaryView = Utils.uiTableToArrayList(table);
				if(ClusterSummaryData.sprSummaryView.size() == 1 && ClusterSummaryData.sprSummaryUI.isEmpty()){
					if(!ClusterSummaryData.sprSummaryView.get(0).containsValue("No supervisor found")){
						ClusterSummaryData.logger.error("Supervisor summary table test failed");
						failedCases.add("Supervisor summary table test failed");
					}
				}else{
					test = Utils.testTable(ClusterSummaryData.sprSummaryView, (ArrayList<HashMap<String, Object>>)ClusterSummaryData.sprSummaryUI.get(0).get("supervisors"), ClusterSummaryData.supervisoId);
					if(!test){
						ClusterSummaryData.logger.error("Supervisor summary table test failed");
						failedCases.add("Supervisor summary table test failed");
					}
				}	
			} catch (Exception e) {
				ClusterSummaryData.logger.error("Supervisor summary table test failed", e);
				failedCases.add("Supervisor summary table test failed"+e.getCause());
			}finally{
				ClusterSummaryData.logger.info("Supervisor summary table test ends");
			}

			try {
				ClusterSummaryData.logger.info("Nimbus config table test begins");
				WebElement collapseNbs = driver.findElement(By.xpath("//a[@href='#collapseNbs']"));
				collapseNbs.click();
				table = driver.findElement(By.id("nbsConfigTable"));
				ClusterSummaryData.nbsConfigView = Utils.uiTableToArrayList(table);

				if(ClusterSummaryData.nbsConfigView.size() == 1 && ClusterSummaryData.nbsConfigUI.isEmpty()){
					if(!ClusterSummaryData.nbsConfigView.get(0).containsValue("No nimbus configuration found")){
						ClusterSummaryData.logger.error("Nimbus config table test failed");
						Assert.assertFalse(true, "Nimbus config table test failed");
					}
				}else{
					test = Utils.testTable(ClusterSummaryData.nbsConfigView,  ClusterSummaryData.nbsConfigUI, "");
					if(!test){
						ClusterSummaryData.logger.error("Nimbus config table test failed");
						Assert.assertFalse(true, "Nimbus config table test failed");
					}
				}
			} catch (Exception e) {
				ClusterSummaryData.logger.error("Nimbus config table test failed", e);
				failedCases.add("Nimbus config table test failed"+e.getCause());
			}finally{
				ClusterSummaryData.logger.info("Nimbus config table test ends");
			}

			if (!failedCases.isEmpty()) {
				ClusterSummaryData.logger.error("Cluster summary page test failed \n Reason :"+failedCases.toString());
				Assert.assertFalse(true, "Topology table testing failed \n Reason :"+failedCases.toString());
			}
		} catch (Exception e) {
			ClusterSummaryData.logger.error("Cluster summary page test failed", e);
			Assert.assertFalse(true, "Cluster summmary test failed \nReason : "+e.getMessage());			
		}finally{
			ClusterSummaryData.logger.info("Cluster summary page test ends");
		}
	}

	@DataProvider
	public Object[][] dataProvider() {
		return new Object[][] {
				new Object[] { new ClusterSummaryData()}
		};
	}

	@BeforeTest
	public void beforeTest() {
		ClusterSummaryData.logger.info("Inside ClusterSummmaryTest.beforeTest Method");
		ClusterSummaryData.logger.info("Check if Storm monitoring view is up");
		
		if(!Navigator.isStormViewAvailable)
			throw new SkipException("Storm Monitoring page not available : skipping Cluster summary test");
		
		ClusterSummaryData.logger.info("Go to Cluster summary page");
		try {
			WebDriver driver = Navigator.driver;
			(new WebDriverWait(driver, 100))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@data-id='cluster']"))).click();
		} catch (Exception e) {
			ClusterSummaryData.logger.error("Loading of topology details page failed");
			Assert.assertFalse(true, "Loading Cluster summary page failed : skipping the test \n Reason :"+e.getCause());
		}
	}	
}
