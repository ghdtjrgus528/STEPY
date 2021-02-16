<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
</head>
<body
	style="background-image: linear-gradient(to bottom right, rgba(67, 117, 217, 1), rgba(67, 117, 217, 0.3));"
>
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">

		<div class="center-block"
			style="box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.10); max-width: 600px; margin-top: 50px; margin-bottom: 100px;"
		>

			<div class="container mt-4 center-block text-center"
				style="max-width: 600px; background-color: #4375d9; color: white;"
			>
				<h1>Join STEPY</h1>
			</div>
			<div class="container mt-4 center-block text-dark text-center"
				style="max-width: 600px; background-color: #fafafa;"
			>
				<br> <br>

				<form action="mJoinProc" method="post" onsubmit="return scanform()">

					<div class="form-group row">
						<label for="#" class="col-sm-2 col-form-label">*아이디</label>
						<div class="col-sm-7">
							<input type="text" name="m_id" class="form-control" placeholder="id입력" id="checkid"
								onclick="resetid()" onfocus="this.value=''" required
							> <input type="hidden" id="fixid" value="0" name="fixid">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn" onclick="idDuplicationCheck()"
								style="background-color: #4375d9; color: white;"
							>중복확인</button>
						</div>
					</div>

					<div class="form-group row">
						<span id="idMsg" style="display: none;"></span>
					</div>

					<div class="form-group row">
						<label for="inputPassword3" class="col-sm-2 col-form-label">*비밀번호</label>
						<div class="col-sm-7">
							<input type="password" name="m_pwd" class="form-control" id="password1" placeholder="비밀번호"
								required
							> <input type="hidden" id="fixpwd" value="0" name="fixpwd">
						</div>

					</div>

					<div class="form-group row">
						<label for="passdoublecheck" class="col-sm-2 col-form-label">*비밀번호 확인</label>
						<div class="col-sm-7">
							<input type="password" class="form-control" id="password2" onclick="needprepwd()"
								placeholder="비밀번호 확인" required
							>
						</div>
					</div>

					<div class="form-group row">
						<span id="passwordConfirm" style="display: none"></span>
					</div>

					<div class="form-group row">
						<label for="inputEmail3" class="col-sm-2 col-form-label">*이메일</label>
						<div class="col-sm-7">
							<input type="email" name="m_email" class="form-control" id="emailaddr"
								placeholder="ex) stepy@naver.com" required
							> <input type="hidden" id="fixemail" value="0" name="fixemail">
						</div>
						<div class="col-sm-2">
							<button type="button" class="btn" onclick="authByEmail()"
								style="background-color: #4375d9; color: white;"
							>이메일발송</button>
						</div>
						<input type="hidden" id="preemailcheck" value="0">
					</div>

					<div class="form-group row">
						<span id="sentmail" style="display: none;"></span>
					</div>

					<div class="form-group row">
						<label for="passdoublecheck" class="col-sm-2 col-form-label">*이메일 인증번호</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="emailauth" placeholder="이메일 인증번호 입력" required>
						</div>
					</div>

					<div class="form-group row">
						<span id="emailMsg" style="display: none;"></span>
					</div>


					<div class="form-group row">
						<label for="name" class="col-sm-2 col-form-label">*성명</label>
						<div class="col-sm-7">
							<input type="text" name="m_name" class="form-control" id="name" placeholder="ex)홍길동" required>
						</div>
					</div>

					<div class="form-group row">
						<label for="nickname" class="col-sm-2 col-form-label">*닉네임</label>
						<div class="col-sm-7">
							<input type="text" name="m_nickname" class="form-control" id="nickname" maxlength="11" required>
						</div>
					</div>
					
					<div class="form-group row">
						<label for="mobilephone" class="col-sm-2 col-form-label">*연락처</label>
							<div class="col-sm-7">
								<input type="tel" name="m_phone" class="form-control" id="mobilephone"
								placeholder="여백없이 번호 11자리 입력 " minlength="11" maxlength="11" required
							>
						</div>
					</div>
					
					<fieldset class="form-group">
						<div class="row">
							<div class="col-sm-2 col-form-label">
								<b>*성별</b>
							</div>
							<div class="col-sm-7 requirecheck">
								<div class="form-check">
									<input class="form-check-input" type="radio" name="m_gender" id="gender_wm" value="여성">
									<label class="form-check-label" for="gender_wm"> 여성 </label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="m_gender" id="gender_m" value="남성">
									<label class="form-check-label" for="gender_m"> 남성 </label>
								</div>
							</div>
						</div>
					</fieldset>

					<br>
					<h4>
						<i>선택사항</i>
					</h4>
					<br>

					<div class="form-group row">
						<label for="birth" class="col-sm-2 col-form-label">생일</label>
						<div class="col-sm-7">
							<input type="text" name="m_birth" class="form-control" id="birth" minlength="8" maxlength="8"
								placeholder="19980323"
							>
						</div>
					</div>
					<br>

					<div class="form-group row">
						<label for="address" class="col-sm-2 col-form-label">거주지</label> <input type="text"
							id="sample6_postcode" placeholder="우편번호"
						> <input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
						<input type="text" id="sample6_address" placeholder="주소" name="address_without_specific"><br>
						<input type="text" id="sample6_detailAddress" placeholder="상세주소" name="address_with_specific">
						<input type="text" id="sample6_extraAddress" placeholder="참고항목"><br>
					</div>
					<br> <br>


					<button type="submit" class="btn btn-primary">Submit</button>

				</form>
				<br> <br>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	// 비밀번호 일치안하면 안되게 hidden 속성 처리 

	var authkey;
	console.log(authkey);

	$(document).ready(function() {

		$("#password2").keyup(confirmpass);
		$("#emailauth").keyup(confirmMail);

	});

	function scanform() {
		var fixid = $("#fixid").val();
		var fixpwd = $("#fixpwd").val();
		var fixemail = $("#fixemail").val();
		var wm = $("#gender_wm");
		var m = $('#gender_m');
		var tf1 = wm.prop('checked');
		
		var m = $("#gender_m");
		var tf2 = m.prop('checked');
		
		if (fixid == 0) {
			alert("아이디 중복확인 필요");
			return false;
		}
		if (fixpwd == 0) {
			alert("비밀번호 재확인 필요");
			return false;
		}
		if (fixemail == 0) {
			alert("이메일 인증 필요");
			return false;
		}

		if(tf1 == false && tf2 == false){
			alert("성별을 입력해주세요");
			return false;
		}
		
	}

	
	function confirmMail() {
		var emailauth = $("#emailauth").val();
		if (emailauth == authkey) {
			$("#emailMsg").css("display", "block");
			$("#emailMsg").css("color", "green");
			$("#emailMsg").text("인증번호 일치");
			$("#fixemail").val(1);

		} else {
			$("#emailMsg").css("display", "block");
			$("#emailMsg").css("color", "red");
			$("#emailMsg").text("인증번호 불일치");
			$("#fixemail").val(0);
		}
	}

	function authByEmail() {
		var mailvar = $("#emailaddr").val();
		var mailaddr = {
			"mailaddr" : mailvar
		};
		console.log(mailaddr);
		if (mailvar == "") {
			$("#sentmail").css("display", "block");
			$("#sentmail").text("이메일 주소를 입력해주세요");

		} else {
			$.ajax({
				url : "mAuthMail",
				type : "post",
				data : mailaddr,
				dataType : "json",
				success : function(redata) {
					$("#sentmail").css("display", "block");
					$("#sentmail").css("color", "green");
					
					if(redata.msg.includes('success')){
						$("#sentmail").text("인증번호를 입력해주세요!");
						authkey = redata.authkey;
					}
					
				},
				error : function(error) {
					alert(error);
				}
			});
		}
	}

	
	function confirmpass() {

		var pass = $("#password1").val();
		var conpass = $("#password2").val();

		if (pass == conpass) {
			$("#passwordConfirm").css("display", "block");
			$("#passwordConfirm").css("color", "green");
			$("#passwordConfirm").text("비밀번호 일치");
			$("#fixpwd").val(1);
		}
		if (pass != conpass) {
			$("#passwordConfirm").css("display", "block");
			$("#passwordConfirm").css("color", "red");
			$("#passwordConfirm").text("비밀번호 불일치");
			$("#fixpwd").val(0);
		}

	}

	function needprepwd() {

		var pass = $("#password1").val();
		var conpass = $("#password2").val();

		if (pass == "") {
			$("#password1").focus();
		}
	}

	function resetid() {
		console.log("resetid field");
		$("#fixid").val(0);
	}

	function idDuplicationCheck() {
		var enteredId = $('#checkid').val();
		if (enteredId == "") {
			$("#idMsg").css("display", "block");
			$("#idMsg").text("아이디를 입력해주세요");
			$("#checkid").focus();
			return;
		}
		var object = {
			"tempid" : enteredId
		};

		$.ajax({
			url : "mIdDuplicationCheck",
			type : "get",
			data : object,
			success : function(rtdata) {
				if (rtdata == "1") {
					$("#idMsg").css("display", "block");
					$("#idMsg").css("color", "red");
					$("#idMsg").text("이미 존재하는 아이디 입니다");
					$("#checkid").focus();
				}
				if (rtdata == "0") {
					$("#idMsg").css("display", "block");
					$("#idMsg").css("color", "green");
					$("#idMsg").text("사용가능한 아이디 입니다.");
					$("#fixid").val(1);
				}
			},
			error : function(rtdata) {
				alert("error occured");
			}
		});

	}

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