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
<title>이벤트 상세보기</title>
<jsp:include page="aImports.jsp" />
<link href="resources/css/style.css" rel="stylesheet">
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
							<td class="col-md-1 col-xs-2"
								style="background-color: #CCFFFF; text-align: center;">제목</td>
							<td class="col-md-7 col-xs-9">${event.e_title}
						</tr>

						<tr>
							<td class="hidden-xs col-md-1"
								style="background-color: #CCFFFF; text-align: center;">종료일</td>
							<td class="hidden-xs col-md-3"><fmt:formatDate
									pattern="yyyy-MM-dd HH:mm:ss" value="${event.e_date}" /></td>
						</tr>

						<tr>
							<td class="hidden-xs col-md-1" rowspan="2"
								style="text-align: center; background-color: #CCFFFF">내용</td>
							<td class="col-md-7 col-xs-11 " style="height: 450px;">${event.e_contents}
								<br> <c:if test="${!empty fList}">
									<c:forEach var="f" items="${fList}">
										<c:if test="${fn:contains(f.f_sysname, '.jpg')}">
											<img src="resources/upload/${f.f_sysname}" width="250">
										</c:if>
										<c:if test="${fn:contains(f.f_sysname, '.png')}">
											<img src="resources/upload/${f.f_sysname}" width="250">
										</c:if>
										<c:if test="${fn:contains(f.f_sysname, '.gif')}">
											<img src="resources/upload/${f.f_sysname}" width="250">
										</c:if>
									</c:forEach>
								</c:if>
							</td>
						</tr>
					</table>
					<table class="table col-xs-12 col-md-8" style="margin-bottom: 0;">
						<tr>
							<td class="col-md-1 col-xs-3"
								style="background-color: #CCFFFF; text-align: center;">첨부파일</td>
							<td class="col-md-7 col-xs-8" colspan="5">
								<!--  --> <c:if test="${empty fList}">
								첨부된 파일이 없습니다.
							</c:if> <c:if test="${!empty fList}">
									<c:forEach var="file" items="${fList}">
										<a href="./aFileDown?sysName=${file.f_sysname}"> <span
											class="file-title">${file.f_oriname}</span></a>&nbsp;&nbsp;
								</c:forEach>
								</c:if>
							</td>
						</tr>
						<c:if test="${!empty fList}">
							<tr>
								<th style="text-align: center !important;">PREVIEW</th>
								<td colspan="5"><c:forEach var="f" items="${fList}">
										<c:if test="${fn:contains(f.f_sysname, '.jpg')}">
											<img src="resources/upload/${f.f_sysname}" width="100">
										</c:if>
										<c:if test="${fn:contains(f.f_sysname, '.png')}">
											<img src="resources/upload/${f.f_sysname}" width="100">
										</c:if>
										<c:if test="${fn:contains(f.f_sysname, '.gif')}">
											<img src="resources/upload/${f.f_sysname}" width="100">
										</c:if>
									</c:forEach></td>
							</tr>
						</c:if>
					</table>
					<div class="row col-md-8 col-md-offset-2 col-xs-8 col-xs-offset-2"
						style="text-align: center;">
						<button class="btn btn-info" id="puls"
							onclick="location.href='./aEventUpdateFrm?e_num=${event.e_num}'"
							style="margin-top: 30px; text-align: center;">수정</button>
						<button type="button" class="btn btn-info" id="pulss"
							onclick="stopConfirm('${event.e_num}');"
							style="margin-top: 30px; text-align: center;">중지</button>
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
	//이벤트 중지 확인
	function stopConfirm(eNum) {
		console.log("중단할 이벤트의 이벤트 코드 : " + eNum)
		var stopSelect = confirm("이 이벤트를 중지합니다");

		if (stopSelect == true) {
			location.href = "aDeleteEvent?e_num=" + eNum;
		}
	}
</script>
</html>