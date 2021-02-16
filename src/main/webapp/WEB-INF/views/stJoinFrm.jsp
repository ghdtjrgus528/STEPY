<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en">
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>STEPY 사장님 회원가입</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

<style type="text/css">
.btn {
	background-color: #F2b950;
	color: white;
	margin-top: 10px;
	width: 100%;
}
.storemargin {
	margin-top: 50px;
}
label {
	margin-top: 10px;
}
.btn-mb {
	margin-bottom: 50px;
}
.btn-color {
	background-color: #4375D9;
}
.btn-size {
	width: 30%;
	margin-top: 0;
	float: right;
}
.input-size {
	width: 68%;
	display: inline;
}
.alert {
	margin-top: 5px;
}
</style>
<script type="text/javascript">
$(function(){	
	$(".suc").css("display", "none");
	$(".bef").css("display", "block");	
	}
);
</script>

</head>
<body>
	<header>
		<jsp:include page="stHeader.jsp" />
	</header>
	
	<!-- section -->
	<div class="container center-block" style="width:400px;">
	<form class="row g-3" name="stJoinFrm" action="./stJoinProc" method="post" enctype="multipart/form-data">
		<h3 class="text-left">사업주 정보</h3>
		<div class="col-12">
    		<label for="inputAddress" class="form-label">사업자번호</label><br>
    		<input type="text" class="form-control input-size" id="c_num" name="c_num" required autofocus placeholder="예) 1234567890">
  			<button type="button" class="btn btn-size" onclick="stIdCheck()">중복확인</button>
  		</div>
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">비밀번호</label>
    		<input type="password" class="form-control" name="c_pwd" id="c_pwd" required placeholder="비밀번호 입력">
    		<input type="password" class="form-control" id="c_pwd_check" required placeholder="비밀번호 재입력">
    		<div class="alert alert-success" id="alert-success">비밀번호가 일치합니다.</div>
    		<div class="alert alert-danger" id="alert-danger">비밀번호가 틀립니다.</div>
  		</div>
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">사업주명</label>
    		<input type="text" class="form-control" name="c_name" required>
  		</div>
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">사업주 연락처</label>
    		<input type="text" class="form-control" name="c_phone" required placeholder="예) 01012345678">
  		</div>
  		<div class="col-12">  			
    		<label for="inputAddress" class="form-label">이메일</label><br> 		   		
    		<input type="text" class="form-control" id="c_email" name="c_email" required>
    	</div>
    	<div class="col-12">
  			<label for="file">사업자등록증 업로드</label>
  			<input type="file" name="files" id="file" multiple required>
  			<input class="hidden upload-name" value="선택된 파일이 없습니다" readonly>
  			<input type="hidden" id="filecheck" value="0" name="fileCheck">
		</div>	
    	
    	<h3 class="text-left storemargin">가게 정보</h3>  		
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">가게 이름</label>
    		<input type="text" class="form-control" name="s_name" required>
  		</div>
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">가게 연락처</label>
    		<input type="text" class="form-control" name="s_phone" required placeholder="예) 01012345678">
  		</div>  
  		
  		<div class="form-group"> 
  			<label for="inputAddress" class="form-label">가게 주소</label><br>                
			<input class="form-control" style="width: 68%; display: inline;" placeholder="우편번호" name="s_addr1" id="s_addr1" type="text" readonly="readonly" >
   			<button type="button" class="btn btn-size" onclick="execPostCode();"><i class="fa fa-search"></i>우편번호 찾기</button>                               
		</div>
		<div class="form-group">
    		<input class="form-control" style="top: 5px;" placeholder="도로명 주소" name="s_addr2" id="s_addr2" type="text" readonly="readonly" />
		</div>
		<div class="form-group">
    		<input class="form-control" placeholder="상세주소" name="s_addr3" id="s_addr3" type="text" required />
		</div> 				
  		
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">가게 설명</label>
    		<textarea class="form-control" rows="3" name="s_summary" placeholder="1000자 이내"></textarea>
  		</div>
  		<div class="col-12">
    		<label for="inputAddress" class="form-label">카테고리</label>
    		<select class="form-control" name="s_category" required>
    			<option>숙박</option>
    			<option>음식점</option>
    			<option>지역시설</option>
    		</select>
  		</div>
  		
  		<div class="col-12">
   			<button type="submit" class="btn btn-mb btn-color">입점 신청 완료</button>
 		</div>	
	</form>
	</div>
	
	
	<footer>
		<jsp:include page="stFooter.jsp" />
	</footer>
</body>

<script type="text/javascript">
function stIdCheck(){
	var id = $('#c_num').val();
	if(id == ""){
		alert("사업자 번호를 입력하세요.")
		$('#c_num').focus();
		return;
	}
	var ckObj = {"c_num" : id};
	console.log(ckObj);
	
	$.ajax({
		url: "stIdCheck",
		type: "get",
		data: ckObj,
		success: function(data){
			if(data == "success"){
				alert('사용 가능합니다.');
			} else {
				alert('사용할 수 없는 사업자번호입니다.');
				$('#c_num').val('');
				$('#c_num').focus();
			}
		},
		error: function(error){
			console.log(error);
		}
	});
	
}

function execPostCode() {
    new daum.Postcode({
        oncomplete: function(data) {
           // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

           // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
           // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
           var fullRoadAddr = data.roadAddress; // 도로명 주소 변수
           var extraRoadAddr = ''; // 도로명 조합형 주소 변수

           // 법정동명이 있을 경우 추가한다. (법정리는 제외)
           // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
           if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
               extraRoadAddr += data.bname;
           }
           // 건물명이 있고, 공동주택일 경우 추가한다.
           if(data.buildingName !== '' && data.apartment === 'Y'){
              extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
           }
           // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
           if(extraRoadAddr !== ''){
               extraRoadAddr = ' (' + extraRoadAddr + ')';
           }
           // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
           if(fullRoadAddr !== ''){
               fullRoadAddr += extraRoadAddr;
           }

           // 우편번호와 주소 정보를 해당 필드에 넣는다.
           console.log(data.zonecode);
           console.log(fullRoadAddr);
           
           
           $("[name=s_addr1]").val(data.zonecode);
           $("[name=s_addr2]").val(fullRoadAddr);
           
           /* document.getElementById('signUpUserPostNo').value = data.zonecode; //5자리 새우편번호 사용
           document.getElementById('signUpUserCompanyAddress').value = fullRoadAddr;
           document.getElementById('signUpUserCompanyAddressDetail').value = data.jibunAddress; */
       }
    }).open();
}

$("#file").on('change', function(){
	var files = $("#file")[0].files;
	console.log(files);
	
	var fileName = "";
	
	for(var i = 0; i < files.length; i++){
		fileName += files[i].name + " ";
	}
	console.log(fileName);
	
	$(".upload-name").val(fileName);
	
	if(fileName == ""){
		console.log("empty");
		$("#filecheck").val(0);
		$(".upload-name").val("선택된 파일 없음");
	} else {
		console.log("not empty");
		$("#filecheck").val(1);
	}
});

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