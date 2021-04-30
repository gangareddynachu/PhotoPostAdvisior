package tools.gcs;

import java.util.ArrayList;

import com.google.appengine.api.datastore.EntityNotFoundException;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


/** 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class Photo implements Serializable {
	private static final long serialVersionUID = -5437674884593196427L;
	private String url;
	private String name;
	private ArrayList<String> comments;
	private int likes;
	private ArrayList<Label> labels;
	private float score;
	private boolean labeled;
	
	/**
	 * Initiate a Photo object.
	 * 
	 * @param name			The name of the photo.
	 * @param url			The FB public URL of the photo.
	 * @param comments		The comment of the photo.
	 * @param likes			The like count of the photo.
	 */
	public Photo(String name, String url, ArrayList<String> comments, int likes) {
		this.url = url;
		this.name = name;
		this.comments = comments;
		this.likes = likes;
		
		this.score = Rater.rate(this.likes, this.comments);
		this.labels = new ArrayList<Label>();
		this.labeled = false;
	}
	
	/**
	 * Get the FB URL of the photo.
	 * 
	 * @return The FB URL of the photo.
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Get the comments of the photo.
	 * 
	 * @return The comments of the photo.
	 */
	public ArrayList<String> getComments() {
		return comments;
	}

	/**
	 * Get the like count of the photo.
	 * 
	 * @return The like count of the photo.
	 */
	public int getLikes() {
		return likes;
	}
	
	/**
	 * Get the label objects of the photo.
	 * 
	 * @return The label objects of the photo.
	 */
	public ArrayList<Label> getLabels() {
		return labels;
	}
	
	/**
	 * Set the label objects of the photo.
	 * 
	 * @param labels	The label objects for setting.
	 */
	public void setLabels(ArrayList<Label> labels) {
		this.labels = labels;
	}

	/**
	 * Get the score of the photo.
	 * 
	 * @return The score of the photo.
	 */
	public float getScore() {
		return score;
	}
	
	/**
	 * Fill up the label object for this photo.
	 * 
	 * @param projectId		The Google App id.
	 */
	public void completeLabelsByGCD(String projectId) {
		try {
			ArrayList<String> labels = GCV.getImageLabels(this.url);
			Word2VecGCD finder = new Word2VecGCD(projectId);
			this.labels = finder.getLabels(labels);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.labeled = true;
	}
	
	/**
	 * Get the nearest label in the provided list.
	 * 
	 * @param categorylabels		The candidate labels
	 * @return						The nearest label.
	 */
	public Label nearestLabel(ArrayList<Label> categorylabels) {
		Label nearest = labels.get(0);
		double dist = 50;
		for (Label cLabel : categorylabels) {
			for (Label label : this.labels) {
				double newDist = label.dist(cLabel);
				if (newDist < dist) {
					nearest = cLabel;
					dist = newDist;
					break;
				}
			}
		}
		return nearest;
	}
	
	/**
	 * Check if this photo is labeled or not.
	 * 
	 * @return	True if it is labeled. Otherwise, False.
	 */
	public boolean isLabeled() {
		return labeled;
	}
	
	/**
	 * Get the name of this photo.
	 * 
	 * @return The name of the photo.
	 */
	public String getName() {
		return this.name;
	}
}
