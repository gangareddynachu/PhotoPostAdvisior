package tools.gcs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Word2VecDict {
	private final File[] models =  new File("./model").listFiles();
	private HashMap<String, Label> dictionary;
	
	public Word2VecDict () {
		this.dictionary = new HashMap<String, Label>();
		loadDict();
	}
	
	private void loadDict() {
		for (File model : this.models) {
			try {
			    Scanner fin = new Scanner(model, "UTF-8");
			    while (fin.hasNextLine()) {
			    	String[] elements = fin.nextLine().split(" ");
			    	String name = elements[0];
			    	ArrayList<Float> vec = new ArrayList<Float>();
			    	for (int i=1; i<elements.length; i ++) {
			    		vec.add(Float.parseFloat(elements[i]));
			    	}
			    	Label label = new Label(name, vec);
			    	this.dictionary.put(name, label);
			    }
			    fin.close();
			} catch (FileNotFoundException e) {
			    System.out.println("An error occurred.");
			    e.printStackTrace();
			}
		}
	}
	
	public boolean hasLabel(String labelName) {
		return this.dictionary.containsKey(labelName);
	}
	
	public Label getLabel(String labelName) {
		return this.dictionary.get(labelName);
	}
}
