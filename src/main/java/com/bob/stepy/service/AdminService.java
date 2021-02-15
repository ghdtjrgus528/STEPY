package com.bob.stepy.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dao.AdminDao;
import com.bob.stepy.dto.*;
import com.bob.stepy.util.*;

//서비스 클래스임을 인지하도록 어노테이션 처리
@Service
public class AdminService {
	//DAO 임포트+와이어드
	@Autowired
	private AdminDao aDao;

	//특정 데이터들이 담길 모델뷰 (와이어드없이 각 기능마다 new로 빈공간으로 사용)
	private ModelAndView mv;

	//세션 객체+와이어드
	@Autowired
	private HttpSession session;

	//어드민 로그인 처리
	public String loginProc(MemberDto member, RedirectAttributes rttr) {
		//목적지가 담길 String view
		String view =null;

		String id = member.getM_id();
		String rawPw = member.getM_pwd();

		//파라미터받은 DTO내의 id를 기반으로 DB에서 SELECT로 비밀번호 가져오기
		//암호화된 비밀번호화 입력한 비밀번호의 비교 처리를 위한
		//인코더 생성
		BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();

		//DB에서 가져오는 pw (암호화 상태)
		String pw = aDao.getPwd(id);

		if (pw != null) {
			//입력한 날것 상태의 값 - 암호화 상태의 암호값 대조 명령어
			if (pwdEncode.matches(rawPw, pw)) {

				//단, 어드민 전용 로그인이므로 가져온(입력한) id까지 미리 등록한 (DB)실제 어드민 ID와 같아야 진행
				if(id.equals("admin")) {
					member = aDao.getMemberInfo(member.getM_id());

					//회원정보는 다른 페이지들에서도 돌려 쓰므로 세션에 DTO째로 등록
					session.setAttribute("member", member);
					view = "aHome";
				}
				//id가 admin이 아닌 경우 어드민이 아니라고 판단, 권한 없음 경고
				else {
					view = "redirect:aLoginFrm";
					rttr.addFlashAttribute("msg", "접속 권한이 없습니다");
				}
			}//ID도 PW도 맞는 경우의 if 끝

			//ID는 맞았으나 비밀번호를 틀린 경우
			else {
				//로그인 페이지로 돌아감, 보여줄 메시지를 리다이렉트의 플래시 어트리뷰트에 등록
				view ="redirect:aLoginFrm";
				rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다");
			}//ID는 맞고 PW는 틀린 경우의 else 끝
		}//ID는 맞는 경우의 IF 끝

		//아이디부터 DB에 없어 아예 없는 계정으로 판단
		else {
			//로그인 페이지로 돌아감, 보여줄 메시지를 리다이렉트의 플래시 어트리뷰트에 등록
			view ="redirect:aLoginFrm";
			rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다");
		}
		return view;
	}//어드민 전용 로그인 Proc 끝

	private String getPaging(int num, int listSelect) {
		//전체 글 개수 구하기(DB에서의 게시글 레코드 수량과 같음 - SELECT)
		int maxNum = 0;
		String ttl = null;

		switch(listSelect) {
		case 1://일반회원 카운트
			maxNum = aDao.getMemberCnt();
			ttl ="일반 회원 리스트";
			break;
		case 2://업체회원 카운트 - 전체
			maxNum = aDao.getAllCeoCnt();
			ttl ="업체 회원 리스트 - 전체";
			break;
		case 3://업체회원 카운트 - 완료
			maxNum = aDao.getApproveCeoCnt();
			ttl ="업체 회원 리스트 - 승인 완료";
			break;
		case 4://업체회원 카운트 - 대기
			maxNum = aDao.getPendingCeoCnt();
			ttl ="업체 회원 리스트 - 승인 대기";
			break;
		case 6://신고 리스트 카운트 - 회원
			maxNum = aDao.getEventCnt();
			ttl ="신고 리스트 - 회원";
			break;
		case 7://신고 리스트 카운트 - 게시글
			maxNum = aDao.getEventCnt();
			ttl ="신고 리스트 - 게시글";
			break;
		case 8://신고 리스트 카운트 - 댓글
			maxNum = aDao.getEventCnt();
			ttl ="신고 리스트 - 댓글";
			break;
		}

		//설정값 각인
		int pageCnt = 5;//한 페이지 그룹당 페이지 수
		int listCnt = 20;//한 페이지당 레코드 수
		String listName = ttl;//게시판 이름

		//결정된 설정값들을 Paging 생성자에 파라미터해 값 세트
		Paging paging = new Paging
				(maxNum, num, listCnt,
						pageCnt, listName);

		//세트된 값을 바탕으로 페이징 메소드 실행
		//리턴받은 String pagingHtml은 태그를 포함한 리스트 SELECT결과를 전부 합친 문장
		String pagingHtml = paging.makePaing();

		return pagingHtml;
	}//getPaging 메소드 끝

