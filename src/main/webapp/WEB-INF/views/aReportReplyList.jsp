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
					<font id="caption_title">신고된 댓글 리스트 보기 </font>
				</caption>
				<thead>
					<tr>
						<th>댓글 등록 번호</th>
						<th>댓글 내용</th>
						<th>비밀댓글 여부</th>
						<th>댓글 등록일</th>
						<th>작성자 ID</th>
						<th>작성된 글번호</th>
						<th>신고 등록 여부</th>
						<th>신고 처리</th>
					</tr>
				</thead>
				<c:forEach var="rItem" items="${rList}">
					<tr class="item">
						<td>${rItem.r_num }</td>
						<td>${rItem.r_contents }</td>
						<td>${rItem.r_secret }</td>
						<td>${rItem.r_date }</td>
						<td>${rItem.r_id }</td>
						<td>${rItem.r_pnum }</td>
						<td>${rItem.r_report }</td>
						<td class="delete" onclick="deleteConfirm_r(${rItem.r_num})">처리하기</td>
					</tr>
				</c:forEach>
				<c:if test="${empty rList }">
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
<script type="text/javascript">
function deleteConfirm_r(r_num) {
	console.log(r_num);
	var select = confirm("이 게시글을 강제 삭제합니다 | "+r_num);
	if (select == true){
		location.href="aBlockReply?r_num="+r_num;
	}
}
</script>
<script src="resources/js/aAutoColspan.js"></script>
</html>