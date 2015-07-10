package de.mmis.applications.whiteboardviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import de.mmis.core.base.abstracttree.InnerNode;
import de.mmis.core.base.abstracttree.LeafNode;
import de.mmis.core.base.abstracttree.LeafNode.Encoding;
import de.mmis.core.base.abstracttree.Tree;
import de.mmis.core.base.event.AbstractObservable;
import de.mmis.core.base.event.Event;
import de.mmis.core.gbi.GoalBasedInteractor;
import de.mmis.core.gbi.GoalType;
import de.mmis.core.gbi.exception.InconsistentGoalException;
import de.mmis.core.pojop.NetworkPublisherClient;
import de.mmis.core.pojop.PublisherClient.PublisherClientException;
import de.mmis.core.pojop.PublisherClientProxy;
import de.mmis.devices.camera.AXISHTTPv2;
import de.mmis.devices.pensensor2.PenSensor;
import de.mmis.devices.pensensor2.PenSensor.Event.Type;
import de.mmis.utilities.hmm.discrete.HMMFilterEvent;
import de.mmis.utilities.hmm.discrete.State;


public class WhiteboardViewerImpl extends AbstractObservable<Event> implements WhiteboardViewer {
	
	public static final long TIMEOUT = 20000;
	
	

	boolean waitingForLecturerPosition = false;
	/**
	 * a whiteboard is shown on the best possible screen
	 */
	boolean isActive = false;
	/**
	 * Positions of each tag, T0 if not positioned
	 * T1-T9 for tables 1-9
	 */
	Map<String , String> sittingPositions;
	/**
	 * position of lecturer tag, W0 if not positioned,
	 * W1-W4 for whiteboards 1-4
	 */
	Map<String,GoalBasedInteractor> projectors = new HashMap<>();
	Map<String,GoalBasedInteractor> lamps = new HashMap<String,GoalBasedInteractor>();
	Map<String,GoalBasedInteractor> screens = new HashMap<String,GoalBasedInteractor>();
	GoalBasedInteractor extron;
	
	String lecturerPosition;
	long whiteBoardTime;
	int input=0;
	String lecturerID;
	AXISHTTPv2 camera;
	
	
	/**
	 * Whiteboard that is currently shown (W1-W4) W0 for no whiteboard shown
	 */
	String whiteboard;
	/**
	 * screen where output is currently shown (S1-S7) or S0 for no projection
	 */
	String displayPosition;
	Timer timer;
	static final int[][] viewQualityMatrix = 
		{{-1,0,1,1,1,1,-1,-1,-1},
		{-1,-1,-1,-1,1,1,1,1,0},
		{-1,-1,-1,-1,0,1,1,1,1},
		{0,-1,-1,-1,0,0,1,1,1},
		{0,-1,-1,-1,-1,-1,1,1,1},
		{1,-1,-1,-1,-1,-1,0,1,1},
		{1,1,1,0,-1,-1,-1,-1,-1}};
		
	static List<List<Integer>> badViewTable;
	
	public WhiteboardViewerImpl(int input){
		sittingPositions = new HashMap<String,String>();
		this.input = input;

		
		//build badViewTable
		badViewTable=new ArrayList<List<Integer>>();
		List<Integer> bad0 = new ArrayList<Integer>();
		bad0.add(1);
		bad0.add(7);
		bad0.add(8);
		bad0.add(9);
		badViewTable.add(0, bad0);
		List<Integer> bad1 = new ArrayList<Integer>();
		bad1.add(1);
		bad1.add(4);
		bad1.add(3);
		bad1.add(2);
		badViewTable.add(1, bad1);
		List<Integer> bad2 = new ArrayList<Integer>();
		bad2.add(5);
		bad2.add(6);
		bad2.add(4);
		bad2.add(3);
		bad2.add(2);
		badViewTable.add(2, bad2);
		List<Integer> bad3 = new ArrayList<Integer>();
		bad3.add(6);
		bad3.add(5);
		bad3.add(9);
		bad3.add(8);
		bad3.add(7);
		badViewTable.add(3, bad3);

		lecturerPosition="W0";
		whiteboard="S0";
		displayPosition="S0";
		timer = new Timer();
	}


