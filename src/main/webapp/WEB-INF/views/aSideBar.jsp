<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="resources/css/aSideBarStyle.css">
</head>
<div class="col-sm-3 col-md-2 sidebar no-drag">
	<ul class="nav nav-sidebar">
		<li class="active sideBar_title" onclick="location.href='aHome2';">관리자
			메뉴</li>
		<li id="allMember"><a href="aMemberList">전체 회원<br>리스트 보기
		</a></li>
		<li id="allCeo"><a href="aCeoList">전체 업체 회원<br>리스트 보기
		</a></li>
		<li id="acceptedCeo"><a href="aAuthList">승인 완료 업체 회원<br>리스트
				보기
		</a></li>
		<li id="pendingCeo"><a href="stAuthMail">승인 대기 업체 회원<br>리스트
				보기
		</a></li>
	</ul>
	<ul class="nav nav-sidebar">
		<li id="allMail"><a href="aGroupMailFrm">단체 메일 발송하기</a></li>
	</ul>
	<ul class="nav nav-sidebar">
		<li id="event"><a href="aEvent">이벤트 관리 페이지</a></li>
		<li id="report"><a href="aReport">신고 관리 페이지</a></li>
		<li id="suggest"><a href="aSuggest">건의사항 관리 페이지</a></li>
	</ul>
</div>
