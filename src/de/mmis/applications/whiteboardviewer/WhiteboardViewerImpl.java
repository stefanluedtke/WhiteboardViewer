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
			//alles los
		}else if(event.getEventType()==Type.PENINSERTED){
			//weiter anzeigen wie bisher, aber keine aenderungen
			//mehr machen
		}	
	}	
	
	public void processTableHMMEvent(HMMFilterEvent event) {
		//??, jedes mal aktualisieren
	}
	
	public void processScreenHMMEvent(HMMFilterEvent event){
		
		//if(prob(aktuellerscreen)<0.5):
		//vll screen aktualisieren, blabla
	}

}
