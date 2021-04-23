package tools.gcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class PhotoSet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8204618349102673081L;
	private ArrayList<Photo> photos;
	private HashMap<String, Integer> labelCounts;
	private HashSet<Label> labelPool;
	
	public PhotoSet (ArrayList<Photo> photos) {
		this.photos = photos;
		this.labelPool = new HashSet<Label>();
		this.labelCounts = new HashMap<String, Integer>();
		for (Photo photo : photos) {
			labelPool.addAll(photo.getLabels());
			for (Label label : photo.getLabels()) {
				String name = label.getName();
				Integer count = this.labelCounts.get(name);
				if (count == null) {
					this.labelCounts.put(name, 1);
				} else {
					this.labelCounts.put(name, count + 1);
				}
			}
		}
	}

	public Map<String, Integer> getLabelCounts() {
		return labelCounts;
	}
	
	public ArrayList<String> suggestedLabels() {
		HashMap<Integer, ArrayList<String>> fliped = new HashMap<Integer, ArrayList<String>>();
		for (Entry<String, Integer> entry : this.labelCounts.entrySet()) {
			ArrayList<String> labels = (fliped.containsKey(entry.getValue())) ?
					fliped.get(entry.getValue()) : new ArrayList<String>();
			labels.add(entry.getKey());
			fliped.put(entry.getValue(), labels);
		}
		TreeMap<Integer, ArrayList<String>> sorted = new TreeMap<Integer, ArrayList<String>>(fliped);
		ArrayList<String> suggested = sorted.entrySet().iterator().next().getValue();
		return suggested;
	}
	
	public ArrayList<Photo> getPhotos() {
		return this.photos;
	}
	
	public ArrayList<String> getPhotoNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Photo photo : this.photos) {
			names.add(photo.getName());
		}
		return names;
	}
	
	public int getTotalLikesCount() {
		int count = 0;
		for (Photo photo : this.photos) {
			count += photo.getLikes();
		}
		return count;
	}
	
	public int getTotalCommentsCount() {
		int count = 0;
		for (Photo photo : this.photos) {
			count += photo.getComments().size();
		}
		return count;
	}
	
	public float getTotalScore() {
		float count = 0;
		for (Photo photo : this.photos) {
			count += photo.getScore();
		}
		return count;
	}
}
