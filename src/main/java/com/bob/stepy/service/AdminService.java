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
		String view =null;
		//입력했던 값
		String id = member.getM_id();
		String rawPw = member.getM_pwd();
		//인코더 생성
		BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
		//DB에서 가져오는 pw (암호화 상태)
		String pw = aDao.getPwd(id);

		if (pw != null) {
			//입력한 값, 암호값 대조
			if (pwdEncode.matches(rawPw, pw)) {
				//입력한 ID와 미리 등록해둔 관리자의 전용 ID가 같아야 로그인 진행
				if(id.equals("admin")) {
					member = aDao.getMemberInfo(id);
					session.setAttribute("member", member);
					view = "aHome";
				}else {
					view = "redirect:aLoginFrm";
					rttr.addFlashAttribute("msg", "접속 권한이 없습니다");
				}
			}else {
				view ="redirect:aLoginFrm";
				rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다");
			}
		}else {
			view ="redirect:aLoginFrm";
			rttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다");
		}
		return view;
	}

	private String getPaging(int num, int listSelect) {
		int maxNum = 0;//레코드 총 개수
		String ttl = null;
		switch(listSelect) {
		case 1://일반회원 카운트
			maxNum = aDao.getMemberCnt(); ttl ="aMemberList"; break;
		case 2://업체회원 카운트 - 전체
			maxNum = aDao.getAllCeoCnt(); ttl ="aCeoList"; break;
		case 3://업체회원 카운트 - 완료
			maxNum = aDao.getApproveCeoCnt(); ttl ="aAuthList"; break;
		case 4://업체회원 카운트 - 대기
			maxNum = aDao.getPendingCeoCnt(); ttl ="aPendingList"; break;
		case 6://신고 리스트 카운트 - 회원
			maxNum = aDao.getReportCnt(); ttl ="aReportStoreList"; break;
		case 7://신고 리스트 카운트 - 게시글
			maxNum = aDao.getReportCnt_P(); ttl ="aReportPostList"; break;
		case 8://신고 리스트 카운트 - 댓글
			maxNum = aDao.getReportCnt_R(); ttl ="aReportReplyList"; break;
		case 9://신고 리스트 카운트 - 댓글
			maxNum = aDao.getSuggestCnt(); ttl ="aReportReplyList"; break;
		}

		//설정값 각인
		int pageCnt = 5;//한 페이지 그룹당 페이지 수
		int listCnt = 20;//한 페이지당 레코드 수
		String listName = ttl;

		Paging paging = new Paging
				(maxNum, num, listCnt,
						pageCnt, listName);

		//세트된 값을 바탕으로 페이징 메소드 실행
		String pagingHtml = paging.makePaging();

		return pagingHtml;
	}

	public ModelAndView listSet (Integer pageNum, Integer listSelect) {
		mv = new ModelAndView();

		switch (listSelect) {
		case 1://일반회원 전체보기
			mv= new ModelAndView();
			int num = (pageNum==null)? 1 : pageNum;

			List<MemberDto> mList = aDao.getMemberList(num);
			mv.addObject("mList", mList);
			mv.addObject("paging", getPaging(num,1));

			session.setAttribute("pageNum", num);

			mv.setViewName("aMemberList");
			break;

		case 2://업체 회원 전부 보기
			mv= new ModelAndView();
			int num2 = (pageNum==null)? 1 : pageNum;

			List<CeoDto> allCeoList = aDao.getAllCeoList(num2);
			mv.addObject("ceoList", allCeoList);
			mv.addObject("paging", getPaging(num2,2));

			session.setAttribute("pageNum", num2);
			mv.setViewName("aAllCeoList");
			break;

		case 3://승인완료 업체보기
			mv= new ModelAndView();
			int num3 = (pageNum==null)? 1 : pageNum;

			List<CeoDto> approvedList = aDao.getApproveList(num3);
			mv.addObject("ceoList", approvedList);
			mv.addObject("paging", getPaging(num3,3));

			session.setAttribute("pageNum", num3);
			mv.setViewName("aAuthList");
			break;

		case 4://승인대기 업체보기
			mv= new ModelAndView();
			int num4 = (pageNum==null)? 1 : pageNum;

			List<CeoDto> pendingList = aDao.getPendingList(num4);
			mv.addObject("ceoList", pendingList);
			mv.addObject("paging", getPaging(num4,4));

			session.setAttribute("pageNum", num4);
			mv.setViewName("aPendingList");
			break;

		case 5://이벤트 리스트 보기
			mv= new ModelAndView();
			int num5 = (pageNum==null)? 1 : pageNum;

			List<EventDto> eventList = aDao.getEventList(num5);
			mv.addObject("eList", eventList);
			mv.addObject("paging", getPaging(num5,5));

			session.setAttribute("pageNum", num5);
			mv.setViewName("aEventList");
			break;

		case 6 ://신고된 업체 리스트 보기
			mv= new ModelAndView();
			int num6 = (pageNum==null)? 1 : pageNum;

			List<ReportDto> reportList1 = aDao.getReportList_C(num6);
			mv.addObject("rpList", reportList1);
			mv.addObject("paging", getPaging(num6,6));

			session.setAttribute("pageNum", num6);
			mv.setViewName("aReportStoreList");
			break;

		case 7 ://신고된 게시글 리스트 보기
			mv= new ModelAndView();
			int num7 = (pageNum==null)? 1 : pageNum;

			List<PostDto> reportList2 = aDao.getReportList_P(num7);
			mv.addObject("pList", reportList2);
			mv.addObject("paging", getPaging(num7,7));

			session.setAttribute("pageNum", num7);
			mv.setViewName("aReportPostList");
			break;

		case 8 ://신고된 댓글 리스트 보기
			mv= new ModelAndView();
			int num8 = (pageNum==null)? 1 : pageNum;

			List<ReplyDto> reportList3 = aDao.getReportList_R(num8);
			mv.addObject("rList", reportList3);
			mv.addObject("paging", getPaging(num8,8));

			session.setAttribute("pageNum", num8);
			mv.setViewName("aReportReplyList");
			break;

		case 9://건의사항 리스트 보기
			mv= new ModelAndView();
			int num9 = (pageNum==null)? 1 : pageNum;

			List<SuggestDto> sugList = aDao.getSuggestList(num9);
			mv.addObject("sugList", sugList);
			mv.addObject("paging", getPaging(num9,8));

			session.setAttribute("pageNum", num9);
			mv.setViewName("aSuggestList");
			break;

		}
		return mv;

	}

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
	public String deleteSwitch(String id, int deleteSelect, RedirectAttributes rttr) {
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
		rttr.addFlashAttribute("msg",resStr);
		return view;
	}

	public String mailSend
	(EmailDto email,int mailType,
	RedirectAttributes rttr,
	MultipartHttpServletRequest multi)
	throws Exception {
		String resStr = ""; String view = null;
		String check = multi.getParameter("fileCheck");
		List<String> mailList = new ArrayList<String>();
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

			InternetAddress[] toAddr = new InternetAddress[mailList.size()];
			for(int i=0; i<mailList.size(); i++) {
				toAddr[i]= new InternetAddress(mailList.get(i));
			}
			String subject = email.getSubject();
			StringBuffer sb = new StringBuffer();
			String body = email.getContents();
			sb.append(body);
			String content = sb.toString();
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
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
				String path = multi.getSession().getServletContext().getRealPath("/");
				path += "resources/upload/";
				List<MultipartFile> fList = multi.getFiles("files");
				for (int i =0; i< fList.size(); i++) {
					mFilePart = new MimeBodyPart();
					MultipartFile mf = fList.get(i);
					String on = mf.getOriginalFilename();
					String sn = System.currentTimeMillis()+on.substring(on.lastIndexOf("."));
					String fullPath = path+sn;
					File file = new File(fullPath);
					mf.transferTo(file);
					mFilePart.attachFile(file);
					mParts.addBodyPart(mFilePart);
				}
			}
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("stepy.tester@gmail.com"));
			mimeMessage.setRecipients(Message.RecipientType.TO, toAddr );
			mimeMessage.setSubject(subject);
			mTextPart.setText(content);
			mParts.addBodyPart(mTextPart);
			mimeMessage.setContent(mParts);
			Transport.send(mimeMessage);
			resStr = "메일 발송이 완료되었습니다";
		} catch (AddressException e) {
			resStr = "메일 발송에 실패했습니다";
		}
		rttr.addFlashAttribute("msg",resStr);
		return view;
	}
	
	//이벤트 신규 등록
	@Transactional
	public String addEvent (MultipartHttpServletRequest multi,
			RedirectAttributes rttr) {
		String view = null;
		String resStr = null;

		String title = multi.getParameter("e_title");
		String contents = multi.getParameter("e_contents");
		contents = contents.trim();
		String e_dateStr = multi.getParameter("e_date");

		String[] dateArray = e_dateStr.split("T");
		e_dateStr = dateArray[0]+" "+dateArray[1]+":00";
		Timestamp e_date = java.sql.Timestamp.valueOf(e_dateStr);

		String check = multi.getParameter("fileCheck");

		EventDto event = new EventDto();
		event.setE_title(title);//이벤트 제목
		event.setE_contents(contents);//이벤트 내용
		event.setE_date(e_date);

		try {
			aDao.InsertEvent(event);
			
			if (check.equals("1")) {
				try {
					fileUp(multi,event.getE_num());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			resStr = "이벤트 등록 성공";
			view ="redirect:aEventList";
		}
		catch (Exception e) {
			e.printStackTrace();
			resStr = "이벤트 등록 실패";
			view ="redirect:aEventList";
		}
		rttr.addFlashAttribute("msg", resStr);
		return view;
	}

	//파일 업로드 처리 메소드
	public boolean fileUp(
			MultipartHttpServletRequest multi,
			int e_num
			) throws Exception{
		boolean res = false;
		String path = multi.getSession().getServletContext().getRealPath("/");
		path += "resources/upload/";
		File dir = new File(path);
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
		Map<String, String> fmap = new HashMap<String, String>();
		fmap.put("e_num", String.valueOf(e_num));
		List<MultipartFile> fList = multi.getFiles("files");
		for (int i =0; i< fList.size(); i++) {
			MultipartFile mf = fList.get(i);
			String on = mf.getOriginalFilename();
			fmap.put("oriName", on);
			String sn = System.currentTimeMillis()+on.substring(on.lastIndexOf("."));
			fmap.put("sysName", sn);
			mf.transferTo(new File(path+sn));
			aDao.fileInsert(fmap);
		}
		return res;
	}

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

	//이벤트 DELETE
	@Transactional
	public String deleteEvent(int e_num, RedirectAttributes rttr) {
		int res = aDao.deleteEvent(e_num);
		if (res >0) {
			rttr.addFlashAttribute("msg","삭제 성공");
		} else {
			rttr.addFlashAttribute("msg","삭제 실패");
		}
		return "redirect:aEventList";
	}

	@Transactional
	public String updateEvent(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		String view = null;
		String resStr = null;
		
		String e_num = multi.getParameter("e_num");
		String title = multi.getParameter("e_title");
		String contents = multi.getParameter("e_contents");
		contents = contents.trim();
		String e_dateStr = multi.getParameter("e_date");
		String[] dateArray = e_dateStr.split("T");
		e_dateStr = dateArray[0]+" "+dateArray[1]+":00";
		Timestamp e_date = java.sql.Timestamp.valueOf(e_dateStr);
		String check = multi.getParameter("fileCheck");
		
		EventDto event = new EventDto();
		event.setE_num(Integer.parseInt(e_num));//UPDATE의 탐색에 쓸 이벤트 번호
		event.setE_title(title);//이벤트 제목
		event.setE_contents(contents);//이벤트 내용
		event.setE_date(e_date);

		try {
			aDao.updateEvent(event);
			if (check.equals("1")) {
				try {
					fileUp(multi,event.getE_num());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			resStr = "이벤트 수정 성공";
			view ="redirect:aEventList";
		}
		catch (Exception e) {
			//e.printStackTrace();
			resStr = "이벤트 수정 실패";
			view ="redirect:aEventList";
		}
		rttr.addFlashAttribute("msg", resStr);
		return view;
	}

	//수정 도중 사진을 삭제하려는 경우
	@Transactional
	public void deletePic (int f_num) {
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