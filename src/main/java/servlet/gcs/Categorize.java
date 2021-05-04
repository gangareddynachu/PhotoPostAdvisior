package servlet.gcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tools.gcs.Label;
import tools.gcs.PhotoSet;
import tools.gcs.Word2VecGCD;
import tools.gcs.Categorization;
import tools.gcs.GCD;

/**
 * The Servlet for categorizing photos.
 * 
 * @author      Su Yeh-Tarn, ysu19@horizon.csueastbay.edu
 * @since       1.0
 */
@WebServlet("/Categorize")
public class Categorize extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	private static final String projectId = "test-eclipse-tools";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Categorize() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] choosedLabelNames = request.getParameterValues("choosed_label");		
		Word2VecGCD finder = new Word2VecGCD(projectId);
		ArrayList<Label> choosedLabels = finder.getLabels(choosedLabelNames);
		

		HttpSession session = request.getSession();
		PhotoSet photoSet = (PhotoSet) session.getAttribute("photoSet");
		HashMap<String, PhotoSet> categories = Categorization.nearest(choosedLabels, photoSet.getPhotos());
		request.setAttribute("categories", categories);
		
		String pageId = session.getAttribute("pageId").toString();
		GCD gcd = new GCD(Categorize.projectId);
		gcd.deleteAllPhotos(pageId);
		//System.out.println("pageid: " + pageId);
		
		getServletContext().getRequestDispatcher("/categories.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
