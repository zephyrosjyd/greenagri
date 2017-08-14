<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		<a href="#">감성분석</a> 
		<a href="#">게시글관리</a> 
		<a href="#">구매경로분석</a> 
		<a href="#">선호제품분석</a>
	</h2>
	
	<p>
	<h3>자동 크롤링</h3>
	<form action="crawling/board">
		게시 URL: <input type="text" name="url"> 
		<input type="submit" value="게시글 크롤링"><br>
	</form><br>
	<button type="submit" formaction="crawling/product" formtarget="_self">상품정보 크롤링</button>
	<button type="submit" formaction="" formtarget="_self">집계</button>
	<br><br>
	<p>
	<table>
		<tr>
			<th><h3>수동 크롤링 - 게시글</h3></th>
			<th><h3>수동 크롤링 - 상품정보</h3></th>
		</tr>
		<tr>
			<td>
				<form action="manreg/board">
				채널번호: <input type="text" name=""><br>
				게시글번호: <input type="text" name=""><br>
				게시시간: <input type="text" name=""><br>
				게시글: <textarea name="" rows="10" cols="50"></textarea><br>
				<input type="submit" value="입력"><br>
				</form><br>
			</td>
			<td>
				<form action="manreg/product">
				채널번호: <input type="text" name=""><br>
				URL: <input type="text" name=""><br>
				상품명: <input type="text" name=""><br>
				가격기준: <input type="text" name=""><br>
				적정가격: <input type="text" name=""><br>
				<input type="submit" value="입력"><br>
				</form><br>
			</td>
		</tr>
	</table>
	<button type="submit" formaction="" formtarget="_self">집계</button>
	<br><br><br><br>
	<p>
	<table>
		<tr><th>채널번호</th><th>게시글개수</th><th>상품명개수</th></tr>
		<tr><td></td><td></td><td></td></tr>
	</table>
</body>
</html>
