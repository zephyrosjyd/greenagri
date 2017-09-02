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
 * Servlet implementation class ProductRegister
 */
public class ProductRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletConfig config;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
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
		String chno = request.getParameter("chno");
		String url = request.getParameter("url");
		String title = request.getParameter("title");
		String price = request.getParameter("price");
		String base = request.getParameter("base");
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			
			String sql = null;
			sql = "insert into t_product (chno, url, title, price, base) ";
			sql += "values (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.valueOf(chno));
			pstmt.setString(2, url);
			pstmt.setString(3, title);
			pstmt.setInt(4, Integer.valueOf(price));
			pstmt.setString(5, base);
			int result = pstmt.executeUpdate();
			System.out.println("<SQL> " + sql + " ==> " + result);
			pstmt.close();
			
			sql = "select b.chno, b.cnt, p.cnt from " +
					"(select chno, count(*) as cnt from t_board group by chno) b " +
					"full outer join " +
					"(select chno, count(*) as cnt from t_product group by chno) p " +
					"on b.chno = p.chno";
			rs = stmt.executeQuery(sql);
			System.out.println("<SQL> " + sql);
			
			request.setAttribute("rs", rs);
				
			// TODO Auto-generated method stub
			//response.getWriter().append("Served at: ").append(request.getContextPath());
			callJspPage(request, response, "/index.jsp");
		} catch (Exception e) {
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
