package de.mmis.applications.presenter;

import de.mmis.devices.camera.AXISHTTPv2;
import java.awt.image.BufferedImage;

public class IPT{
    public static void main(String[] args){
    	ImagePerspectiveTransformation ipt=new ImagePerspectiveTransformation();

		AXISHTTPv2 camera = new AXISHTTPv2;
		String cameraPreset = new String();

		while (true) {
			BufferedImage image = ipt.imageCalculation(camera, cameraPreset);
			ipt.showImage(image);
		}

	}
    }
}