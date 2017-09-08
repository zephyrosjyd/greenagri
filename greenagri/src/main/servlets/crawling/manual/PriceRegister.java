package crawling.manual;

import java.io.IOException;
import java.sql.PreparedStatement;
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
 * Servlet implementation class PriceRegister
 */
public class PriceRegister extends HttpServlet {
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
		String pid = request.getParameter("pid");
		if (pid == null || pid.isEmpty()) {
			callJspPage(request, response, "/preferprod.jsp");
			return;
		}
		String price = request.getParameter("price");
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			//stmt = conn.createStatement();
			
			String sql = "update t_product set std_price = ? where pid = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.valueOf(price));
			pstmt.setInt(2, Integer.valueOf(pid));
			int result = pstmt.executeUpdate();
			System.out.println("<SQL> " + sql + " ==> " + result);
			pstmt.close();
			
			request.setAttribute("updated", "ok");
			
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
