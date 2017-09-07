package analysis;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BuyPathAnalysis
 */
public class BuyPathAnalysis extends HttpServlet {
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
		String kind = request.getParameter("kind");
		if (kind == null) {
			callJspPage(request, response, "/buypath.jsp");
			return;
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			if (kind.equals("freq")) {
				stmt = conn.createStatement();
				
			// 0. 대상 db table truncation
				sql = "truncate table t_prod_freq";
				int result = stmt.executeUpdate(sql);
				System.out.println("<SQL> " + sql + " ==> " + result);
			
			// 1. 제품 사전 데이터 메모리 로딩 --> 제품별 토크닝 
				sql = "select pid, title from t_product";
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				HashMap<Integer, String[]> prodDict = new HashMap<Integer, String[]>();
				while (rs.next()) {
					prodDict.put(rs.getInt(1), rs.getString(1).split("[^A-Za-z0-9가-힣]"));
				}
				rs.close();
			
			// 2. 게시글 데이터 로딩
				sql = "select bid, contents from t_board";
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				HashMap<Integer, String[]> brdDict = new HashMap<Integer, String[]>();
				while (rs.next()) {
					brdDict.put(rs.getInt(1), rs.getString(1).split("[^A-Za-z0-9가-힣]"));
				}
				rs.close();
			
			// 3. 게시글별로 각 제품 토큰에 대한 빈도수 계산 --> db insertion 
				sql = "insert into t_prod_freq (bid, pid, freq) values (?, ?, ?)";
				for (int bid : brdDict.keySet()) {
					String[] words = brdDict.get(bid);
					
					for (int pid : prodDict.keySet()) {
						int matchedCount = 0;
						String[] prods = prodDict.get(pid);
						
						for (int i = 0; i < words.length; i++) 
							for (int j = 0; j < prods.length; j++) 
								if (words[i].startsWith(prods[j]))
									matchedCount++;
						
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, bid);
						pstmt.setInt(2, pid);
						pstmt.setInt(3, matchedCount);
						result = pstmt.executeUpdate();
						System.out.println("<SQL> " + sql + " ==> " + result);
						pstmt.close();
					}
				}
				
				request.setAttribute("ok", 1);
			} else if (kind.equals("path")) {
				stmt = conn.createStatement();
				
			// 4. 채널별 제품빈도 총계 집계 및 가중치 데이터 로딩
				sql = "select x.ch, x.frq, w.wt, frq * w.wt / 100 from ("
						+ "select b.chno ch, sum(freq) frq "
						+ "from t_prod_freq pf, t_board b "
						+ "where pf.bid = b.bid "
						+ "group by b.chno "
						+ ") x, t_path_weight w "
						+ "where x.ch = w.chno ";
				rs = stmt.executeQuery(sql);
				System.out.println("<SQL> " + sql);
				
				request.setAttribute("rs", rs);
			}
			
			// TODO Auto-generated method stub
			//response.getWriter().append("Served at: ").append(request.getContextPath());
			callJspPage(request, response, "/buypath.jsp");
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
