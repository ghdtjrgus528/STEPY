<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp"/>
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<jsp:include page="aMailWriteFrm.jsp">
			<jsp:param value="aSendMemberMail" name="action" />
			<jsp:param value="일반" name="mail_tgt" />
		</jsp:include>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>