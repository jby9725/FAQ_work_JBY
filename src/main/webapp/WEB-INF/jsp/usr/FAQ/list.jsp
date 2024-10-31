<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="FAQ"></c:set>
<%@ include file="../common/head.jspf"%>

<hr />

<section class="mt-24 text-xl px-4">
	<div class="mx-auto">

		<div class="mb-4 flex items-center">
			<div class="flex-grow"></div>
			<form action="" class="w-full">
				<div class="flex items-center space-x-3">
					<select class="select select-lg select-bordered max-w-xs" name="searchKeywordTypeCode"
						data-value="${param.searchKeywordTypeCode}">
						<option value="title">제목</option>
						<option value="body">내용</option>
						<option value="title,body">제목+내용</option>
						<option value="nickname">작성자</option>
					</select>
					<label class="input input-bordered input-lg flex items-center gap-3 w-full px-4 py-2">
						<input type="text" placeholder="검색어를 입력하세요" name="searchKeyword" value="${param.searchKeyword}"
							class="w-full text-lg" />
						<button type="submit" class="p-2">
							<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="h-6 w-6 opacity-70">
                                <path fill-rule="evenodd"
									d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
									clip-rule="evenodd" />
                            </svg>
						</button>
					</label>
				</div>
			</form>
		</div>

		<table class="table" border="1" cellspacing="0" cellpadding="5" style="width: 100%; border-collapse: collapse;">
			<thead>
				<tr>
					<th style="text-align: center;">ID</th>
					<th style="text-align: center;">Registration Date</th>
					<th style="text-align: center;">Title</th>
					<th style="text-align: center;">Body</th>
					<th style="text-align: center;">Writer</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="faq" items="${faqs}">
					<tr class="hover">
						<td style="text-align: center;">${faq.id}</td>
						<td style="text-align: center;">${faq.regDate.substring(0,10)}</td>
						<td style="text-align: center;">
							<a class="hover:underline" href="detail?id=${faq.id}">${faq.title}</a>
						</td>
						<td style="text-align: center;">
							<a class="hover:underline" href="detail?id=${faq.id}">${faq.body}</a>
						</td>
						<td style="text-align: center;">${faq.extra__writer}</td>
					</tr>
				</c:forEach>

				<c:if test="${empty faqs}">
					<tr>
						<td colspan="5" style="text-align: center;">FAQ가 없습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>

	<!-- 동적 페이징 -->
	<div class="pagination flex justify-center mt-3">
		<c:set var="paginationLen" value="3" />
		<c:set var="startPage" value="${page - paginationLen >= 1 ? page - paginationLen : 1}" />
		<c:set var="endPage" value="${page + paginationLen <= pagesCount ? page + paginationLen : pagesCount}" />

		<c:set var="baseUri" value="?boardId=${boardId }" />
		<c:set var="baseUri" value="${baseUri }&searchKeywordTypeCode=${searchKeywordTypeCode}" />
		<c:set var="baseUri" value="${baseUri }&searchKeyword=${searchKeyword}" />

		<c:if test="${startPage > 1 }">
			<a class="btn btn-sm" href="${ baseUri}&page=1">1</a>
		</c:if>
		<c:if test="${startPage > 2 }">
			<button class="btn btn-sm btn-disabled">...</button>
		</c:if>

		<c:forEach begin="${startPage }" end="${endPage }" var="i">
			<a class="btn btn-sm ${param.page == i ? 'btn-active' : '' }" href="${ baseUri}&page=${i }">${i }</a>
		</c:forEach>

		<c:if test="${endPage < pagesCount - 1 }">
			<button class="btn btn-sm btn-disabled">...</button>
		</c:if>

		<c:if test="${endPage < pagesCount }">
			<a class="btn btn-sm" href="${ baseUri}&page=${pagesCount }">${pagesCount }</a>
		</c:if>
	</div>

</section>

<%@ include file="../common/foot.jspf"%>