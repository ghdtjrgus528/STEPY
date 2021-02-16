package com.bob.stepy.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Target;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dao.BoardDao;
import com.bob.stepy.dto.LikedDto;
import com.bob.stepy.dto.MemberDto;
import com.bob.stepy.dto.PostDto;
import com.bob.stepy.dto.PostDto2;
import com.bob.stepy.dto.PostFileDto;
import com.bob.stepy.dto.ReplyDto;
import com.bob.stepy.dto.SuggestDto;
import com.bob.stepy.util.Paging;

import lombok.extern.java.Log;

@Service
@Log
public class BoardService {

	@Autowired
	BoardDao bDao;

	ModelAndView mv;

	@Autowired
	private HttpSession session;
	
	
	//메이트 게시판 페이징&게시글
	public ModelAndView getBoardList(Integer pageNum) {
		log.info("getBoardList() - pageNum : " + pageNum);

		mv = new ModelAndView();

		int num = (pageNum == null) ? 1: pageNum;

		List<PostDto> pList = bDao.getList_1(num);

		//DB에서 조회한 데이터 목록을 모델에 추가
		mv.addObject("pList", pList);

		//페이징 처리
		mv.addObject("paging", getPaging(num,"bMateList"));

		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", num);

		mv.setViewName("bMateList");

		return mv;
	}


	//자유 게시판 페이징&게시글
	public ModelAndView getBoardList_2(Integer pageNum) {
		log.info("getBoardList() - pageNum : " + pageNum);

		mv = new ModelAndView();

		int num = (pageNum == null) ? 1: pageNum;

		List<PostDto> pList = bDao.getList_2(num);

		//DB에서 조회한 데이터 목록을 모델에 추가
		mv.addObject("pList", pList);

		//페이징 처리
		mv.addObject("paging", getPaging(num,"bFreeList"));

		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", num);

		mv.setViewName("bFreeList");

		return mv;
	}

	//리뷰 게시판 페이징&게시글
	public ModelAndView getBoardList_3(Integer pageNum) {
		log.info("getBoardList() - pageNum : " + pageNum);

		mv = new ModelAndView();

		int num = (pageNum == null) ? 1: pageNum;

		List<PostDto> pList = bDao.getList_3(num);

		//DB에서 조회한 데이터 목록을 모델에 추가
		mv.addObject("pList", pList);

		//페이징 처리
		mv.addObject("paging", getPaging(num,"bReviewList"));

		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", num);

		mv.setViewName("bReviewList");

		return mv;
	}
	
	//공지 게시판 페이징&게시글
		public ModelAndView getBoardList_4(Integer pageNum) {
			log.info("getBoardList() - pageNum : " + pageNum);

			mv = new ModelAndView();

			int num = (pageNum == null) ? 1: pageNum;
			
			List<PostDto> pList = bDao.getList_4(num);

			//DB에서 조회한 데이터 목록을 모델에 추가
			mv.addObject("pList", pList);

			//페이징 처리
			mv.addObject("paging", getPaging(num,"bCommunity"));

			//세션에 페이지 번호 저장
			session.setAttribute("pageNum", num);

			mv.setViewName("bCommunity");

			return mv;
		}
		
	//건의사항 게시판 페이징&게시글
		public ModelAndView getBoardList_5(Integer pageNum) {
			log.info("getBoardList() - pageNum : " + pageNum);
				
			mv = new ModelAndView();

			int num = (pageNum == null) ? 1: pageNum;
					
			List<SuggestDto> sList = bDao.getList_5(num);
			
			log.info("sug : " + sList);

			//DB에서 조회한 데이터 목록을 모델에 추가
			mv.addObject("sList", sList);

			//페이징 처리
			mv.addObject("paging", getPaging(num,"bSugList"));

			//세션에 페이지 번호 저장
			session.setAttribute("pageNum", num);

			mv.setViewName("bSugList");

			return mv;
		}

