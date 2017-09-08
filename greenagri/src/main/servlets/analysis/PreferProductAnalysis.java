package analysis;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PreferProductAnalysis
 */
public class PreferProductAnalysis extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletConfig config;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		return this.config;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext sc = this.getServletContext();
		java.sql.Connection conn = (java.sql.Connection)sc.getAttribute("dbconn");
		try {
			System.out.println(conn.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		request.setCharacterEncoding("UTF-8");
		String exec = request.getParameter("exec");
		if (exec == null || exec.isEmpty()) {
			callJspPage(request, response, "/preferprod.jsp");
			return;
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			
			String sql = "select chno, title, rate, "
						+ "case when rate > 0 then title else '' end "
						+ "from ("
						+ "select chno, title, coalesce(round((1. - std_price::float / price)::numeric, 4), 0) as rate "
						+ "from t_product) x "
						+ "order by rate desc";
			rs = stmt.executeQuery(sql);
			System.out.println("<SQL> " + sql);

			request.setAttribute("rs", rs);
		
			// TODO Auto-generated method stub
			//response.getWriter().append("Served at: ").append(request.getContextPath());
			callJspPage(request, response, "/preferprod.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) { }
			try { if (stmt != null) stmt.close(); } catch (Exception e) { }
		}
	}
	
	private void callJspPage(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		//request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
	
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
