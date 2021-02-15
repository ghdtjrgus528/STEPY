<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>STEPY - 관리자 페이지</title>
<jsp:include page="aImports.jsp"/>
<link rel="stylesheet" href="resources/css/aWriteStyle.css">
</head>
<body>
	<!-- header,footer는 공통 양식으로 include 처리 -->
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container">
		<jsp:include page="aSideBar.jsp" />
		<div class="formArea col-sm-10" align=center>
			<form action="aWriteEvent" method="post"
				enctype="multipart/form-data">
				<fieldset>
					<legend>이벤트 제목</legend>
					<input type="text" name="e_title" placeholder="이벤트 제목" required>
				</fieldset>
				<fieldset>
					<legend>이벤트 내용</legend>
					<textarea rows="10" name="e_contents" placeholder="이벤트 내용" required
						id="eContents"></textarea>
				</fieldset>
				<fieldset>
					<legend>이벤트 기간</legend>
					이벤트 종료일 : <input type="datetime-local" name="e_date" id="eDate"
						required>
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
				<input type="submit" value="이벤트 등록" class="eventBtn">
				<!--  -->
				<input type="reset" value="작성 초기화" class="eventBtn">
			</form>
		</div>
	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
	$("#file").on('change', function() {
		//파일 여러개를 선택하고(input multiple속성)
		//해당 파일들의 이름을 모두 출력하려는 경우
		var files = $("#file")[0].files;
		console.log(files);

		var fileName = "";
		//선택한 파일의 파일명 value 가져오기
		for (var i = 0; i < files.length; i++) {
			fileName += files[i].name + " ";
			//선택한 파일이 늘어갈 수록 문자열 fileName도 길어지며 재정의
		}
		//배열의 반복이 끝났으므로 콘솔과 readonly input에 출력
		console.log(fileName);
		$(".upload-name").val(fileName);

		//파일명을 옆의 readonly input에 출력하기
		if (fileName == "") {
			console.log("파일 없음");
			//파일명 "", 파일 등록하지 않은 것으로 판단
			$("#filecheck").val(0);
			//$("#filecheck").value = 0; 같은 기능
		} else {
			console.log("파일 있음");
			//파일명이 있음, 파일을 등록한 것으로 판단
			$("#filecheck").val(1);
			//$("#filecheck").value = 1; 같은 기능
		}
	});

	$("#eDate").on('change', function() {
		console.log(eDate.value);
	});
</script>
</html>