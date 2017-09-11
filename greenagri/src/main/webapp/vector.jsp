<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	ResultSet rs = (ResultSet)request.getAttribute("rs"); 
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
		<a href="/greenagri/analysis/buypath">구매경로분석</a> 
		<a href="/greenagri/analysis/preferprod">선호제품분석</a>
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
		<td><%= String.format("%.4f", rs.getDouble(4)) %></td>
		<td><%= String.format("%.4f", rs.getDouble(5)) %></td>
		<td><%= rs.getString(3) %></td>
		</tr>
		<% } %>
	</table>
	<% } %>
</body>
</html>