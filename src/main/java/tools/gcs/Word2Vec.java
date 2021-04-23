package tools.gcs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import java.util.Scanner;

public class Word2Vec {
	static private File[] models =  new File("./model").listFiles();
	
	static private HashMap<String, ArrayList<Float>> vectorsFound(File model, HashSet<String> targets) {
		HashMap<String, ArrayList<Float>> vectors =
				new HashMap<String, ArrayList<Float>>();
		try {
		    Scanner fin = new Scanner(model, "UTF-8");
		    while (fin.hasNextLine()) {
		    	String[] elements = fin.nextLine().split(" ");
		    	String label = elements[0];		    	
		    	if (targets.contains(label)) {
		    		System.out.println("Found: " + label);
		    		ArrayList<Float> vec = new ArrayList<Float>();
			    	for (int i=1; i<elements.length; i ++) {
			    		vec.add(Float.parseFloat(elements[i]));
			    	}
			    	vectors.put(label, vec);
		    	}
		    }
		    fin.close();
		} catch (FileNotFoundException e) {
		    System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		return vectors;
	}
	
	static public HashMap<String, ArrayList<Float>> findWords(String serialized_words) {
		String[] words = serialized_words.split(" ");
		return Word2Vec.findWords(words);
	}
	
	static public HashMap<String, ArrayList<Float>> findWords(String[] words) {		
		HashSet<String> targets = new HashSet<String>();  	// Using set for uniqueness
		targets.addAll(Arrays.asList(words));		
		HashMap<String, ArrayList<Float>> results =
				new HashMap<String, ArrayList<Float>>();
		for (File model : Word2Vec.models) {
			results.putAll(vectorsFound(model, targets));	// Collect the found vectors
		}		
		targets.removeAll(results.keySet());				// Remove all found vectors from the target pool
		if (!targets.isEmpty()) {
			System.out.println("Words not found: " + targets.toString());
		}
		return results;
	}
	
	static public ArrayList<Label> getLabels(String serialized_words) {
		String[] words = serialized_words.split(" ");
		return Word2Vec.getLabels(words);
	}
	
	static public ArrayList<Label> getLabels(String[] words) {
		HashMap<String, ArrayList<Float>> results = Word2Vec.findWords(words);	
		ArrayList<Label> labels = new ArrayList<Label>();		
		for (Entry<String, ArrayList<Float>> entry: results.entrySet()) {
			Label label = new Label(entry.getKey(), entry.getValue());
			labels.add(label);
		}
		return labels;
	}
	
	static public ArrayList<Label> getLabels(ArrayList<String> words) {
		HashMap<String, ArrayList<Float>> results = Word2Vec.findWords(String.join(" ", words));	
		ArrayList<Label> labels = new ArrayList<Label>();		
		for (Entry<String, ArrayList<Float>> entry: results.entrySet()) {
			Label label = new Label(entry.getKey(), entry.getValue());
			labels.add(label);
		}
		return labels;
		
	}
}
