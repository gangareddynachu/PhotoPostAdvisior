package tools.gcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Categorization {
	public static HashMap<String, PhotoSet> nearest(ArrayList<Label> labels, ArrayList<Photo> photos) {
		HashMap<String, ArrayList<Photo>> categories = new HashMap<String, ArrayList<Photo>>();
		for (Label label : labels) {
			categories.put(label.getName(), new ArrayList<Photo>());
		}
		for (Photo photo : photos) {
			Label nearest = photo.nearestLabel(labels);
			categories.get(nearest.getName()).add(photo);
		}
		HashMap<String, PhotoSet> newPhotoSets = new HashMap<String, PhotoSet>();
		for (Entry<String, ArrayList<Photo>> entry : categories.entrySet()) {
			newPhotoSets.put(entry.getKey(), new PhotoSet(entry.getValue()));
		}
		return newPhotoSets;
	}
}
