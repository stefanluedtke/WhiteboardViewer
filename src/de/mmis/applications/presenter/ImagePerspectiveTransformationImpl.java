package de.mmis.applications.presenter;

import georegression.struct.point.Point2D_F64;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import org.ejml.data.DenseMatrix64F;

import boofcv.abst.geo.Estimate1ofEpipolar;
import boofcv.alg.distort.DistortImageOps;
import boofcv.alg.distort.PointToPixelTransform_F32;
import boofcv.alg.distort.PointTransformHomography_F32;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.geo.FactoryMultiView;
import boofcv.struct.distort.PixelTransform_F32;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.MultiSpectral;
import de.mmis.devices.camera.AXISHTTPv2;



public class ImagePerspectiveTransformationImpl extends Thread implements ImagePerspectiveTransformation{
	private JFrame presentationWindow;
	private JLabel imageLabel;
	
	private boolean stopFlag=false;
	private AXISHTTPv2 camera;
	private String cameraPreset;
	
	public ImagePerspectiveTransformationImpl(){
		
	}
	

	@Override
	public void run(){
		while(!stopFlag){
			BufferedImage image = imageCalculation(camera, cameraPreset);
			showImage(image);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopThread(){
		stopFlag=true;
	}
	@Override
	public void startImageProcessing(String cameraPreset){
		System.out.println("Start image processing");
		
		//-------------------build frame
		presentationWindow = new JFrame();
		presentationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		presentationWindow.setUndecorated(true);
		presentationWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
		presentationWindow.setVisible(true);
		
		//ESC Key Close Frame
        presentationWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Close");
        presentationWindow.getRootPane().getActionMap().put("Close", new AbstractAction(){
			private static final long serialVersionUID = 1L;
			
			public void actionPerformed(ActionEvent ae){
        		presentationWindow.dispose();
            }
        });
		//----------------end build frame
        
		imageLabel = new JLabel();
		presentationWindow.add(imageLabel);
		
		
		this.camera=camera;
		this.cameraPreset=cameraPreset;
		start();
	}
	
	public BufferedImage imageCalculation(AXISHTTPv2 camera, String cameraPreset){

		//Byte [] in BufferedImage
		BufferedImage cameraImage = null;
	    InputStream cameraImageByteArray = null;
	    try{
	    	cameraImageByteArray = new ByteArrayInputStream(camera.getJpeg());
	        cameraImage = ImageIO.read(cameraImageByteArray);
	    }
	    catch(IOException e){
	        e.printStackTrace();
	    }

		MultiSpectral<ImageFloat32> input = ConvertBufferedImage.convertFromMulti(cameraImage, null, true, ImageFloat32.class);

	    //Erstelle die Augabe
		Dimension screenSize=presentationWindow.getToolkit().getScreenSize();
		MultiSpectral<ImageFloat32> output = input._createNew((int) screenSize.getWidth(), (int) screenSize.getHeight());

		//Position der Bilder
	    ArrayList<AssociatedPair> associatedPairs = new ArrayList<AssociatedPair>();
	    int topLeftCornerOldPositionX = 0;
	    int topLeftCornerOldPositionY = 0;
	    int bottomLeftCornerOldPositionX = 0;
	    int bottomLeftCornerOldPositionY = 0;
	    int bottomRightCornerOldPositionX = 0;
	    int bottomRightCornerOldPositionY = 0;
	    int topRightCornerOldPositionX = 0;
	    int topRightCornerOldPositionY = 0;
	    
	    if(cameraPreset.equals("W1")){
	    	//Board 1 = S1
	    	topLeftCornerOldPositionX = 12;
	    	topLeftCornerOldPositionY = 40;
	    	bottomLeftCornerOldPositionX = 578;
	    	bottomLeftCornerOldPositionY = 80;
	    	bottomRightCornerOldPositionX = 550;
	    	bottomRightCornerOldPositionY = 387;
	    	topRightCornerOldPositionX = 28;
	    	topRightCornerOldPositionY = 320;
	    }
	    else if(cameraPreset.equals("W2")){
	        //Board 2 = S3
	    	topLeftCornerOldPositionX = 8;
	    	topLeftCornerOldPositionY = 80;
	    	bottomLeftCornerOldPositionX = 556;
	    	bottomLeftCornerOldPositionY = 53;
	    	bottomRightCornerOldPositionX = 543;
	    	bottomRightCornerOldPositionY = 327;
	    	topRightCornerOldPositionX = 28;
	    	topRightCornerOldPositionY = 387;
	    }
	    else if(cameraPreset.equals("W3")){
	        //Board 3 = S7
	    	topLeftCornerOldPositionX = 32;
	    	topLeftCornerOldPositionY = 113;
	    	bottomLeftCornerOldPositionX = 545;
	    	bottomLeftCornerOldPositionY = 153;
	    	bottomRightCornerOldPositionX = 521;
	    	bottomRightCornerOldPositionY = 434;
	    	topRightCornerOldPositionX = 54;
	    	topRightCornerOldPositionY = 360;
	    }
	    else if(cameraPreset.equals("W4")){
	        //Board 4 = S9
	    	topLeftCornerOldPositionX = 30;
	    	topLeftCornerOldPositionY = 136;
	    	bottomLeftCornerOldPositionX = 554;
	    	bottomLeftCornerOldPositionY = 89;
	    	bottomRightCornerOldPositionX = 534;
	    	bottomRightCornerOldPositionY = 342;
	    	topRightCornerOldPositionX = 59;
	    	topRightCornerOldPositionY = 415;
	    }
	    else{
	    	System.out.println("In ImagePersepectiveTransformation: Preset unknown: "+cameraPreset);
	    }
	    associatedPairs.add(new AssociatedPair(new Point2D_F64(0,0), new Point2D_F64(topLeftCornerOldPositionX, topLeftCornerOldPositionY)));
	    associatedPairs.add(new AssociatedPair(new Point2D_F64(output.width-1,0), new Point2D_F64(bottomLeftCornerOldPositionX, bottomLeftCornerOldPositionY)));
	    associatedPairs.add(new AssociatedPair(new Point2D_F64(output.width-1,output.height-1), new Point2D_F64(bottomRightCornerOldPositionX, bottomRightCornerOldPositionY)));
	    associatedPairs.add(new AssociatedPair(new Point2D_F64(0,output.height-1), new Point2D_F64(topRightCornerOldPositionX, topRightCornerOldPositionY)));
	    
	    //Homography Algorithmus, erfordert ein Minimum von 4 Punkten
	    Estimate1ofEpipolar computeHomography = FactoryMultiView.computeHomography(true);
	    
	    //Berechne Homography
	    DenseMatrix64F H = new DenseMatrix64F(3,3);
	    computeHomography.process(associatedPairs, H);
	    
	    //Erstelle Transformation fuer Bild
	    PointTransformHomography_F32 homography = new PointTransformHomography_F32(H);
	    PixelTransform_F32 pixelTransform = new PointToPixelTransform_F32(homography);
	    
	    //Uebernehme Verzerrung
	    DistortImageOps.distortMS(input, output, pixelTransform, true, TypeInterpolate.POLYNOMIAL4);

	    BufferedImage finalImage = ConvertBufferedImage.convertTo_F32(output, null, true);

		return finalImage;
	}
	
	public void showImage(BufferedImage image){

		//zeige Bild
		imageLabel.setIcon(new ImageIcon(image));
		presentationWindow.repaint();

	}
	
	public void setCamera(AXISHTTPv2 camera){
		this.camera=camera;
	}
	
}
