package tools.gcs;

import java.io.Serializable;
import java.util.ArrayList;


/** 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class Label implements Serializable {
	private static final long serialVersionUID = -2646114581787005854L;
	private String name;
	private ArrayList<Float> vector;
	private float score;
	
	/**
	 * Initiate a Label object.
	 * 
	 * @param description	The name of the label.
	 * @param vec			The vector of the label as an array list of floating point numbers.
	 */
	public Label (String description, ArrayList<Float> vec) {
		this.name = description;
		this.vector = vec;
		this.score = 1;
	}
	
	/**
	 * Initiate a Label object.
	 * 
	 * @param description	The name of the label.
	 * @param vec			The vector of the label as an array list of floating point numbers.
	 * @param score			The score of the label.
	 */
	public Label (String description, ArrayList<Float> vec, float score) {
		this.name = description;
		this.vector = vec;
		this.score = score;
	}
	
	/**
	 * Get the name of the label
	 * 
	 * @return		The name of the label
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the vector of the label.
	 * 
	 * @return		The vector of the label.
	 */
	public ArrayList<Float> getVector() {
		return vector;
	}
	
	/**
	 * Get the score of the label
	 * 
	 * @return		The score of the label.
	 */
	public float getScore() {
		return score;
	}
	
	/**
	 * Compute the distance from this label to the input label.
	 *  
	 * @param other		The other label.
	 * @return			The distance.
	 */
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
