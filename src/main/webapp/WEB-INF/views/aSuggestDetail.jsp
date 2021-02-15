<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>건의사항 상세보기</title>
<jsp:include page="aImports.jsp" />
<link href="resources/css/style.css" rel="stylesheet">
<link href="resources/css/aDetailStyle.css" rel="stylesheet">
</head>
<body>
	<header>
		<jsp:include page="header.jsp" />
	</header>

	<section>
		<div class="container">
			<div class="row">
				<jsp:include page="aSideBar.jsp" />
				<div class="col-md-8 col-md-offset-2 col-xs-12"
					style="border: 2px solid skyblue; padding-top: 20px; padding-bottom: 20px;">
					<table class="table col-xs-12 col-md-8" style="margin-bottom: 0;">
						<tr>
							<td class="col-md-1 col-xs-2 td-left"
								style="background-color: #CCFFFF; text-align: center;">제목</td>
							<td class="col-md-7 col-xs-9">${sug.sug_title}
						</tr>


						<tr>
							<td class="hidden-xs col-md-1 col-xs-2 td-left"
								style="background-color: #CCFFFF; text-align: center;">게시일</td>
							<td class="hidden-xs col-md-3"><fmt:formatDate
									pattern="yyyy-MM-dd hh:mm" value="${sug.sug_date}" /></td>
						</tr>

						<tr>
							<td class="hidden-xs col-md-1 td-left" rowspan="2"
								style="text-align: center; background-color: #CCFFFF">내용</td>
							<td class="col-md-7 col-xs-11 " style="height: 450px;">${sug.sug_contents}
							</td>
						</tr>
					</table>
					<div class="row col-md-8 col-md-offset-2 col-xs-8 col-xs-offset-2"
						style="text-align: center;">
						<button type="button" class="btn btn-info" id="pulss"
							onclick="deleteConfirm('${sug.sug_num}');"
							style="margin-top: 30px; text-align: center;">강제 삭제</button>
						<button class="btn btn-info"
							style="margin-top: 30px; text-align: center;"
							onclick="window.history.back();">뒤로가기</button>
					</div>

				</div>
			</div>
		</div>
	</section>

	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
	//건의 처리후 강제삭제
	function deleteConfirm(sug_num) {
		var con = confirm("이 건의글을 삭제합니다 | " + sug_num);
		if (con) {
			location.href = "aDeleteSuggest?sug_num=" + sug_num;
		}
	}
</script>
</html>