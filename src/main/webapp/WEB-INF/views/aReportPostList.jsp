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
<head>
</head>
<head>
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
					<font id="caption_title">신고된 게시글 리스트 보기 </font>
				</caption>
				<thead>
					<tr>
						<th>게시글 등록 번호</th>
						<th>작성자 ID</th>
						<th>제목</th>
						<th>분류</th>
						<th>게시일</th>
						<th>조회</th>
						<th>좋아요</th>
						<th>신고 등록 여부</th>
						<th>신고 처리</th>
					</tr>
				</thead>
				<c:forEach var="pItem" items="${pList}">
					<tr class="item">
						<td>${pItem.p_num }</td>
						<td>${pItem.p_mid }</td>
						<td>${pItem.p_title }</td>
						<td>${pItem.p_category }</td>
						<td>${pItem.p_date }</td>
						<td>${pItem.p_view }</td>
						<td>${pItem.p_like }</td>
						<td>${pItem.p_report }</td>
						<td class="delete" onclick="deleteConfirm_p(${pItem.p_num})">처리하기</td>
					</tr>
				</c:forEach>
				<c:if test="${empty pList }">
					<tr>
						<!-- 칼럼의 수만큼 colspan -->
						<td id="noData">신고를 처리할 대상이 없습니다</td>
					</tr>
				</c:if>
			</table>
			<!-- 페이지 버튼 영역 -->
			<div class="btn-area" align="center">
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
<script type="text/javascript">
function deleteConfirm_p(p_num) {
	console.log(p_num);
	var select = confirm("이 게시글을 강제 삭제합니다 | "+p_num);
	if (select == true){
		location.href="aBlockPost?p_num="+p_num;
	}
}

</script>
</html>