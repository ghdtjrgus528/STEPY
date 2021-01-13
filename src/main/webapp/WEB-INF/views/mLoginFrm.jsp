<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome Travelers!</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
</head>
<body>

	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">

		<div class="container mt-4 center-block text-center"
			style="margin-top: 50px; max-width: 600px; background-color:#4375d9; color:white;">
			<h1>Log In</h1>
		</div>
		<div
			class="container mt-4 center-block text-dark text-center"
			style="max-width: 600px;background-color:#F5F5F5;"><br>
			<form action="mLoginProc" method="post">
				<input stype="text" name="m_id" autofocus placeholder="아이디"><br><br>
				<input type="password" name="m_pwd" placeholder="비밀번호"><br><br>
				<input type="submit" value="로그인">
			</form><br>
		</div>

	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>