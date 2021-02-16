package com.bob.stepy.util;

import lombok.extern.java.Log;

//페이징 처리 클래스
@Log
public class Paging {
	private int maxNum;//전체 글 개수
	private int pageNum; //현재 페이지 번호
	private int listCnt;//페이지당 보여줄 글 개수
	private int pageCnt;
	//페이징 버튼 영역에서 보여질 페이지번호의 개수 1,2,3 or 1,2,3,4,5,6,..,10 등
	private String listName; //목록 페이지의 이름(종류)

	//생성자를 통해 초기 필수 데이터 저장 (컨트롤러,서비스에서 준 설정값들을 세트)
	public Paging (int maxNum, int pageNum, int listCnt, int pageCnt, String listName) {
		this.maxNum=maxNum;
		this.pageNum=pageNum;
		this.listCnt=listCnt;
		this.pageCnt=pageCnt;
		this.listName=listName;
	}

	//페이징 처리
	public String makePaging() {
		//전체 페이지 개수에 따라 경우의 수가 달라짐
		int totalPage = (maxNum % listCnt > 0) ?
				maxNum/listCnt + 1 :
					maxNum/listCnt;
		//현재 페이지가 속해있는 그룹 번호
		int curGroup = (pageNum % pageCnt > 0) ?
				pageNum/pageCnt + 1 :
					pageNum/pageCnt;
		//스트링 버퍼 임포트
		StringBuffer sb = new StringBuffer();
		int start = (curGroup * pageCnt) - (pageCnt-1);
		int end =  (curGroup * pageCnt >= totalPage) ?
				totalPage : curGroup * pageCnt;

		if (start != 1) {
			sb.append("<a class='pno' href='" + listName +
					"?pageNum=" + (start - 1) + "'>");
			sb.append("&nbsp;이전&nbsp;");
			sb.append("</a>");
		}
		for (int i = start; i <= end; i++) {
			if(pageNum != i) {
				sb.append("<a class='pno' href='"+listName+
						"?pageNum="+i+"'>");
				sb.append("&nbsp;"+i+"&nbsp;</a>");
			}
			else {
				sb.append("<font class='pno' style='color:red;'>");
				sb.append("&nbsp;"+i+"&nbsp;</font>");
			}
		}
		if (end != totalPage) {
			sb.append("<a class='pno' href='" + listName +
					"?pageNum=" + (end + 1) + "'>");
			sb.append("&nbsp;다음&nbsp;</a>");
		}
		return sb.toString();
	}

	public void numbers() {
		System.out.println("총 게시글 수 maxNum : "+maxNum+"\n"
				+"현재 페이지 pageNum : "+pageNum+"\n"
				+"한 페이지에서 보여줄 글 수 listCnt : "+listCnt+"\n"
				+"한 페이지 그룹의 페이지 수 pageCnt : "+pageCnt+"\n"
				+"현재 게시판 리스트의 이름 listName : "+listName);
	}



	public String makePageBtnForMulti() {
		numbers();
		log.info("한 페이지에서 보여줄 글 수 listCnt : "+listCnt +"(서비스 클래스에서 정했음)");

		int totalPage = (maxNum % listCnt > 0) ?
				maxNum/listCnt + 1 :
					maxNum/listCnt;

		log.info("총 페이지 수 totalPage : "+totalPage);


		int curGroup = (pageNum % pageCnt > 0) ?
				pageNum/pageCnt + 1 :
					pageNum/pageCnt;

		log.info("현재 페이지 번호 pageNum : "+pageNum);
		log.info("현재 페이지의 소속 그룹 curGroup : "+curGroup);


		StringBuffer sb = new StringBuffer();


		int start = (curGroup * pageCnt) - (pageCnt-1);

		log.info("페이지의 시작 번호 start : "+start);


		int end =  (curGroup * pageCnt >= totalPage) ?
				totalPage : curGroup * pageCnt;


		if (start != 1) {
			sb.append("<a class='pno' href='" + listName
					+"pageNum=" + (start - 1) + "'>");
			sb.append("&nbsp;이전&nbsp;");
			sb.append("</a>");
			//<a class='pno' href="list?pageNum5'>이전</a>와 같음
		}

		for (int i = start; i <= end; i++) {
			if(pageNum != i) {

				sb.append("<a class='pno' href='"+listName+
						"pageNum="+i+"'>");
				sb.append("&nbsp;"+i+"&nbsp;</a>");
				//

			}
			else {
				//pageNum==i, 현재 페이지로 판단, 링크를 걸지 않음
				sb.append("<font class='pno' style='color:red;'>");
				sb.append("&nbsp;"+i+"&nbsp;</font>");
			}

		}

		if (end != totalPage) {
			sb.append("<a class='pno' href='" + listName +
					"pageNum=" + (end + 1) + "'>");
			sb.append("&nbsp;다음&nbsp;</a>");

		}//다음 버튼 처리 if 끝

		//어펜드로 추가되어가면서 쌓인 문자열 조각들을 하나로 합쳐 완성된 문자열로 취급
		return sb.toString();
	}//makePaging 메소드 끝

}//페이징 클래스 끝
