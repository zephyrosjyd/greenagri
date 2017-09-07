<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    ResultSet rs = (ResultSet)request.getAttribute("rs"); 
	String result = (String)request.getAttribute("result");
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
		<b>|감성분석|</b> 
		<a href="/greenagri/analysis/vector">게시글검색(비평글관리)</a> 
		<a href="#">구매경로분석</a> 
		<a href="#">선호제품분석</a>
	</h2>
	
	<p>
	<a href="?calc=true">감성점수 계산</a> | <a href="?calc=false">감성분석</a>

    <p>
    <% if (result == "ok") { %>
    계산 완료!
    <% } %>
	<br><br><br><br>
	<p>
	<% if (rs != null) { %>
	<table>
		<tr><th>채널번호</th><th>게시글번호</th><th>감성점수</th><th>게시글</th></tr>
		<% while (rs.next()) { %>
		<tr><td><%= rs.getInt(1) %></td><td><%= rs.getString(2) %></td><td><%= rs.getDouble(4) %></td><td><%= rs.getString(3) %></td></tr>
		<% } %>
	</table>
	<% } %>
</body>
</html>