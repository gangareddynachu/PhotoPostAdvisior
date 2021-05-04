package tools.gcs;

import java.util.ArrayList;

/** 
 * The class for rating the score of photo.
 * 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class Rater {
	/**
	 * Give a score with the input information.
	 * 
	 * @param likes		The like count.
	 * @param comments	The comments
	 * @return			The score.
	 */
	public static float rate(int likes, ArrayList<String> comments) {
		float score = 0;
		score += likes;
		score += comments.size();
		for (String comment : comments) {
			score += comment.length() * 0.1;
		}
		return score;
	}
}
