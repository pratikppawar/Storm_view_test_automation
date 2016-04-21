package testcase.topogyDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import utility.Utils;
import common.Navigator;

public class TopologyDetailsData {

	public static ArrayList<HashMap<String, Object>> topologyDetailView = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> SpoutsSummaryView = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> boltsSummaryView = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> optsSummaryView = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> exrtSummaryView = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> errorSummaryView = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> topoConfigView = new ArrayList<HashMap<String, Object>>();

	public static ArrayList<HashMap<String, Object>> topologyDetailUi= new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> SpoutsSummaryUi = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> SpoutsDetailsUi = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> boltsSummaryUi = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> optsSummaryUi = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> exrtSummaryUi = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> errorSummaryUi = new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> topoConfigUi = new ArrayList<HashMap<String, Object>>();

	public static String topology = "http://"+Navigator.uiURL+"/api/v1/topology/";
	public static String spoutId = "spoutId";
	public static String outStatsId = "";
	public static String executorId = "id";
	public static String errorSummaryId = "";
	public static String boltId = "boltId";
	public static String topoName = "";
	public static String topoid;
	public static Logger logger = Logger.getLogger(TopologyDetailTest.class);

	@SuppressWarnings("unchecked")
	public static String buildTopoURL(String topoId, String sys, String win){
		
		try {
			if(!sys.equals("") && !win.equals("")){
				topologyDetailView = Utils.JSONToMapGetRequest(topology+topoId+"?win="+win+"&sys="+sys);
			}else if(!sys.equals("")){
				topologyDetailView = Utils.JSONToMapGetRequest(topology+topoId+"?sys="+sys);
			}else if(win.equals("")){
				topologyDetailView = Utils.JSONToMapGetRequest(topology+topoId+"?win="+win);
			}else{
				topologyDetailView = Utils.JSONToMapGetRequest(topology+topoId);
			}
			
			SpoutsSummaryUi = (ArrayList<HashMap<String, Object>>) topologyDetailView.get(0).get("spouts");
			if(!SpoutsSummaryUi.isEmpty()){
				for (Map<String, Object> spout : SpoutsSummaryUi) {
					buildSpoutURL(topoId, (String) spout.get("spoutId"));					
				}
			}
			
			boltsSummaryUi = (ArrayList<HashMap<String, Object>>) topologyDetailView.get(0).get("bolts");
			topoConfigUi = (ArrayList<HashMap<String, Object>>) topologyDetailView.get(0).get("configuration");
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void buildSpoutURL(String topoId, String spoutId){
		SpoutsDetailsUi = Utils.JSONToMapGetRequest(topology+""+topoId+"/component/"+""+spoutId);
		optsSummaryUi = (ArrayList<HashMap<String, Object>>) SpoutsDetailsUi.get(0).get("outputStats");
		exrtSummaryUi = (ArrayList<HashMap<String, Object>>) SpoutsDetailsUi.get(0).get("executorStats");
		errorSummaryUi = (ArrayList<HashMap<String, Object>>) SpoutsDetailsUi.get(0).get("componentErrors");
	}
}
