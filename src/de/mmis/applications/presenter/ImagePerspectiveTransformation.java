package de.mmis.applications.presenter;

import de.mmis.devices.camera.AXISHTTPv2;

public interface ImagePerspectiveTransformation {
	
	public void startImageProcessing(AXISHTTPv2 camera,String cameraPreset);


}
