package tools.gcs;

import java.util.ArrayList;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

public class GCD {
	private Datastore datastore;
	
	public GCD(String projectId) {
		this.datastore = DatastoreOptions.newBuilder().setProjectId(projectId).build().getService();
	}
	
	public Entity createEntity(String kind, String name) {
		Key key = this.datastore.newKeyFactory()
			    .setKind(kind)
			    .newKey(name);
		Entity entity = Entity.newBuilder(key)
			    .build();
		Entity put = this.datastore.put(entity);
		return put;
	}
	
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
	
	public Entity putEntity(Entity entity) {
		Entity put = datastore.put(entity);
		return put;
	}
	
	public Entity getEntity(Entity entity) {
		Entity get = this.datastore.get(entity.getKey());
		return get;
	}
	
	public Entity getEntity(String kind, String name) {
		Key key = this.datastore.newKeyFactory().setKind(kind).newKey(name);
		Entity get = this.datastore.get(key);
		return get;
	}
	
	public Entity createEntity(String kind, String name, String url) {
		Key key = this.datastore.newKeyFactory()
			    .setKind(kind)
			    .newKey(name);
		Entity entity = Entity.newBuilder(key)
				.set("url", url)
			    .build();
		System.out.println("entity: " + entity.toString());
		Entity put = this.datastore.put(entity);
		return put;
	}
	
	public QueryResults<Entity> runQuery(Query query) {
		return this.datastore.run(query);
	}
}
