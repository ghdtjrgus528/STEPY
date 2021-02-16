<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개인정보 수정</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
<style>
.gradi {
	background-image: linear-gradient(to bottom right, rgba(67, 117, 217, 1),
		rgba(67, 117, 217, 0.5));
}

label {
	color: white;
}

.shady {
	box-shadow: 0 8px 8px 0 rgba(0, 0, 0, 0.3), 0 6px 20px 0
		rgba(0, 0, 0, 0.10);
	border-radius: 50px;
}

.profileback {
	background-image: linear-gradient(to bottom right, rgba(220, 220, 217, 1),
		rgba(67, 117, 217, 0.5));
	border-radius: 50px 20px;
}

li .active{
	background-color: #4375d9;
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
			<li class="nav-item"><a class="nav-link" href="./mMyPayment">내 결제 내역/쿠폰 확인</a></li>
			<li class="nav-item active"><a class="nav-link" href="./mModifyMyinfo">내 정보 수정</a></li>
		</ul>
		
		<br> <br> <br>
		<h1>개인정보 수정</h1>
		<hr>
		<div class="row">
			<!-- left column -->
			<div class="col-md-3">
				<div class="text-center profileback shady" style="height: 400px;">
					<br>
					<br>
					<br>
					<br>
					<br>
					<div>
						<img src="resources/profile/${profile.f_oriname }" class="avatar img-circle" alt="avatar"
							style="height: 110px; width: 110px; border-radius: 50%;" id="preview"
						> <br>
						<br>
						<h6 style="color: black; display: none;">사진을 먼저 선택해주세요</h6>
						<label for="profileup" style="color: black;"> 프로파일 변경하기</label> <input type="file"
							id="profileup" class="form-control" accept="jpg,jpeg,png,bmp" style="display: none;"
						>
						<button id="upload" class="btn btn-outline-secondary" style="color: black; display: none;">
							저장</button>
						<br>
						<br> <span id="respan" style="color: black;"></span>
					</div>
				</div>
			</div>


			<!-- edit form column -->
			<div class="col-md-9 personal-info text-center gradi shady">
				<!-- 
				<div class="alert alert-info alert-dismissable">
					<a class="panel-close close" data-dismiss="alert">×</a> <i class="fa fa-coffee"></i> This is an
					<strong>.alert</strong>. Use this to show important messages to the user.
				</div>
				 -->
				<br> <br>
				<h4 style="color: white; margin-right: 0;" id="formsg">개인정보 변경 후 하단의 upload 버튼을 눌러주세요</h4>
				<br>
				<br>

				<form class="form-horizontal" id="modifyFrm" role="form" action="mModifyProc" method="post" onsubmit="return submitcheck()">
					<div class="form-group">
						<label class="col-lg-3 control-label">회원이름:</label>
						<div class="col-lg-8">
							<input class="form-control" name="m_name" type="text" value="${member.m_name }" readonly>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">닉네임: </label>
						<div class="col-lg-8">
							<input class="form-control" id="nickname" name="m_nickname" type="text" maxlength="11" value="${member.m_nickname }">
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">회원 아이디: </label>
						<div class="col-lg-8">
							<input class="form-control" name="m_id" type="text" value="${member.m_id }" readonly>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label"></label>
						<div class="col-md-5"></div>
						<div class="col-md-3">
							<a href="./mModifyPwd"><button type="button" class="btn" style="background-color: #fafafa; color: black;">
							비밀번호 변경하기</button></a>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">Email: </label>
						<div class="col-lg-8">
							<input class="form-control" name="m_email" type="email" value="${member.m_email }" readonly>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">연락처: </label>
						<div class="col-md-8">
							<input type="tel" name="m_phone" class="form-control" id="mobilephone"
								placeholder="여백없이 번호 11자리 입력 " minlength="11" maxlength="11" value="${member.m_phone }"
							>

						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">생일: </label>
						<div class="col-lg-8">
							<input class="form-control" name="m_birth" type="text" minlength="8" maxlength="8"
								placeholder="19980323" value="${member.m_birth }" id="birth"
							>
						</div>
					</div>

					<div class="form-group">
						<label class="col-md-3 control-label">거주지: </label>
						<div class="col-md-8">
							<input class="form-control" type="text" value="${member.m_addr }" readonly>
						</div>
					</div>

					<div class="form-group row">
						<label for="address" class="col-md-3 col-form-label control-label">거주지 변경 : </label>
						<div class="col-md-8">
							<input type="text" id="sample6_postcode" placeholder="우편번호"> <input type="button"
								onclick="sample6_execDaumPostcode()" value="우편번호 찾기"
							><br> <input type="text" id="sample6_address" placeholder="주소"
								name="address_without_specific"
							><br> <input type="text" id="sample6_detailAddress" placeholder="상세주소"
								name="address_with_specific"
							> <input type="text" id="sample6_extraAddress" placeholder="참고항목"><br>
						</div>
					</div>

					<br>
					<br>
					<div class="form-group">
						<label class="col-md-3 control-label"></label>
						<div class="col-md-8">
							<button class="btn btn-primary" type="submit" id="submitbtn">업로드</button>
							<a href="javascript:history.back()"><button type="button" class="btn btn-default">취소</button></a><br><br>
							<span id="btalarm" style="diaplay:none; color:white;"></span>
						</div>
					</div>

				</form>
				<br>
				<br>
			</div>
		</div>

	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>

</body>

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="resources/js/jquery.serializeObject.js"></script>
<script>

	function submitcheck(){
		
		var nickname = $("#nickname").val();
		var birth = $("#birth").val();
		var phone = $("#mobilephone").val();
		console.log(phone);
		var addhead = $("#sample6_postcode").val();
		
		if(nickname ==  "${member.m_nickname}" && birth == "${member.m_birth}"
				&& phone == "${member.m_phone}" && addhead == "" ){
			$("#btalarm").text("변경된 사항이 없습니다^^!");
			return false;
		}
		
	}
	

	$(function(){
		var msg = "${msgFromModify}";
		if(msg != ""){
			$("#formsg").text(msg);
		}
	});

	$("#profileup").on('change', function(e) {
		
		$("#upload").css("display","none");
		$("#respan").css("display","none");

		var profileup = this.files;
		var profile = profileup[0];

		var reader = new FileReader();

		reader.addEventListener('load', function(e) {
			preview.src = e.target.result;
		});

		reader.readAsDataURL(profile);
		$("#upload").css("display", "inline-block");

	});

	var upload = document.getElementById("upload");
	upload.addEventListener("click", function() {
		var control = document.getElementById("profileup");
		var profile = control.files[0];

		console.log(profile);

		var form = new FormData();
		form.append("pfile", profile);

		console.log(form);

		$.ajax({

			url : "mProfileUpdate",
			type : "post",
			data : form,
			processData : false,
			contentType : false,
			success : function(res) {
				$("#respan").css("display","inline-block");
				$("#respan").text("프로필사진이 변경되었습니다!");
			},
			error : function(error) {
				alert("파일이 너무 크거나 이미지 파일이 아닙니다");
			}
		});

	});

	function sample6_execDaumPostcode() {
		new daum.Postcode(
				{
					oncomplete : function(data) {
						// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

						// 각 주소의 노출 규칙에 따라 주소를 조합한다.
						// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
						var addr = ''; // 주소 변수
						var extraAddr = ''; // 참고항목 변수

						//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
						if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
							addr = data.roadAddress;
						} else { // 사용자가 지번 주소를 선택했을 경우(J)
							addr = data.jibunAddress;
						}

						// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
						if (data.userSelectedType === 'R') {
							// 법정동명이 있을 경우 추가한다. (법정리는 제외)
							// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
							if (data.bname !== ''
									&& /[동|로|가]$/g.test(data.bname)) {
								extraAddr += data.bname;
							}
							// 건물명이 있고, 공동주택일 경우 추가한다.
							if (data.buildingName !== ''
									&& data.apartment === 'Y') {
								extraAddr += (extraAddr !== '' ? ', '
										+ data.buildingName : data.buildingName);
							}
							// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
							if (extraAddr !== '') {
								extraAddr = ' (' + extraAddr + ')';
							}
							// 조합된 참고항목을 해당 필드에 넣는다.
							document.getElementById("sample6_extraAddress").value = extraAddr;

						} else {
							document.getElementById("sample6_extraAddress").value = '';
						}

						// 우편번호와 주소 정보를 해당 필드에 넣는다.
						document.getElementById('sample6_postcode').value = data.zonecode;
						document.getElementById("sample6_address").value = addr;
						// 커서를 상세주소 필드로 이동한다.
						document.getElementById("sample6_detailAddress")
								.focus();
					}
				}).open();
	}
</script>
</html>