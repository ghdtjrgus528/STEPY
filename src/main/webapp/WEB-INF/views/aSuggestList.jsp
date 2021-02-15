<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp" />
<link rel="stylesheet" href="resources/css/aListStyle.css">
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<jsp:include page="aSideBar.jsp" />
		<div class="list_area col-sm-3 col-md-10">
			<table class="listTbl table-bordered table-hover table-striped">
				<caption>
					<font id="caption_title">건의사항 리스트 보기</font> <br>
				</caption>
				<thead>
					<tr>
						<th>등록 번호</th>
						<th>제목</th>
						<th>내용</th>
						<th>작성자</th>
						<th>조회 수</th>
						<th>등록</th>
					</tr>
				</thead>
				<c:forEach var="sugItem" items="${sugList}">
					<tr class="item">
						<td>${sugItem.sug_num}</td>
						<td
							onclick="location.href='aSuggestDetail?sug_num=${sugItem.sug_num}';"
							style="cursor: pointer;"><a>${sugItem.sug_title}</a></td>
						<td>${sugItem.sug_contents}</td>
						<td>${sugItem.sug_mid}</td>
						<td>${sugItem.sug_view}</td>
						<td>${sugItem.sug_date}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty sugList }">
					<tr>
						<!-- 칼럼의 수만큼 colspan -->
						<td id="noData">등록된 건의사항 글이 없습니다</td>
					</tr>
				</c:if>
			</table>
			<!-- 페이지 버튼 영역 -->
			<div class="btn-area">
				<!-- 페이지 버튼, EL 처리 -->
				<div class="paging">${paging}</div>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script src="resources/js/aAutoColspan.js"></script>
</html>