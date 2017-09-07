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
		<a href="/greenagri/analysis/buypath">구매경로분석</a> 
		<b>|선호제품분석|</b>
	</h2>
	
	<p>
	
	<p>
	<% if (rs != null) { %>
	<table>
		<tr><th>채널번호</th><th>상품명</th><th>프리미엄비율</th><th>선호제품</th></tr>
		<% while (rs.next()) { %>
		<tr><td><%= rs.getInt(1) %></td><td><%= rs.getString(2) %></td><td><%= rs.getDouble(3) %></td><td><%= rs.getString(4) %></td></tr>
		<% } %>
	</table>
	<% } %>
</body>
</html>