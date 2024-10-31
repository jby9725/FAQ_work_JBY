<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="DETAIL"></c:set>
<%@ include file="../common/head.jspf"%>

<hr />

<table border="1" cellspacing="0" cellpadding="5">
	<tr>
		<th>ID</th>
		<td>${faq.id}</td>
	</tr>
	<tr>
		<th>Title</th>
		<td>${faq.title}</td>
	</tr>
	<tr>
		<th>Registration Date</th>
		<td>${faq.regDate}</td>
	</tr>
	<tr>
		<th>Writer</th>
		<td>${faq.extra__writer}</td>
	</tr>
	<tr>
		<th>Body</th>
		<td>${faq.body}</td>
	</tr>
</table>

<div class="btns">
	<button class="btn" type="button" onclick="history.back()">뒤로 가기</button>
	<c:if test="${faq.userCanModify }">
		<a class="btn" href="../FAQ/modify?id=${faq.id }">수정</a>
	</c:if>
	<c:if test="${faq.userCanDelete }">
		<a class="btn" href="../FAQ/doDelete?id=${faq.id }">삭제</a>
	</c:if>
</div>

</body>
</html>