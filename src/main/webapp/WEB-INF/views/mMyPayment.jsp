<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">

<style type="text/css">
.resConfirm {
	margin: 0 auto;
	background: #F8F8F8;
	padding: 30px;
	margin-bottom: 45px;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.19);
}

.title {
	font-size: 24px;
	font-weight: bold;
}

.storeInfo {
	font-size: 20px;
	font-weight: bold;
	color: #444444;
	margin-bottom: 10px;
}

.home {
	float: right;
	border-style: none;
	background-color: #F8F8F8;
}

.contentsTitle {
	font-size: 20px;
	color: #808080;
	margin-bottom: 10px;
}

.checking {
	float: right;
	color: black;
	font-weight: bold;
}

.price {
	float: right;
	font-size: 23px;
	font-weight: bold;
	color: #F2B950;
}

.hul {
	margin: 0;
	font-size: 18px;
	color: #555555;
}

.hli {
	margin-bottom: 5px;
}

.btnStyle {
	margin-top: 30px;
	font-size: 15px;
	color: white;
}

.cancleBtn {
	width: 240px;
	height: 45px;
	background: #4375D9;
	border: 1px solid #4375D9;
	border-radius: 5px;
}

.payBtn {
	width: 240px;
	height: 45px;
	background: #F2B950;
	border: 1px solid #F2B950;
	border-radius: 5px;
}

a:link, a:visited, a:hover, a:active {
	color: black;
	text-decoration: none;
}

.myli {
	margin: 0 auto;
	max-width: 400px;
	font-size: 20px;
	max-width: 400px;
}

.sname {
	font-size: 40px;
}
</style>

