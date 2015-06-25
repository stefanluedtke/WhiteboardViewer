package de.mmis.applications.whiteboardviewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mmis.core.base.event.AbstractObservable;
import de.mmis.core.base.event.Event;
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
	
	public WhiteboardViewerImpl(){
		sittingPositions = new HashMap<String,Integer>();
		//neue tags werden mit addListenerTag hinzugefuegt
	}

	@Override
	public void processPenEvent(PenSensor.Event event) {
		if(event.getEventType()==Type.PENTAKEN){
			//wenn bereits min. ein stift genommen, ignoriere, sonst:
			//beschriebenes whiteboard ermitteln
			//anzeigeposition ermitteln
			//kamera ausrichten
			//dauerhaft whiteboard abfilmen und anzeigen
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
		
	}
	
	@Override
	public void processScreenHMMEvent(HMMFilterEvent event){
		
		//if(prob(aktuellerscreen)<0.5):
			//anzeigeposition aktualisieren
	}
	

	/**
	 * 
	 */
	public void updateDisplay(){
		
	}
	
	/**
	 * Adds a new id of a listener. To work properly, 
	 * a listener-hmm with the same id has to exist
	 * @param tagID
	 */
	public void addListenerID(String id){
		sittingPositions.put(id, 0);
	}

}
