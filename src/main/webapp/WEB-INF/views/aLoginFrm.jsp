<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome Travelers!</title>
<jsp:include page="aImports.jsp" />
<link rel="stylesheet" href="resources/css/aSideBarStyle.css">
<link href="resources/css/style.css" rel="stylesheet">
<script type="text/javascript">
	var msg = "${msg}";

	if (msg != "") {
		alert(msg);//msg 메시지를  alert출력
		//현재 페이지를 리로드, 새로고침해 현재 페이지를 초기화
		location.reload(true);
	}
</script>
<style type="text/css">
form.form-horizontal>h3 {
	margin-top: 10px;
	font-weight: bolder;
	text-align: center;
}
</style>
</head>
<body>
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<div class="container mt-4 center-block text-center"
			style="margin-top: 50px; max-width: 600px; background-color: #4375d9; color: white;">
			<h1>Log In</h1>
		</div>
		<div class="container mt-4 center-block text-dark text-center"
			style="max-width: 600px; background-color: #F5F5F5;">
			<br>
			<form action="aHome" method="post">
				<input type="text" name="m_id" autofocus placeholder="아이디"><br>
				<br> <input type="password" name="m_pwd" placeholder="비밀번호"><br>
				<br> <input type="submit" value="로그인">
			</form>
			<br>
		</div>

	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>