<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false" %>
<%@ page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.*" %>
<html>
<style>
	table, th, td { border: 1px solid black; text-align: center; }
	form { margin-top: 10px; }
</style>
<head>
	<title>게시판 홈</title>
</head>
<body>

	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>내용</th>
				<th>작성자</th>
				<th>조회수</th>
				<th>작성일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="list">
				<tr>
					<td>${list.bno}</td>
					<td><a href="/detail?bno=${list.bno}">${list.title}</a></td>
					<td>${list.contents}</td>
					<td>${list.writer}</td>
					<td>${list.viewcnt}</td>
					<td>${list.regdate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<form>
		<input type="button" value="작성" onclick="location.href='/write'" />
	</form>

</body>
</html>
