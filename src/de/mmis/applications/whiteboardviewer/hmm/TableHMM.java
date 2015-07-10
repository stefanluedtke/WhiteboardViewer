package de.mmis.applications.whiteboardviewer.hmm;

import de.mmis.utilities.hmm.discrete.MSHMMFilter;

public class TableHMM {

	public static void main(String[] args) {
		MSHMMFilter hmm = new de.mmis.utilities.hmm.discrete.MSHMMFilter("T1", "T2", "T3", "T4",
				"T5", "T6", "T7", "T8", "T9");


		double s = 0.98;
		double n1 = (1 - s) / 2;

		hmm.setPrior(1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		hmm.setTransitionProbabilities(0, s, n1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, n1); // T1
		hmm.setTransitionProbabilities(1, n1, s, n1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0); // T2
		hmm.setTransitionProbabilities(2, 0.0, n1, s, n1, 0.0, 0.0, 0.0, 0.0, 0.0); // T3
		hmm.setTransitionProbabilities(3, 0.0, 0.0, n1, s, n1, 0.0, 0.0, 0.0, 0.0); // T2
		hmm.setTransitionProbabilities(4, 0.0, 0.0, 0.0, n1, s, n1, 0.0, 0.0, 0.0); // T3
		hmm.setTransitionProbabilities(5, 0.0, 0.0, 0.0, 0.0, n1, s, n1, 0.0, 0.0); // T4
		hmm.setTransitionProbabilities(6, 0.0, 0.0, 0.0, 0.0, 0.0, n1, s, n1, 0.0); // T5
		hmm.setTransitionProbabilities(7, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, n1, s, n1); // T6
		hmm.setTransitionProbabilities(8, n1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, n1, s); // T7

		hmm.setNormalDiagObservationModel(0, new double[] { 2.30, 2.18 }, new double[] { 1.0, 1.0 }); // D
		hmm.setNormalDiagObservationModel(1, new double[] { 2.30, 3.78 }, new double[] { 1.0, 1.0 }); // S1
		hmm.setNormalDiagObservationModel(2, new double[] { 3.70, 3.78 }, new double[] { 1.0, 1.0 }); // S3
		hmm.setNormalDiagObservationModel(3, new double[] { 5.10, 3.78 }, new double[] { 0.5, 0.5 }); // 3
		hmm.setNormalDiagObservationModel(4, new double[] { 6.50, 2.98 }, new double[] { 0.5, 0.5 }); // 4
		hmm.setNormalDiagObservationModel(5, new double[] { 6.50, 1.58 }, new double[] { 0.5, 0.5 }); // 5
		hmm.setNormalDiagObservationModel(6, new double[] { 5.10, 1.58 }, new double[] { 0.5, 0.5 }); // 6
		hmm.setNormalDiagObservationModel(7, new double[] { 3.70, 1.58 }, new double[] { 0.5, 0.5 }); // 7
		hmm.setNormalDiagObservationModel(8, new double[] { 2.30, 1.58 }, new double[] { 0.5, 0.5 }); // 8

	}

}
