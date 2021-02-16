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
.chip {
	display: inline-block;
	padding: 0 25px;
	height: 100px;
	font-size: 16px;
	line-height: 100px;
	border-radius: 50px;
	background-color: #f1f1f1;
	background-image: linear-gradient(to bottom right, rgba(67, 117, 217, 1),
		rgba(67, 117, 217, 0.5));
	color: white;
	margin-bottom: 40px;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.10);
}

.chip img {
	float: left;
	margin: 0 10px 0 -25px;
	height: 100px;
	width: 100px;
	border-radius: 50%;
}

.beneath {
	position: relative;
}

.ontext {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	font-size: 18px;
}

.gradcolor {
	background-color: rgba(67, 117, 217, 0.5);
	color: white;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.10);
}

.shady {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.19);
	text-align: center;
}

.ctbody {
	padding: 10px;
}
</style>

</head>
<body>

	<header>
		<jsp:include page="header.jsp" />
	</header>
	<main class="container" style="margin-top: 70px; margin-bottom: 200px;">

		<div class="row">
			<div class="col-sm-9">
				<div class="chip">
					<img src="resources/profile/${hostprofile.f_oriname }" alt="Person" width="96" height="96">
					<b>${hostaccount.m_nickname } 님의 게시판 입니다 ! 방문을 환영합니다!</b>
				</div>
			</div>
			<div class="col-sm-2" style="display: none" id="forguest">
				<a class="btn gradcolor" target="popup" role="button"
					onclick="window.open('http://localhost/stepy/mSendMessage?toid=${hostaccount.m_id}&fromid=${member.m_id}', 'popup', 'width=600, height=600'); return false;"
					style="padding: 10px; margin-top: 18px;"
				>쪽지보내기</a>
			</div>
			<div class="col-sm-1 forhostre" style="display: none; margin-right: 30px;" id="removealarm">
				<a class="btn gradcolor" target="popup" role="button" onclick="openreceivebox();removealarm();"
					style="padding: 10px; margin-top: 18px;"
				>쪽지함 <span id="newMsg" class="badge badge-pill"></span></a>
			</div>
			<!-- 
			<div class="col-sm-1 forhostse" style="display: none" id="sendbox">
				<a class="btn gradcolor" target="popup" role="button"
					onclick="opensendbox()"
					style="padding: 10px; margin-top: 18px;"
				>보낸 쪽지함</a>
			</div>
			 -->
		</div>

		<c:if test="${sort == 1}">
			<ul class="nav nav-tabs nav-justified">
				<li class="nav-item active"><a class="nav-link" id="li1"
					href="./mMyLittleBlog?blog_id=${hostaccount.m_id }"
				>작성한 게시글</a></li>
				<li class="nav-item"><a class="nav-link"
					href="./mMyLittleBlog?blog_id=${hostaccount.m_id }&sort=2"
				>댓글 단 게시글</a></li>
			</ul>
		</c:if>
		<c:if test="${sort == 2 }">
			<ul class="nav nav-tabs nav-justified">
				<li class="nav-item"><a class="nav-link" id="li1"
					href="./mMyLittleBlog?blog_id=${hostaccount.m_id}"
				>작성한 게시글</a></li>
				<li class="nav-item active"><a class="nav-link"
					href="./mMyLittleBlog?blog_id=${hostaccount.m_id }&sort=2"
				>댓글 단 게시글</a></li>
			</ul>
		</c:if>

		<c:if test="${sort ==1 }">
			<div class="row text-center center-block" style="margin-top: 50px;">
				<c:if test="${!empty mpList }">
					<c:forEach var="mp" items="${mpList }">
						<div class="card col-sm-4 text-center center-block" style="margin-bottom: 30px;">
							<div class="shady" style="margin: 10px;">
								<c:if test="${!empty mp.con_sysname }">
									<img class="card-img-top" src="resources/uplod/${mp.con_sysname}" alt="Card image cap"
										style="width: 100%; max-height: 360px;"
									>
								</c:if>
								<div class="card-body ctbody">
									<h5 class="card-title" style="color: #4375d9;">제목 : ${mp.p_title }</h5>
									<p class="card-text">${fn:substring(mp.p_contents,0,100)}···</p>
									<a href="./contents?pnum=${mp.p_num}" class="btn btn-primary">자세히 보기</a>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>

				<c:if test="${empty mpList}">
					<p>아직 작성한 글이 없습니다 !</p>
				</c:if>

			</div>
		</c:if>



		<c:if test="${sort ==2 }">
			<div class="row text-center center-block" style="margin-top: 50px;">

				<c:if test="${!empty mrList }">
					<c:forEach var="mr" items="${mrList }">
						<div class="card col-sm-4 text-center center-block" style="margin-bottom: 30px;">
							<div class="shady" style="margin: 10px;">
								<c:if test="${!empty mr.con_sysname }">
									<img class="card-img-top" src="resources/uplod/${mr.con_sysname}" alt="Card image cap"
										style="width: 100%; max-height: 360px;"
									>
								</c:if>
								<div class="card-body ctbody">
									<h5 class="card-title" style="color: #4375d9;">제목 : ${mr.p_title }</h5>
									<p class="card-text">${fn:substring(mr.p_contents,0,100)}···</p>
									<a href="./contents?pnum=${mr.p_num}" class="btn btn-primary">자세히 보기</a>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>


				<c:if test="${empty mrList}">
					<p>아직 작성한 댓글이 없습니다!</p>
				</c:if>

			</div>
		</c:if>

	</main>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>

<script src="resources/js/jquery.serializeObject.js"></script>
<script>
	var host = "${member.m_id}";
	var guest = "${hostaccount.m_id}";

	$(function() {
		console.log(host);
		console.log(guest);
		if (host == guest) {
			$(".forhostre").css("display", "block");
			$(".forhostse").css("display", "block");

		} else {
			$("#forguest").css("display", "block");
		}

		mNewMsgCount(host);
	});

	function removealarm() {

		var m_id = {
			"ms_mid" : host
		};
		
		$.ajax({
			url : "mUploadAfterView",
			type : "get",
			data : m_id,
			success : function(msgCount) {
				mNewMsgCount(host);
			},
			error : function(error) {
			}
		});
	}

	function mNewMsgCount(host) {

		var m_id = {
			"ms_mid" : host
		};

		$.ajax({
			url : "mNewMsgCount",
			type : "get",
			data : m_id,
			dataType : "json",
			success : function(msgCount) {
				var bf = msgCount.ms_bfread;
				var af = msgCount.ms_afread;
				var newmsg = bf - af;
				$("#newMsg").text(newmsg);
			},
			error : function(error) {
			}
		});
	}

	function opensendbox() {
		window.open(
				'http://localhost/stepy/mMeSendOverview?hostid=${member.m_id}',
				'popup', 'width=600, height=600');
	}

	function openreceivebox() {
		window
				.open(
						'http://localhost/stepy/mReceiveOverview?hostid=${member.m_id}',
						'popup', 'width=600, height=600');
	}
</script>
</html>