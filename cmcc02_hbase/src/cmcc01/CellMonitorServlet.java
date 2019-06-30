package cmcc01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class CellMonitorServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		List cellName = new ArrayList<String>();
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
			cellName.add(random.nextInt(1000));
		}
		List cellDropNum = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			cellDropNum.add(random.nextInt(1000));
		}
		Map m = new HashMap<String, List>();
		m.put("xAxis", cellName);
		m.put("series", cellDropNum);
		Gson g = new Gson();
		out.print(g.toJson(m));
		out.flush();
		out.close();
	}

}
