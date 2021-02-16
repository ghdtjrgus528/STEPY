<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내가 좋아요한 글</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
<style>
.forblock {
	display: block;
	max-width: 600px;
	padding: 40px;
}

a:link, a:visited, a:hover, a:active {
	color: black;
	text-decoration: none;
}

.shady {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.19);
	text-align: center;
}
</style>

</head>

<body>

	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container text-center center-block" style="margin-bottom: 200px; margin-top: 100px;">


		<ul class="nav nav-tabs nav-justified">
			<li class="nav-item"><a class="nav-link" href="./mMyPage">myPage</a></li>
			<li class="nav-item"><a class="nav-link" href="./pPlanList?id=${member.m_id}">나의 여행 플랜</a></li>
			<li class="nav-item active"><a class="nav-link" href="./mMyLikedPages">좋아요 한 게시글</a></li>
			<li class="nav-item"><a class="nav-link" href="./mMyCartPages">찜한 상품</a></li>
			<li class="nav-item"><a class="nav-link" href="./mMyPayment">내 결제 내역/쿠폰 확인</a></li>
			<li class="nav-item"><a class="nav-link" href="./mModifyMyinfo">내 정보 수정</a></li>
		</ul>


		<div class="row" style="margin-top: 90px;">


			<c:if test="${!empty pList }">
				<c:forEach var="p" items="${pList}">
					<div id="cardBigBody" class="text-center center-block col-sm-6">
						<div class="card center-block shady"
							style="margin-bottom: 40px; background-color: #f5f5f5; max-width: 500px; padding: 5px;"
						>
							<a class="forblock" href="./contents?pnum=${p.p_num}">
								<div class="card-body">
									<div style="height: 50px; background: #4375d9;">
										<h4 class="card-title"
											style="color: white; -ms-transform: translateY(60%); transform: translateY(60%);"
										>${p.p_title}</h4>
									</div>
									<br><br><br>
									<p class="card-text">${fn:substring(p.p_contents,0,30)}···</p>
									<br>
									<br> <span>작성일: <fmt:formatDate pattern="yyyy-MM-dd" value="${p.p_date}" /></span>
								</div>
							</a>
						</div>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${empty pList }">
			아직 좋아요를 누른 게시글이 없습니다!
			</c:if>

		</div>

		<div class="paging">${paging }</div>


	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script>
	
</script>

</html>