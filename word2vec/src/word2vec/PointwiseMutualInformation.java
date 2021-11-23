package word2vec;

import java.util.Arrays;

public class PointwiseMutualInformation {
	
	/*
	 * Pointwise Mutual Information (PMI) tells us how informative a context word is about a target word
	 * PMI(context, target) = log2(P(context,target)/P(context)*P(target)
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
		double [] c = coocurrence[cIndex];
		double cc = Arrays.stream(c).sum();

		double pContext = cc/n;
		
		return pContext;
	}
	
	public double getPTarget(double[][] coocurrence, int tIndex, double n) {
		SparseVector sv = new SparseVector(); 
		
		double[] tColumn = sv.getColumn(coocurrence, tIndex);
		double tt = Arrays.stream(tColumn).sum();
		
		double pTarget = tt/n;

		return pTarget;
	}
	
}
