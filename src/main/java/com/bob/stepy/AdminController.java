package com.bob.stepy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dto.CeoDto;
import com.bob.stepy.dto.EmailDto;
import com.bob.stepy.dto.MemberDto;
import com.bob.stepy.dto.ReportDto;
import com.bob.stepy.service.AdminService;

//'컨트롤러'임을 인지, 자동실행 하도록 어노테이션 처리
@Controller
public class AdminController {
	@Autowired
	private AdminService aServ;

	//모델 데이터,뷰네임을 동시에 기억
	private ModelAndView mv;

	//어드민 로그인 페이지로 이동
	@GetMapping("aLoginFrm")
	public String adminLogin() {
		System.out.println("어드민 로그인 시도");
		return "aLoginFrm";
	}

	//로그인 처리
	@PostMapping("aHome")
	public String aLogin(MemberDto member,RedirectAttributes rttr) {
		String view = null;
		view = aServ.loginProc(member, rttr);
		System.out.println("결정된 view : "+view);
		return view;
	}

	//사이드바 전용 홈 복귀
	@GetMapping("aHome2")
	public String aHome2() {
		return "aHome";
	}

	//전체 회원 페이지로 이동
	@GetMapping("aMemberList")
	public ModelAndView allMemberList(Integer pageNum) {
		System.out.println("전체 회원 리스트 페이지로 이동");
		//파라미터된 pageNum이 있으면 해당 페이지를, 없다면 미리 설정해둔 1 페이지로 간주
		//리턴되는 모델뷰에는 모델=회원 리스트mList+뷰=리스트 페이지를 동시에 담고 있음
		mv = aServ.listSet(pageNum, 1);
		return mv;
	}

	//전체 업체 회원 리스트 페이지로 이동
	@GetMapping("aCeoList")
	public ModelAndView ceoList(Integer pageNum) {
		System.out.println("전체 업체 회원 리스트 페이지로 이동");
		mv = aServ.listSet(pageNum, 2);
		return mv;
	}

	//승인 완료 업소 리스트 페이지로 이동
	@GetMapping("aAuthList")
	public ModelAndView authStoreList(Integer pageNum) {
		System.out.println("승인 완료 업체 회원 리스트 페이지로 이동");
		mv = aServ.listSet(pageNum, 3);
		return mv;
	}

	//승인 대기 업소 리스트 페이지로 이동
	@GetMapping("aPendingList")
	public ModelAndView pendingMemberList(Integer pageNum) {
		System.out.println("승인 대기 업체 회원 리스트 페이지로 이동");
		mv = aServ.listSet(pageNum, 4);
		return mv;
	}

	//승인 완료로 업데이트하기 (UPDATE - 단일 레코드)
	@GetMapping("aPermitStore")
	public String permitStore (String c_num) {
		String view = null;
		view = aServ.aPertmitStore(c_num);
		return view;
	}

	//선택한 회원의 데이터를 삭제하기 (일반) (DELETE - 단일 레코드)
	@GetMapping("aDeleteMember")
	public String deleteMember (String m_id) {
		String view = aServ.deleteSwitch(m_id, 1);
		return view;
	}

	//선택한 회원의 데이터를 삭제하기 (업체) (DELETE - 단일 레코드)
	@GetMapping("aDeleteStore")
	public String deleteStore (String c_num) {
		String view = aServ.deleteSwitch(c_num, 2);
		return view;
	}
	//회원 관리 구역 끝//

	//단체 메일 선택 페이지로 이동
	@GetMapping("aGroupMailFrm")
	public String aGroupMailFrm() {
		System.out.println("단체 메일 선택 페이지로 이동");
		return "aGroupMailFrm";
	}

	//일반 회원 전체 메일 작성으로 이동
	@GetMapping("aGroupMail_M")
	public String aGroupMail_M() {
		System.out.println("단체 메일 작성_M");
		return "aGroupMailFrm_M";		
	}

