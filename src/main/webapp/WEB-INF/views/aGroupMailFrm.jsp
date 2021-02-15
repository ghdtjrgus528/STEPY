<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp"/>
<link rel="stylesheet"
	href="resources/css/aGroupMail&Event&ReportStyle.css">
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
					<a class="mailBtn col-sm-3 no-drag" href="aGroupMail_M">일반 회원</a>
					<!--  -->
					<a class="mailBtn col-sm-3 no-drag" href="aGroupMail_C">업체 회원</a>
					<!--  -->
				</div>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>