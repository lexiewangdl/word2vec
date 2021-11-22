package word2vec;

import java.util.*;

public class CosineSimilarity {

	/*
	 * Cosine-similarity of two word vectors A and B is defined as: cosine(A, B) =
	 * A.B/|A||B| A.B is the dot product of A and B, for A being a vector [A1, A2,
	 * A3], B being [B1, B2, B3] A.B = A1*B1+A2*B2+A3*B3 |A| represents the length
	 * of vector A, |A| = sqrt(A*A) = sqrt(A1*A1+A2*A2+A3*A3)
	 * https://stackoverflow.com/questions/520241/how-do-i-calculate-the-cosine-similarity-of-two-vectors
	 */

	public double cosineSim(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0, normA = 0.0, normB = 0.0, cosSim;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		cosSim = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));

		return cosSim;
	}
	
	public static void main(String[] args) {
		CosineSimilarity cs = new CosineSimilarity();

		double[] ecstatic = {1.45, 1.95, 0.61}; // Double array data initialization:
		double[] excited = {3.11, 1.43, 2.33}; // https://stackoverflow.com/questions/18578864/double-array-initialization-in-java
		
		double result = cs.cosineSim(ecstatic, excited);
		System.out.println(ecstatic[1]);
		System.out.println(excited[1]);
		System.out.println(result);
		
		double[] chased = {0,0,1,0,1,0,2};
		double[] saw = {1,0,1,0,1,0,1};
		double result2 = cs.cosineSim(chased, saw);
		System.out.println(result2);
	}

}
