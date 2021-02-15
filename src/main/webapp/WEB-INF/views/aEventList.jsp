<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp"/>
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
					<font id="caption_title">이벤트 리스트 보기</font>
				</caption>
				<thead>
					<tr>
						<th>이벤트 번호</th>
						<th>이벤트 제목</th>
						<th>이벤트 내용</th>
						<th>이벤트 종료일</th>
						<th>이벤트 중지</th>
					</tr>
				</thead>
				<c:forEach var="eItem" items="${eList}">
					<tr>
						<td>${eItem.e_num}</td>
						<td class="detail"
							onclick="location.href='aEventDetail?e_num=${eItem.e_num}';">${eItem.e_title}</td>
						<td>${eItem.e_contents}</td>
						<td>${eItem.e_date}</td>
						<td class="delete delBtn" onclick="stopConfirm('${eItem.e_num}');">중지</td>
					</tr>
				</c:forEach>
				<c:if test="${empty eList }">
					<tr>
						<!-- 칼럼의 수만큼 자동 colspan (스크립트 처리) -->
						<td id="noData">등록된 이벤트가 없습니다</td>
					</tr>
				</c:if>
				<tfoot>
					<tr>
						<td id="blank_td"></td>
						<td id="new_event" onclick="location.href='aEventWriteFrm'">신규
							이벤트 등록</td>
					</tr>
				</tfoot>
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
<script type="text/javascript">
	//메시지 확인
	$(document).ready(function() {
		var msg = "${msg}";

		if (msg != "") {
			alert(msg);
			location.reload(true);
		}
	});

	//이벤트 중지 확인
	function stopConfirm(eNum) {
		console.log("중단할 이벤트의 이벤트 코드 : " + eNum)
		var stopSelect = confirm("이 이벤트를 중지합니다");

		if (stopSelect == true) {
			location.href = "aDeleteEvent?e_num=" + eNum;
		}
	}

	function eventDetail(eNum) {
		location.href = "aEventDetail?e_num=" + eNum;
	}

	var ths = document.getElementsByTagName("th");
	var blank_colspan = ths.length - 2;
	blank_td.setAttribute("colspan", blank_colspan);
	new_event.setAttribute("colspan", ths.length - blank_colspan);
</script>
<script src="resources/js/aAutoColspan.js"></script>
</html>