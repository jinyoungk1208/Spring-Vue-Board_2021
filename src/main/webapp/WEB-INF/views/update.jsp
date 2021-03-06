<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
	form label { display: block; margin-bottom: 10px; }
</style>
<title>게시물 수정</title>
</head>
<body>

	<form name="update" method="post" action="/update?bno=${detail.bno}">
		<label>제목: 
			<input type="text" name="title" value="${detail.title}" required />
		</label>
		<label>작성자: 
			<input type="text" name="writer" value="${detail.writer}" required />
		</label>
		<label>내용: 
			<textarea name="contents" required>${detail.contents}</textarea>
		</label>
		<input type="submit" value="작성" />
		<a href="/"><button>취소</button></a>
	</form>

</body>
</html>