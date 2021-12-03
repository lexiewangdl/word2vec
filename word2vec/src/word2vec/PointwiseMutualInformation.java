package word2vec;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class PointwiseMutualInformation {

	static String doc = "The dog saw a bell. The dog chased the ball. A boy saw the dog. The boy chased the dog.";

	/*
	 * Pointwise Mutual Information (PMI) tells us how informative a context word is
	 * about a target word PMI(context, target) =
	 * log2(P(context,target)/P(context)*P(target)
	 */

	/*
	 * P(context) = total for feature c aross all vectors
	 */
	public double getN(double[][] coocurrence) {
		double n = 0.0;
		for (int i = 0; i < coocurrence.length; i++) {
			n += Arrays.stream(coocurrence[i]).sum();
		}
		return n;
	}

	public double getPContext(double[][] coocurrence, int cIndex, double n) {
		double[] c = coocurrence[cIndex];
		double cc = Arrays.stream(c).sum();

		double pContext = cc / n;

		return pContext;
	}

	public double getPTarget(double[][] coocurrence, int tIndex, double n) {
		SparseVector sv = new SparseVector();

		double[] tColumn = sv.getColumn(coocurrence, tIndex);
		double tt = Arrays.stream(tColumn).sum();

		double pTarget = tt / n;

		return pTarget;
	}

	public double getPct(double[][] coocurrence, int cIndex, int tIndex, double n) {
		double Tc = coocurrence[cIndex][tIndex]; // value of context feature c in target t
		double pct = Tc / n;
		return pct;
	}

	public double calculatePMI(double[][] coocurrence, int cIndex, int tIndex) {
		double N = getN(coocurrence);
		double pt = getPTarget(coocurrence, tIndex, N);
		double pc = getPContext(coocurrence, cIndex, N);
		double jointPct = getPct(coocurrence, cIndex, tIndex, N);
		double valuePMI = Math.log(jointPct / (pt * pc));
		return valuePMI;
	}
	
	public double[][] updateCoocurrence(double[][] coocurrence){
		double[][] newCoocurrence = new double[coocurrence[0].length][coocurrence[0].length];
		for (int t=0; t<coocurrence[0].length; t++) {
			for (int c=0; c<coocurrence[0].length; c++) {
				double PMI = calculatePMI(coocurrence, c, t);
				if (PMI<=0) {
					newCoocurrence[c][t] = 0.0;
				} else {
					newCoocurrence[c][t] = calculatePMI(coocurrence, c, t);
				}
			}
		}
		return newCoocurrence;
	}

	public static void main(String[] args) {
		SparseVector sv = new SparseVector();
		PointwiseMutualInformation pmi = new PointwiseMutualInformation();

		System.out.println("----PRINTING TEST MATRIX----");
		double[][] testMatrix = sv.getCoocurrence(doc, 2);
		System.out.println(Arrays.deepToString(testMatrix));

		double NN = pmi.getN(testMatrix);
		System.out.println("----Value of N:----" + NN);
		double pt = pmi.getPTarget(testMatrix, 2, NN);
		double pc = pmi.getPContext(testMatrix, 7, NN);
		System.out.println("Value of P(target) is " + pt + " and value of P(context) is " + pc);

		/*
		 * [[0.0, 3.0, 3.0, 0.0, 1.0, 1.0, 3.0, 0.0, 1.0, 2.0], 
		 *  [3.0, 0.0, 5.0, 2.0, 0.0, 0.0, 5.0, 4.0, 1.0, 3.0], 
		 *  [3.0, 5.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0, 0.0, 0.0], 
		 *  [0.0, 2.0, 2.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0, 1.0], 
		 *  [1.0, 0.0, 1.0, 2.0, 0.0, 1.0, 2.0, 0.0, 0.0, 1.0], 
		 *  [1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0], 
		 *  [3.0, 5.0, 2.0, 0.0, 2.0, 1.0, 0.0, 0.0, 1.0, 0.0], 
		 *  [0.0, 4.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0], 
		 *  [1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0], 
		 *  [2.0, 3.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0]]
		 */

		// context word: chased, index = 7
		// target word: dog, index = 2
		double PMIValue = pmi.calculatePMI(testMatrix, 7, 2);
		System.out.println("Value of PMI of context word chased about target word dog is " + PMIValue);
		
		double[][] newMatrix = pmi.updateCoocurrence(testMatrix);
		System.out.println(Arrays.deepToString(newMatrix));

	}

}