	//복수 레코드 리스트 보기 통합 메소드 (스위치 사용)
	public ModelAndView listSet (Integer pageNum, Integer listSelect) {
		mv = new ModelAndView();

		switch (listSelect) {
		case 1://일반회원 전체보기
			//빈 모델뷰를 새로 만들어내 데이터 추가 대기 (데이터를 넣을 때 까지는 속이 빈 null상태)
			mv= new ModelAndView();

			//조건식 (식)? T일 때:F일 때
			//들어온 pageNum이 null이 맞으면 T에 해당하는 1을
			//숫자가 들어온 것이 있다면 F에 해당하는 자기 자신의 숫자를 대입
			int num = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num);

			//DAO에서 SELECT처리후 결과물 레코드들을 리스트 mList에 누적해 리스트화
			List<MemberDto> mList = aDao.getMemberList(num);

			//정리된 리스트 mList를 모델뷰에 똑같은 이름을 재사용해 통째로 등록
			//모델은 리스트와 같은 객체도 기억 가능
			mv.addObject("mList", mList);

			//페이징 처리 - 오브젝트 paging에
			//getPaging 메소드의 결과물 문자열들 삽입
			//이 paging은 리스트 페이지에서
			//EL {paging}을 통해 페이징 버튼들로 출력됨
			mv.addObject("paging", getPaging(num,1));

			//세션에 현재 페이지 번호(1,2,3,...) 기억시킴
			session.setAttribute("pageNum", num);

			//목적지 페이지를 모델뷰에 기억시킴
			mv.setViewName("aMemberList");

			break;
		case 2://승인 완료,대기 여부 불문, 업체 회원 전부 보기
			//빈 모델뷰를 새로 만들어내 데이터 추가 대기 (데이터를 넣을 때 까지는 속이 빈 null상태)
			mv= new ModelAndView();

			//조건식 (식)? T일 때:F일 때
			//들어온 pageNum이 null이 맞으면 T에 해당하는 1을
			//숫자가 들어온 것이 있다면 F에 해당하는 자기 자신의 숫자를 대입
			int num2 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num2);

			List<CeoDto> allCeoList = aDao.getAllCeoList(num2);

			mv.addObject("ceoList", allCeoList);

			//페이징 처리 - 오브젝트 paging에
			//getPaging 메소드의 결과물 문자열들 삽입
			//이 paging은 리스트 페이지에서
			//EL {paging}을 통해 페이징 버튼들로 출력됨
			mv.addObject("paging", getPaging(num2,2));

			//세션에 현재 페이지 번호(1,2,3,...) 기억시킴
			session.setAttribute("pageNum", num2);

			//목적지 페이지를 모델뷰에 기억시킴
			mv.setViewName("aAllCeoList");
			break;
		case 3://승인완료 업체보기
			//빈 모델뷰를 새로 만들어내 데이터 추가 대기 (데이터를 넣을 때 까지는 속이 빈 null상태)
			mv= new ModelAndView();

