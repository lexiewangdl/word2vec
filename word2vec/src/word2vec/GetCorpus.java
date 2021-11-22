package word2vec;

import java.io.*;
import java.util.HashMap;

public class GetCorpus {
	
	static HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	
	//Pre-process line
	public String[] getSents(String line) {
		String [] sents = null;
		if (line != null) {
			line = line.replaceAll("(\\r|\\n|,|- )", ""); //Remove comma, dash, and other special characters //dash always comes with 2 spaces
			line = line.toLowerCase();
			sents = line.split("\\."); //Split line into sentences
			for (int i=0;i<sents.length;i++) {
				String sent = sents[i];
				sent = sent.strip();
				sents[i] = sent; //If a sentence begins with empty space, strip this space;
			}
		}
		return sents;
	}
	
	public void updateDict(String[] sents) {
		int len = sents.length;
		for (int i=0; i<len; i++) {
			String sent = sents[i];
			sent = sent.strip(); //If a sentence begins with empty space, strip this space;
			
			String[] vocab = sent.split(" ");
			for (int j=0; j<vocab.length; j++) { //For every word in the sentence
				if (dictionary.containsKey(vocab[j])) { //Check if word already exists in dictionary
					int val = dictionary.get(vocab[j]);
					dictionary.replace(vocab[j], val+1); //Update count
				} else {
					dictionary.put(vocab[j], 1);
				}
			}
		}
		return;
	}
	
	public static void main(String[] args) {
		
		GetCorpus gc = new GetCorpus();
		
		//Read the corpus from text file
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("small.txt"));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
				String[] sents = gc.getSents(line);
				if (sents != null) {
					gc.updateDict(sents);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Current dictionary: "+dictionary);
	}

}
