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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GCV {

  public static ArrayList<String> getImageLabels(String projectId, String bucketName, String objectName) throws IOException {
	  Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
	  Blob blob = storage.get(BlobId.of(bucketName, objectName));
	  return getImageLabels(blob.getContent());
  }
  
  public static ArrayList<String> getImageLabels(byte[] imgBytes) throws IOException {
		ByteString byteString = ByteString.copyFrom(imgBytes);
		Image image = Image.newBuilder().setContent(byteString).build();

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
		}
		
		return words;
	}
}