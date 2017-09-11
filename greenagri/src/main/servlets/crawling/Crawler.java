package crawling;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class Crawler {
	
	private final String NAVERCAFE = "cafe.naver.com";
	private final String FARMMATE = "www.farmmate.com";
	
	private List<Article> articles = new ArrayList<Article>();
	private String domainAddress;
	private HashMap<String, Document> documents = new HashMap<String, Document>();

	public Crawler(String url) throws Exception {
		this.domainAddress = url.substring(7, url.indexOf('/', 8));
		
		preProcess(url);
		crawl(url);
	}
	
	public List<Article> getArticles() {
		return this.articles;
	}
	
	private void preProcess(String url) throws Exception {
		if (NAVERCAFE.equals(this.domainAddress)) {
			preprocessNaverCafe(url);
		} else if (FARMMATE.equals(this.domainAddress)) {
			preprocessFarmMate(url);
		}
	}
	
	private void preprocessNaverCafe(String url) throws Exception {
		int listUrlPart = url.indexOf("/ArticleList.nhn");
		if (listUrlPart < this.domainAddress.length())	return;
		
		String articleListUrl = "http://" + this.domainAddress + URLDecoder.decode(url.substring(listUrlPart), "UTF-8");
		System.out.println(articleListUrl);
		Document articleList = Jsoup.connect(articleListUrl).get();
		Elements links = articleList.select("div.article-board").select("a[href]");
		for (Element link : links) {
			String uriPart = URLDecoder.decode(link.attr("href"), "UTF-8");
			if (!uriPart.startsWith("/ArticleRead.nhn")) continue;
			
			String articleUrl = "http://" + this.domainAddress + uriPart;
			System.out.println(articleUrl);
			this.documents.put(articleUrl, Jsoup.connect(articleUrl).get() );
		}
	}
	
	private void preprocessFarmMate(String url) throws IOException {
		this.documents.put(url, Jsoup.connect(url).get() );
	}
	
	private void crawl(String url) {
		for (String suburl : documents.keySet()) {
			if (NAVERCAFE.equals(this.domainAddress)) {
				crawlNaverCafe(suburl, documents.get(suburl));
			} else if (FARMMATE.equals(this.domainAddress)) {
				crawlFarmMate(suburl, documents.get(suburl));
			}
		}
	}
	
	private void crawlNaverCafe(String url, Document doc) {
		try {
			String findArticleidStr = "articleid=";
			int foundPos = url.indexOf(findArticleidStr);
			String articleId = url.substring(foundPos + findArticleidStr.length(), url.indexOf('&', foundPos));
			String articleWdate = doc.select("div.fr").select("td.date").first().text().substring(0, 10).replaceAll("\\.", "-");
			String articleCtx = doc.select("div.tbody").first().text();
			
			Article article = new Article(2, url);
			article.setId(articleId);
			article.setWrittenDate(articleWdate);
			article.setContext(articleCtx);
			System.out.println(article);
			
			this.articles.add(article);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void crawlFarmMate(String url, Document doc) {
		Elements rows = doc.select("form[name=formsub]").select("tr");
			
		for(Element row : rows) {
			List<String> textList = row.children().eachText();
			if (textList.size() > 5) continue;
			
			Article article = new Article(1, url);
			article.setIdFrom(textList);
			article.setWrittenDate(textList.get(4));
			article.setContext(textList.get(3));
			
			this.articles.add(article);
		}
	}
	
	public String toString() {
		return domainAddress;
	}
}
