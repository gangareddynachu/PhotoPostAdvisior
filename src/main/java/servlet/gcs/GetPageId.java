package servlet.gcs;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.json.JsonValue;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;

import tools.gcs.GCD;

/**
 * Servlet implementation class GetPageId
 */
@WebServlet("/GetPageId")
public class GetPageId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String projectId = "test-eclipse-tools";
	private FacebookClient facebookClient;
	private String pageId;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPageId() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("access_token");		
		this.facebookClient = new DefaultFacebookClient(token, Version.LATEST);		
		this.pageId = this.getPageId();		
		ArrayList<String> photoIds = this.getPhotoIds();		
		this.recordPhotoInfo(photoIds);
		
		HttpSession session = request.getSession();
		session.setAttribute("pageId", pageId);
		request.getRequestDispatcher("/GetLabels").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String getPageId() {
		JsonObject accountsFetch = this.facebookClient.fetchObject("me/accounts", JsonObject.class);		
		JsonArray page_arr = accountsFetch.get("data").asArray();
		JsonObject page = page_arr.get(0).asObject();
		String pageId = page.get("id").toString().replaceAll("^\"|\"$", "");
		return pageId;
	}
	
	private ArrayList<String> getPhotoIds() {
		JsonArray photos = facebookClient.fetchObject(
				this.pageId + "/photos" , JsonObject.class, Parameter.with("type", "uploaded"))
				.get("data")
				.asArray();
		
		ArrayList<String> photoIds = new ArrayList<String>();
		for (JsonValue photo : photos) {
			String photoId = photo.asObject().get("id")
					.toString().replaceAll("^\"|\"$", "");
			photoIds.add(photoId);
		}
		return photoIds;
	}
	
	private void recordPhotoInfo(ArrayList<String> photoIds) {
		for (String photoId : photoIds) {
			JsonObject result = facebookClient.fetchObject(
					photoId, JsonObject.class, Parameter.with("fields", "images"));
			JsonArray images = (JsonArray) result.get("images");
			String url = "";
			for (JsonValue image : images) {
				if (image.asObject().get("height").asInt() < 400) {
					url = image.asObject().get("source").asString();
					break;
				}
			}
			result = facebookClient.fetchObject(
					photoId, JsonObject.class, Parameter.with("fields", "likes.summary(true)"));
			JsonObject likes = (JsonObject) result.get("likes");
			JsonObject summary = (JsonObject) likes.get("summary");
			int count = summary.get("total_count").asInt();
			
			GCD gcd = new GCD(projectId);			
			gcd.createPhoto(this.pageId, photoId, url, count); // save entities, kind: page id, key: photo id.
		}
	}
	
	/*private void FBLlogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String appId = "1024365154762700";
		String redirectUri = URLEncoder.encode(
				"https://test-eclipse-tools.wl.r.appspot.com/GetPageId", "UTF-8");
		FacebookClient facebookClient = new DefaultFacebookClient("", Version.LATEST);
		
		ScopeBuilder sb = new ScopeBuilder();
		sb.addPermission(FacebookPermissions.PUBLIC_PROFILE);
		sb.addPermission(FacebookPermissions.EMAIL);
		sb.addPermission(FacebookPermissions.PAGES_MANAGE_POSTS);
		sb.addPermission(FacebookPermissions.PAGES_READ_ENGAGEMENT);
		
		String url = facebookClient.getLoginDialogUrl(appId, redirectUri, sb);
		request.getRequestDispatcher(url).forward(request, response);
	}*/

}
