<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>여행 동행</title>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link href="resources/css/style.css" rel="stylesheet">
<script type="text/javascript">
	$(function() {
		$(".suc").css("display", "none");
		$(".bef").css("display", "none");
		
	

});
</script>
</head>
<body>

	<header>
		<jsp:include page="header.jsp" />
	</header>

	<section>
		<form class="form-horizontal" action="login" method="post">
			<div class="form-group">
				<label for="inputEmail3"
					class="col-sm-offset-1 col-sm-3 control-label"></label>
				<div class="col-sm-4"> 
					<input type="text" class="form-control" id="inputEmail3"
						name="m_id" placeholder="게시글 제목을 입력해주세요">
				</div>
			</div>
		</form>
		<div class="row">
			<div class="col-md-8 col-md-offset-2 col-xs-12">
				<table class="table">
					<c:forEach var="pitem" items="${pList}">
					<tr>
						<td class="active" style="width:40px;">${pitem.pmid}</td>
						<td class="warning" style="width:230px;">
							<a href="contents?pnum=${pitem.pnum}">${pitem.ptitle}</a></td>
						<td class="danger hidden-xs" style="width:50px;">조회수 : ${pitem.pview}</td>	
						<td class="info hidden-xs" style="width:50px;">추천수 : ${pitem.plike}</td>
						<td class="active hidden-xs" style="width:40px;">
						<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${pitem.pdate}"/></td>	
					</tr>
					</c:forEach>				
				</table>
					<div class="row">
						<div class="col-xs-12" style="text-align: center;">${paging}</div>	
					</div>
					<div class="row" style="text-align:center;">
						<button type="button" class="btn btn-info"
							onclick="location.href='./bWriteProc'" style="margin-top:50px;">글쓰기</button>
					</div>
			</div>
		</div>
	</section>

	<footer>
		<jsp:include page="footer.jsp" />
	</footer>

</body>
</html>