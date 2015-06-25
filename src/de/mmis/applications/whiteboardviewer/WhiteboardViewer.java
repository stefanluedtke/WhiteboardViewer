package de.mmis.applications.whiteboardviewer;

import java.util.List;
import java.util.Map;

import de.mmis.core.base.event.Event;
import de.mmis.core.base.event.Observable;
import de.mmis.devices.pensensor2.PenSensor;
import de.mmis.utilities.hmm.discrete.HMMFilterEvent;
import de.mmis.utilities.hmm.discrete.State;

public interface WhiteboardViewer{
	
	public void processPenEvent(PenSensor.Event event);
	
	public void processListenerHMMEvent(Map<State<List<Double>>,Double> stateProbability, String sender);
	
	public void processScreenHMMEvent(HMMFilterEvent event);

}
