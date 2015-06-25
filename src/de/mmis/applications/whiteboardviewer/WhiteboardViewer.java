package de.mmis.applications.whiteboardviewer;

import de.mmis.core.base.event.Event;
import de.mmis.core.base.event.Observable;
import de.mmis.devices.pensensor2.PenSensor;
import de.mmis.utilities.hmm.discrete.HMMFilterEvent;

public interface WhiteboardViewer{
	
	public void processPenEvent(PenSensor.Event event);
	
	public void processTableHMMEvent(HMMFilterEvent event);
	
	public void processScreenHMMEvent(HMMFilterEvent event);

}