	//업체 회원 전체 메일 작성으로 이동
	@GetMapping("aGroupMail_C")
	public String aGroupMail_C() {
		System.out.println("단체 메일 작성_C");
		return "aGroupMailFrm_C";		
	}

	//일반 회원,업체 회원 메일 발송 메소드 - 관련 API 사용
	//일반 회원 (발송 메소드+스위치 1)
	@PostMapping("aSendMemberMail")
	public String aSendMemberMail
	(EmailDto email, RedirectAttributes rttr, MultipartHttpServletRequest multi)
			throws Exception {
		System.out.println("메일 전송 서비스 실행");
		String view = aServ.mailSend(email, 1,rttr, multi);
		return view;
	}

	//업체 회원 (발송 메소드+스위치 2)
	@PostMapping("aSendStoreMail")
	public String aSendStoreMail
	(EmailDto email, RedirectAttributes rttr, MultipartHttpServletRequest multi)
			throws Exception {
		System.out.println("메일 전송 서비스 실행");
		String view = aServ.mailSend(email, 2,rttr,multi);
		return view;
	}
	//회원 관리 구역 끝//

	//이벤트 관리 페이지로 이동
	@GetMapping("aEvent")
	public String aEvent() {
		System.out.println("이벤트 관리 페이지로 이동");
		return "aEvent";
	}

	//이벤트 리스트 보기 - 복수 레코드 SELECT 필요
	@GetMapping("aEventList")
	public ModelAndView aEventList(Integer pageNum) {
		mv = new ModelAndView();

		System.out.println("이벤트 리스트 페이지로 이동");

		mv = aServ.listSet(pageNum, 5);

		return mv;
	}

	//이벤트 추가 페이지로 이동
	@GetMapping("aEventWriteFrm")
	public String aEventWriteFrm() {
		System.out.println("이벤트 작성 페이지로 이동");
		return "aEventWriteFrm";
	}

	//이벤트 추가 INSERT
	@PostMapping("aWriteEvent")
	public String aWriteEvent
	(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		System.out.println("이벤트 추가 실행");

		String view = aServ.addEvent(multi,rttr);
		return view;
	}

	//이벤트 삭제 DELETE
	@GetMapping("aDeleteEvent")
	public String aDeleteEvent(int e_num) {
		System.out.println("이벤트 삭제 실행");
		String view =  aServ.deleteEvent(e_num);

		System.out.println("이벤트 삭제 완료");
		return view;
	}

	//이벤트 상세보기 SELECT
	@GetMapping("aEventDetail")
	public ModelAndView aEventDetail (int e_num) {
		mv = new ModelAndView();
		mv = aServ.eventDetail(e_num,1);
		return mv;
	}

	//파일 다운로드 처리
	@GetMapping("aFileDown")
	public void fileDown(String sysName, HttpServletRequest requst,
			HttpServletResponse response) {
		aServ.fileDown(sysName, requst, response);
	}

	//이벤트 수정 Frm으로 이동
	@GetMapping("aEventUpdateFrm")
	public ModelAndView aEventUpdateFrm (int e_num) {
		mv = new ModelAndView();
		mv = aServ.eventDetail(e_num,2);
		return mv;
	}

	//이벤트 수정도중 파일 삭제 (JSON, 페이지 변화없이 처리)
	@PostMapping(value = "aDeleteFile",
			produces = "application/json; charset=utf-8")
	@ResponseBody
	public void delFile (int f_num) {
		aServ.deletePic(f_num);
	}

	//이벤트 수정 UPDATE
	@PostMapping("aUpdateEvent")
	public String aUpdateEvent (MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		String view = aServ.updateEvent(multi,rttr);
		return view;
	}
	//이벤트 관리 구역 끝//

	//신고 관리 페이지로 이동
	@GetMapping("aReport")
	public String aReport() {
		System.out.println("신고 관리 페이지로 이동");
		return "aReport";
	}

