package tools.gcs;

import java.util.ArrayList;
import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class Word2VecGCD {
	private Datastore datastore;
	private String kind = "Vector";
	
	public Word2VecGCD(String projectId) {
		this.datastore = DatastoreOptions.newBuilder()
				.setProjectId(projectId)
				.build().getService();
	}
	
	public Entity getVectorEntity(String labelName) {
		Key key = this.datastore.newKeyFactory()
			    .setKind(this.kind)
			    .newKey(labelName);
		Entity get = this.datastore.get(key);
		return get;
	}
	
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
	
	public ArrayList<Label> getLabels(String[] words) {
		ArrayList<String> _words = new ArrayList<String>();
		for (String word : words) {
			_words.add(word);
		}
		return this.getLabels(_words);
	}
	
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
