<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp" />
<link rel="stylesheet" href="resources/css/aListStyle.css">
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<jsp:include page="aSideBar.jsp" />
		<div class="list_area col-sm-3 col-md-10">
			<table class="listTbl table-bordered table-hover table-striped">
				<caption>
					<font id="caption_title">일반 회원 리스트 보기</font> <br>
					<!--  -->
					<input type="radio" name="choice" value="1" id="all" checked>
					<label for="all">전체 보기</label>
					<!--  -->
					<input type="radio" name="choice" value="2" id="m-only"> <label
						for="m-only">남성 회원만</label>
					<!--  -->
					<input type="radio" name="choice" value="3" id="f-only"> <label
						for="f-only">여성 회원만</label><br>
				</caption>
				<thead>
					<tr>
						<th>아이디</th>
						<th>이메일</th>
						<th>이름</th>
						<th>닉네임</th>
						<th>생년월일</th>
						<th>성별</th>
						<th>연락처</th>
						<th>주소</th>
						<th>회원 추방</th>
					</tr>
				</thead>
				<c:forEach var="mItem" items="${mList}">
					<tr class="item">
						<td class="mid">${mItem.m_id}</td>
						<td>${mItem.m_email}</td>
						<td>${mItem.m_name}</td>
						<td>${mItem.m_nickname}</td>
						<td>${mItem.m_birth}</td>
						<td class="gender">${mItem.m_gender}</td>
						<td>${mItem.m_phone}</td>
						<td>${mItem.m_addr}</td>
						<td class="delete" onclick="delConfirm('${mItem.m_id}');">
							<div>추방</div>
						</td>
					</tr>
				</c:forEach>
				<!-- 어드민도 회원이므로 mList가 empty인 것은 불가능해 noData 생략 -->
			</table>
			<!-- 페이지 버튼 영역 -->
			<div class="btn-area">
				<!-- 페이지 버튼, EL 처리 -->
				<div class="paging">${paging}</div>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
	mIds = document.getElementsByClassName("mid");
	checks = document.getElementsByClassName("check");
	deleteCells = document.getElementsByClassName("delete");
	trs = document.getElementsByClassName("item");
	genders = document.getElementsByClassName("gender");

	function delConfirm(m_id) {
		if (m_id == 'admin') {
			return;
		}

		var checkConfirm = confirm("이 회원의 데이터를 삭제합니다 | " + m_id);
		if (checkConfirm == true) {
			location.href = "aDeleteMember?m_id=" + m_id;
		}
		;
	}

	//admin은 추방될 수 없는 계정으로 판단하고 추방하기를 볼 수 없는 예외로 등록
	for (var i = 0; i < mIds.length; i++) {
		if (mIds[i].innerHTML == "admin") {
			deleteCells[i].innerHTML = "";
			deleteCells[i].classList.add("clearBg");
		}
	}

	//라디오 버튼 선택에 따라 해당 필터 적용 (AJAX없이 즉석 처리)
	$('#all').change(function() {
		console.log("전체 보기");
		for (var i = 0; i < trs.length; i++) {
			$(trs[i]).removeClass("hide");
		}
	});

	$('#m-only').change(function() {
		console.log("남성 보기");
		for (var i = 0; i < trs.length; i++) {
			if (genders[i].innerHTML == "남") {
				$(trs[i]).removeClass("hide");
			} else {
				trs[i].classList.add("hide");
			}
		}
	});

	$('#f-only').change(function() {
		console.log("여성 보기");
		for (var i = 0; i < trs.length; i++) {
			if (genders[i].innerHTML == "여") {
				$(trs[i]).removeClass("hide");
			} else {
				trs[i].classList.add("hide");
			}
		}
	});
</script>
</html>