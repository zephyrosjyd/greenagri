package crawling;

import java.util.List;
import java.sql.Date;

public class Article {
	private int channelNo;
	private String url;
	private String id;	// 게시글 번호
	private Date writtenDate;
	private String context;
	
	public Article(int chno, String url) {
		this.channelNo = chno;
		this.url = url;
	}
	
	public void setIdFrom(List<String> list) {
		String prodno = list.get(0).substring(list.get(0).indexOf('[') + 1, list.get(0).indexOf(']'));
		String custid = list.get(1);
		String regdate = "2017-" + list.get(4);
		this.id = String.join("|", prodno, custid, regdate);
	}
	
	public void setId(String no) {
		this.id = no;
	}
	
	public void setWrittenDate(String date) {
		if (date.length() < 10) {
			date = "2017-" + date;
		} else if (date.contains(".")) {
			date = date.replaceAll("\\.", "-");
		}
		
		this.writtenDate = Date.valueOf(date);
	}
	
	public void setContext(String ctx) {
		this.context = ctx;
	}
	
	public int getChannelNo() {
		return this.channelNo;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public Date getWrittenDate() {
		return this.writtenDate;
	}
	
	public String getContext() {
		return this.context;
	}
	
	public String toString() {
		return this.context;
	}
}
