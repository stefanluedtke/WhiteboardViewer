package de.mmis.applications.presenter;

import de.mmis.devices.camera.AXISHTTPv2;
import de.mmis.devices.camera.AXISHTTPv2Impl;

import java.awt.image.BufferedImage;

public class IPT{
	

    public static void main(String[] args){
    	ImagePerspectiveTransformation ipt=new ImagePerspectiveTransformation();

		AXISHTTPv2 camera = new AXISHTTPv2Impl(null);
		String cameraPreset = new String();

		while (true) {
			BufferedImage image = ipt.imageCalculation(camera, cameraPreset);
			ipt.showImage(image);
		}

	
    }
}