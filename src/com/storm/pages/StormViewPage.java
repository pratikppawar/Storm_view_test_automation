package com.storm.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StormViewPage extends BasePage{
	
	@FindBy(xpath=".//*[@id='container']/div[1]/div[1]/div[2]/div/div[1]/div/div/div[1]/h4")
	public WebElement topologyListingHeader;
	
	@FindBy(xpath=".//*[@id='container']/div[1]/div[1]/div[2]/div/div[1]/div/div/div[2]/div/table/tbody/tr/td[1]/a/span[2]")
	public WebElement firstTopologyLink;
	
	@FindBy(xpath=".//*[@id='container']/div/div[2]/div[1]/div/div[1]")
	public WebElement topologySummaryHeader;
	
	@FindBy(xpath=".//*[@id='container']/div[1]/div[1]/div/div/div/div[1]/div[4]/div/button[2]")
	public WebElement topologyDeactivateButton;
	
	@FindBy(xpath=".//*[@id='container']/div[1]/div[1]/div/div/div/div[1]/div[4]/div/button[1]")
	public WebElement topologyActivateButton;
	
	@FindBy(xpath=".//*[@id='container']/div[1]/div[1]/div/div/div/div[1]/div[4]/div/button[3]")
	public WebElement topologyRebalanceButton;
	
	@FindBy(xpath=".//*[@id='container']/div[1]/div[1]/div/div/div/div[1]/div[4]/div/button[5]")
	public WebElement changeLogLevelButton;
	
	@FindBy(xpath="html/body/div[4]/div/div/div[2]/button[2]")
	public WebElement confirmationMessageYesButton;	
	
	@FindBy(xpath=".//*[@id='container']/div/div[1]/div/div/div/div[1]/div[2]/div")
	public WebElement systemSummaryButton;	
	
	@FindBy(xpath=".//*[@id='container']/div/div[1]/div/div/div/div[1]/div[3]/div")
	public WebElement debugButton;	
	
	@FindBy(xpath=".//*[@id='breadcrumb']/li[2]/a")
	public WebElement breadCrumTopologyListingBtn;
	
	@FindBy(xpath=".//*[@id='container']/div/div/div/div/div/div[1]/h4")
	public WebElement topologyListPageHeader;
	
	@FindBy(xpath=".//*[@id='container']/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[1]/a/span[2]")
	public WebElement topologyNameOnListPage;
	
	@FindBy(xpath=".//*[@id='breadcrumb']/li[1]/a")
	public WebElement breadcrumHomeIcon;
	
	@FindBy(xpath=".//*[@id='breadcrumb']/li[3]/a")
	public WebElement breadcrumTopologyName;
	
	@FindBy(xpath=".//*[@id='modal-rebalance']/div/div/div[1]/h4")
	public WebElement rebalanceTopologyWindowHeader;
	
	@FindBy(id="graph-icon")
	public WebElement minimizeWordCount;
	
	@FindBy(id="spout-box")
	public WebElement minimizeSpouts;
	
	@FindBy(id="bolt-box")
	public WebElement minimizeBolts;
	
	@FindBy(xpath=".//*[@id='collapse-spout']/div[2]/table/tbody/tr/td[1]/a")
	public WebElement firstSpoutId;
	
	@FindBy(xpath=".//*[@id='container']/div/div[2]/div[2]/div/div[1]")
	public WebElement spoutStatsHeader;
	
	@FindBy(xpath=".//*[@id='collapse-bolt']/div[2]/table/tbody/tr[1]/td[1]/a")
	public WebElement firstBoltId;
	
	@FindBy(xpath=".//*[@id='container']/div/div[2]/div[2]/div/div[1]")
	public WebElement boltStatsHeader;
	
	public void getTopologyWindowList()
	{
//		List<WebElement> elem=driver.findElements(By.xpath(".//*[@id='container']/div/div[2]/div[2]/div/div[2]/table/tbody/tr/td[1]"));
//		for(WebElement ele:elem)
//		{
//			String windowList=ele.getText();
//			windowList.compareTo()
//		}
//		return "";
	}
	
//	public void getTopologyName(String topologyName)
//	{
//		List<WebElement> listofTopologies=driver.findElements(By.xpath(".//*[@id='container']/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[1]/a/span[2]"));
//		for(WebElement topologies:listofTopologies)
//		{
//			if(topologies.getText().equals(topologyName))
//			{
//				topologies.findElement(By.xpath("//td[1]/a/span[2]"));
//			}
//		}
//	}
	
	public String getTopologyStatus(String topologyName) throws InterruptedException
	{
		String currentStatus=null;
		List<WebElement> allListedTopologies= getTopologyListTableRows();
		int numberOfTopologies = allListedTopologies.size();
		for (int i=1;i<=numberOfTopologies;i++)
		{
			WebElement ele=driver.findElement(By.xpath(".//*[@id='container']/div/div/div/div/div/div[2]/div[2]/table/tbody/tr["+i+"]/td[1]/a/span[2]"));
			String topology =ele.getText();
			
			if (topology.equals(topologyName))
			{ 
				currentStatus=driver.findElement(By.xpath(".//*[@id='container']/div/div/div/div/div/div[2]/div[2]/table/tbody/tr["+i+"]/td[2]/span/span[2]")).getText();
				ele.click();
//				return currentStatus;
//				WebElement deleteModal = driver.findElement(By.cssSelector("div.modal-footer"));
//				deleteModal.findElement(By.xpath("./button[2]")).click();
//				Thread.sleep(2000);
				break;
			}
		}
		return currentStatus;
	}
	
	public List<WebElement> getTopologyListTableRows() {
		return driver.findElements(By.xpath(".//*[@id='container']/div/div/div/div/div/div[2]/div[2]/table/tbody/tr"));
	}

}
