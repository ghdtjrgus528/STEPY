<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp" />
<!-- 메일&이벤트&신고 공통 스타일 -->
<link rel="stylesheet"
	href="resources/css/aGroupMail&Event&ReportStyle.css">
<script type="text/javascript">
	var msg = "${msg}";

	if (msg != "") {
		alert(msg);//msg 메시지를  alert출력
		//현재 페이지를 리로드, 새로고침해 현재 페이지를 초기화
		msg = "";
		location.reload(true);
	}
</script>
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<div class="row">
			<jsp:include page="aSideBar.jsp" />
			<div class="col-md-10 col-sm-9">
				<div>
					<a class="reportBtn col-sm-3 no-drag" href="aReportStoreList">신고된
						업체<br> 목록 보기
					</a>
					<!--  -->
					<a class="reportBtn col-sm-3 no-drag" href="aReportPostList"><span>신고된
							게시글<br>목록 보기
					</span> </a>
					<!--  -->
					<a class="reportBtn col-sm-3 no-drag" href="aReportReplyList">신고된
						댓글<br>목록 보기
					</a>
				</div>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>