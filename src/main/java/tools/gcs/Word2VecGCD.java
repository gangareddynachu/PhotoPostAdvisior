package tools.gcs;

import java.util.ArrayList;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

/** 
 * The class fetching stored vector information from the Google Could Datastore.
 * 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class Word2VecGCD {
	private Datastore datastore;
	private String kind = "Vector";
	
	/**
	 * Initiate the object getting vector information from the datastore.
	 * 
	 * @param projectId The Google App ID.
	 */
	public Word2VecGCD(String projectId) {
		this.datastore = DatastoreOptions.newBuilder()
				.setProjectId(projectId)
				.build().getService();
	}
	
	/**
	 * Get the vector entity object from the cloud datastore.
	 * 
	 * @param labelName		The specified vector name.
	 * @return				The returned vector entity.
	 */
	public Entity getVectorEntity(String labelName) {
		Key key = this.datastore.newKeyFactory()
			    .setKind(this.kind)
			    .newKey(labelName);
		Entity get = this.datastore.get(key);
		return get;
	}
	
	/**
	 * Get the label object with the specified label name
	 * 
	 * @param labelName	The specified vector name.
	 * @return			The label object.
	 */
	public Label getLabel(String labelName) {
		Entity get = getVectorEntity(labelName);
		
		Label label = null;
		
		if (get != null) {
			String vec_str = get.getValue("vec").get().toString();
			ArrayList<Float> vec = new ArrayList<Float>();
			for (String element : vec_str.split(" ")) {
				vec.add(Float.parseFloat(element));
			}
			label = new Label(labelName, vec);
		}
		
		return label;
	}
	
	/**
	 * Get the label objects with a string array of word.
	 * 
	 * @param words	The string array of words.
	 * @return		The array list of label objects.
	 */
	public ArrayList<Label> getLabels(String[] words) {
		ArrayList<String> _words = new ArrayList<String>();
		for (String word : words) {
			_words.add(word);
		}
		return this.getLabels(_words);
	}
	
	/**
	 * Get the label objects with an array list of words.
	 * 
	 * @param words	The array list of words.
	 * @return		The array list of label objects.
	 */
	public ArrayList<Label> getLabels(ArrayList<String> words) {
		ArrayList<Label> results = new ArrayList<Label>();
		for (String word : words) {
			Label label = this.getLabel(word);
			if (label != null) {
				results.add(label);
			}
		}
		return results;
	}
}
