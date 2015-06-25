package de.mmis.applications.whiteboardviewer;

import de.mmis.core.base.event.AbstractObservable;
import de.mmis.core.base.event.Event;
import de.mmis.devices.pensensor2.PenSensor;
import de.mmis.devices.pensensor2.PenSensor.Event.Type;
import de.mmis.utilities.hmm.discrete.HMMFilterEvent;


public class WhiteboardViewerImpl extends AbstractObservable<Event> implements WhiteboardViewer {

	

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
	public void processTableHMMEvent(HMMFilterEvent event) {
		//sitzpositionen aktualisieren (jedes mal?)
	}
	
	@Override
	public void processScreenHMMEvent(HMMFilterEvent event){
		
		//if(prob(aktuellerscreen)<0.5):
			//anzeigeposition aktualisieren
	}

}
