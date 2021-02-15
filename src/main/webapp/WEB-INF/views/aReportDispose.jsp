<%@page import="com.bob.stepy.dto.EmailDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp" />
<link rel="stylesheet" href="resources/css/aWriteStyle.css">
<link rel="stylesheet" href="resources/css/aReportDetailStyle.css">
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<div class="row">
			<jsp:include page="aSideBar.jsp" />
			<div class="formArea col-sm-10" align="center">
				<form action="aSendMail" method="post">
					<fieldset>
						<legend>발송 대상</legend>
						<div class="mail_tgt">
							수신자 ID <input type="text" value="${report.rp_cnum }" disabled>
							<input type="hidden" name="c_num" value="${report.rp_cnum }">
						</div>
						<div class="mail_tgt">
							수신자 이메일 <input type="hidden" value="${ceo.c_email }"
								name="receiveMail">
							<!--  -->
							<input type="text" value="${ceo.c_email }" disabled>
						</div>
					</fieldset>
					<fieldset>
						<legend>제목</legend>
						<input type="hidden" name="rp_num" value="${report.rp_num }">
						<input type="hidden" name="subject" value="신고 대상자 처리 공지">
						<input type="text" name="subject" value="신고 대상자 처리 공지" disabled>
					</fieldset>
					<fieldset>
						<legend>내용</legend>
						<textarea rows="10" cols="120" name="contents" placeholder="메일 내용"
							required></textarea>
					</fieldset>
					<!--  -->
					<fieldset>
						<legend>첨부파일</legend>
						<label for="file">업로드</label>
						<!-- label이 묶일 대상인 for=id=file, file input -->
						<input type="file" name="files" id="file" multiple>
						<!-- 선택한 파일명을 보여줄 readonly 속성 부여
						(text) input
						이때 등록된 파일은 실제 경로명이 아닌 fakepath를 보여줌-->
						<input type="text" class="upload-name" value="파일선택" readonly>
						<!-- 파일을 올렸는지 체크할 히든 input
						(파일이 있으면 1, 없으면 0으로 JS가 처리) -->
						<input type="hidden" id="filecheck" value="0" name="fileCheck">
					</fieldset>
					<input type="submit" value="메일 전송&회원 추방" class="mailBtn">
					<!--  -->
					<input type="reset" value="메일 리셋" class="mailBtn"> <input
						type="button" value="뒤로가기" class="mailBtn"
						onclick="window.history.back();">
				</form>
			</div>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
	//파일 선택시 상태가 변하는 change 이벤트 발생
	//해당 이벤트에 반응하는 함수 실행
	$("#file").on('change', function() {
		//파일 여러개를 선택하고(input multiple속성)
		//해당 파일들의 이름을 모두 출력하려는 경우
		var files = $("#file")[0].files;
		console.log(files);

		var fileName = "";

		for (var i = 0; i < files.length; i++) {
			fileName += files[i].name + " ";

		}

		console.log(fileName);
		$(".upload-name").val(fileName);

		if (fileName == "") {
			console.log("파일 없음");
			$("#filecheck").val(0);
		} else {
			console.log("파일 있음");
			$("#filecheck").val(1);
		}
	});
<%%>
	
</script>
</html>