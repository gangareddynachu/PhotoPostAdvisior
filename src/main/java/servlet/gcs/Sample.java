package servlet.gcs;

import java.net.MalformedURLException;
import java.util.ArrayList;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;

import tools.gcs.GCD;
import tools.gcs.Photo;

public class Sample {
	private static final String projectId = "test-eclipse-tools";
	
	/**
	 * Generate testing samples
	 * 
	 * @return An array list of Photo objects.
	 */
	public static ArrayList<Photo> getPhotoSamples() {
		ArrayList<Photo> samples = new ArrayList<Photo>();
		
		String[] urls = {
				"https://storage.cloud.google.com/test-eclipse-tools.appspot.com/temp/Sketchpad.png",
				"https://storage.cloud.google.com/test-eclipse-tools.appspot.com/temp/IMG_20160319_084342.jpg",
				"https://storage.cloud.google.com/test-eclipse-tools.appspot.com/temp/IMG_20160321_133943.jpg"
		};
		int likes = 10;
		String[] _comments = {
				"comment 1",
				"comment 2",
				"comment 3"
			};
		ArrayList<String> comments = new ArrayList<String>();
		for (String c : _comments) {
			comments.add(c);
		}
		for (String url : urls) {
			Photo photo = new Photo("", url, comments, likes);
			samples.add(photo);
		}
		
		return samples;
	}
	
	/**
	 * Generate the photo objects belongs to the FB page
	 * 
	 * @param pageId	The FB page ID.
	 * @return			An array list of photo objects.
	 */
	public static ArrayList<Photo> getPhotos(String pageId) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		GCD gcd = new GCD(projectId);
		ArrayList<Entity> photoEntities = gcd.getPagePhotos(pageId);
		
		ArrayList<String> comments = new ArrayList<String>();
		//int likes = 10;
		
		for (Entity photoEntity : photoEntities) {
			String url = photoEntity.getString("url");
			String name = photoEntity.getKey().getName();
			Long likes = photoEntity.getLong("likes");
			Photo photo = new Photo(name, url, comments, likes.intValue());
			photos.add(photo);
		}
		return photos;
	}
}