			//조건식 (식)? T일 때:F일 때
			//들어온 pageNum이 null이 맞으면 T에 해당하는 1을
			//숫자가 들어온 것이 있다면 F에 해당하는 자기 자신의 숫자를 대입
			int num3 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num3);

			List<CeoDto> approvedList = aDao.getApproveList(num3);

			mv.addObject("ceoList", approvedList);

			//페이징 처리 - 오브젝트 paging에
			//getPaging 메소드의 결과물 문자열들 삽입
			//이 paging은 리스트 페이지에서
			//EL {paging}을 통해 페이징 버튼들로 출력됨
			mv.addObject("paging", getPaging(num3,3));

			//세션에 현재 페이지 번호(1,num3,3,...) 기억시킴
			session.setAttribute("pageNum", num3);

			//목적지 페이지를 모델뷰에 기억시킴
			mv.setViewName("aAuthList");

			break;
		case 4://승인대기 업체보기
			mv= new ModelAndView();

			int num4 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num4);

			List<CeoDto> pendingList = aDao.getPendingList(num4);

			mv.addObject("ceoList", pendingList);

			mv.addObject("paging", getPaging(num4,4));

			session.setAttribute("pageNum", num4);

			mv.setViewName("aPendingList");

			break;

		case 5://이벤트 리스트 보기
			mv= new ModelAndView();

			int num5 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num5);

			List<EventDto> eventList = aDao.getEventList(num5);

			mv.addObject("eList", eventList);

			mv.addObject("paging", getPaging(num5,5));

			session.setAttribute("pageNum", num5);

			mv.setViewName("aEventList");
			break;
		case 6 ://신고된 업체 리스트 보기
			mv= new ModelAndView();

			int num6 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num6);

			List<ReportDto> reportList1 = aDao.getReportList_C(num6);

			mv.addObject("rpList", reportList1);

			mv.addObject("paging", getPaging(num6,6));

			session.setAttribute("pageNum", num6);

			mv.setViewName("aReportStoreList");
			break;
		case 7 ://신고된 게시글 리스트 보기
			mv= new ModelAndView();

			int num7 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num7);

			List<PostDto> reportList2 = aDao.getReportList_P(num7);

			mv.addObject("pList", reportList2);

			mv.addObject("paging", getPaging(num7,7));

			session.setAttribute("pageNum", num7);

			mv.setViewName("aReportPostList");
			break;
		case 8 ://신고된 댓글 리스트 보기
			mv= new ModelAndView();

			int num8 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num8);

			List<ReplyDto> reportList3 = aDao.getReportList_R(num8);

			mv.addObject("rList", reportList3);

			mv.addObject("paging", getPaging(num8,8));

			session.setAttribute("pageNum", num8);

			mv.setViewName("aReportReplyList");
			break;
		case 9://건의사항 리스트 보기
			mv= new ModelAndView();

			int num9 = (pageNum==null)? 1 : pageNum;
			System.out.println("num : "+num9);

			List<SuggestDto> sugList = aDao.getSuggestList(num9);

			mv.addObject("sugList", sugList);

			mv.addObject("paging", getPaging(num9,8));

			session.setAttribute("pageNum", num9);

			mv.setViewName("aSuggestList");
			break;
		}//스위치 끝
		//스위치가 결정하고 데이터를 담아온 mv를 리턴해 각 스위치별 페이지로 이동
		//모델에 담긴 리스트를 꺼내는건 각 페이지에서 EL로 처리함
		return mv;
	}//셀렉트 스위치 메소드 끝

	//승인 요청 수락하기 UPATE 메소드
	//DB를 고치는 작업은 트랜젝셔널 처리 별도 추가
	@Transactional
	public String aPertmitStore(String c_num) {
		//결과는 해당 리스트 페이지에서 볼 수 있으므로 결과 메시지 생략
		String view = null;
		int res = aDao.permitStore(c_num);
		if (res > 0) {//UPDATE 처리시 res는 1이 됨 (executeQuery의 처리와 유사)
			System.out.println("처리 결과 res : "+res);
			//처리 성공으로 판단, 승인 리스트에서 찾게 함
			view = "redirect:aAuthList";
		} else {
			System.out.println("처리 결과 res : "+res);
			//처리 실패로 판단, 대기 리스트에서 찾게 함
			view = "redirect:aPendingList";
		}
		return view;
	}

	//삭제 스위치 (일반 회원, 업체 회원 통합 스위치)
	@Transactional
	public String deleteSwitch(String id, int deleteSelect) {
		String view = null;
		int resInt = 0;//콘솔 확인용 INT
		String resStr = null;//콘솔 확인용 STR

		switch(deleteSelect) {
		case 1://회원 추방
			resInt = aDao.deleteMember(id);
			if (resInt >0) {
				resStr = "삭제 성공";				
			} else {
				resStr = "삭제 실패";
			}
			view = "redirect:aMemberList";
			break;
		case 2://업체 추방
			resInt = aDao.deleteStore(id);
			if (resInt >0) {
				resStr = "삭제 성공";				
			} else {
				resStr = "삭제 실패";
			}
			view = "redirect:aCeoList";
			break;
		}
		System.out.println(resStr);
		return view;
	}

	public String mailSend
	(EmailDto email, int mailType, RedirectAttributes rttr, MultipartHttpServletRequest multi)
			throws Exception {
		String resStr = "";
		String view = null;
		String check = multi.getParameter("fileCheck");
		List<String> mailList = new ArrayList<String>();
		System.out.println("mailSend");

		// 네이버일 경우 smtp.naver.com을 입력
		// Google일 경우 smtp.gmail.com을 입력
		try {
			String host = "smtp.gmail.com";//SMTP 호스트 서버
			final String username = "stepy.tester@gmail.com";//발신자 계정 아이디
			final String password = "stepy1234!";//발신자 계정 비밀번호
			int port=587; //포트번호

			//메일 내용
			//받는 사람의 메일주소들 가져오기
			switch(mailType) {
			case 1://M테이블 전원
				mailList = aDao.getMailList_M();
				view = "redirect:aGroupMailFrm";
				break;
			case 2://C테이블 전원
				mailList = aDao.getMailList_C();
				view = "redirect:aGroupMailFrm";
				break;
			case 3://특정 단일 대상 (emailDTO에 기록된 수신자 가져와서 add)
				mailList.add(email.getReceiveMail());
				view = "redirect:aReport";
				break;
			}
			System.out.println("DAO에서 이메일 리스트 가져오기 완료");

			//메일리스트(M 또는 C에서 가져온 이메일 레코드 리스트)의 길이만큼 반복해 수신자 목록에 추가
			InternetAddress[] toAddr = new InternetAddress[mailList.size()];
			for(int i=0; i<mailList.size(); i++) {
				toAddr[i]= new InternetAddress(mailList.get(i));
			}

			//메일 제목
			String subject = email.getSubject();

			//메일 내용 가져와서 스트링버퍼에 옮겨 담기
			StringBuffer sb = new StringBuffer();
			String body = email.getContents();
			sb.append(body);

			//어펜드로 종합된 버퍼들을 하나의 String으로 포장
			String content = sb.toString();

			// 정보를 담기 위한 객체 생성
			Properties props = System.getProperties();

			// SMTP 서버 정보 설정
			props.put("mail.smtp.starttls.enable", "true");//TLS 설정 (네이버의 경우 SSL-465포트사용)
			props.put("mail.smtp.host", host);//호스트 서버 세팅
			props.put("mail.smtp.port", port);//호스트 포트 세팅
			props.put("mail.smtp.auth", "true");//auth 설정
			System.out.println("props 설정 완료");

			//Session 생성
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				String un=username;
				String pw=password;
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(un, pw);
				}
			});
			session.setDebug(true);//for debug

			// 메일 콘텐츠 설정
			Multipart mParts = new MimeMultipart();
			MimeBodyPart mTextPart = new MimeBodyPart();
			MimeBodyPart mFilePart = new MimeBodyPart();

			//check가 1, 첨부파일이 있는 경우에만 실행되는 블럭 (첨부가 없으면 생략)
			if(check.equals("1")) {
				System.out.println("첨부파일 있음");
				String path = multi.getSession().getServletContext().getRealPath("/");
				path += "resources/upload/";
				System.out.println("현재 경로 path : "+path);

				List<MultipartFile> fList = multi.getFiles("files");

				for (int i =0; i< fList.size(); i++) {
					mFilePart = new MimeBodyPart();

					//첨부파일이 1개여도 n개여도 0~n-1까지 전부 처리하게 작성
					MultipartFile mf = fList.get(i);

					//mf에 들어간 데이터에서 파일의 오리지널네임, 원래 이름을 가져옴 (가명 'on'으로 사용)
					String on = mf.getOriginalFilename();
					//System.out.println(on);
					//변경된 파일 이름을 맵에 저장
					//sn의 값을 정하는 '순간의 밀리초'+'.확장자'를 모두 합쳐 sn의 시스템 파일명이 결정됨
					//단, 확장자를 찾기 위해서는 서브스트링(".")으로 .과 이후의 문자열(확장자 포함)만 가져옴
					String sn = System.currentTimeMillis()+on.substring(on.lastIndexOf("."));
					String fullPath = path+sn;

					//경로+파일명을 합친 전체 경로&파일명을 file의 new에 등록, '파일'로서 이식시킴
					File file = new File(fullPath);

					//해당 폴더에 파일 저장하기 (attachFile을 할 때 실제로 해당 위치에 파일이 있어야 하기 때문에 저장 필요)
					//path 경로에 sn 파일명으로 된 파일을 저장하는 메소드
					//transferTo
					mf.transferTo(file);

					//마임파트에 파일 기억시킴 (단, 실제로 file의 경로에 파일이 존재하고 있어야 함)
					mFilePart.attachFile(file);

					//같은 이름을 사용해도 내용이 다르면 add할 때마다 신규 add
					mParts.addBodyPart(mFilePart);
				}
			}

			//MimeMessage 생성 - 메일의 각각의 요소를 기억
			Message mimeMessage = new MimeMessage(session);

			//발신자 세팅 , 보내는 사람의 이메일 주소를 한번 더 입력
			mimeMessage.setFrom(new InternetAddress("stepy.tester@gmail.com"));

			//수신자 세팅 (배열도 가능)
			mimeMessage.setRecipients(Message.RecipientType.TO, toAddr );

			//제목 세팅
			mimeMessage.setSubject(subject);

			//내용 세팅
			mTextPart.setText(content);

			//멀티파트도 내용으로 기억가능, 멀티파트 mParts에 몰아 담기
			mParts.addBodyPart(mTextPart);


			mimeMessage.setContent(mParts); //add가 쌓인 mParts를 세팅

			//mimeMessage.setFileName();//첨부파일명으로 쓰일 이름 세팅

			System.out.println("메시지에 모든 요소 세팅 완료");

			Transport.send(mimeMessage); //javax.mail.Transport.send() 이용

			resStr = "메일 발송이 완료되었습니다";
		} catch (AddressException e) {
			resStr = "메일 발송에 실패했습니다";
		}
		rttr.addFlashAttribute("msg",resStr);

		return view;
	}//메일 발송 메소드 끝

	//이벤트 신규 등록
	@Transactional
	public String addEvent (MultipartHttpServletRequest multi,
			RedirectAttributes rttr) {
		System.out.println("서비스 클래스 진입");
		String view = null;
		String resStr = null;

		//form으로 작성한 내용들 꺼내기
		//멀티리퀘스트도 일종의 리퀘스트이므로 겟파라미터(NAME)으로 꺼낼 수 있음

		//e_num은 PK로 시퀀스, DAO에서 처리하므로 제외
		//이벤트 제목
		String title = multi.getParameter("e_title");
		//이벤트 내용 (textarea = 문자열이므로 string 받음)
		String contents = multi.getParameter("e_contents");
		//날짜 가져오기
		String e_dateStr = multi.getParameter("e_date");
		System.out.println("입력값 그대로 : "+e_dateStr);
		String[] dateArray = e_dateStr.split("T");
		System.out.println("스플릿 후 [0] : "+dateArray[0]);
		System.out.println("스플릿 후 [1] : "+dateArray[1]);
		e_dateStr = dateArray[0]+" "+dateArray[1]+":00";
		System.out.println("날짜+ +시간+:00 >> "+e_dateStr);

		Timestamp e_date = java.sql.Timestamp.valueOf(e_dateStr);
		System.out.println("타임스탬프화 : "+e_date);

		//파일 업로드 체크, 파일을 등록했다면 1, 아니라면 0이 됨
		String check = multi.getParameter("fileCheck");
		System.out.println("파일 체크 : "+check);

		//단, textarea는 입력한 문자열의 앞뒤로 공백이 발생
		//문자열의 앞뒤 공백 제거하는 명령어가 추가로 필요 (trim())
		contents = contents.trim();

		//form에서 꺼낸 데이터들을 이벤트DTO에 삽입
		EventDto event = new EventDto();
		event.setE_title(title);//이벤트 제목
		event.setE_contents(contents);//이벤트 내용
		event.setE_date(e_date);

		//INSERT처리
		try {
			//데이터들을 담은 DTO째로 파라미터해 INSERT
			aDao.InsertEvent(event);

			//check가 1인 상황, 파일이 있는 경우메나 파일 INSERT 처리
			//파일 업로드 메소드 호출해 처리
			if (check.equals("1")) {
				try {
					//분리한 파일 업로드 메소드 호출해 파일 업로드 처리
					fileUp(multi,event.getE_num());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//예외가 발생하지 않았다면 플래시 어트리뷰트용 메시지+뷰 지정
			resStr = "이벤트 등록 성공";
			view ="redirect:aEventList";
		}
		catch (Exception e) {
			e.printStackTrace();

			//성공,실패 여부에 상관없이 뷰는 동일, 메시지만 다름
			resStr = "이벤트 등록 실패";
			view ="redirect:aEventList";
		}



		rttr.addFlashAttribute("msg", resStr);
		//플래시 어트리뷰트를 담고 view 리턴
		return view;
	}//이벤트 추가 메소드 끝

	//파일 업로드 처리 메소드
	public boolean fileUp(
			MultipartHttpServletRequest multi,
			int e_num
			) throws Exception{
		//파일은 항상 절대경로를 통해서 인식함
		boolean res = false;

		String path = multi.getSession().getServletContext().getRealPath("/");
		path += "resources/upload/";


		//파일 생성, File (io패키지) 임포트 필요
		File dir = new File(path);
		if(dir.isDirectory() == false) {
			//dir이 디렉토리,폴더가 아니라면 디렉토리,폴더 생성
			dir.mkdir();//폴더가 없는 경우 폴더 생성
		}

		//실제 파일명과 저장용 파일명을 함께 관리 - 맵&해시맵 사용
		Map<String, String> fmap = new HashMap<String, String>();

		//fileInsert의 파라미터인 맵에 필요한 데이터는
		//1.번호 e_num
		//2.파일의 원래 이름 oriName
		//3.파일의 변경 이름 sysName

		//맵에 입력 (put 메소드)
		//숫자인 bnum을 문자열 변환 처리후 맵에 입력
		fmap.put("e_num", String.valueOf(e_num));


		//파일의 처리는 '다중 선택'을 기본으로 하고 있어
		//멀티파트 요청 객체에서 꺼낼 때는 
		//파일이 하나여도 배열로 받아야 함
		//JSP에서 클래스를 따오는 것과 같은 이유
		//하나일 수도 여럿일 수도 있으므로 여럿인 경우를 기본으로 해 절차 최소화
		List<MultipartFile> fList = multi.getFiles("files");
		for (int i =0; i< fList.size(); i++) {
			//1개여도 n개여도 0~n-1까지 전부 처리하게 작성
			MultipartFile mf = fList.get(i);

			//mf에 들어간 데이터에서 파일의 오리지널네임, 원래 이름을 가져옴
			String on = mf.getOriginalFilename();


			//이름 on을 맵에 등록
			fmap.put("oriName", on);

			//변경된 파일 이름을 맵에 저장
			//sn의 값을 정하는 '순간의 밀리초'+'.확장자'를 모두 합쳐 sn의 시스템 파일명이 결정됨
			//단, 확장자를 찾기 위해서는 서브스트링(".")으로 .과 이후의 문자열(확장자 포함)만 가져옴
			String sn = System.currentTimeMillis()+on.substring(on.lastIndexOf("."));

			//이름 sn을 맵에 등록
			fmap.put("sysName", sn);


			//해당 폴더에 파일 저장하기
			//path 경로에 sn 파일명으로 된 파일을 저장하는 메소드
			//transferTo
			mf.transferTo(new File(path+sn));

			//bnum,path,on,sn을 모두 담은 맵을
			//파일 테이블인 BF에 INSERT처리
			aDao.fileInsert(fmap);
		}//꺼내기 for 끝

		return res;
	}//파일 업로드 처리 메소드 끝

	//선택한 이벤트 상세보기
	public ModelAndView eventDetail (int e_num, int select) {
		mv = new ModelAndView();
		//이벤트 내용 텍스트 가져오기
		EventDto event = aDao.getEventRecord(e_num);
		mv.addObject("event", event);
		//해당 이벤트에 대한 첨부파일 가져오기
		List<FileUpDto> files = aDao.getEventFiles(e_num);
		mv.addObject("fList", files);

		switch(select) {
		case 1:
			mv.setViewName("aEventDetail");
			break;
		case 2:
			mv.setViewName("aEventUpdateFrm");
			break;
		}
		return mv;
	}

	public void fileDown(String sysName, HttpServletRequest requst,
			HttpServletResponse response) {
		String path = requst.getSession().getServletContext().getRealPath("/");

		path += "resources/uplod/";

		String oriName = aDao.getOriName(sysName);
		path += sysName;//다운로드할 파일 경로 + 파일명 

		InputStream is = null;
		OutputStream os = null;

		try {
			String pFileName = URLEncoder.encode(oriName,"UTF-8");

			//파일 객체 생성
			File file = new File(path);
			is = new FileInputStream(file);

			//응답 객체(response)의 헤더 설정
			//파일 전송용 contentType 설정
			response.setContentType("application/octet-stream");
			response.setHeader("content-Disposition", "attachment; filename=\"" + pFileName + "\"");
			//attachment; filename="가나다라.jpg"

			os = response.getOutputStream();

			//파일전송(byte 단위로 전송)
			byte[] buffer = new byte[1024];
			int length;
			while((length = is.read(buffer)) != -1) {
				os.write(buffer, 0 , length);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				os.flush();
				os.close();
				is.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//이벤트 중지
	@Transactional
	public String deleteEvent(int e_num) {
		int res = aDao.deleteEvent(e_num);
		if (res >0) {
			System.out.println("삭제 성공");
		} else {
			System.out.println("삭제 실패");
		}

		return "redirect:aEventList";
	}

	@Transactional
	public String updateEvent(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		String view = null;
		String resStr = null;
		//form으로 작성한 내용들 꺼내기
		//멀티리퀘스트도 일종의 리퀘스트이므로 겟파라미터(NAME)으로 꺼낼 수 있음

		String e_num = multi.getParameter("e_num");
		//이벤트 제목
		String title = multi.getParameter("e_title");
		//이벤트 내용 (textarea = 문자열이므로 string 받음)
		String contents = multi.getParameter("e_contents");
		//날짜 가져오기
		String e_dateStr = multi.getParameter("e_date");
		System.out.println("입력값 그대로 : "+e_dateStr);
		String[] dateArray = e_dateStr.split("T");
		System.out.println("스플릿 후 [0] : "+dateArray[0]);
		System.out.println("스플릿 후 [1] : "+dateArray[1]);
		e_dateStr = dateArray[0]+" "+dateArray[1]+":00";
		System.out.println("날짜+ +시간+:00 >> "+e_dateStr);

		Timestamp e_date = java.sql.Timestamp.valueOf(e_dateStr);
		System.out.println("타임스탬프화 : "+e_date);

		//파일 업로드 체크, 파일을 등록했다면 1, 아니라면 0이 됨
		String check = multi.getParameter("fileCheck");
		System.out.println("파일 체크 : "+check);

		//단, textarea는 입력한 문자열의 앞뒤로 공백이 발생
		//문자열의 앞뒤 공백 제거하는 명령어가 추가로 필요 (trim())
		contents = contents.trim();

		//form에서 꺼낸 데이터들을 이벤트DTO에 삽입
		EventDto event = new EventDto();
		event.setE_num(Integer.parseInt(e_num));//UPDATE의 탐색에 쓸 이벤트 번호
		event.setE_title(title);//이벤트 제목
		event.setE_contents(contents);//이벤트 내용
		event.setE_date(e_date);

		try {
			//데이터들을 담은 DTO째로 파라미터해 INSERT
			aDao.updateEvent(event);

			//check가 1인 상황, 파일이 있는 경우에만 파일 INSERT 처리
			//파일 업로드 메소드 호출해 처리
			if (check.equals("1")) {
				try {
					//분리한 파일 업로드 메소드 호출해 파일 업로드 처리
					fileUp(multi,event.getE_num());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//예외가 발생하지 않았다면 플래시 어트리뷰트용 메시지+뷰 지정
			resStr = "이벤트 수정 성공";
			view ="redirect:aEventList";
		}
		catch (Exception e) {
			e.printStackTrace();

			//성공,실패 여부에 상관없이 뷰는 동일, 메시지만 다름
			resStr = "이벤트 수정 실패";
			view ="redirect:aEventList";
		}



		rttr.addFlashAttribute("msg", resStr);
		//플래시 어트리뷰트를 담고 view 리턴
		return view;
	}

	//수정 도중 사진을 삭제하려는 경우
	@Transactional
	public void deletePic (int f_num) {
		System.out.println("글 수정중 파일 삭제 처리");

		aDao.deleteFile(f_num);
	}

	//단일 신고글 세부사항 보기
	public ModelAndView reportDetail (int rp_num) {
		System.out.println("(서비스) 파라미터된 rp_num : " +rp_num);
		mv = new ModelAndView();
		ReportDto report = aDao.getReportRecord(rp_num);
		CeoDto ceo = aDao.getCeoRecord(report.getRp_cnum());

		mv.addObject("report", report);
		mv.addObject("ceo", ceo);
		mv.setViewName("aReportDetail");
		return mv;
	}

	//어드민 권한으로 게시글&댓글 강제 삭제
	@Transactional
	public String deletePandR (int num, int switchNum, RedirectAttributes rttr) {
		switch(switchNum) {
		case 1://게시글 강제 삭제
			aDao.deletePost(num);
			break;
		case 2://댓글 강제 삭제
			aDao.deleteReply(num);
			break;
		}
		rttr.addFlashAttribute("msg","처리 완료");
		String view = "redirect:aReport";
		return view;
	}

	@Transactional
	public void aReportFinished(int rp_num) {
		aDao.updateReport(rp_num);
		System.out.println("업데이트 완료");
	}

	public ModelAndView suggestDetail(int sug_num) {
		mv = new ModelAndView();
		SuggestDto sug = aDao.getSuggestRecord(sug_num);
		mv.addObject("sug", sug);
		mv.setViewName("aSuggestDetail");
		return mv;
	}

	@Transactional
	public void deleteSuggest(int sug_num) {
		aDao.deleteSuggest(sug_num);
	}


}//서비스 클래스 끝