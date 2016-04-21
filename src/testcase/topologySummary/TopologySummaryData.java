package testcase.topologySummary;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import utility.Utils;
import common.Navigator;

public class TopologySummaryData {

	public static String topologySummaryURL = "http://"+Navigator.uiURL+"/api/v1/topology/summary";
	public static ArrayList<HashMap<String, String>> topoLogyList;
	
	public static String searchIdTopology = "name";

	public static ArrayList<HashMap<String, Object>> topoLogyListUI =  new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> topoLogyListView =  new ArrayList<HashMap<String, Object>>();
	
	public static Logger logger = Logger.getLogger(TopologySummaryTest.class);
	
	static{		
		topoLogyListUI =  Utils.JSONToMapGetRequest(topologySummaryURL);
	}
}
