package de.mmis.applications.whiteboardviewer;

import java.util.List;
import java.util.Map;

import de.mmis.core.base.event.Event;
import de.mmis.core.base.event.Observable;
import de.mmis.core.gbi.GoalBasedInteractor;
import de.mmis.devices.camera.AXISHTTPv2;
import de.mmis.devices.pensensor2.PenSensor;
import de.mmis.utilities.hmm.discrete.HMMFilterEvent;
import de.mmis.utilities.hmm.discrete.State;

public interface WhiteboardViewer{
	

	void processHMMEvent(String ubiid, String stateName);

	void setCamera(AXISHTTPv2 camera);

	void penTaken(int penID);

	void addGBI(String gbiID, GoalBasedInteractor gbi);

	void setLecturerID(String id);

}
