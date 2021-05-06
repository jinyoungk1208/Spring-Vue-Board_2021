<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false" %>
<%@ page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.*" %>
<html>
<style>
	div { margin: 10px 0; }
	table, th, td { border: 1px solid black; text-align: center; }
	form { margin-top: 10px; }
</style>
<head>
	<title>게시판 홈</title>
</head>
<body>
	<c:if test="${result eq 'false'}">
		<a href="/join"><button>회원가입</button></a>
		<a href="/login"><button>로그인</button></a>
	</c:if>
	<c:if test="${result eq 'true'}">
		<a href="/logout"><button id="logoutBtn">로그아웃</button></a>
	</c:if>
	<div>
	
	</div>
	
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
	
	<div>
		<c:if test="${prev}">
			<span>[ <a href="/?num=1">처음</a> ]</span>
		</c:if>
		<c:if test="${prev}">
			<span>[ <a href="/?num=${startPageNum - 1}">이전</a> ]</span>
		</c:if>
		
		<c:forEach begin="${startPageNum}" end="${endPageNum}" var="num">
			<span>
				<c:if test="${select != num}">
					<a href="/?num=${num}">${num}</a>
				</c:if>
				<c:if test="${select == num}">
					<b>${num}</b>
				</c:if>
			</span>
		</c:forEach>
		
		<c:if test="${next}">
			<span>[ <a href="/?num=${endPageNum + 1}">다음</a> ]</span>
		</c:if>
		<c:if test="${next}">
			<span>[ <a href="/?num=${pageNum}">끝</a> ]</span>
		</c:if>
	</div>
	
	<form method="get">
		<input type="button" value="작성" onclick="location.href='/write'" />
	</form>

</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">

	$("#logoutBtn").click(function() {
		$.ajax({
			url : '/logout',
			type : 'GET',
			async : true,
			success : function(data) {
				alert('로그아웃 성공');
				location.href="";
			},
			error : function(e) {
				console.log(e);
				alert("오류가 발생했습니다.");
			}
		});
	});
	
</script>
</html>
