package tools.gcs;

import java.util.ArrayList;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

/** 
 * The class for interacting with the Google Cloud Datastore.
 * 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
public class GCD {
	private Datastore datastore;
	
	/**
	 * Initiate a GCD object for Google cloud datastore operations.
	 * @param projectId 		The ID of the Google App.
	 */
	public GCD(String projectId) {
		this.datastore = DatastoreOptions.newBuilder().setProjectId(projectId).build().getService();
	}
	
	/**
	 * Create an entity of cloud datastore with the specified kind and name.
	 * 
	 * @param kind	The 'kind' of the entity. 
	 * @param name	The 'name' of the entity
	 * @return		The Entity in the cloud datastore.
	 */
	public Entity createEntity(String kind, String name) {
		Key key = this.datastore.newKeyFactory()
			    .setKind(kind)
			    .newKey(name);
		Entity entity = Entity.newBuilder(key)
			    .build();
		Entity put = this.datastore.put(entity);
		return put;
	}
	
	/**
	 * Get the photo entities of a specified FB page from the cloud datastore.
	 * 
	 * @param pageId	The FB page ID.
	 * @return			An array list of entities fetched from the cloud datastore.
	 */
	public ArrayList<Entity> getPagePhotos(String pageId) {
		ArrayList<Entity> photos = new ArrayList<Entity>();
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(pageId).build();
		QueryResults<Entity> results = this.datastore.run(query);
		while(results.hasNext()) {
			Entity entity = results.next();
			photos.add(entity);
		}
		return photos;
	}
	
	/**
	 * Update an Entity.
	 * 
	 * @param entity	The Entity object to update.
	 * @return			The returned Entity object.
	 */
	public Entity putEntity(Entity entity) {
		Entity put = datastore.put(entity);
		return put;
	}
	
	/**
	 * Get an Entity from the cloud datastore.
	 * 
	 * @param entity	The entity having the same key as the desired entity.
	 * @return			The returned Entity object.
	 */
	public Entity getEntity(Entity entity) {
		Entity get = this.datastore.get(entity.getKey());
		return get;
	}
	
	/**
	 * Get an Entity from the cloud datastore.
	 * 
	 * @param kind 		The desired entity 'kind'. 
	 * @param name		The desired entity 'name'.
	 * @return			The returned Entity object.
	 */
	public Entity getEntity(String kind, String name) {
		Key key = this.datastore.newKeyFactory().setKind(kind).newKey(name);
		Entity get = this.datastore.get(key);
		return get;
	}
	
	/**
	 * Create a photo of cloud datastore with the specified kind and name, together with an URL property.
	 * 
	 * @param kind		The 'kind' of the entity. 
	 * @param name		The 'name' of the entity
	 * @param url		The url string.
	 * @param likes		The number of likes.
	 * @param comments	The list values of comments.
	 * @return			The Entity in the cloud datastore.
	 */
	public Entity createPhoto(String kind, String name, String url, int likes, ListValue comments) {
		Key key = this.datastore.newKeyFactory()
			    .setKind(kind)
			    .newKey(name);
		Entity entity = Entity.newBuilder(key)
				.set("url", url)
				.set("likes", likes)
				.set("comments", comments)
			    .build();
		Entity put = this.datastore.put(entity);
		return put;
	}
	
	/**
	 * Run a query on the datastore.
	 * 
	 * @param query		The Query object to run on the datastore.
	 * @return			The returned Query results.
	 */
	public QueryResults<Entity> runQuery(Query<Entity> query) {
		return this.datastore.run(query);
	}
	
	public void deleteAllPhotos(String pageId) {
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(pageId).build();
		QueryResults<Entity> photos = this.datastore.run(query);
		while (photos.hasNext()) {
			Entity photo = photos.next();
			datastore.delete(photo.getKey());
		}
	}
}
