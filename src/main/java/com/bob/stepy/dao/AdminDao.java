package com.bob.stepy.dao;

import java.util.List;
import java.util.Map;

import com.bob.stepy.dto.*;

public interface AdminDao {
	public String loginProc (String id);

	//로그인을 위한 대조용 비밀번호를 DB에서 가져오기 (SELECT - 단일 레코드 중에서도 칼럼 하나)
	public String getPwd(String id);

	//로그인후 사용될 사용자(관리자)의 DTO 정보 가져오기 (SELECT - 단일 레코드)
	public MemberDto getMemberInfo(String id);

	//전체 회원 목록 가져오기 (SELECT-복수 레코드)
	public List<MemberDto> getMemberList(int num);

	//업체 목록 전부 가져오기 (SELECT-복수 레코드)
	public List<CeoDto> getAllCeoList(int num2);
	//업체 목록 (승인 완료) 가져오기 (SELECT-복수 레코드)
	public List<CeoDto> getApproveList(int num3);
	//업체 목록 (승인 대기) 가져오기 (SELECT-복수 레코드)
	public List<CeoDto> getPendingList(int num4);

	//카운트 가져오기 - 각 리스트에 부가적으로 필요함
	public int getMemberCnt();
	public int getAllCeoCnt();
	public int getApproveCeoCnt();
	public int getPendingCeoCnt();

	//업체 회원의 승인 요청 수락하기 (UPDATE-단일 레코드)
	//UPDATE의 성공 여부만 알리면 되므로 리턴은 String으로 간주
	public int permitStore (String c_num);

	//일반 회원, 업체 회원 어드민 권한으로 원격 제거
	//단, 일반 회원의 m_id, 업체 회원의 c_num은 다른 테이블이 외래키를 통해 참조하고 있음
	//딸린 자식 레코드가 있으면 부모 레코드를 지울 수 없어 자식 레코드들도 먼저 지워야 함

	//방법 1.DB에서 직접 자식 레코드를 찾아 DELETE
	//방법 2.DB에서 자식 테이블에 'on delete cascade'제약조건을 걸어 부모 레코드가 지워질 때 연쇄 삭제 처리

	//(방법 2 사용 - 각각 M,C 테이블에서 각각의 자식 테이블이 m_id,c_num을 참조하고 있음)
	//일반 회원 삭제
	public int deleteMember(String m_id);
	//업체 회원 삭제
	public int deleteStore(String c_num);

	//단체 메일 발송용 이메일 목록 칼럼만 조회하기
	public List<String> getMailList_M ();
	public List<String> getMailList_C ();

	//이벤트 리스트 조회하기 (복수 레코드 SELECT)
	//완성형 쿼리를 사용하므로 페이지 번호를 제외한 파라미터 불필요
	//레코드의 데이터 전체가 필요하므로 데이터를 한 곳에 담은 DTO의 리스트로 리턴
	public List<EventDto> getEventList (Integer pageNum);
	public int getEventCnt();

	//해당 이벤트 상세보기 - 단독 SELECT
	//이벤트에 관련된 첨부파일이 있을 수 있으므로 같은 e_num을 근거로 F테이블에서도 SELECT
	public EventDto getEventRecord (Integer e_num);
	public List<FileUpDto> getEventFiles(Integer e_num);
	
	public String getOriName(String sysName);

	//E테이블에 INSERT
	public int InsertEvent(EventDto event);

	public boolean fileInsert(Map<String, String> fmap);
	public int deleteEvent(int e_num);

	public int updateEvent(EventDto event);
	public void deleteFile(int f_num);
	
	//신고된 레코드 조회
	//C만 RP테이블에서 조회, P,R은 P,R테이블에서 직접 조건 조회
	public List<ReportDto> getReportList_C (Integer pageNum);
	public List<PostDto> getReportList_P (Integer pageNum);
	public List<ReplyDto> getReportList_R (Integer pageNum);
	public int getReportCnt();

	//신고글 상세보기에 필요한 DTO 단독 조회
	public ReportDto getReportRecord(Integer rp_num);
	public CeoDto getCeoRecord(String rp_cnum);

	//어드민 권한 - 게시글,댓글 강제 삭제
	public int deletePost(Integer num);
	public int deleteReply(Integer num);

	public void updateReport(int rp_num);

	//건의사항 리스트 가져오기 - 다중 SELECT
	public List<SuggestDto> getSuggestList(Integer num9);

	public SuggestDto getSuggestRecord(int sug_num);

	public void deleteSuggest(int sug_num);







}//DAO 인터페이스 끝
