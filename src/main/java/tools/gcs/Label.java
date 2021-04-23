package tools.gcs;

import java.io.Serializable;
import java.util.ArrayList;

public class Label implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646114581787005854L;
	private String name;
	private ArrayList<Float> vector;
	private float score;
	
	public Label (String description, ArrayList<Float> vec) {
		this.setName(description);
		this.setVector(vec);
		this.setScore(1);
	}
	
	public Label (String description, ArrayList<Float> vec, float score) {
		this.setName(description);
		this.setVector(vec);
		this.setScore(score);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Float> getVector() {
		return vector;
	}
	
	public void setVector(ArrayList<Float> vector) {
		this.vector = vector;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	public double dist (Label other) {
		ArrayList<Float> vec1 = other.getVector();
		ArrayList<Float> vec2 = this.vector;		
		int sizeOfDimension = Math.min(vec1.size(), vec2.size());
		float sum = 0;
		for (int i=0; i<sizeOfDimension; ++i) {
			sum += Math.pow(vec1.get(i) - vec2.get(i), 2);
		}
		return Math.sqrt(sum);
	}
}
