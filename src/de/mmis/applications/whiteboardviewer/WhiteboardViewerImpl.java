package de.mmis.applications.whiteboardviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mmis.core.base.event.AbstractObservable;
import de.mmis.core.base.event.Event;
import de.mmis.core.pojop.NetworkPublisherClient;
import de.mmis.core.pojop.PublisherClient.PublisherClientException;
import de.mmis.core.pojop.PublisherClientProxy;
import de.mmis.devices.camera.AXISHTTPv2;
import de.mmis.devices.pensensor2.PenSensor;
import de.mmis.devices.pensensor2.PenSensor.Event.Type;
import de.mmis.utilities.hmm.discrete.HMMFilterEvent;
import de.mmis.utilities.hmm.discrete.State;


public class WhiteboardViewerImpl extends AbstractObservable<Event> implements WhiteboardViewer {

	/**
	 * Positions of each tag, 0 if not positioned
	 * 1-9 for tables 1-9
	 */
	Map<String , Integer> sittingPositions;
	/**
	 * number of whiteboard where lecuturer currently stands,
	 * not necessarily the same as variable whiteboard,
	 * if lecturer has just moved to this position
	 */
	int lecturerPosition;
	int nrPensTaken;
	AXISHTTPv2 camera;
	/**
	 * Whiteboard that is currently shown
	 */
	int whiteboard;
	/**
	 * Number of screen where output is currently shown
	 */
	int displayPosition;
	static final int[][] viewQualityMatrix = 
		{{-1,0,1,1,1,1,-1,-1,-1},
		{-1,-1,-1,-1,1,1,1,1,0},
		{-1,-1,-1,-1,0,1,1,1,1},
		{0,-1,-1,-1,0,0,1,1,1},
		{0,-1,-1,-1,-1,-1,1,1,1},
		{1,-1,-1,-1,-1,-1,0,1,1},
		{1,1,1,0,-1,-1,-1,-1,-1}};
		
	static List<List<Integer>> badViewTable;
	
	public WhiteboardViewerImpl(){
		sittingPositions = new HashMap<String,Integer>();
		//new tags are added with addListenerID
		
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
		
		
		lecturerPosition=0;
		nrPensTaken=0;
		whiteboard=0;
		displayPosition=0;
		
		//Connect to camera
		try {
			//TODO richtigen port fuer kamera nehmen
			camera= PublisherClientProxy.createProxy(AXISHTTPv2.class, new NetworkPublisherClient("localhost", 21219));
		} catch (PublisherClientException e) {
			System.err.println("Could not find camera");
			e.printStackTrace();
		}
	}

	@Override
	public void processPenEvent(PenSensor.Event event) {
		
		if(event.getEventType()==Type.PENTAKEN){
			//if at least 1 pen taken: do nothing
			if(nrPensTaken>=1){
				nrPensTaken++;
				return;
			}//else:
			nrPensTaken++;
			
			//get whiteboard where lecturer writes on
			whiteboard=lecturerPosition;
			//get screen where to show whiteboard
			displayPosition = getDisplayPosition(whiteboard);
			
			
			//permanently get camera image and show on displayPosition
			//->Thread
			startOutput();
			//TODO
			
		}else if(event.getEventType()==Type.PENINSERTED){
			//if more than 1 pen or 0 pens are taken: do nothing
			if(nrPensTaken>=2){
				nrPensTaken--;
				return;
			}else if(nrPensTaken==0){
				return;
			}else{//nrPensTaken==1
				nrPensTaken=0;
				stopOutput();
			}
			
		}	
	}	
	

	@Override
	/**
	 * The id is the name of the hmm (@ID in GP)
	 */
	public void processListenerHMMEvent(Map<State<List<Double>>,Double> stateProbability, String id){
		//update sitting positions (every time?)
		//iterate over all states, find state with max prob
		double maxprob = 0;
		State<List<Double>> maxprobstate=null;
		for(State<List<Double>> state:stateProbability.keySet()){
			double prob = stateProbability.get(state);
			if(prob>maxprob){
				maxprob=prob;
				maxprobstate=state;
			}
		}
		
		//add (tag_id, state (^=table)) to sittingPositions
		sittingPositions.put(id,Integer.parseInt(maxprobstate.getName()));
		
		//TODO what else?
		
	}
	
	@Override
	public void processScreenHMMEvent(Map<State<List<Double>>,Double> stateProbability){
		
		double maxprob = 0;
		State<List<Double>> maxprobstate=null;
		for(State<List<Double>> state:stateProbability.keySet()){
			double prob = stateProbability.get(state);
			if(prob>maxprob){
				maxprob=prob;
				maxprobstate=state;
			}
		}
		
		lecturerPosition=Integer.parseInt(maxprobstate.getName());
		
		//TODO do not update immediately, 
		//update output position
		whiteboard=lecturerPosition;
		displayPosition=getDisplayPosition(whiteboard);
	}
	

	/**
	 * get display position using whiteboardPosition
	 * and sittingPositions (variable displayPosition is not changed)
	 */
	protected int getDisplayPosition(int whiteboard){
		//TODO 
		return 0;
	}
	
	/**
	 * Adds a new id of a listener. To work properly, 
	 * a listener-hmm with the same id has to exist
	 * @param tagID
	 */
	public void addListenerID(String id){
		sittingPositions.put(id, 0);
	}
	
	protected void startOutput() {
		// TODO	as a thread
		//adjust camera
		
		//TODO which preset for which number?
		//TODO only if whiteboard changed(?)
		try {
			camera.moveToPreset(whiteboard);
		} catch (IOException e) {
			System.err.println("Error adjusting camera");
			e.printStackTrace();
		}
		
		
	}
	
	protected void stopOutput(){
		//TODO
	}
	
	


}
