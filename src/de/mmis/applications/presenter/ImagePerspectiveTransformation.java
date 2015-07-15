package de.mmis.applications.presenter;

import de.mmis.devices.camera.AXISHTTPv2;

public interface ImagePerspectiveTransformation {
	
	public void startImageProcessing(String cameraPreset);
	
	public void stopThread();


}