	//RP테이블 리스트 가져오기 SELECT
	@GetMapping("aReportStoreList")
	public ModelAndView aReportStoreList(Integer pageNum) {
		mv = new ModelAndView();
		System.out.println("신고 업체 페이지로 이동");
		mv = aServ.listSet(pageNum, 6);
		return mv;
	}

	//P테이블에서 신고된 레코드만 가져오기 SELECT
	@GetMapping("aReportPostList")
	public ModelAndView aReportPostList(Integer pageNum) {
		mv = new ModelAndView();
		System.out.println("신고 게시글 페이지로 이동");
		mv = aServ.listSet(pageNum, 7);
		return mv;
	}

	//R테이블에서 신고된 레코드만 가져오기 SELECT
	@GetMapping("aReportReplyList")
	public ModelAndView aReportReplyList(Integer pageNum) {
		mv = new ModelAndView();
		System.out.println("신고 댓글 페이지로 이동");

		mv = aServ.listSet(pageNum, 8);
		return mv;
	}

	//신고글(RP테이블) 상세보기
	@GetMapping("aReportDetail")
	public ModelAndView aReportDetail (int rp_num) {
		System.out.println("(컨트롤러) 파라미터된 rp_num : " +rp_num);
		mv = new ModelAndView();
		mv = aServ.reportDetail(rp_num);
		return mv;
	}

	//해당 신고글 처리
	@GetMapping("aReportDispose")
	public ModelAndView aReportDispose (int rp_num) {
		mv = aServ.reportDetail(rp_num);
		//같은 SELECT대상이지만 서비스에서 뷰네임을 정했으므로 처리 페이지로 뷰네임 재설정
		mv.setViewName("aReportDispose");
		return mv;
	}

	//신고 처리 메일 발송+처리 완료로 RP 레코드 UPDATE
	@PostMapping("aSendMail")
	public String aSendMail
	(EmailDto email, RedirectAttributes rttr, CeoDto ceo, ReportDto report, MultipartHttpServletRequest multi)
			throws Exception {
		System.out.println("(신고 처리) 메일 전송 서비스 실행");
		System.out.println(email);
		String c_num = ceo.getC_num();

		String view = aServ.mailSend(email, 3, rttr,multi);
		aBlockStore(c_num);		
		aServ.aReportFinished(report.getRp_num());

		return view;
	}

	//신고 처리후 업체 회원 처리 (DELETE)
	public int aBlockStore(String c_num) {
		int res = 0;
		String resStr = aServ.deleteSwitch(c_num, 2);
		if (resStr != "") {
			res = 1;
		}

		return res;
	}

	//신고 처리후 게시글 처리 (DELETE)
	@GetMapping("aBlockPost")
	public String aBlockPost(Integer p_num, RedirectAttributes rttr) {
		String view = null;
		view = aServ.deletePandR(p_num, 1,rttr);
		return view; 
	}

	//신고 처리후 댓글 처리 (DELETE)
	@GetMapping("aBlockReply")
	public String aBlockReply(Integer r_num, RedirectAttributes rttr) {
		String view = null;
		view = aServ.deletePandR(r_num, 2,rttr);
		return view; 
	}
	//신고 관리 구역 끝//

	//건의사항 리스트 보기 SELECT
	@GetMapping("aSuggestList")
	public ModelAndView aSuggest(Integer pageNum) {
		mv = new ModelAndView();
		mv = aServ.listSet(pageNum, 9);
		return mv;
	}

	//건의글 상세보기 SELECT
	@GetMapping("aSuggestDetail")
	public ModelAndView aSuggestDetail(int sug_num) {
		mv = new ModelAndView();
		mv = aServ.suggestDetail(sug_num);
		return mv;
	}

	//불필요한 글, 강제 삭제 처리 DELETE
	@GetMapping("aDeleteSuggest")
	public String aDeleteSuggest (int sug_num) {
		aServ.deleteSuggest(sug_num);
		return "redirect:aSuggestList";
	}
	//건의사항 구역 끝

}//컨트롤러 끝
