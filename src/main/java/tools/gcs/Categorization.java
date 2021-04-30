package tools.gcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/** 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class Categorization {
	/**
	 * @param 	labels		An array list of labels.
	 * @param 	photos		An array list of photos.
	 * 
	 * @return 	A Hash map mapping the category names to the photo sets.
	 */
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