</head>
<body>

	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container" style="margin-bottom: 200px; margin-top: 100px;">

		<ul class="nav nav-tabs nav-justified">
			<li class="nav-item"><a class="nav-link" href="./mMyPage">myPage</a></li>
			<li class="nav-item"><a class="nav-link" href="./pPlanList?id=${member.m_id}">나의 여행 플랜</a></li>
			<li class="nav-item"><a class="nav-link" href="./mMyLikedPages">좋아요 한 게시글</a></li>
			<li class="nav-item"><a class="nav-link" href="./mMyCartPages">찜한 상품</a></li>
			<li class="nav-item active"><a class="nav-link" href="./mMyPayment">내 결제 내역/쿠폰 확인</a></li>
			<li class="nav-item"><a class="nav-link" href="./mModifyMyinfo">내 정보 수정</a></li>
		</ul>

		<div class="row text-center center-block" style="margin-top: 50px;">

			<div class="col">
				<ul class="list-group list-group-flush">
					<c:if test="${sort == 1 }">
						<a href="./mMyPayment"><li class="list-group-item myli"
							style="background-color: #4375d9; color: white;"
						>미결제 상품보기</li></a>
						<a href="./mMyPayment?sort=2"><li class="list-group-item myli">결제 완료 상품보기</li></a>
					</c:if>
					<c:if test="${sort == 2 }">
						<a href="./mMyPayment"><li class="list-group-item myli">미결제 상품보기</li></a>
						<a href="./mMyPayment?sort=2"><li class="list-group-item myli"
							style="background-color: #F2B950;"
						>결제 완료 상품보기</li></a>
					</c:if>
				</ul>
			</div>
			<br> <br>


			<c:if test="${sort == 1 }">
				<c:if test="${!empty tkList}">
					<c:forEach var="tk" items="${tkList}">
						<div id="cardBigBody" class="text-center center-block col-sm-6">

							<div class="resConfirm">
								<div class="title">
									<span>예약 내역 확인</span>
								</div>
								<hr>

								<div>
									<div class="row">
										<div class="col-sm-6">
											<img src="resources/upload${tk.f_sysname }" style="max-width:100%; height:150px;" >
										</div>
										<div class="col-sm-6">
											<div class="storeInfo">
												<a href="./plProductList?c_num=${tk.pl_cnum }"> <span style="" class="sname">${tk.s_name}</span>&nbsp;&nbsp;
													<span class="glyphicon glyphicon-home -empty" style=""></span></a>
											</div>
											<div class="storeInfo">${tk.pl_name}</div>
										</div>
									</div>
									<hr>
									<div class="contentsTitle">
										<span>체크인</span> <span class="checking">${tk.res_checkindate} &nbsp;15시</span>
									</div>
									<div class="contentsTitle">
										<span>체크아웃</span> <span class="checking">${tk.res_checkoutdate} &nbsp;11시</span>
									</div>
									<div class="contentsTitle">
										<span>가격</span> <span class="price"> <fmt:formatNumber type="number"
												maxFractionDigits="3" value="${tk.pl_price}"
											/>원
										</span>
									</div>
								</div>
								<hr>


								<div class="btnStyle row">
									<div class="col">
										<button class="cancleBtn" onclick="location.href='./resCancle?res_num=${res_num}'">예약
											취소</button>
									</div>
									<br>
									<div class="col">
										<input class="payBtn" id="import"
											onclick="importfunction('${tk.pl_name}','${tk.pl_price }','${tk.res_num }')"
											type="button" value="결제하기"
										>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>

				</c:if>

				<c:if test="${empty tkList}">
					<p>아직 예약한 상품이 없습니다!</p>
				</c:if>
			</c:if>


			<c:if test="${sort == 2 }">
				<c:if test="${!empty tkList}">
					<c:forEach var="tk" items="${tkList}">
						<div id="cardBigBody" class="text-center center-block col-sm-6">

							<div class="resConfirm">
								<div class="title">
									<span>결제 내역 확인</span>
								</div>
								<hr>

								<div>
									<div class="row">
										<div class="col-sm-6">
											<img src="resources/upload${tk.f_sysname }" style="max-width:100%; height:150px;" >
										</div>
										<div class="col-sm-6">
											<div class="storeInfo">
												<a href="./plProductList?c_num=${tk.pl_cnum }"> <span style="" class="sname">${tk.s_name}</span>&nbsp;&nbsp;
													<span class="glyphicon glyphicon-home -empty" style=""></span></a>
											</div>
											<div class="storeInfo">${tk.pl_name}</div>
										</div>
									</div>
									<hr>
									<div class="contentsTitle">
										<span>체크인</span> <span class="checking">${tk.res_checkindate} &nbsp;15시</span>
									</div>
									<div class="contentsTitle">
										<span>체크아웃</span> <span class="checking">${tk.res_checkoutdate} &nbsp;11시</span>
									</div>
									<div class="contentsTitle">
										<span>가격</span> <span class="price"> <fmt:formatNumber type="number"
												maxFractionDigits="3" value="${tk.pl_price}"
											/>원
										</span>
									</div>
								</div>
								<hr>

								<div>
									<div>
										<ul class="hul">
											<li class="hli">결제까지 마친 후에 객실을 이용하실 수 있습니다.</li>
											<li class="hli">미성년자는 보호자 동반 시 투숙이 가능합니다.</li>
											<li class="hli">취소 및 환불 규정에 따라 취소 수수료 부과 및 취소 불가 될 수 있습니다.</li>
										</ul>
									</div>
								</div>

								<div class="btnStyle row">
									<div class="col">
										<button class="cancleBtn" onclick="location.href='./resCancle?res_num=${res_num}'">결제
											취소</button>
									</div>
									<br>
								</div>
							</div>
						</div>
					</c:forEach>

				</c:if>
				<c:if test="${empty tkList}">
					<p>아직 구매한 상품이 없습니다!</p>
				</c:if>

			</c:if>



		</div>


	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>

</body>

<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
<script type="text/javascript">
	function importfunction(plname, plprice, resnum) {

		IMP.init('imp49865347');

		IMP.request_pay(
						{
							pg : 'inicis',
							pay_method : 'card',
							merchant_uid : 'merchant_' + new Date().getTime(),
							name : plname,
							amount : plprice,
							buyer_name : '${member.m_name}',
							buyer_email : '${member.m_email}',
							buyer_tel : '${member.m_phone}',
							buyer_addr : '${member.m_addr}',
							buyer_postcode : '123-456'
						},
						function(rsp) {
							console.log(rsp);
							if (rsp.success) {

								var object = {
									"resnum" : resnum
								};

								$.ajax({
										url : "mPaiedInFull",
										type : "post",
										data : object,
										success : function(rsp) {
											window.location.href = 
												'http://localhost/stepy/mMyPayment?sort=2';
										},
										error : function(rsp) {
											alert("error");
										}

									});

							} else {
								var msg = "error or cancled payment";
							}

						});

	}
</script>

</html>