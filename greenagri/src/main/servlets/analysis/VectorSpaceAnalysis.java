package analysis;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VectorSpaceAnalysis
 */
public class VectorSpaceAnalysis extends HttpServlet {
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
		String keyword = request.getParameter("keyword");
		String count = request.getParameter("count");
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			if (keyword != null && count != null) {
				stmt = conn.createStatement();
				
				String sql = "select contents, emotion, bid from t_board "
						+ "where contents like '%" + keyword + "%' "
						+ "order by emotion desc limit 1";
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				
				if (!rs.next()) {
					System.out.println("ERR: No data!");
					callJspPage(request, response, "/vector.jsp");
					return;
				}
				
				Vector<Integer> vBid = new Vector<Integer>();
				ArrayList<Document> documents = new ArrayList<Document>();
				Document query = new Document( rs.getString(1) );
				double stdSentiment = rs.getDouble(2);
				vBid.add( rs.getInt(3) );
				rs.close();
				
				documents.add(query);
				
//				sql = "select chno, postno, contents, emotion from t_board "
//						+ "where bid = " + stdBid;
//				rs = stmt.executeQuery(sql);
//				System.out.println("<SQL> " + sql);
				
				//Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String stdS = String.valueOf(stdSentiment);
				sql = "select bid, contents from t_board "
						+ "where emotion > " + stdS + " - " + stdS + " * 2 and emotion < " + stdS + " + " + stdS + " * 2 "
						+ "and bid <> " + String.valueOf(vBid.get(0)) + " "
						+ "order by emotion desc";
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				
				while (rs.next()) {
					vBid.add(rs.getInt(1));
					documents.add(new Document(rs.getString(2)));
				}
				rs.close();
				//stmt2.close();
				
				VectorSpaceModel vectorSpace = new VectorSpaceModel(new Corpus(documents));
				//Vector<Double> vSimilarity = new Vector<Double>();
				for (int i = 0; i < documents.size(); i++) {
					sql = "update t_board set similarity = ? where bid = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setDouble(1, vectorSpace.cosineSimilarity(query, documents.get(i)));
					pstmt.setInt(2, vBid.get(i));
					int result = pstmt.executeUpdate();
					System.out.println("<SQL> " + sql + " ==> " + result);
					pstmt.close();
				}
				
				sql = "select chno, postno, contents, emotion, similarity from t_board "
						+ "where similarity >= 0 "
						+ "order by similarity desc limit " + count;
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				
				request.setAttribute("rs", rs);
			}
		
			// TODO Auto-generated method stub
			//response.getWriter().append("Served at: ").append(request.getContextPath());
			callJspPage(request, response, "/vector.jsp");
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