	@Override
	public void penTaken(){
	    if(!isActive){
	    	System.out.println("penTaken()");
	    	System.out.println("Turn light on, blinds should go down.");
	    	
	    	isActive=true;
	    	waitingForLecturerPosition = true;
	    	
	    	Tree goalLightOn = new InnerNode(new LeafNode("DIM_VALUE"), new LeafNode("1.0"));
	    	Tree goalScreenDown = new InnerNode(new LeafNode("POSITION"), new LeafNode("false"));
	    	
	    	//turn lights on
	    	lamps.forEach((name,gbi) -> {
	    		try {
					gbi.addGoal(GoalType.ACHIEVE, goalLightOn, 1, 10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	});
	    	
	    	//blinds go down!
	    	screens.forEach((name,gbi) -> {
	    		try {
	    			if(name.startsWith("SB")){
	    				gbi.addGoal(GoalType.ACHIEVE, goalScreenDown, 1, 10000);
	    			}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	});	    	
	    	timer.schedule(new OutputTask(), TIMEOUT);
	    }
	}
	
	@Override
	public String toString(){
		return "Ich bin der Whiteboardviewer";
	}
		
	
	@Override
	public void processHMMEvent(String ubiid, String stateName){
		//TODO: update whiteboard position if necessary. (maybe timer)
		if(ubiid.equals(lecturerID) && stateName.startsWith("W")){
			System.out.println("New lecturer position: " + stateName);
			//A pen is taken and the lecturer is still moving around
			if(waitingForLecturerPosition){	
				whiteboard = stateName;
				timer.cancel();
				timer = new Timer();
				timer.schedule(new OutputTask(), 10000);
			}
			lecturerPosition=stateName;			
		}else if(stateName.startsWith("T")){
			System.out.println("Listener sitting at table: " + stateName);
			sittingPositions.put(ubiid, stateName);
		}
	}
	

	/**
	 * get display position using whiteboardPosition
	 * and sittingPositions (variable displayPosition is not changed)
	 * @throws Exception 
	 */
	protected Integer getDisplayPosition(String whiteboard) throws Exception {
		System.out.println("Determine optimal screen...");
		int whiteboardIndex=0;
		switch (whiteboard) { 				
		case "W1": whiteboardIndex = 0; break;
		case "W2": whiteboardIndex = 1; break;
		case "W3": whiteboardIndex = 2; break;
		case "W4": whiteboardIndex = 3; break;
		default : return -1;
		}		
		List<Integer> badViewPositions = badViewTable.get(whiteboardIndex);
		Integer[] badListener = {0,0,0,0,0,0,0,0,0};
		
		sittingPositions.forEach((uid, table) -> {
			Integer tableId = getTableNumber(table);
			if(badViewPositions.contains(tableId)){
				badListener[(tableId-1)]++;
			}
		});
		
		Integer optimalScreen=-1;
		Integer bestScore= Integer.MIN_VALUE;
		Integer[] screenQualities = {0,0,0,0,0,0,0};
 		for(int i = 0 ; i < 7 ; i++ ){
			for(int j = 0; j < 9 ; j++ ){
				screenQualities[i] += viewQualityMatrix[i][j] * badListener[j];
			}
			if(screenQualities[i] > bestScore){
				bestScore = screenQualities[i];
				optimalScreen = i;				
			}
		}		
 		System.out.println("BestScore: " + bestScore);
 		System.out.println("Done.");
 		//translate screen number to match the roomids
 		if(optimalScreen == 7){
 			return (optimalScreen+2);
 		}
 		else if(optimalScreen>= 2){
 			return ++optimalScreen;
 		};
		return optimalScreen;
	}
	

	/**
	 * Set id of lecturer (= id of ubisense-tag)
	 * @param id
	 */
	@Override
	public void setLecturerID(String id){
		System.out.println("setLecturerID"+id);
		lecturerID=id;
	}
	

	@Override
	public void setCamera(AXISHTTPv2 camera){
		System.out.println("setCamera(camera)");
		this.camera=camera;
		try {
			camera.moveToPreset("S1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the component to show the rectified image
	 */
	public void setPresenter(){
		//
	}
	
	/**
	 * Add gbi for extron, projectors, screens, lamps, ...
	 * @param gbiID
	 * @param gbi
	 */
	@Override
	public void addGBI(String gbiID, GoalBasedInteractor gbi){
		System.out.println("addGBI(gibID,gbi) "+ gbiID);
		
		if(gbiID.startsWith("Projector")){
				projectors.put(gbiID, gbi);
		}
		else if(gbiID.startsWith("S")){
				screens.put(gbiID, gbi);
		}
		else if(gbiID.startsWith("Lamp")){
			lamps.put(gbiID, gbi);
		}
		else if(gbiID.startsWith("Extron")){
			extron = gbi;
		}
	}
	
	
	protected void startOutput() {

	}
	
	
	protected void stopOutput(){
		//TODO
	}
	
	private Integer getTableNumber(String tableid){
		return Integer.parseInt(tableid.substring(1));
	}
	
	/**
	 * Translates a HMM-State to a camera known preset
	 * @param HMM-State
	 * @return cameraPreset
	 */
	private String translateStateToPreset(String state){
		switch (state){
		case "W1": return "S1";
		case "W2": return "S3";
		case "W3": return "S7";
		case "W4": return "S9";
		default: System.err.println("Unkown HMM-State: " + state);
			return null;
		}
	}
	
	class OutputTask extends TimerTask {
		@Override
		public void run() {
			
			System.out.println("The lecturer intends to write @ whiteboard" + whiteboard);			
			waitingForLecturerPosition = false;
			Integer screen = -1;
			//Determine optimal screen
			try {
				screen = getDisplayPosition(whiteboard);
				if(screen == -1){
					System.out.println("Couldn't determine optimal Screen.");
					waitingForLecturerPosition = true;
					return;
				}
				System.out.println("Show whiteboard on screen: "+screen);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//move camera to correct preset
			System.out.println("Move Cam to preset:" + whiteboard);
			try {
				camera.moveToPreset(translateStateToPreset(whiteboard));
			} catch (IOException e) {
				System.err.println("Error adjusting camera");
				e.printStackTrace();
			}
			
			//control extron
			System.out.println("Extron input:" + whiteboard);
			Tree goalE = new InnerNode(new LeafNode(String.valueOf(input)),new LeafNode(String.valueOf(screen)), new LeafNode("BOTH"));
			try {
				extron.addGoal(GoalType.ACHIEVE, goalE, 1, 10000);
			} catch (InconsistentGoalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//turn projector on
			System.out.println("Turning Projector on.. ");
			
			Tree goalProjector = new InnerNode(new LeafNode("INPUT"), new LeafNode("vga",Encoding.Quoted));
			
			String projector = "Projector" + String.valueOf(screen) + "_GBI";
			try {
				projectors.get(projector).addGoal(GoalType.ACHIEVE, goalProjector , 1, 10000);
			} catch (InconsistentGoalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
