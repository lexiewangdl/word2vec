package word2vec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class SparseVector {

	/*
	 * Used GetCorpus() to get a dictionary containing all unique words in corpus,
	 * and the corresponding count of that word.
	 */

	static String doc = "The dog saw a bell. The dog chased the ball. A boy saw the dog. The boy chased the dog.";

	public String[] addStartAndEnd(String[] sentences) {
		for (int i = 0; i < sentences.length; i++) {
			sentences[i] = "<s> " + sentences[i] + " </s>";
			// <s> indicates beginning of sentence
			// </s> indicates end of sentence
		}
		return sentences;
	}

	// store individual words in corpus in a list
	public ArrayList<String> listWords(String[] sentences) {
		ArrayList<String> words = new ArrayList<String>(); // ArrayList<Type> str = new ArrayList<Type>();
		for (int i = 0; i < sentences.length; i++) {
			String sentence = sentences[i];
			String[] wordsInSentence = sentence.split(" ");
			for (int j = 0; j < wordsInSentence.length; j++) {
				words.add(wordsInSentence[j]);
			}
		}
		return words;
	}
	// test result: [<s>, the, dog, saw, a, bell, </s>, <s>, the, dog, chased, the,
	// ball, </s>, <s>, a,
	// boy, saw, the, dog, </s>, <s>, the, boy, chased, the, dog, </s>]

	/**
	 * Determine a dictionary of distinct words in the corpus.
	 * 
	 * @param words a list of strings, each item being one word.
	 * @return vocab a dictionary of distinct words in the corpus as keys, and their
	 *         corresponding word count as values.
	 */
	public LinkedHashMap<String, Integer> distinctWords(ArrayList<String> words) {
		LinkedHashMap<String, Integer> vocab = new LinkedHashMap<String, Integer>(); // vocab is a dictionary to save distinct words

		for (int i = 0; i < words.size(); i++) {
			if (vocab.containsKey(words.get(i))) {
				int val = vocab.get(words.get(i));
				vocab.replace(words.get(i), val + 1);
			} else {
				vocab.put(words.get(i), 1);
			}
		}

		return vocab;
	}

	// test result: {<s>=4, the=6, dog=4, saw=2, a=2, bell=1, </s>=4, chased=2, ball=1, boy=2}
	// vocab.size() = 10

	/**
	 * Assigns a unique index to represent each distinct word in the corpus.
	 * 
	 * @param vocab a dictionary of distinct words in the corpus as keys, and their
	 *              corresponding word count as values.
	 * @return word2idx a dictionary with unique words in the corpus as keys, and a
	 *         unique index representing the word numerically as values.
	 */
	public LinkedHashMap<String, Integer> wordToIndices(LinkedHashMap<String, Integer> vocab) {
		LinkedHashMap<String, Integer> word2idx = new LinkedHashMap<String, Integer>();
		int i = 0;
		for (String key : vocab.keySet()) {
			word2idx.put(key, i);
			i += 1;
		}
		return word2idx;
	}

	// test result: {<s>=0, the=1, dog=2, saw=3, a=4, bell=5, </s>=6, chased=7, ball=8, boy=9}

	/**
	 * 
	 * @param vocab      a dictionary containing all unique words in corpus and
	 *                   their corresponding counts
	 * @param windowSize an integer value indicating size of context window, if
	 *                   windowSize=2, we consider 2 words before and 2 words after
	 *                   the target word
	 * @return a two dimensional array of shape (number of distinct words, number of
	 *         distinct words)
	 */
	public double[][] coocurrenceMatrix(ArrayList<String> words, LinkedHashMap<String, Integer> word2idx, int windowSize) {
		double[][] coocurrence = new double[word2idx.size()][word2idx.size()];
		// array_name[row_index][column_index] = value;

		for (int i = 0; i < words.size(); i++) {
			String target = words.get(i);
			int targetIndex = word2idx.get(target);
			
			for (int j = 1; j <= windowSize; j++) {
				int bf = i-j, af = i+j;
				if (bf>=0) {
					String context = words.get(bf);
					int contextIndex = word2idx.get(context);
					coocurrence[contextIndex][targetIndex] += 1;
				}
				if (af < words.size()) {
					String c = words.get(af);
					int cIndex = word2idx.get(c);
					coocurrence[cIndex][targetIndex] += 1;
				}
			}
			
		}

		return coocurrence;
	}
	
	// Gets single column from 2D Array 
	public double[] getColumn(double[][] matrix, int targetIndex) {
		double[] column = new double[matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			column[i] = matrix[i][targetIndex];
		}
		return column;
		
	}


	public static void main(String[] args) {
		GetCorpus gc = new GetCorpus();
		SparseVector sv = new SparseVector(); 

		String[] sents = gc.getSents(doc);
		String[] sentes = sv.addStartAndEnd(sents);

		// Create new list, each item in list being one individual word token
		ArrayList<String> words = sv.listWords(sentes);
		System.out.println("--------WORDS--------");
		System.out.println(words);


		LinkedHashMap<String, Integer> vocab = sv.distinctWords(words);

		System.out.println("--------GET DISTINCT WORDS--------");
		System.out.println(vocab);
		System.out.println(vocab.size()); // Number of distinct words in our corpus

		System.out.println("--------WORD TO INDICES--------");
		LinkedHashMap<String, Integer> w2idx = sv.wordToIndices(vocab);
		System.out.println(w2idx);
		
		double[][] coocurrence = sv.coocurrenceMatrix(words, w2idx, 2);
		System.out.println(Arrays.deepToString(coocurrence));
		
		double[] firstColumn = sv.getColumn(coocurrence, 0);
		System.out.println(Arrays.toString(firstColumn));
		
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
		
		System.out.println("--------COMPUTE COSINE SIMILARITY--------");
		CosineSimilarity cs = new CosineSimilarity();
		Random r = new Random();
		
		List<String> keysAsArray = new ArrayList<String>(w2idx.keySet());
		System.out.println(keysAsArray);
		String t1 = keysAsArray.get(r.nextInt(keysAsArray.size()));
		String t2 = keysAsArray.get(r.nextInt(keysAsArray.size()));
		int t1idx = w2idx.get(t1);
		int t2idx = w2idx.get(t2);
		double[] t1Embedding = sv.getColumn(coocurrence, t1idx);
		double[] t2Embedding = sv.getColumn(coocurrence, t2idx);
		
		double result = cs.cosineSim(t1Embedding, t2Embedding);
		System.out.println(String.format("The cosine similarity between %s and %s is %f", t1, t2, result));
		// test result: The cosine similarity between dog and chased is 0.621970
	}

}
