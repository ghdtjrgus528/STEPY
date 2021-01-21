<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<!-- 메일&이벤트&신고 공통 스타일 -->
<link rel="stylesheet"
	href="resources/css/aGroupMail&Event&ReportStyle.css">
<!-- 이벤트 페이지들 전용 스타일-->
<link rel="stylesheet" href="resources/css/aGroupMail&Event&ReportStyle.css">

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
					<a class="eventBtn col-sm-3" href="aEventList">진행중<br>이벤트
					</a>
					<!--  -->
					<a class="eventBtn col-sm-3" href="aWriteEventFrm">이벤트<br>목록
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