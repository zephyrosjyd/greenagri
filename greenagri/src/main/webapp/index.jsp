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
		<b>|빅데이터구축|</b> 
		<a href="/greenagri/analysis/sentiment">감성분석</a> 
		<a href="/greenagri/analysis/vector">게시글검색(비평글관리)</a> 
		<a href="#">구매경로분석</a> 
		<a href="#">선호제품분석</a>
	</h2>
	
	<p>
	<a href="create">Create DATABASE</a>
	
	<p>
	<form action="/greenagri/crawling/board">
	<table>
		<tr>
			<th><h3>자동 크롤링</h3></th>
		</tr>
		<tr>
			<td>
				게시 URL: <input type="text" name="url" value="http://www.farmmate.com/shop/home_y9.php3" width="400"> 
				<input type="submit" value="게시글 크롤링"><br>
			</td>
		</tr>
		<tr>
			<td>
				<button type="submit" formaction="/greenagri/crawling/product" formtarget="_self">상품정보 크롤링</button>
			</td>
		</tr>
	</table>
	</form>
	<!-- <button type="submit" formaction="" formtarget="_self">집계</button> -->
	<br><br>
	<p>
	<table>
		<tr>
			<th><h3>수동 크롤링 - 게시글</h3></th>
			<th><h3>수동 크롤링 - 상품정보</h3></th>
		</tr>
		<tr>
			<td>
				<form action="/greenagri/manreg/board" method="post">
				채널번호: <input type="text" name="chno"><br>
				URL: <input type="text" name="url"><br>
				게시글번호: <input type="text" name="postno"><br>
				게시시간: <input type="text" name="regdate"><br>
				게시글: <textarea name="contents" rows="10" cols="50"></textarea><br>
				<input type="submit" value="입력"><br>
				</form><br>
			</td>
			<td>
				<form action="/greenagri/manreg/product" method="post">
				채널번호: <input type="text" name="chno"><br>
				URL: <input type="text" name="url"><br>
				상품명: <input type="text" name="title"><br>
				가격기준: <input type="text" name="base"><br>
				적정가격: <input type="text" name="price"><br>
				<input type="submit" value="입력"><br>
				</form><br>
			</td>
		</tr>
	</table>
	<!-- <button type="submit" formaction="" formtarget="_self">집계</button> -->
	<br><br><br><br>
	<p>
	<% if (rs != null) { %>
	<table>
		<tr><th>채널번호</th><th>게시글개수</th><th>상품명개수</th></tr>
		<% while (rs.next()) { %>
		<tr><td><%= rs.getInt(1) %></td><td><%= rs.getInt(2) %></td><td><%= rs.getInt(3) %></td></tr>
		<% } %>
	</table>
	<% } %>
</body>
</html>
