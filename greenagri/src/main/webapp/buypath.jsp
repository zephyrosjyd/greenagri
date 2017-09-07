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
		<a href="/greenagri/analysis/vector">게시글검색(비평글관리)</a> 
		<b>|구매경로분석|</b> 
		<a href="#">선호제품분석</a>
	</h2>
	
	<p>
	| <a href="?kind=freq">제품별 빈도수 계산</a> || <a href="?kind=path">구매경로분석</a> |
	
	<% if (request.getAttribute("ok") != null) { %>
	<p>success
	<% } %>
	
	<p>
	<% if (rs != null) { %>
	<table>
		<tr><th>채널번호</th><th>제품빈도총계</th><th>가중치</th><th>구매경로비율</th></tr>
		<% while (rs.next()) { %>
		<tr><td><%= rs.getInt(1) %></td><td><%= rs.getInt(2) %></td><td><%= rs.getDouble(3) %></td><td><%= rs.getDouble(4) %></td></tr>
		<% } %>
	</table>
	<% } %>
</body>
</html>