	//페이징 처리
	private String getPaging(int num, String str) {
		//전체 글 개수 구하기(from DB)

		int maxNum = 0;
		if(str.equals("bMateList")) {
			maxNum = bDao.getPostCnt_1();
		}
		else if(str.equals("bFreeList")) {
			maxNum = bDao.getPostCnt_2();

			log.info("bFreeList : "+ maxNum);
		}
		else if(str.equals("bReviewList")){
			maxNum = bDao.getPostCnt_3();
		}
		else if(str.equals("bCommunity")) {
			maxNum = bDao.getPostCnt_4();
		}
		else if(str.equals("bSugList")) {
			maxNum = bDao.getPostCnt_5();
		}


		//설정값(페이지 당 글 개수, 그룹 당 페이지 개수)
		int listCnt = 15;
		int pageCnt = 5;
		String listName = str;

		Paging paging = new Paging(maxNum, num, listCnt,
				pageCnt, listName);

		String pagingHtml = paging.makePaging();

		return pagingHtml;

	}
	//게시글 보기
		public ModelAndView getContents(Integer pnum) {
			mv = new ModelAndView();

			//게시글이 클릭과 동시에  view 증가
			bDao.viewUpdate(pnum);
			//글 내용 가져오기

			String cat = bDao.getcategory(pnum);
			log.info("category : " + cat);

			PostDto2 pList_1;

			int num = bDao.getreport(pnum);
			
			log.info("report : " + num);

			
			if(num == 0) {
				
				if(cat.equals("메이트 구하기")) {
					pList_1 = bDao.getContent_1(pnum);
				}
				else if(cat.equals("자유")) {
					pList_1 = bDao.getContent_2(pnum);
				}
				else if(cat.equals("후기")) {
					pList_1 = bDao.getContent_3(pnum);
				}
				else {
					pList_1 = bDao.getContent_4(pnum);
				}
			

				//파일목록 가져오기
				List<PostFileDto> fList = bDao.getpostFile(pnum);


				//댓글목록 가져오기
				List<ReplyDto> rList = bDao.getReplyList(pnum);

				//모델 데이터 담기
				mv.addObject("pList", pList_1);
				mv.addObject("fList", fList);
				mv.addObject("rList", rList);
				mv.setViewName("bViewPost");
	
			}
			else {
				mv.addObject("report", num);
				mv.setViewName("bViewPost");
			}
				
			return mv;
		}
		
		@Transactional
		public ModelAndView getsList(Integer snum) {
			mv = new ModelAndView();
			
			//조회수 증가
			bDao.sviewUpdate(snum);
			
			//공지글 내용 가져오기
			SuggestDto sDto = bDao.getContent_5(snum);
			
			mv.addObject("sList", sDto);
			mv.setViewName("bSugPost");
			
			return mv;
		
		}
		
		
	//게시글 작성
	@Transactional
	public String boardInsert(MultipartHttpServletRequest multi,
								RedirectAttributes rttr) {
		log.info("boardInsert()");
		
		String view = null;
		
		String ptitle = multi.getParameter("ptitle");
		String pcategory = multi.getParameter("pcategory");
		String pcontents = multi.getParameter("pcontents");
		String check = multi.getParameter("fileCheck");
		String id = multi.getParameter("pmid");
		
		pcontents = pcontents.trim();
		
		
		PostDto post = new PostDto();
		post.setP_title(ptitle);
		post.setP_category(pcategory);
		post.setP_contents(pcontents);
		post.setP_mid(id);
		
		log.info("mid" + post);
		
		
		try {
			//게시글 내용 저장.
			bDao.PostInsert(post);
			
			//파일 업로드 메소드 호출
			if(check.equals("1")) {
				fileUp(multi, post.getP_num());
			}
			
			
			view = "redirect:bCommunity";
			rttr.addFlashAttribute("msg", "글 등록 성공");
			
			
		}catch (Exception e) {
			e.printStackTrace();
			view = "redirect:bWriteProc";
			rttr.addFlashAttribute("msg", "글 등록 실패");
		}
		
		return view;
	}
	
	//파일 업로드 처리 메소드
	public boolean fileUp(MultipartHttpServletRequest multi,
							int pnum) throws Exception {
		
		//저장 공간 절대 경로
		String path = multi.getSession().getServletContext()
				.getRealPath("/");
		
		path += "resources/uplod/";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();//폴더가 없을 경우 폴더 생성
		}
		
