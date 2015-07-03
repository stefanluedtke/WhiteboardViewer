package de.mmis.applications.whiteboardviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mmis.core.base.event.AbstractObservable;
import de.mmis.core.base.event.Event;
import de.mmis.core.gbi.GoalBasedInteractor;
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
	 * Positions of each tag, T0 if not positioned
	 * T1-T9 for tables 1-9
	 */
	Map<String , String> sittingPositions;
	/**
	 * position of lecturer tag, W0 if not positioned,
	 * W1-W4 for whiteboards 1-4
	 */
	String lecturerPosition;
	String lecturerID;
	AXISHTTPv2 camera;
	/**
	 * Whiteboard that is currently shown (W1-W4) W0 for no whiteboard shown
	 */
	String whiteboard;
	/**
	 * screen where output is currently shown (S1-S9) or S0 for no projection
	 */
	String displayPosition;
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
		sittingPositions = new HashMap<String,String>();

		
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
		
		
	}


	@Override
	public void penTaken(int penID){
		System.out.println("penTaken("+penID+")");
		//TODO start everything...
	}
		
	
	@Override
	public void processHMMEvent(String ubiid, String stateName){
		System.out.println("processHMMEvent("+ubiid+","+stateName+")");
		if(ubiid==lecturerID){
			lecturerPosition=stateName;
		}else{
			sittingPositions.put(ubiid, stateName);
		}
		
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
	}
	
	/**
	 * Add gbi for extron, projectors, screens, lamps, ...
	 * @param gbiID
	 * @param gbi
	 */
	@Override
	public void addGBI(String gbiID, GoalBasedInteractor gbi){
		System.out.println("addGBI(gibID,gbi)");
		//TODO ...
	}
	
	
	protected void startOutput() {
		// TODO	as a thread
		//adjust camera
		
		//TODO which preset for which number?
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
