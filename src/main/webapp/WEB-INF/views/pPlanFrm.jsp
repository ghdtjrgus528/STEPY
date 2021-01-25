<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>여행 일정</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
<link href="resources/css/style_hjk.css" rel="stylesheet">
</head>
<body>
<header>
<jsp:include page="header.jsp"/>
</header>
<section>
<div class="container">
	<button class="btn btn-default btn-lg" type="button" onclick="location.href='pPlanList?id=${member.m_id}'">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"> 목록으로&nbsp;</span>
	</button>
	<div class="page-header">
		<h1 class="plan-edit">${plan.t_planname} &nbsp;&nbsp;<small>${plan.t_spot}</small></h1>
		<h3 style="margin-top: 0"><small>${plan.t_stdate} ~ ${plan.t_bkdate}</small></h3>
		<input class="btn btn-default add-party-btn inviteBtn" type="button" value="일행 초대하기 +">
	</div>
	<ul class="nav nav-pills nav-justified">
	  <li role="presentation" class="active"><a href="pPlanFrm?planNum=${curPlan}">일정</a></li>
	  <li role="presentation"><a href="pHouseholdFrm?planNum=${curPlan}">가계부</a></li>
	  <li role="presentation"><a href="pCheckSupFrm?planNum=${curPlan}">체크리스트</a></li>
	</ul>
	<div class="contents-box">
		<div class="dayList">
			<c:set var="daycnt" value="1"/>
			<c:set var="daynum" value="${days}"/>
			<c:forEach begin="1" end="${daynum + 1}">
			<div class="dayList-sub">
				<div class="page-header">
				  <h1>DAY ${daycnt}</h1>
				</div>
			<c:set var="planCnt" value="1"/>		
			<c:forEach var="list" items="${planContentsList}">
				<c:if test="${!empty list.ap_contents && list.ap_day == daycnt}">
					<div class="panel panel-default">
					  <div class="panel-body">
					  <span class="glyphicon glyphicon-map-marker" aria-hidden="true">&nbsp;&nbsp;${list.ap_contents}</span>
					  <div class="dayDelBtn pull-right" title="일정 삭제" onclick="if(confirm('삭제하시겠습니까?')) location.href='delAccompanyPlan?&day=${daycnt}&num=${planCnt}'"><img src="resources/images/remove.png"></div>
					  <div class="dayEditBtn pull-right" title="일정 수정" onclick="location.href='pEditAccompanyPlanFrm?day=${daycnt}&planCnt=${planCnt}'"><img src="resources/images/edit.png"></div>
					  </div>
					</div>
					<c:set var="planCnt" value="${planCnt + 1}"/>
				</c:if>
			</c:forEach>
			<input class="btn btn-default btn-lg btn-block add-day-plan-btn" id="add-day-plan-btn" type="button" value="일정추가 +" 
			onclick="location.href='pStoreSearch?day=${daycnt}&planCnt=${planCnt}'">
			<c:set var="daycnt" value="${daycnt + 1}"/>
						</div>
			</c:forEach>
		</div>
	</div>
	<c:if test="${iCnt != 0}">
		<div class="invite-alert-btn">
			<img src="resources/images/warning.png" width="50" title="새로운 초대가 있습니다">
		</div>
	</c:if>
	<div class="invite-wrap">
	<div class="close-btn">
	<img src="resources/images/close.png" width="15">
	</div>
	<c:forEach var="iList" items="${iList}">
		<c:if test="${iList.i_inviteid == member.m_id}">
			<div class="invite-alert">
				<div class="invite-contents">
					<p><strong>'${iList.i_mid}'</strong> 님의 여행초대</p>
					<p><strong>'${iList.i_planname}'</strong></p>
				</div>
				<div class="invite-select-box">
					<input class="btn btn-default add-party-btn col-sm-5" type="button" value="참여" onclick="location.href='pJoinPlan?code=${iList.i_code}&planNum=${iList.i_plannum}&id=${member.m_id}'">
					<input class="btn btn-default del-party-btn col-sm-offset-2 col-sm-5" type="button" value="거절" onclick="reject(${iList.i_code})">
				</div>
			</div>
		</c:if>
	</c:forEach>	
	</div>	
</div>
</section>
<footer>
<jsp:include page="footer.jsp"/>
</footer>
</body>
<script src="resources/js/jquery.serializeObject.js"></script>
<script type="text/javascript">
$(function(){
	//메시지 출력
	var chk = "${msg}";
	
	if(chk != ""){
		alert(chk);
		location.reload(true);
	}
	
	//일행 초대 버튼 클릭
	$(".inviteBtn").click(function(){
		var id = '${member.m_id}';
		var planNum = ${curPlan};
		var planName = '${plan.t_planname}';
		
		location.href="pInviteMemberFrm?id=" + id + "&planName=" + planName;
	})
	
	//초대 알림창 켜기
	$(".invite-alert-btn").click(function(){
		$(".invite-alert").css("opacity", "1");
		$(".invite-alert").css("visibility", "visible");
		$(".close-btn").css("opacity", "1");
		$(".close-btn").css("visibility", "visible");
	})
	
	//초대 알림창 끄기
	$(".close-btn").click(function(){
		$(".invite-alert").css("opacity", "0");
		$(".invite-alert").css("visibility", "hidden");
		$(".close-btn").css("opacity", "0");
		$(".close-btn").css("visibility", "hidden");
	})
	
});
function reject(code){
	if(confirm("거절 후에는 초대를 다시 확인할 수 없습니다")){
		var obj = {"code": code}
		console.log(obj);
		
		$.ajax({
			url: "pRejectPlan",
			type: "post",
			data: obj,
			dataType: "json",
			success: function(data){
				var str = '<div class="close-btn"><img src="resources/images/close.png" width="15"></div>';
				var iList = data.iList;
				
				for(var i = 0; i < iList.length; i++){
					
					if(iList[i].i_inviteid == '${member.m_id}'){
						str += '<div class="invite-alert" style="opacity: 0, visibility: hidden"><div class="invite-contents">' +
							'<p><strong>' + iList[i].i_mid + '</strong> 님의 여행초대</p>' +
							'<p><strong>' + iList[i].i_planname + '</strong></p></div>' +
							'<div class="invite-select-box"><input class="btn btn-default add-party-btn col-sm-5" type="button" value="참여"' + 
							'onclick="location.href=' +
							"'pJoinPlan?code=" + iList[i].i_code + '&planNum=' + iList[i].i_plannum + "&id=${member.m_id}'" + '">' +
							'<input class="btn btn-default del-party-btn col-sm-offset-2 col-sm-5" type="button" value="거절"' +
							'onclick="reject(' + iList[i].i_code + ')"></div></div>';
					}
				}
				
				$(".invite-wrap").html(str);
				
				//새로운 초대가 없으면 버튼 숨김
				if(!($(".invite-wrap").html().includes("invite-alert"))){
					$(".invite-alert-btn").css("opacity", "0");
					$(".invite-alert-btn").css("visibility", "hidden");
				}
				
				//초대 알림창 끄기
				$(".close-btn").click(function(){
					$(".invite-alert").css("opacity", "0");
					$(".invite-alert").css("visibility", "hidden");
					$(".close-btn").css("opacity", "0");
					$(".close-btn").css("visibility", "hidden");
				})
			},
			error: function(error){
				console.log(error);
				alert("오류가 발생했습니다");
			}
		})
		
	}
}
</script>
</html>