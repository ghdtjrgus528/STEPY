<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>

<!doctype html>
<html lang="en">
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>비밀번호 재설정</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
<script type="text/javascript">
$(function(){	
	$(".suc").css("display", "none");
	$(".bef").css("display", "block");
	
	var chk = "${msg}";
	if(chk != ""){
		alert(chk);
		location.reload(true);
	}
});
</script>

<style type="text/css">
.btn {
	background-color: #4375D9;
	color: white;
	margin-top: 30px;
	width: 100%;
}
.btn-color {
	background-color: #F2b950;
	margin-top: 10px;
}
h6 {
	color: #F25D07;
}
label {
	margin-top: 10px;
}
.input-mt {
	margin-top: 5px;
}
.alert {
	margin-top: 5px;
}
</style>

</head>

<body>
	<header>
		<jsp:include page="stHeader.jsp" />
	</header>
	
	<!-- section -->
	<div class="container center-block" style="width:400px;">
		<form action="./stResetPwdProc" method="post">
			<h3 class="text-left">3. 비밀번호 재설정</h3>
			<h6>*새로운 비밀번호를 입력해주세요.</h6>
			<div class="col-12">
				<label for="inputAddress" class="form-label">새 비밀번호</label><br>
    			<input type="password" class="form-control" id="c_pwd" name="c_pwd" required placeholder="새 비밀번호">
    			<input type="password" class="form-control input-mt" id="c_pwd_check" required placeholder="새 비밀번호 확인">
    			<div class="alert alert-success" id="alert-success">비밀번호가 일치합니다.</div>
    			<div class="alert alert-danger" id="alert-danger">비밀번호가 틀립니다.</div>    			
			</div>
			<div class="col-12">
  				<h6 class="pwdComp text-right"></h6>
  			</div> 	
						
			<button type="submit" class="btn">다음</button>
			<button type="button" class="btn btn-color" onclick="location.href='./stHome'">돌아가기</button>				
		</form>
	</div>
		
	<footer>
		<jsp:include page="stFooter.jsp" />
	</footer>
</body>
<script type="text/javascript">
$(function(){
	$("#alert-success").hide();
	$("#alert-danger").hide();
	$("input").keyup(function(){
		var pwd1 = $("#c_pwd").val();
		var pwd2 = $("#c_pwd_check").val();
		if(pwd1 != "" || pwd2 != ""){
			if(pwd1 == pwd2){
				$("#alert-success").show();
				$("#alert-danger").hide();
				$("submit").removeAttr("disabled");
			} else {
				$("#alert-success").hide();
				$("#alert-danger").show();
				$("#submit").attr("disabled", "disabled");
			}
		}
	});	
});
</script>
</html>