package tools.gcs;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** 
 * The class for interacting with the Google Cloud Vision.
 * 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class GCV {

	/**
	 * Get the array list containing the label objects of an image stored in the cloud storage.
	 * 
	 * @param projectId			The Google app id.
	 * @param bucketName		The name of the bucket storing the image.
	 * @param objectName		The image name.
	 * @return					An array list containing strings of the labels.
	 * @throws IOException		
	 */
  public static ArrayList<String> getImageLabels(String projectId, String bucketName, String objectName) throws IOException {
	  Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
	  Blob blob = storage.get(BlobId.of(bucketName, objectName));
	  return getImageLabels(blob.getContent());
  }
	/**
	 * Get the array list containing the label objects of an image stored in the cloud storage.
	 * 
	 * @param imgBytes			The byte array of the image.
	 * @return					An array list containing strings of the labels.
	 * @throws IOException
	 */
  public static ArrayList<String> getImageLabels(byte[] imgBytes) throws IOException {
		ByteString byteString = ByteString.copyFrom(imgBytes);
		Image image = Image.newBuilder().setContent(byteString).build();
		return getImageLabels(image);
  }
  
  /**
   * Get the array list containing the label objects of an image stored in the cloud storage.
   * 
   * @param url					The FB image URL.
   * @return					An array list containing strings of the labels.
   * @throws IOException
   */
  public static ArrayList<String> getImageLabels(String url) throws IOException {
	  	URL link = new URL(url);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = link.openStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;
	
			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
		}
		catch (IOException e) {
		  System.err.printf ("Failed while reading bytes from %s: %s", link.toExternalForm(), e.getMessage());
		  e.printStackTrace ();
		}
		finally {
			  if (is != null) { is.close(); }
	    }
		  
		return getImageLabels(baos.toByteArray());
  }  
  
  /**
   * Get the array list containing the label objects of an image stored in the cloud storage.
   * 
   * @param image			The Image object of the image.
   * @return					An array list containing strings of the labels.
   * @throws IOException
   */
  public static ArrayList<String> getImageLabels(Image image) throws IOException {
		Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
		AnnotateImageRequest request =
				AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
		List<AnnotateImageRequest> requests = new ArrayList<>();
		requests.add(request);

		ImageAnnotatorClient client = ImageAnnotatorClient.create();
		BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
		client.close();
		
		List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();
		AnnotateImageResponse imageResponse = imageResponses.get(0);

		if (imageResponse.hasError()) {
			System.err.println("Error getting image labels: " + imageResponse.getError().getMessage());
			return null;
		}
		
		ArrayList<String> words = new ArrayList<String>();
		for (EntityAnnotation entity : imageResponse.getLabelAnnotationsList()) {
			words.add(entity.getDescription().toLowerCase());
			//System.out.println("find " + entity.getDescription().toLowerCase());
		}
		
		return words;
	}
}