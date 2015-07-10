package de.mmis.applications.whiteboardviewer;

public class WhiteboardViewerTest {

	public static void main(String[] args) throws InterruptedException {
		WhiteboardViewer wv = new WhiteboardViewerImpl();
		
		wv.setLecturerID("S");
		
		wv.processHMMEvent("2", "T1");
		Thread.sleep(2000);
		wv.processHMMEvent("1", "T1");
		Thread.sleep(1000);
		wv.processHMMEvent("9", "T7");
		Thread.sleep(1000);
		wv.processHMMEvent("S", "W0");
		Thread.sleep(10000);
		wv.processHMMEvent("3", "T9");
		Thread.sleep(2000);
		wv.processHMMEvent("5", "T9");
		Thread.sleep(5000);	
		wv.penTaken();
		Thread.sleep(2000);
		wv.processHMMEvent("S", "W1");
		Thread.sleep(2000);
		wv.processHMMEvent("S", "W2");
		Thread.sleep(2000);
		wv.processHMMEvent("S", "W1");

	}

}