		//실제 파일과 저장 파일명을 함께 관리
		Map<String, String> fmap =
					new HashMap<String, String>();
		
		fmap.put("pnum", String.valueOf(pnum));
		
		//파일 전송시 기본 값을 파일다중 선택임.
		//멀티파트 요청 객체에서 꺼내올 때는 List를 사용.
		List<MultipartFile> fList = multi.getFiles("files");
		
		for(int i = 0; i < fList.size(); i++) {
			MultipartFile mf = fList.get(i);
			String on = mf.getOriginalFilename();
			fmap.put("oriName", on);
			
			//변경된 파일 이름 저장
			String sn = System.currentTimeMillis() + "."
						+ on.substring(on.lastIndexOf(".") + 1);
			fmap.put("sysName", sn);
			
			//해당 폴더에 파일 저장하기
			mf.transferTo(new File(path + sn));
			
			bDao.fileInsert(fmap);
		}
		
		
		
		return true;
	}
	
	public void fileDown(String sysName, HttpServletRequest requst,
			HttpServletResponse response) {
		String path = requst.getSession().getServletContext().getRealPath("/");
		
		path += "resources/uplod/";
		log.info(path);
		
		String oriName = bDao.getOriName(sysName);
		log.info("sysName : " + sysName);
		log.info("oriName : " + oriName);
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
	
	//업데이트 폼으로 이동하며 기존 데이터 전달
	public ModelAndView updateFrm(int pnum,
			RedirectAttributes rttr) {
		mv = new ModelAndView();
		
		//1행 가져오기
		String cat = bDao.getcategory(pnum);
		
		
		PostDto2 pList;
		
		if(cat.equals("메이트 구하기")) {
			pList = bDao.getContent_1(pnum);
		}
		else if(cat.equals("자유")) {
			pList = bDao.getContent_2(pnum);
		}
		else if(cat.equals("후기")) {
			pList = bDao.getContent_3(pnum);
		}
		else {
			pList = bDao.getContent_4(pnum);
		}
		
		mv.addObject("post", pList);
		List<PostFileDto> fList = bDao.getpostFile(pnum);
		mv.addObject("fList", fList);
		mv.setViewName("bModifyFrm");
		
		return mv;
	}
	//게시글 업데이트 내용 업로드
	@Transactional
	public String PostUpdate(MultipartHttpServletRequest multi,
					RedirectAttributes rttr) {
		String view = null;
		
		String pnum = multi.getParameter("pnum");
		String ptitle = multi.getParameter("ptitle");
		String pcontents = multi.getParameter("pcontents");
		String pcategory = multi.getParameter("pcategory");
		String check = multi.getParameter("fileCheck");
		String id = multi.getParameter("pmid");
		
		pcontents = pcontents.trim();
		
		log.info("boardUpdate() t: " + ptitle + ", c: " + pcontents + "pnum : " + pnum);
		
		PostDto post = new PostDto();
		post.setP_num(Integer.parseInt(pnum));
		post.setP_title(ptitle);
		post.setP_contents(pcontents);
		post.setP_category(pcategory);
		post.setP_mid(id);
		
		
		
		try {
			bDao.boardUpdate(post);
			//파일 업로드 메소드 호출
			if(check.equals("1")) {
				fileUp(multi, post.getP_num());
			}
			
			
			rttr.addFlashAttribute("msg", "수정 성공");
			view = "redirect:bCommunity";
		}catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "수정 실패");
			view = "redirect:bModifyFrm?pnum=" + pnum;
		}
		
		
		
		return view;
	}
	
	
	@Transactional
	public Map<String, List<PostFileDto>> fileDelete(String sysname,
										int pnum){
		Map<String, List<PostFileDto>> rMap = null;
		log.info("fileDelete() - sysname : " 
				+ sysname + ", pnum : " + pnum);
		String path = session.getServletContext()
				.getRealPath("/");
		
		log.info(path);
		path += "resources/uplod/" + sysname;
		
		try {
			bDao.fileDelete(sysname);
			
			File file = new File(path);
			
			if(file.exists()) {
				if(file.delete()) {
					List<PostFileDto> fList = bDao.getpostFile(pnum);
					rMap = new HashMap<String, List<PostFileDto>>();
					rMap.put("fList", fList);
				}
				else {
					rMap = null;
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			rMap = null;
		}
		
		return rMap;
	}
	
	@Transactional
	public Map<String, List<ReplyDto>> replyInsert(ReplyDto reply){
		Map<String, List<ReplyDto>> rmap = null;
		
		try {
			//댓글을 DB에 입력
			bDao.replyInsert(reply);
			log.info("pnum : " + reply.getR_pnum());
			
			//댓글 가져오기
			List<ReplyDto> rList = bDao.getReplyList(reply.getR_pnum());
			
			rmap = new HashMap<String, List<ReplyDto>>();
			rmap.put("rList", rList);
			
		}catch (Exception e) {
			e.printStackTrace();
			rmap = null;
			
		}
		
		return rmap;
	}
	//댓글 1행 삭제
	@Transactional
	public String delete(Integer pnum,Integer rnum, 
			RedirectAttributes rttr) {
		
		log.info("p : " + pnum + ", r : " + rnum);	
		
		String view;
		
		try {
			bDao.Delete(rnum);
			
			
			view = "redirect:contents?pnum=" + pnum;
			rttr.addFlashAttribute("msg", "댓글 삭제 성공");
			
			
		}catch (Exception e) {
			e.printStackTrace();
			view = "redirect:contents?pnum=" + pnum;
			rttr.addFlashAttribute("msg", "댓글 삭제 실패");
		}
		
		return view;
	}
	
	//게시글 삭제
	@Transactional
	public String condel(int pnum,
			RedirectAttributes rttr) {
		log.info("condel, pnum :" + pnum);
		
		String view=null;
		
		try {
			//댓글 삭제
			bDao.replyDelete(pnum);
			//파일 삭제
			bDao.fileListDelete(pnum);
			//게시글 삭제
			bDao.boardDelet(pnum);
			
			view = "redirect:bCommunity";
			rttr.addFlashAttribute("msg", "게시글 삭제 성공");
			
		}catch (Exception e) {
			view = "redirect:bCommunity";
			rttr.addFlashAttribute("msg", "게시글 삭제 성공");
		}
		
		return view;
	}
	
	//좋아요 처리
	public String like(Integer pnum, String mid,
			RedirectAttributes rttr) {
		
		log.info("pnum : " + pnum + ", mid : " + mid);
		
		String view = null;
				
		
		LikedDto like = new LikedDto();
		
		like.setL_pnum(pnum);
		like.setL_mid(mid);
		
		bDao.setLike(like);
		//처음은 게시글 1
		//두번쨰 게시글 2
		
		
		int num = bDao.getlike(like);
		
		if(num == 1) {
			bDao.likeup(pnum);
			
			view = "redirect:contents?pnum=" + pnum;
			rttr.addFlashAttribute("msg", "좋아요가 증가되었습니다.");
		}
		else if(num == 2) {
			bDao.dellike(like);
			bDao.likeupdown(pnum);
			
			view = "redirect:contents?pnum=" + pnum;
			rttr.addFlashAttribute("msg", "좋아요가 취소되었습니다..");
		}
		
		
		return view;
	}
	
	@Transactional
	public String singo(Integer pnum,
			RedirectAttributes rttr) {
		log.info("service singo pnum : " + pnum);
		
		String view;
		
		
		String cat = bDao.getcategory(pnum);
		log.info("category : " + cat);


		bDao.singoup(pnum);
			
			if(cat.equals("메이트 구하기")) {
				view = "redirect:bMateList";
			}
			else if(cat.equals("자유")) {
				view = "redirect:bFreeList";
			}
			else {
				view = "redirect:bReviewList";
			}
		
		rttr.addFlashAttribute("msg", "신고접수가 되었습니다.");
				
				
		return view;
	}
	//댓글 신고
	@Transactional
	public String replysingo(Integer pnum,Integer rnum, 
			RedirectAttributes rttr) {
		log.info("p : " + pnum + ", r : " + rnum);
		
		String view;
		
		bDao.replysingo(rnum);
		
		view = "redirect:contents?pnum=" + pnum;
		rttr.addFlashAttribute("msg", "신고접수가 되었습니다.");
		
		
		return view;
		
	}
	
	//건의사항 글 작성
	@Transactional
	public String SugData (MultipartHttpServletRequest multi,
			RedirectAttributes rttr) {
		log.info("SugData()");
		
		String view = null;
		
		String sug_title = multi.getParameter("sug_title");
		String sug_contents = multi.getParameter("sug_contents");
		String sug_mid = multi.getParameter("sug_mid");
		
		//띄어쓰기 제거
		sug_contents = sug_contents.trim();
		
		SuggestDto sug = new SuggestDto();
		sug.setSug_title(sug_title);
		sug.setSug_contents(sug_contents);
		sug.setSug_mid(sug_mid);
		
		log.info("sug :"+ sug);
		
		try {
			//게시글 저장
			bDao.SugInsert(sug);
			
			view = "redirect:bSugList";
			rttr.addFlashAttribute("msg", "글 등록 성공");
			
		}catch (Exception e) {
			e.printStackTrace();
			view = "redirect:bSugList";
			rttr.addFlashAttribute("msg", "글 등록 실패");
		}
				
		
		return view;
	}
	
	//건의사항 업데이트전 기존 내용 전달
		@Transactional
		public ModelAndView sugupdate(int snum) {
			mv = new ModelAndView();
			
			//1행 가져오기
			SuggestDto sList = bDao.getContent_5(snum);
			
			log.info("sList : " + sList);
			//가져온 데이터 넘겨주기
			mv.addObject("sList", sList);
			mv.setViewName("bSugModifyProc");
			
			return mv;
		}
		//건의사항 업데이트 데이터 업로드
		@Transactional
		public String SugUpdate(MultipartHttpServletRequest multi,
						RedirectAttributes rttr) {
			String view = null;
			
			String sug_num = multi.getParameter("sug_num");
			String sug_title = multi.getParameter("sug_title");
			String sug_contents = multi.getParameter("sug_contents");
			
			//게시글 띄어쓰기 정렬
			sug_contents = sug_contents.trim();
			
			
			SuggestDto sug = new SuggestDto();
			sug.setSug_num(Integer.parseInt(sug_num));
			sug.setSug_title(sug_title);
			sug.setSug_contents(sug_contents);			
			
			try {
				bDao.sugUpdate(sug);
				
				rttr.addFlashAttribute("msg", "수정 성공");
				view = "redirect:bSugList";
			}catch (Exception e) {
				e.printStackTrace();
				rttr.addFlashAttribute("msg", "수정 실패");
				view = "redirect:bSugModifyProc?sug_num=" + sug_num;
			}
			
			return view;
		}
		
	//건의사항 삭제 처리
		@Transactional
		public String Sugdel(Integer snum, RedirectAttributes rttr) {
			
			String view;
			
			//받아온 게시글 넘버로 해당 게시글 1행 삭제
			try {
			bDao.Sugdelete(snum);
			
			view = "redirect:bSugList";
			rttr.addFlashAttribute("msg", "게시글 삭제 성공");
			}catch (Exception e) {
				view = "redirect:bSugPost?sug_num=" + snum;
				rttr.addFlashAttribute("msg", "게시글 삭제 실패");
			}
			
			return view;
		}
		@Transactional
		public ModelAndView HomeList(Integer pageNum) {
			mv = new ModelAndView();

			int num = (pageNum == null) ? 1: pageNum;
					
			List<PostDto> homeList_1 = bDao.homeList_1(num);
			log.info("homeList_1 : " + homeList_1);
			
			List<PostDto> homeList_2 = bDao.homeList_2(num);
			log.info("homeList_2 : " + homeList_2);
			
			List<PostDto> homeList_3 = bDao.homeList_3(num);
			log.info("homeList_3 : " + homeList_3);

			//DB에서 조회한 데이터 목록을 모델에 추가
			mv.addObject("homeList_1", homeList_1);
			mv.addObject("homeList_2", homeList_2);
			mv.addObject("homeList_3", homeList_3);

			mv.setViewName("home");

			return mv;
		}

}













