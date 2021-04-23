package tools.gcs;

import java.util.ArrayList;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Photo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5437674884593196427L;
	private URL url;
	private ArrayList<String> comments;
	private int likes;
	private ArrayList<Label> labels;
	private float score;
	private String bucketName;
	private String objectName;
	private boolean labeled;
	
	public Photo(String url, ArrayList<String> comments, int likes) throws MalformedURLException {
		this.url = new URL(url);
		this.bucketName = null;
		this.objectName = null;
		this.setSource();
		
		this.comments = comments;
		this.likes = likes;
		
		this.score = Rater.rate(this.likes, this.comments);
		this.labels = new ArrayList<Label>();
		this.labeled = false;
	}
	
	private void setSource() {
		String[] path = this.url.getPath().split("/");
		ArrayList<String> segments = new ArrayList<String>();
		int index = (path[0] == "") ? 2 : 1;
		for (int i=index+1; i<path.length; ++i) {
			segments.add(path[i]);
		}
		this.bucketName = path[index];
		this.objectName = String.join("/", segments);
	}

	public URL getUrl() {
		return url;
	}

	public ArrayList<String> getComments() {
		return comments;
	}

	public int getLikes() {
		return likes;
	}

	public ArrayList<Label> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<Label> labels) {
		this.labels = labels;
	}

	public float getScore() {
		return score;
	}

	public String getBucketName() {
		return bucketName;
	}

	public String getObjectName() {
		return objectName;
	}
	
	public void completeLabelsByDict(String projectId, Word2VecDict dict) {
		try {
			ArrayList<String> labels =
					GCV.getImageLabels(projectId, this.bucketName, this.objectName);			 
			for (String labelName : labels) {				
				if (dict.hasLabel(labelName)) {
					Label label = dict.getLabel(labelName);
					this.labels.add(label);
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.labeled = true;
	}
	
	public void completeLabels(String projectId) {
		try {
			ArrayList<String> labels =
					GCV.getImageLabels("test-eclipse-tools", this.bucketName, this.objectName);			 
			this.labels = Word2Vec.getLabels(labels);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.labeled = true;
	}
	
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

	public boolean isLabeled() {
		return labeled;
	}
	
	public String getName() {
		Path path = Paths.get(this.url.getFile());
		return path.getFileName().toString();
	}
}
