<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Page</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">


</head>
<body>

	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container" style="margin-top: 100px;">


		<div class="list-group col-sm-4">
			<a href="#" class="list-group-item list-group-item-action active"
				style="background-color: #4375d9; border: none;"
			> My Page </a> <a href="#" class="list-group-item list-group-item-action">나의 여행 플랜</a> <a href="#"
				class="list-group-item list-group-item-action"
			>좋아요 한 게시글</a> <a href="#" class="list-group-item list-group-item-action">찜한 상품</a> <a href="#"
				class="list-group-item list-group-item-action"
			>내 결제 내역/쿠폰 확인</a> <a href="#" class="list-group-item list-group-item-action">내 정보 수정</a>
		</div>

		<div class="col-sm-8 jumbotron" style="height:300px;">
			<div class="col-sm-10 container">
			<h2>반가워요 " ${member.m_nickname } "님!</h2><br>
			<h2>새로운 여행을 떠나볼까요?</h2>
			</div>
			
			<div class="col-sm-2 container">
			<!-- <img src="resources/profile/1595252064_img_640x640.jpg" style="height:110px; width:110px; border-radius: 50%;"> -->
			</div>

		</div>


	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>