package analysis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class SentimentAnalysis
 */
public class SentimentAnalysis extends HttpServlet {
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
		String calc = request.getParameter("calc");
		System.out.println("calc: " + calc);
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			if ("true".equals(calc)) {
				calcSentiment(request, conn);
			} else if ("false".equals(calc)) {
				String sql = null;
				stmt = conn.createStatement();
				sql = "select chno, postno, contents, emotion from t_board limit 10";
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				request.setAttribute("rs", rs);
				request.setAttribute("result", "view");
			} else {
				System.out.println("'true'.equals(calc): " + String.valueOf("true".equals(calc)));
			}
		
			// TODO Auto-generated method stub
			//response.getWriter().append("Served at: ").append(request.getContextPath());
			callJspPage(request, response, "/sentiment.jsp");
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
	
	private void calcSentiment(HttpServletRequest request, Connection conn ) throws ServletException, SQLException {
		System.out.println("calcSentiment() called!"); 
		Statement stmt = null;
		ResultSet rs = null;
		
		HashMap<String, Integer> wordDic = new HashMap<String, Integer>();
		//HashMap<Integer, String> contentsDic = new HashMap<Integer, String>();
		
		stmt = conn.createStatement();
		
		String sql = null;
		sql = "select word, sentiment from t_sentiment_word_dic";
		rs = stmt.executeQuery(sql);
		System.out.println("<SQL> " + sql);
		while (rs.next()) {
			wordDic.put(rs.getString(1), rs.getInt(2));
		}
		rs.close();
		
		sql = "select bid, contents from t_board ";
		rs = stmt.executeQuery(sql);
		System.out.println("<SQL> " + sql);
		while (rs.next()) {
			//contentsDic.put(rs.getInt(1), rs.getString(2));
			int bid = rs.getInt(1);
			String contents = rs.getString(2);
			String[] contentsList = contents.split("[\\.\\s]");
			//System.out.println(contentsList.length);
			
			int totalCount = 0, totalPoint = 0;
			for (String cont : contentsList) {
				//System.out.println(cont);
				Iterator<String> keyIter = wordDic.keySet().iterator();
				while (keyIter.hasNext()) {
					String word = keyIter.next();
					if (cont.startsWith(word)) {
						//System.out.println(word);
						totalCount++;
						totalPoint += wordDic.get(word);
					}
				}
			}
			
			double sentiPoint = 0.0;
			if (totalCount > 0)
				sentiPoint = totalPoint / (float)totalCount;
			
			sql = "update t_board set emotion = ? where bid = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, sentiPoint);
			pstmt.setInt(2, bid);
			int result = pstmt.executeUpdate();
			System.out.println("<SQL> " + sql + " ==> " + result);
			pstmt.close();
		}
		rs.close();
		
		request.setAttribute("result", "ok");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
