package crawling;

import java.io.IOException;
import java.net.URLDecoder;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class NaverBoardCrawler
 */
public class NaverBoardCrawler extends HttpServlet {
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
		String nurl = request.getParameter("nurl");
		//String url = "http://cafe.naver.com/tlsxh?iframe_url=/ArticleRead.nhn%3Fclubid=25257486%26page=1%26menuid=144%26boardtype=L%26articleid=430136%26referrerAllArticles=false";
		//http://cafe.naver.com/tlsxh?iframe_url=/ArticleRead.nhn%3Fclubid=25257486%26page=1%26menuid=144%26boardtype=L%26articleid=430125%26referrerAllArticles=false
		//String url = "http://cafe.naver.com/ArticleRead.nhn?clubid=25257486&page=1&menuid=144&boardtype=L&articleid=430136";
//		String domain = "http://cafe.naver.com";
//		String findstr = "/ArticleRead.nhn";
//		String url = domain + URLDecoder.decode(nurl.substring(nurl.indexOf(findstr)), "UTF-8");
		System.out.println(nurl);
//		Document rawPage = Jsoup.connect(url).get();
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Crawler crawler = new Crawler(nurl);
			System.out.println(crawler);
			
			stmt = conn.createStatement();
			
			List<Article> articles = crawler.getArticles();
//			String findArticleidStr = "articleid=";
//			int foundPos = url.indexOf(findArticleidStr);
//			String postno = url.substring(foundPos + findArticleidStr.length(), url.indexOf('&', foundPos));
//			String regdate = rawPage.select("div.fr").select("td.date").first().text().substring(0, 10).replaceAll("\\.", "-");
//			String contents = rawPage.select("div.tbody").first().text();
			//System.out.println(articles);
			
			String sql = null;
			for(Article article : articles) {
				System.out.println(article);
				sql = "insert into t_board (chno, url, postno, wdate, contents) ";
				sql += "values (?, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, article.getChannelNo());
				pstmt.setString(2, article.getUrl());
				pstmt.setString(3, article.getId());
				pstmt.setDate(4, article.getWrittenDate());
				pstmt.setString(5, article.getContext());
				int result = pstmt.executeUpdate();
				System.out.println("<SQL> " + sql + " ==> " + result);
				pstmt.close();
			}
			
			sql = "select coalesce(b.chno, p.chno) chno, b.cnt, p.cnt from " +
					"(select chno, count(*) as cnt from t_board group by chno) b " +
					"full outer join " +
					"(select chno, count(*) as cnt from t_product group by chno) p " +
					"on b.chno = p.chno";
			rs = stmt.executeQuery(sql);
			System.out.println("<SQL> " + sql);
			
			request.setAttribute("rs", rs);
			
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
