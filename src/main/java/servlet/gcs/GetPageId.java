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
		FacebookClient facebookClient = new DefaultFacebookClient(token, Version.LATEST);		
		
		JsonObject accountsFetch = facebookClient.fetchObject("me/accounts", JsonObject.class);
		
		JsonArray page_arr = accountsFetch.get("data").asArray();
		JsonObject page = page_arr.get(0).asObject();
		String pageId = page.get("id").toString().replaceAll("^\"|\"$", "");
		
		JsonArray photos = facebookClient.fetchObject(
				pageId + "/photos" , JsonObject.class,
				Parameter.with("type", "uploaded"))
				.get("data")
				.asArray();
				
		ArrayList<String> photoIds = new ArrayList<String>();
		for (JsonValue photo : photos) {
			String photoId = photo.asObject().get("id")
					.toString().replaceAll("^\"|\"$", "");
			photoIds.add(photoId);
			
			JsonObject result = facebookClient.fetchObject(photoId, JsonObject.class,
					Parameter.with("fields", "images"));
			JsonArray images = (JsonArray) result.get("images");
			String url = "";
			for (JsonValue image : images) {
				if (image.asObject().get("height").asInt() < 400) {
					url = image.asObject().get("source").asString();
					break;
				}
			}
			GCD gcd = new GCD(projectId);
			gcd.createEntity(pageId, photoId, url); // save entities, kind: page id, key: photo id.
		}
		
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
