<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<link rel="stylesheet" href="resources/css/aHomeStyle.css">
<link rel="stylesheet" href="resources/css/style.css">
<jsp:include page="aImports.jsp"/>
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<div class="row">
			<jsp:include page="aSideBar.jsp" />
			<div class="aHome_area col-sm-3 col-md-10 no-drag">
				<div class="row">
					<a class="aLink aMenu brMenu col-md-4" href="aMemberList">전체 회원<br>리스트
						보기
					</a>
					<!--  -->
					<a class="aLink aMenu brMenu col-md-4" href="aCeoList">전체 업체 회원<br>리스트
						보기
					</a>
				</div>
				<div class="row">
					<a class="aLink aMenu brMenu col-md-4" href="aAuthList">승인 완료
						업체 회원<br>리스트 보기
					</a>
					<!--  -->
					<a class="aLink aMenu brMenu col-md-4" href="stAuthMail">승인 대기
						업체 회원<br>리스트 보기
					</a>
				</div>
				<div class="row">
					<a class="aLink aMenu nonBrMenu col-md-4" href="aGroupMailFrm">단체
						메일 발송하기</a>
					<!--  -->
					<a class="aLink aMenu nonBrMenu col-md-4" href="aEvent">이벤트 관리
						페이지</a>
				</div>
				<div class="row">
					<a class="aLink aMenu nonBrMenu col-md-4" href="aReport">신고 관리
						페이지</a>
					<!--  -->
					<a class="aLink aMenu nonBrMenu col-md-4" href="aSuggestList">건의사항
						리스트 보기</a>
				</div>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
</html>