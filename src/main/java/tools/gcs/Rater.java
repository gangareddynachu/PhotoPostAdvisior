package tools.gcs;

import java.util.ArrayList;

public class Rater {
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
