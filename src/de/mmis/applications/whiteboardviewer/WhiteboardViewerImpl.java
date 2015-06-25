package de.mmis.applications.whiteboardviewer;

import java.io.IOException;
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
	Map<String , String> sittingPositions;
	String lecturerPosition;
	int nrPensTaken;
	AXISHTTPv2 camera;
	static final int[][] viewQualityMatrix = 
		{{-1,0,1,1,1,1,-1,-1,-1},
		{-1,-1,-1,-1,1,1,1,1,0},
		{-1,-1,-1,-1,0,1,1,1,1},
		{0,-1,-1,-1,0,0,1,1,1},
		{0,-1,-1,-1,-1,-1,1,1,1},
		{1,-1,-1,-1,-1,-1,0,1,1},
		{1,1,1,0,-1,-1,-1,-1,-1}};
			
	
	public WhiteboardViewerImpl(){
		sittingPositions = new HashMap<String,String>();
		//new tags are added with addListenerID
		
		lecturerPosition="0";
		nrPensTaken=0;
		
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
			int whiteboard=Integer.parseInt(lecturerPosition);
			//get screen where to show whiteboard
			int displayPosition = getDisplayPosition(whiteboard);
			
			//adjust camera
			//TODO welcher preset zu welcher nummer??
			try {
				camera.moveToPreset(whiteboard);
			} catch (IOException e) {
				System.err.println("Error adjusting camera");
				e.printStackTrace();
			}
			
			//permanently get camera image and show on displayPosition
			//->Thread
			//TODO
			
		}else if(event.getEventType()==Type.PENINSERTED){
			//weiter anzeigen wie bisher, aber keine 
			//aenderungen der whiteboard- oder anzeigeposition
			//mehr machen
		}	
	}	
	
	@Override
	/**
	 * The id is the name of the hmm (@ID in GP)
	 */
	public void processListenerHMMEvent(Map<State<List<Double>>,Double> stateProbability, String id){
		//sitzpositionen aktualisieren (jedes mal?)
		
		//(wenn eine wahrscheinlichkeit >0.5)
		
		//iteriere over all states, find state with max prob
		double maxprob = 0;
		State<List<Double>> maxprobstate=null;
		for(State<List<Double>> state:stateProbability.keySet()){
			double prob = stateProbability.get(state);
			if(prob>maxprob){
				maxprob=prob;
				maxprobstate=state;
			}
		}
		
		//add (tag_id, state (^=table) to sittingPositions
		sittingPositions.put(id,maxprobstate.getName());
		
		//TODO noch was zu tun?
		
	}
	
	@Override
	public void processScreenHMMEvent(Map<State<List<Double>>,Double> stateProbability){
		
		//if(prob(aktuellerscreen)<0.5):
			//anzeigeposition aktualisieren
	}
	

	/**
	 * get display position using whiteboardPosition
	 * and sittingPositions
	 */
	public int getDisplayPosition(int whiteboard){
		//TODO 
		return 0;
	}
	
	/**
	 * Adds a new id of a listener. To work properly, 
	 * a listener-hmm with the same id has to exist
	 * @param tagID
	 */
	public void addListenerID(String id){
		sittingPositions.put(id, "0");
	}


}
