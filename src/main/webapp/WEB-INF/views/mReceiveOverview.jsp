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
<style>
.spantap {
	font-size: 16px;
	color: white;
	margin-left: 40px;
	margin-right: 40px;
}

.spanbody {
	margin-bottom: 30px;
}
#divhover{
	position:absolute;
	background-color: #ffdf5e;
	z-index:1;
	display:none;
}
#idhover:hover + #divhover{
	display:inline-block;
}
#divhover:hover{
	display:inline-block;
}

</style>
</head>
<body
	style="background-image: linear-gradient(to bottom right, rgba(67, 117, 217, 1), rgba(67, 117, 217, 0.5));"
>
	<main class="container" style="margin-top: 100px;">
		<div class="row spanbody">
			<div class="col-md-12 text-center">
				<a href="./mReceiveOverview?hostid=${member.m_id }" id="now" class="spantap"> <span class="align-middle"
					style="color: black; background-color: #ffdf5e;"
				>받은 메일함</span></a>&nbsp;&nbsp;&nbsp; 
				<a href="./mMeSendOverview?hostid=${member.m_id }" class="spantap">
					<span class="align-middle" style="color: black; background-color: #f5f5f5;">보낸 메일함</span>
				</a>&nbsp;&nbsp;&nbsp; 
				<a href="./mSendMessage?fromid=${member.m_id}&toid=" class="spantap">
					<span class="align-middle" style="color: black; background-color: #f5f5f5;">메일쓰기</span>
				</a>
			</div>
		</div>

		<div class="con-md-12 text-center" style="margin-bottom: 50px;">
			<a href="./mReceiveOverview?hostid=${member.m_id }&pageNum=${pageNum}">
			<span style="float:left; background-color:#ffdf5e; color:gray; display:none;" id="backbtn">뒤로가기
			</span></a>
			<div style="margin-right: 10px; float: right;">
				<select id="select">
					<option value="words">내용</option>
					<option value="sender">발송인 id</option>
				</select> <input type="text" id="fragment" placeholder="검색어를 입력해주세요">
				<button id="searchbtn">검색</button>
				<br> <span id="alarm" style="color: white;"></span>
			</div>
		</div>
		<div class="col-md-12 text-center">
			<table class="table table-hover" id="tabletest">
				<thead style="background-color: rgba(250, 250, 250, 0.9); width:600px;">
					<tr>
						<th scope="col">발신인</th>
						<th scope="col">H</th>
						<th scope="col">내용</th>
						<th scope="col">날짜</th>
					</tr>
				</thead>
				<tbody style="background-color: rgba(250, 250, 250, 0.8); text-align: center; width:600px;" id="msgtbody">
					<c:forEach var="rml" items="${rmList }">
						<tr>
							<th scope="row" width="18%"><a id="idhover"
								href="./mSendMessage?toid=${rml.ms_smid }&fromid=${rml.ms_mid}"
							>${rml.m_nickname }</a><span id="divhover">${rml.ms_smid }</span></th>
							<th width="1%"><a href="./mMyLittleBlog?blog_id=${rml.ms_smid }" target="_blank">
							<span class="glyphicon glyphicon-home">
							</span> </a></th>
							<td width="63%" class=".crop">${rml.ms_contents }</td>
							
							<td width="18%"> <fmt:formatDate
											pattern="yyyy-MM-dd" value="${rml.ms_date }"
										/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="col">
				<div class="paging" id="paging">${paging }</div>
			</div>
		</div>
	</main>

</body>
<script>


$('textrea').keyup(function() {
	var counter = $(this).val().length, current = $('#written');
});

	var btn = document.getElementById("searchbtn");

	btn.addEventListener("click",function() {

						var fork = $("#select").val();
						console.log(fork);
						//words ' sender

						if (fork == "words") {

							var fragment = $("#fragment").val();
							if (fragment == "") {
								$("#alarm").css("display", "none");
								return;
							}

							var contents = {
								"contents" : fragment
							}
							contents.m_id = "${member.m_id}";

							$.ajax({
									url : "mRetrieveByContents",
									type : "post",
									data : contents,
									dataType : "json",
									success : function(result) {
										var nullck = result.searchList;

										if (nullck.length == 0) {
											$("#alarm").css("display",
													"block");
											$("#alarm").text("결과를 찾을 수 없습니다! 다른 글자를 입력해 주세요");
											return;
										}

											$("#alarm").css("display", "none");

											var msgList = result.searchList;
											var print = "";
											for (var i = 0; i < msgList.length; i++) {
												print += '<tr>'
														+ '<th scope="row" width="18%">'
														+ '<a id="idhover" href=\"./mSendMessage?toid='
														+ msgList[i].ms_smid
														+ '&fromid='
														+ msgList[i].ms_mid
														+ '\">'
														+ msgList[i].m_nickname
														+ '</a><span id="divhover">'
														+ msgList[i].ms_smid
														+'</span>'
														+ '</th>'
														+ '<th width="1%"><a href = \"./mMyLittleBlog?blog_id='
														+ msgList[i].ms_smid
														+ '\"'
														+ 'target = "_blank">'
														+ '<span class="glyphicon glyphicon-home">'
														+ '</a></th>'
														+ '<td width="63%">'
														+ msgList[i].ms_contents
														+ '</td>'
														+ '<td width="18%">'
														+ msgList[i].ms_date
														+ '</td>';

											}
											$("#msgtbody").html(print);
											$("#backbtn").css("display","inline-block");
											$("#paging").css("display","none");

										},
										error : function(result) {
											alert("오류");
										}

									});
							return;

						}

						if (fork == "sender") {

							var fragment = $("#fragment").val();
							if (fragment == "") {
								$("#alarm").css("display", "none");
								return;
							}

							var userid = {
								"userid" : fragment
							}
							userid.m_id = "${member.m_id}";

							$
									.ajax({
										url : "mRetrieveByUsername",
										type : "post",
										data : userid,
										dataType : "json",
										success : function(result) {

											var nullck = result.searchList;

											if (nullck.length == 0) {
												$("#alarm").css("display",
														"block");
												$("#alarm").text(
														"일치하는 회원을 찾을 수 없습니다!");
												return;
											}

											$("#alarm").css("display", "none");

											var msgList = result.searchList;
											var print = "";
											for (var i = 0; i < msgList.length; i++) {

												print += '<tr>'
														+ '<th scope="row" width="18%">'
														+ '<a href=\"./mSendMessage?toid='
														+ msgList[i].ms_smid
														+ '&fromid='
														+ msgList[i].ms_mid
														+ '\">'
														+ msgList[i].m_nickname
														+ '</a>'
														+ '</th>'
														+ '<th width="1%"><a href = \"./mMyLittleBlog?blog_id='
														+ msgList[i].ms_smid
														+ '\"'
														+ 'target = "_blank">'
														+ '<span class="glyphicon glyphicon-home">'
														+ '</a></th>'
														+ '<td width="63%">'
														+ msgList[i].ms_contents
														+ '</td>'
														+ '<td width="18%">'
														+ msgList[i].ms_date
														+ '</td>';

											}

											$("#msgtbody").html(print);
											$("#backbtn").css("display","inline-block");
											$("#paging").css("display","none");

										},
										error : function(result) {
											alert("오류");
										}

									});

						}

					});
</script>
</html>