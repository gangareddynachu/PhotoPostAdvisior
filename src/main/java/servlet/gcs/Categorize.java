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
import tools.gcs.Word2VecDict;
import tools.gcs.Categorization;

/**
 * Servlet implementation class Categorize
 */
@WebServlet("/Categorize")
public class Categorize extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Word2VecDict dict;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Categorize() {
        super();
        this.dict = new Word2VecDict();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] choosedLabelNames = request.getParameterValues("choosed_label");
		ArrayList<Label> choosedLabels = new ArrayList<Label>();
		for (String name : choosedLabelNames) {
			Label label = this.dict.getLabel(name);
			choosedLabels.add(label);
		}

		HttpSession session = request.getSession();
		PhotoSet photoSet = (PhotoSet) session.getAttribute("photoSet");
		
		HashMap<String, PhotoSet> categories = Categorization.nearest(choosedLabels, photoSet.getPhotos());
		request.setAttribute("categories", categories);
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
