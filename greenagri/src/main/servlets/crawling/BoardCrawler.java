package crawling;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class BoardCrawler
 */
public class BoardCrawler extends HttpServlet {
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
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		return "version=1.0;author=zephyros"; 
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("BoardCrawler.service() called");
//		
//		ServletContext sc = this.getServletContext();
//		java.sql.Connection conn = (java.sql.Connection)sc.getAttribute("dbconn");
//		try {
//			System.out.println(conn.isClosed());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		this.doGet(request, response);
//	}

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
		//String url = "http://www.farmmate.com/shop/home_y9.php3";
		String url = request.getParameter("url");
		Document rawPage = Jsoup.connect(url).get();
		//String text = rawPage.text();
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			
			String sql = null;
			Elements rows = rawPage.select("form[name=formsub]").select("tr");
			for(Element row : rows) {
				//System.out.println(row.children().eachText().size());
				//System.out.println(row.text());
				//System.out.println(row.childNodeSize());
				String prodno = null, custid = null, regdate = null;
				String contents = null, postno = null;
				List<String> textList = row.children().eachText();
				if (textList.size() > 5) continue;
				
				prodno = textList.get(0).substring(textList.get(0).indexOf('[') + 1, textList.get(0).indexOf(']'));
				custid = textList.get(1);
				contents = textList.get(3);
				regdate = "2017-" + textList.get(4);
				postno = String.join("|", prodno, custid, regdate);
				//System.out.println("postno:"+postno);
				
				sql = "insert into t_board (chno, url, postno, wdate, contents) ";
				sql += "values (1, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, url);
				pstmt.setString(2, postno);
				pstmt.setDate(3, Date.valueOf(regdate));
				pstmt.setString(4, contents);
				int result = pstmt.executeUpdate();
				System.out.println("<SQL> " + sql + " ==> " + result);
				pstmt.close();
			}
			
			sql = "select b.chno, b.cnt, p.cnt from " +
					"(select chno, count(*) as cnt from t_board group by chno) b " +
					"full outer join " +
					"(select chno, count(*) as cnt from t_product group by chno) p " +
					"on b.chno = p.chno";
			rs = stmt.executeQuery(sql);
			System.out.println("<SQL> " + sql);
			
			request.setAttribute("rs", rs);
			
			//response.getWriter().append("Served at: ").append(request.getContextPath());
//			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
//			dispatcher.forward(request, response);
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
