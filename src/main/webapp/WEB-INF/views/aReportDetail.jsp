<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp" />
<link rel="stylesheet" href="resources/css/aReportDetailStyle.css">
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<div class="row">
			<jsp:include page="aSideBar.jsp" />
			<div class="col-md-10 col-sm-9" align="center">
				<fieldset>
					<legend>제목</legend>
					<input type="text" value="${report.rp_title}" disabled>
				</fieldset>
				<fieldset>
					<legend>내용</legend>
					<textarea rows="10" cols="120" disabled>${report.rp_story}</textarea>
				</fieldset>
				<fieldset>
					<legend>신고자 / 신고 대상</legend>
					신고자 ID <input type="text" value="${report.rp_mid}" disabled>
					<!--  -->
					처리 대상 ID <input type="text" value="${report.rp_cnum}" disabled>
				</fieldset>
				<fieldset>
					<legend>처리</legend>
					<input type="text" value="${report.rp_condition }" id="condition"
						class="no-drag" disabled>
					<div class="detailBtn" id="dispose"
						onclick="detail(${report.rp_num});">신고 처리</div>
					<!--  -->
					<div id="back" class="detailBtn"
						onclick="window.history.back();">뒤로가기</div>
				</fieldset>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
function detail(rp_num){
	location.href="aReportDispose?rp_num=" + rp_num;
}

var conditionVal = document.getElementById("condition").value;
console.log(condition.value);
//처리중, 처리완료에 따라 input 배경색 클래스 지정
if (conditionVal == "처리완료") {
	condition.classList.add("finished");
	//처리 완료인 경우 처리 페이지로 가지 않도록 버튼에 숨기는 클래스 추가
	dispose.classList.add("dispose-none");
}
else {
	condition.classList.add("unfinished");
}
</script>
</html>