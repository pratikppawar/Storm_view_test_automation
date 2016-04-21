package testcase.ClusterSummary;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.Assert;

import utility.Utils;
import common.Navigator;

public class ClusterSummaryData {

	public static ArrayList<HashMap<String, Object>> clusterSummaryUI = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> nbsSummaryUI = new ArrayList<>();
	public static ArrayList<HashMap<String, Object>> sprSummaryUI = new ArrayList<>();
	public static ArrayList<HashMap<String, Object>> nbsConfigUI = new ArrayList<>();

	public static ArrayList<HashMap<String, Object>> clusterSummaryView = new ArrayList<>();
	public static ArrayList<HashMap<String, Object>> nbsSummaryView = new ArrayList<>();
	public static ArrayList<HashMap<String, Object>> sprSummaryView = new ArrayList<>();
	public static ArrayList<HashMap<String, Object>> nbsConfigView = new ArrayList<>();

	public static String custerSummaryURL = "http://"+Navigator.uiURL+"/api/v1/cluster/summary";
	public static String sprSummaryURL = "http://"+Navigator.uiURL+"/api/v1/supervisor/summary";
	public static String nimbusSummaryURL = "http://"+Navigator.uiURL+"/api/v1/nimbus/summary";
	public static String nimbusConfigURL = "http://"+Navigator.uiURL+"/api/v1/cluster/configuration";
	public static Logger logger = Logger.getLogger(ClusterSummaryTest.class);
	public static String supervisoId = "id";
	public ClusterSummaryData(){

		try {
			clusterSummaryUI =  Utils.JSONToMapGetRequest(custerSummaryURL);
			sprSummaryUI =  Utils.JSONToMapGetRequest(sprSummaryURL);
			nbsConfigUI =  Utils.JSONToMapGetRequest(nimbusConfigURL);
			nbsSummaryUI = Utils.JSONToMapGetRequest(nimbusSummaryURL);

		} catch (Exception e) {
			Assert.assertFalse(true, "Cluster summmary data loading failed \n Reason :"+e.getCause());
		}
	}
}
