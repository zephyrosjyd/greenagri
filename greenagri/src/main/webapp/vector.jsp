<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="analysis.*" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	//Statement stmt = ((Connection)request.getAttribute("conn")).createStatement();//(Statement)request.getAttribute("stmt");
    ResultSet rs = (ResultSet)request.getAttribute("rs"); 
	//String result = (String)request.getAttribute("result");
	//ArrayList<Document> documents = (ArrayList<Document>)request.getAttribute("documents");
	//String stdBid = String.valueOf(request.getAttribute("stdBid"));
	//Document query = (Document)request.getAttribute("query");

/* 
	String keyword = null;
	if (request.getParameter("keyword") != null && !request.getParameter("keyword").isEmpty()) 
		keyword = request.getParameter("keyword");
	String count = null;
	if (request.getParameter("count") != null && !request.getParameter("count").isEmpty()) 
		count = request.getParameter("count");

	String sql = null;
	ResultSet rs = null, stdRs = null;
	VectorSpaceModel vectorSpace = null;

	if (keyword != null && count != null) {
		Statement stmt = ((Connection)request.getAttribute("conn")).createStatement();
		
		sql = "select chno, postno, contents, emotion from t_board "
				+ "where bid = " + stdBid;
		stdRs = stmt.executeQuery(sql);
		System.out.println("<SQL> " + sql);
		
		Statement stmt2 = ((Connection)request.getAttribute("conn")).createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String stdS = String.valueOf(request.getAttribute("stdSentiment"));
		sql = "select chno, postno, contents, emotion from t_board "
				+ "where emotion > " + stdS + " - " + stdS + " * 2 and emotion < " + stdS + " + " + stdS + " * 2 "
				+ "and bid <> " + stdBid + " "
				+ "order by emotion desc limit " + count;
		rs = stmt2.executeQuery(sql);
		System.out.println("<SQL> " + sql);
		while (rs.next()) {
			documents.add(new Document(rs.getString(3)));
		}
		
		vectorSpace = new VectorSpaceModel(new Corpus(documents));
	}
*/	
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<title>친환경농업제품분석</title>
	<style>
	table {
	    border-collapse: collapse;
	}
	table,th,td {
    	border: 1px solid black;
	}
	</style>
</head>
<body>
	<h2>
		<a href="/greenagri/">빅데이터구축</a> 
		<a href="/greenagri/analysis/sentiment">감성분석</a> 
		<b>|게시글검색(비평글관리)|</b> 
		<a href="#">구매경로분석</a> 
		<a href="#">선호제품분석</a>
	</h2>
	
	<p>
	<h3>검색모델 신규 구축</h3> 
	<br><br>
	<form action="/greenagri/analysis/vector">
	검색키워드: <input type="text" name="keyword" value="<%= request.getParameter("keyword") %>"><br>
	출력개수: <input type="text" name="count" value="<%= request.getParameter("count") %>"><br>
	<input type="submit">
	</form>
	<br><br><br><br>
	<p>
	<% if (rs != null) { %>
	<table>
		<tr><th>채널번호</th><th>게시글번호</th><th>감성점수</th><th>유사도점수</th><th>게시글</th></tr>
		<% while (rs.next()) { %>
		<tr>
		<td><%= rs.getInt(1) %></td>
		<td><%= rs.getString(2) %></td>
		<td><%= rs.getDouble(4) %></td>
		<td><%= rs.getDouble(5) %></td>
		<td><%= rs.getString(3) %></td>
		</tr>
		<% } %>
	</table>
	<% } %>
</body>
</html>