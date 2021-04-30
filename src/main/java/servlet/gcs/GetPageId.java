package servlet.gcs;

import java.io.IOException;
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
		//response.getWriter().append("Object: " + accountsFetch.toString() + "\n");
		
		JsonArray page_arr = accountsFetch.get("data").asArray();
		JsonObject page = page_arr.get(0).asObject();
		String pageId = page.get("id").toString().replaceAll("^\"|\"$", "");
		//response.getWriter().append("Page Id: " + pageId + "\n");
		
		JsonArray photos = facebookClient.fetchObject(
				pageId + "/photos" , JsonObject.class,
				Parameter.with("type", "uploaded"))
				.get("data")
				.asArray();
		
		//response.getWriter().append("photos: " + photos.toString() + "\n");
				
		ArrayList<String> photoIds = new ArrayList<String>();
		for (JsonValue photo : photos) {
			String photoId = photo.asObject().get("id")
					.toString().replaceAll("^\"|\"$", "");
			photoIds.add(photoId);
			//response.getWriter().append("photo id: " + photoId + "\n");
			
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
			//response.getWriter().append("photo url: " + url + "\n");
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

}
