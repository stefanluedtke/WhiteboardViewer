package de.mmis.applications.whiteboardviewer;

public class WhiteboardViewerTest {

	public static void main(String[] args) throws InterruptedException {
		WhiteboardViewerImpl wv = new WhiteboardViewerImpl(3);
		
		wv.setLecturerID("S");
		
//		wv.sittingPositions.put("010-000-004-001", "T1");
//		wv.sittingPositions.put("010-000-004-002", "T1");
//		wv.sittingPositions.put("010-000-004-003", "T2");
//		wv.sittingPositions.put("010-000-004-004", "T2");
//		wv.sittingPositions.put("010-000-004-005", "T3");
//		wv.sittingPositions.put("010-000-004-006", "T3");
//		wv.sittingPositions.put("010-000-004-007", "T4");
//		wv.sittingPositions.put("010-000-004-008", "T4");
//		wv.sittingPositions.put("010-000-004-009", "T5");
//		wv.sittingPositions.put("010-000-004-010", "T5");
//		wv.sittingPositions.put("010-000-004-020", "T6");
//		wv.sittingPositions.put("010-000-004-037", "T6");
		wv.sittingPositions.put("010-000-004-047", "T7");
		wv.sittingPositions.put("010-000-004-057", "T7");
		wv.sittingPositions.put("010-000-004-067", "T8");
		wv.sittingPositions.put("010-000-004-077", "T8");
		wv.sittingPositions.put("010-000-004-087", "T9");
		wv.sittingPositions.put("010-000-004-097", "T9");
		
		String whiteboard = "W4";
		
		System.out.println("Writing on Board "+ whiteboard);

		try {
			System.out.println("Calculated (roomnumber) Screen: " + wv.getDisplayPosition(whiteboard));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
