package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import testcase.topologySummary.TopologySummaryData;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import common.Navigator;

@SuppressWarnings("deprecation")
public class Utils {

	public static WebDriverWait wait = new WebDriverWait(Navigator.driver, 15);
	//spublic static Logger logger = Logger.getLogger(Utils.class);

	@SuppressWarnings("resource")
	public static ArrayList<HashMap<String,  Object>> JSONToMapGetRequest(String url){
		ArrayList<HashMap<String,  Object>> retMap = new ArrayList<HashMap<String,  Object>>();
		HashMap<String,  Object> temp = new HashMap<>();

		try 
		{
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			String line  = rd.readLine();			

			temp = new Gson().fromJson(line, new TypeToken<HashMap<String,  Object>>() {}.getType());
			if(!temp.containsKey("errorMessage")){
				retMap.add(temp);
			}
		} catch (JsonParseException e){
			Assert.assertFalse(false, "Test execution halted \n Reason : "+e.getCause());
		} catch (ClientProtocolException e) {
			Assert.assertFalse(false, "Test execution halted \n Reason : "+e.getCause());
		} catch (IOException e) {			
			if(e.getMessage().equalsIgnoreCase("Connection refused")){
				Navigator.isStormUIUp = false;
			}
			Assert.assertFalse(false, "Test execution halted \n Reason : "+e.getCause());
		}
		return retMap;
	}

	public static boolean testTable(ArrayList<HashMap<String, Object>> tempViewList, ArrayList<HashMap<String, Object>> tempUiList, String id) throws Exception{

		Object[] headerList = tempViewList.get(0).keySet().toArray();

		for (HashMap<String, Object> tempView : tempViewList) {

			for (Map<String, Object> tempUI : tempUiList) {

				if(tempView.get(id) == tempUI.get(id) || id == ""){
					for (Object header : headerList) {
						if(!(header.equals("uptime") || header.equals("nimbusUpTime") || header.equals("logs") || header.equals("key") || header.equals("value"))){
							try {
								if(!(Float.parseFloat(tempView.get(header.toString()).toString()) == Float.parseFloat(tempUI.get(header.toString()).toString()))){
									Logger.getGlobal().info("For property "+header.toString()+", value on storm monitoring view is "+tempView.get(header.toString()).toString()+" and value on storm UI is "+
											tempUI.get(header.toString()).toString()+"");
									return false;
								}
							} catch (NumberFormatException e) {
								try {
									if(!(tempView.get(header.toString()).equals(tempUI.get(header.toString())))){
										Logger.getGlobal().info("For property "+header.toString()+", value on storm monitoring view is "+tempView.get(header.toString()).toString()+" and value on storm UI is "+
												tempUI.get(header.toString()).toString()+"");
										return false;
									}
								} catch (NullPointerException npe) {
									if(!(tempView.get(header.toString()) == tempUI.get(header.toString()))){
										Logger.getGlobal().info("For property "+header.toString()+", value on storm monitoring view is "+tempView.get(header.toString())+" and value on storm UI is "+
												tempUI.get(header.toString())+"");
										return false;
									}
								}
							} catch (NullPointerException e) {
								if(!(tempView.get(header.toString()) == tempUI.get(header.toString()))){
									Logger.getGlobal().info("For property "+header.toString()+", value on storm monitoring view is "+tempView.get(header.toString())+" and value on storm UI is "+
											tempUI.get(header.toString())+"");
									return false;
								}
							}
						}
					}
				}
			}	
		}
		return true;
	}

	public static ArrayList<HashMap<String, Object>> uiTableToArrayList(WebElement table) throws Exception{

		List<WebElement> header = table.findElements(By.tagName("th"));
		List<WebElement> allRows = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		ArrayList<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
		if(header.size() == 2 
				&& header.get(0).getAttribute("class").replace("renderable ", "").replace("sortable ", "").contains("key") 
				&& header.get(1).getAttribute("class").replace("renderable ", "").replace("sortable ", "").contains("value")){
			for (WebElement row : allRows) {	

				HashMap<String, Object> temp = new HashMap<String, Object>();
				List<WebElement> cells = row.findElements(By.tagName("td"));
				temp.put(cells.get(0).getText(), cells.get(1).getText());
				tempList.add(temp);
			}
			return tempList;
		}
		for (WebElement row : allRows) {
			HashMap<String, Object> temp = new HashMap<String, Object>();
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (int i = 0; i<cells.size(); i++) {
				temp.put(header.get(i).getAttribute("class").replace("renderable ", "").replace("sortable ", ""), cells.get(i).getText());
			}
			tempList.add(temp);
		}
		return tempList;
	}

	public static void checkTopologySummaryInfo() {
		try {
			WebElement topoSummaryInfo = (new WebDriverWait(Navigator.driver, 10))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-id='summary']")));
			int totalTopo = 0;
			int activeTopo = 0;
			int inactiveTopo = 0;
			List<WebElement> rows = Navigator.driver.findElements(By.tagName("tr"));
			totalTopo = rows.size() - 1;
			for (WebElement row : rows) {
				if(row.getText().toString().contains("ACTIVE"))
					activeTopo++;
				else if (row.getText().toString().contains("INACTIVE")) 
					inactiveTopo++;
			}

			if(TopologySummaryData.topoLogyListView.get(0).containsValue("No topology found")){
				totalTopo = 0;
			}

			if(!(topoSummaryInfo.findElements(By.tagName("span")).get(0).getText().toString().contains(totalTopo+"")  
					&&	topoSummaryInfo.findElements(By.tagName("span")).get(1).getText().toString().contains(activeTopo+"")
					&& topoSummaryInfo.findElements(By.tagName("span")).get(2).getText().toString().contains(inactiveTopo+""))){
				Assert.assertFalse(false, "Check topology summary test failed");
			}		

		} catch (Exception e) {
			Assert.assertFalse(false, "Check topology summary test failed \n Reason :"+e.getClass());
		}
	}

	public static boolean testAlertText(String msg) {
		try {
			wait.until(ExpectedConditions.textToBePresentInElement(By.xpath("//div[@class='notifications top-right']"), msg));
		} catch (Exception e) {
			return false;
		}
		return true;		
	}

	public static void loadingWait(){
		try {
			//wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='loading']")));
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@style='display: block;']"))));
		} catch (Exception e) {

		}
	}
}
