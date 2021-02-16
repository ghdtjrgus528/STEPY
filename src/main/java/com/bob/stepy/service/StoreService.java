package com.bob.stepy.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dao.StoreDao;
import com.bob.stepy.dto.CeoDto;
import com.bob.stepy.dto.FileUpDto;
import com.bob.stepy.dto.InCartDto;
import com.bob.stepy.dto.MemberDto;
import com.bob.stepy.dto.ProductDto;
import com.bob.stepy.dto.ResTicketDto;
import com.bob.stepy.dto.StoreDto;

import lombok.extern.java.Log;

@Service
@Log
public class StoreService {
	
	@Autowired
	private StoreDao stDao;
	
	@Autowired
	private HttpSession session;
	
	private ModelAndView mv;
		
	//입점신청
	@Transactional
	public String stJoinProc(CeoDto ceo, StoreDto store, RedirectAttributes rttr, MultipartHttpServletRequest multi) {
		log.info("stJoinProc()");
		String view = null;
		
		//암호화 처리
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();		
		String encStPwd = pwdEncoder.encode(multi.getParameter("c_pwd"));		
		
		String cnum = multi.getParameter("c_num");
		String cemail = multi.getParameter("c_email");
		String cname = multi.getParameter("c_name");
		String cphone = multi.getParameter("c_phone");
		String sphone = multi.getParameter("s_phone");
		String sname = multi.getParameter("s_name");
		String saddr1 = multi.getParameter("s_addr1");
		String saddr2 = multi.getParameter("s_addr2");
		String saddr3 = multi.getParameter("s_addr3");
		String ssummary = multi.getParameter("s_summary");
		String scategory = multi.getParameter("s_category");
		String check = multi.getParameter("fileCheck");
		
		ssummary = ssummary.trim();
		
		ceo = new CeoDto();
		ceo.setC_num(cnum);
		ceo.setC_pwd(encStPwd);
		ceo.setC_email(cemail);
		ceo.setC_name(cname);
		ceo.setC_phone(cphone);
				
		store = new StoreDto();
		store.setS_num(cnum);
		store.setS_phone(sphone);
		store.setS_name(sname);
		store.setS_addr1(saddr1);
		store.setS_addr2(saddr2);
		store.setS_addr3(saddr3);
		store.setS_summary(ssummary);
		store.setS_category(scategory);		
		
		try {
			stDao.cJoinProc(ceo);
			stDao.stJoinProc(store);
			
			if(check.equals("1")) {
				stFileUp(multi, ceo.getC_num());
			}			
			view = "redirect:stHome";
			rttr.addFlashAttribute("msg", "가입이 완료되었습니다. 관리자 인증 후 로그인 가능합니다.");			
		} catch(Exception e) {
			e.printStackTrace();
			view = "redirect:stJoinFrm";
			rttr.addFlashAttribute("msg", "가입 실패. 다시 시도하여 주십시오.");
		}		
		return view;
	}	
	
	//사장님 로그인
	public String stLoginProc(CeoDto ceo, StoreDto store, RedirectAttributes rttr) {
		log.info("stLoginProc()");
		String view = null;
		String join = stDao.getCjoin(ceo.getC_num());

		String stEncPwd = stDao.getStEncPwd(ceo.getC_num());

		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();

		//메일 승인 받은 업체만 로그인
		if(join.equals("approve")) {			
			if(stEncPwd != null) {
				if(pwdEncoder.matches(ceo.getC_pwd(), stEncPwd)) {
					ceo = stDao.getCeoInfo(ceo.getC_num());
					store = stDao.getStoreInfo(ceo.getC_num());
					MemberDto member = new MemberDto();
					//상품 페이지 조회를 위해 사업자 정보도 member로 session에 넣어줌
					member.setM_id(ceo.getC_num());

					session.setAttribute("ceo", ceo);
					session.setAttribute("store", store);
					session.setAttribute("member", member);
					view = "redirect:stMyPage";
				} else {
					view = "redirect:stHome";
					rttr.addFlashAttribute("msg", "비밀번호가 틀립니다");
				}
			} else {
				view = "redirect:stHome";
				rttr.addFlashAttribute("msg", "등록된 아이디가 없습니다");
			}			
		} else {
			view = "redirect:stHome";
			rttr.addFlashAttribute("msg", "STEPY 관리자의 승인 후 로그인 가능합니다. 메일을 확인해주세요.");
		}				
		return view;
	}

	//사업자번호 중복체크
	public String stIdCheck(String c_num) {
		log.info("stIdCheck() cnum: " + c_num);
		
		String result = null;
		int cnt = stDao.stIdCheck(c_num); // 0 OR 1
		
		if(cnt == 0) {
			result = "success";
		} else {
			result = "fail";
		}
		return result;
	}

	//사업자등록증 업로드 처리
	public boolean stFileUp(MultipartHttpServletRequest multi, String c_num) throws Exception {
		
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload/";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
			
		Map<String, String> fmap = new HashMap<String, String>();
		fmap.put("c_num", String.valueOf(c_num));
		
		List<MultipartFile> fList = multi.getFiles("files");
		
		for(int i = 0; i < fList.size(); i++) {
			MultipartFile mf = fList.get(i);
			String on = mf.getOriginalFilename();
			fmap.put("f_oriname", on);
			
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf(".") + 1);
			fmap.put("f_sysname", sn);
			
			mf.transferTo(new File(path + sn));
			stDao.stFileUp(fmap);
		}		
		return true;
	}
	
	//비밀번호 찾기 - 사업자번호 입력
	public String stFindPwdProc(HttpServletRequest request, RedirectAttributes rttr) {
		log.info("stFindPwdProc()");		
		
		mv = new ModelAndView();
		String result = null;
		
		String cnum = request.getParameter("c_num");
		int cnt = stDao.stIdCheck(cnum); //입력한 사업자번호 카운트
		
		String cjoin = null;
		cjoin = stDao.getCjoin(cnum);	
		
		if(cnt == 1) { //해당 정보가 있음
			if(cjoin.equals("pending")) { //pending인 업체
				rttr.addFlashAttribute("msg", "미승인 업체는 비밀번호 변경이 불가합니다.");
				result = "redirect:stHome";			
			} else { //approve 업체
				CeoDto ceo = new CeoDto();
				ceo = stDao.getCeoInfo(cnum);
				session.setAttribute("ceo", ceo);
				result = "redirect:stCheckInfoFrm";
			}			
		} else { //해당 정보가 없거나 틀릴 때
			rttr.addFlashAttribute("msg", "일치하는 정보가 없습니다.");
			result = "redirect:stFindPwdFrm";
		}		
		return result;
	}
	
	//비밀번호 찾기 - 본인인증
	public String stCheckInfoProc(HttpServletRequest request, RedirectAttributes rttr) {
		log.info("stCheckInfoProc()");
		
		String result = null;
		CeoDto ceo = new CeoDto();
		ceo = (CeoDto)session.getAttribute("ceo");
		String cnum = ceo.getC_num();
		String cname = request.getParameter("c_name");
		String cphone = request.getParameter("c_phone");
		String cemail = request.getParameter("c_email");		
		
		if(((ceo.getC_name()).equals(cname)) && ((ceo.getC_phone()).equals(cphone)) && ((ceo.getC_email()).equals(cemail))) {
			rttr.addFlashAttribute("c_num", cnum);
			result = "redirect:stResetPwdFrm";
		} else {
			rttr.addFlashAttribute("msg", "일치하는 정보가 없습니다.");
			result = "redirect:stCheckInfoFrm";
		}
		return result;
	}
	
	//비밀번호 찾기 - 비번 변경
	@Transactional
	public String stResetPwdProc(HttpServletRequest request, RedirectAttributes rttr) {
		log.info("stResetPwdProc()");
		
		String result = null;
		CeoDto ceo = new CeoDto();
		ceo = (CeoDto)session.getAttribute("ceo");
		String cnum = ceo.getC_num();
		
		//재암호화
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();		
		String encStPwd = pwdEncoder.encode(request.getParameter("c_pwd"));
				
		ceo.setC_pwd(encStPwd);
		ceo.setC_num(cnum);
		
		try {
			stDao.stResetPwdProc(ceo);
			result = "redirect:stHome";
			rttr.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");	
			session.invalidate();
		} catch(Exception e) {
			//e.printStackTrace();
			result = "redirect:stFindPwdFrm";
			rttr.addFlashAttribute("msg", "비밀번호 변경에 실패하였습니다.");		
		}				
		return result;
	}

	//상품 리스트
	public ModelAndView stMyProd(CeoDto ceo, String pl_cnum) {
		log.info("stMyProd()");
		mv = new ModelAndView();		
		
		ceo = (CeoDto)session.getAttribute("ceo");
		pl_cnum = ceo.getC_num();		
		
		//사업주가 올린 상품 리스트
		List<ProductDto> pList = stDao.getProdList(pl_cnum);
		List<FileUpDto> tList = new ArrayList<FileUpDto>();
		Map<String, Object> ptMap = new LinkedHashMap<String, Object>(); //순서 유지를 위해 linkedhashmap으로 받음
		
		for(int i = 0; i < pList.size(); i++) {
			//각 상품의 썸네일
			FileUpDto fDto = new FileUpDto();
			fDto = stDao.getProdThumb(pList.get(i).getPl_num());
			tList.add(i, fDto);	
			
			for(int j = 0; j < tList.size(); j++) {
				ptMap.put(tList.get(j).getF_sysname(), pList.get(j));
			}
			fDto = null;			
		}					
		if(pList != null) { //상품이 있다면	
			mv.addObject("ceo", ceo);
			mv.addObject("pList", pList);
			mv.addObject("tList", tList); 
			mv.addObject("ptMap", ptMap);
			mv.setViewName("stMyProd");
		}		
		return mv;
	}

	//상품 등록하기
	@Transactional
	public String stWriteProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stWriteProc()");
		
		String view = null;
		
		String pcnum = multi.getParameter("pl_cnum");
		log.info(pcnum);
		
		String name = multi.getParameter("pl_name");
		int price = Integer.parseInt(multi.getParameter("pl_price"));
		int person = Integer.parseInt(multi.getParameter("pl_person"));
		int quantity = Integer.parseInt(multi.getParameter("pl_qty"));
		String text = multi.getParameter("pl_text");
		String check = multi.getParameter("fileCheck");
		
		text = text.trim();
		
		ProductDto product = new ProductDto();
		product.setPl_cnum(pcnum);
		product.setPl_name(name);
		product.setPl_price(price);
		product.setPl_person(person);
		product.setPl_qty(quantity);
		product.setPl_text(text);
		
		try {
			stDao.stProdInsert(product);
			
			if(check.equals("1")) {
				stProdThumbUp(multi, product.getPl_num());
			}			
			view = "redirect:stMyProd";
			rttr.addFlashAttribute("msg", "상품이 성공적으로 등록되었습니다.");				
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:stMyProd";
			rttr.addFlashAttribute("msg", "상품 등록에 실패하였습니다.");
		}		
		return view;
	}
	
	//상품 대표사진 등록
	public boolean stProdThumbUp(MultipartHttpServletRequest multi, int pl_num) throws Exception {
		
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
			
		Map<String, String> tmap = new HashMap<String, String>();
		tmap.put("pl_num", String.valueOf(pl_num));
		
		List<MultipartFile> pList = multi.getFiles("files");
		
		for(int i = 0; i < pList.size(); i++) {
			MultipartFile mf = pList.get(i);
			String on = mf.getOriginalFilename();
			tmap.put("f_oriname", on);
			
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf(".") + 1);
			tmap.put("f_sysname", sn);
			
			mf.transferTo(new File(path + sn));
			stDao.stProdThumbUp(tmap);
		}		
		return true;
	}

	//상품 사진 추가 화면
	public ModelAndView stAddProdPhotos(Integer pl_num){
		mv = new ModelAndView();		
		session.setAttribute("pl_num", pl_num);

		List<FileUpDto> photoList = stDao.getProdPhotos(pl_num);

		mv.addObject("photoList", photoList);
		mv.setViewName("stAddProdPhotos");

		return mv;
	}

	//해당 상품 사진 추가
	public String stAddProdPhotoProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stAddProdPhotoProc()");

		String view = null;		
		String check = multi.getParameter("fileCheck");
		int plnum = (Integer) session.getAttribute("pl_num");				

		try {
			if(check.equals("1")) {
				stProdFileUp(multi, plnum);
			}
			view = "redirect:stAddProdPhotos?pl_num=" + plnum;
			rttr.addFlashAttribute("msg", "상품 사진이 추가되었습니다.");
			session.removeAttribute("pl_num");
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:stAddProdPhotos?pl_num=" + plnum;
			rttr.addFlashAttribute("msg", "사진 등록에 실패하였습니다.");			
		}		
		return view;
	}

	//상품 사진 등록
	public boolean stProdFileUp(MultipartHttpServletRequest multi, int pl_num) throws Exception {
		
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
			
		Map<String, String> pmap = new HashMap<String, String>();
		pmap.put("pl_num", String.valueOf(pl_num));
		
		List<MultipartFile> pList = multi.getFiles("files");
		
		for(int i = 0; i < pList.size(); i++) {
			MultipartFile mf = pList.get(i);
			String on = mf.getOriginalFilename();
			pmap.put("f_oriname", on);
			
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf(".") + 1);
			pmap.put("f_sysname", sn);
			
			mf.transferTo(new File(path + sn));
			stDao.stProdFileUp(pmap);
		}		
		return true;
	}
	
	//사업주, 매장 정보 변경
	@Transactional
	public String stModifyInfo(CeoDto ceo, StoreDto store, RedirectAttributes rttr) {
		log.info("stModifyInfo()");
		String view = null;
		
		store.setS_num(ceo.getC_num());	
		
		try {			
			stDao.stModifyCeo(ceo);
			stDao.stModifyStore(store);
			session.setAttribute("ceo", ceo);
			session.setAttribute("store", store);
			
			view = "redirect:/stMyPage";
			rttr.addFlashAttribute("msg", "정보가 변경되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			view = "redirect:/stMyPage";
			rttr.addFlashAttribute("msg", "정보 변경에 실패하였습니다.");			
		}		
		return view;
	}
	
	//사업주 비밀번호 변경
	@Transactional
	public String stModifyPwd(RedirectAttributes rttr, HttpServletRequest request) {
		log.info("stModifyPwd");
		
		CeoDto ceo = new CeoDto();
		ceo = (CeoDto)session.getAttribute("ceo");
		
		String view = null;
		String DBpwd = null; //DB에 있는 기존 비밀번호
		String nowPwd = request.getParameter("cpwd"); //입력한 현재 비밀번호
		log.info(nowPwd);
		
		DBpwd = stDao.stGetPwd(ceo.getC_num());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();		
		
		//현재 비밀번호와 일치할 경우
		if(pwdEncoder.matches(nowPwd, DBpwd)) { //db와 입력한 현재 비밀번호가 일치할 땐
			
			String newPwd = pwdEncoder.encode(request.getParameter("c_pwd"));
			ceo.setC_pwd(newPwd); //암호화 처리해서 다시 집어넣고
			
			try {
				stDao.stModifyPwd(ceo);				
				view = "redirect:stMyPage";
				rttr.addFlashAttribute("msg", "비밀번호를 변경하였습니다.");				
			} catch(Exception e) {
				e.printStackTrace();
				view = "redirect:stMyPage";
				rttr.addFlashAttribute("msg", "비밀번호 변경에 실패하였습니다.");
			}			
		} else {
			//틀린 경우
			view = "redirect:stModifyPwdFrm";
			rttr.addFlashAttribute("msg", "현재 비밀번호가 틀립니다. 다시 확인하여 주세요.");			
		}		
		return view;		
	}
	
	//로그아웃
	public String stLogout() {
		log.info("stLogout()");
		session.invalidate();
		return "stHome";		
	}
	
	//가게 썸네일 노출
	public ModelAndView stGetThumb() {
		mv = new ModelAndView();
		
		CeoDto ceo = new CeoDto();
		ceo = (CeoDto)session.getAttribute("ceo");
		
		FileUpDto file = new FileUpDto();
		file = stDao.getThumb(ceo.getC_num());
		
		mv.addObject("stThumb", file);
		mv.setViewName("stMyThumb");
		
		return mv;
	}
	
	//가게 썸네일
	public String stMyThumbProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stMyThumbProc()");
		
		CeoDto ceo = new CeoDto();
		ceo = (CeoDto)session.getAttribute("ceo");
		
		String view = null;
		String check = multi.getParameter("fileCheck");
		
		try {
			if(check.equals("1")) {
				stThumbUp(multi, ceo.getC_num());
			}
			view = "redirect:stMyThumb";
			rttr.addFlashAttribute("msg", "스토어 대표사진이 등록되었습니다.");			
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:stMyThumb";
			rttr.addFlashAttribute("msg", "스토어 대표사진 등록에 실패하였습니다.");			
		}		
		return view;
	}
	
	//가게 썸네일 업로드
	public boolean stThumbUp(MultipartHttpServletRequest multi, String pl_cnum) throws Exception {
		
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
			
		Map<String, String> smap = new HashMap<String, String>();
		smap.put("pl_cnum", String.valueOf(pl_cnum));
		
		List<MultipartFile> pList = multi.getFiles("files");
		
		for(int i = 0; i < pList.size(); i++) {
			MultipartFile mf = pList.get(i);
			String on = mf.getOriginalFilename();
			smap.put("f_oriname", on);
			
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf(".") + 1);
			smap.put("f_sysname", sn);
			
			mf.transferTo(new File(path + sn));
			stDao.stThumbUp(smap);
		}		
		return true;
	}
	
	//가게 사진 가져오기
	public ModelAndView stGetPhoto() {
		mv = new ModelAndView();
		
		CeoDto ceo = (CeoDto)session.getAttribute("ceo");		
		
		List<FileUpDto> photoList = stDao.getPhotos(ceo.getC_num()); //사업자가 올린 가게 사진들
		
		mv.addObject("photoList", photoList);
		mv.setViewName("stExtraPhoto");
		
		return mv;
	}
	//가게 사진 추가 업로드
	public String stExtraPhotoProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stExtraPhotoProc()");
		
		CeoDto ceo = new CeoDto();
		ceo = (CeoDto)session.getAttribute("ceo");
		
		String view = null;
		String check = multi.getParameter("fileCheck");
		
		try {
			if(check.equals("1")) {
				stPhotoUp(multi, ceo.getC_num());
			}
			view = "redirect:stExtraPhoto";
			rttr.addFlashAttribute("msg", "스토어 사진이 추가되었습니다.");			
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:stExtraPhoto";
			rttr.addFlashAttribute("msg", "스토어 추가사진 등록에 실패하였습니다.");			
		}				
		return view;
	}	
	
	//가게 사진 추가 업로드
	public boolean stPhotoUp(MultipartHttpServletRequest multi, String pl_cnum) throws Exception {
		
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
			
		Map<String, String> smap = new HashMap<String, String>();
		smap.put("pl_cnum", String.valueOf(pl_cnum));
		
		List<MultipartFile> pList = multi.getFiles("files");
		
		for(int i = 0; i < pList.size(); i++) {
			MultipartFile mf = pList.get(i);
			String on = mf.getOriginalFilename();
			smap.put("f_oriname", on);
			
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf(".") + 1);
			smap.put("f_sysname", sn);
			
			mf.transferTo(new File(path + sn));
			stDao.stPhotoUp(smap);
		}		
		return true;
	}
	
	//스토어 대표사진 삭제
	@Transactional
	public String stDeleteThumb(String f_sysname) {
		String path = session.getServletContext().getRealPath("/");
		path += "resources/" + "upload" + f_sysname;		
		
		String result = null;
		
		try {
			stDao.stDeleteThumb(f_sysname); //db 지우고			
			File file = new File(path);
			
			if(file.exists()) { //해당 경로에 있다면  				
				if(file.delete()) { //지운다					
					result = "success";									
				} else {	
					result = "fail";					
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}		
		return result;
	}
	
	//상품 상세 페이지
		public ModelAndView stGetDetail(Integer pl_num, String s_num) {
			log.info("stGetDetail()");
			mv = new ModelAndView();
			
			//해당 상품 사진(메인, 추가)과 상품 정보		
			FileUpDto fDto = new FileUpDto();
			List<FileUpDto> photoList = new ArrayList<FileUpDto>();
			ProductDto product = new ProductDto();
			StoreDto store = new StoreDto();
			
			fDto = stDao.getProdThumb(pl_num); //상품 메인사진
			photoList = stDao.getProdPhotos(pl_num); //상품 추가사진
			product = stDao.getProdInfo(pl_num); //상품 정보
			store = stDao.getStoreInfo(s_num);
			
			mv.addObject("fDto", fDto);
			mv.addObject("photoList", photoList);
			mv.addObject("product", product);
			mv.addObject("store", store);
			mv.setViewName("stDetail");
			
			return mv;
		}	
		
	//가게 상세 페이지
	public ModelAndView plProductList(String c_num) {
		mv = new ModelAndView();
		int zzim = -1;
	
		MemberDto member = (MemberDto) session.getAttribute("member");
		List<FileUpDto> prodThumbList = new ArrayList<FileUpDto>(); //상품 메인사진 리스트
		Map<String, Object> ptMap = new LinkedHashMap<String, Object>(); //상품번호와 해당 메인사진의 컬렉션
		
		InCartDto incart = new InCartDto();
		incart.setIc_cnum(c_num);
		incart.setIc_mid(member.getM_id());
			
		try {
			StoreDto store = stDao.getStoreInfo(c_num);
			FileUpDto fDto = stDao.getThumb(c_num);
			List<ProductDto> pList = stDao.getProdList(c_num);
			List<FileUpDto> photoList = stDao.getPhotos(c_num);
			zzim = stDao.GetIncart(incart);
			System.out.println(zzim);
				
			for(int i = 0; i < pList.size(); i++) {
				FileUpDto prodThumb = new FileUpDto();
				prodThumb = stDao.getProdThumb(pList.get(i).getPl_num());
				prodThumbList.add(i, prodThumb);
				
				for(int j = 0; j < prodThumbList.size(); j++) {
					ptMap.put(prodThumbList.get(j).getF_sysname(), pList.get(j));
				}
				prodThumb = null;			
			} 			
			mv.addObject("store", store);
			mv.addObject("fDto", fDto);
			mv.addObject("pList", pList);
			mv.addObject("photoList", photoList);
			mv.addObject("ptMap", ptMap);
			mv.addObject("member", member);
			mv.addObject("zzim", zzim);				
			mv.setViewName("plProductList");
				
		} catch(Exception e) {
			e.printStackTrace();		
		}
		return mv;
	}
		
	//미인증 업체 리스트
	public ModelAndView getAuthList() {
		mv = new ModelAndView();	

		List<CeoDto> ceoList = stDao.getAuthList();
		FileUpDto fDto = new FileUpDto();
		List<FileUpDto> bizList = new ArrayList<FileUpDto>();
		Map<String, Object> bizMap = new HashMap<String, Object>();

		for(int i = 0; i < ceoList.size(); i++) {
			fDto = stDao.stGetBiz(ceoList.get(i).getC_num()); //각 사업자들 등록증 가져오고
			bizList.add(i, fDto); //사업자 등록증 리스트에 넣어준다
			for(int j = 0; j < bizList.size(); j++) {
				bizMap.put(bizList.get(j).getF_sysname(), ceoList.get(j)); //사업자번호와 사업자등록증 파일 매칭
			}
			fDto = null;
		}		
		mv.addObject("ceoList", ceoList);
		mv.addObject("bizMap", bizMap);
		mv.setViewName("stAuthMail");

		return mv;		
	}
		
	//찜할 때
	public void stIncart(String m_id, String c_num) {
		InCartDto incart = new InCartDto();
		MemberDto member = (MemberDto)session.getAttribute("member");

		incart.setIc_cnum(c_num);
		incart.setIc_mid(member.getM_id());

		stDao.stIncart(incart);
	}

	//찜 제외할 때
	public void stIncartEmpty(String m_id, String c_num) {
		InCartDto incart = new InCartDto();
		MemberDto member = (MemberDto)session.getAttribute("member");

		incart.setIc_cnum(c_num);
		incart.setIc_mid(member.getM_id());

		stDao.stIncartEmpty(incart);
	}
	
	//상품 정보 및 사진 모두 삭제
	@Transactional
	public String stDeleteProd(Integer pl_num) {
		String result = null;		
		List<FileUpDto> prodPhotoList = stDao.stGetProdPhotos(pl_num);	
		
		try {
			for(int i = 0; i < prodPhotoList.size(); i++) {
				stDao.stDeleteThumb(prodPhotoList.get(i).getF_sysname());
			}
			stDao.stDeleteProd(pl_num);
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";			
		}		
		return result;
	}

	//인증성공 페이지 처리
	public String stAuthorized(String c_num, String key) {
		String view = null;
		try {
			stDao.stUpdateJoin(c_num);
			view = "redirect:stAuthorized";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	//스토어 추가사진 삭제하기
	@Transactional
	public String stDeletePhotos(String c_num) {
		String result = null;

		//스토어 추가사진 파일 삭제
		List<String> sysList = stDao.getPtsSys(c_num); //sysname들 가져오고
		System.out.println(sysList);

		try {
			//파일 삭제			
			for(int i = 0; i < sysList.size(); i++) {
				String path = session.getServletContext().getRealPath("/");				
				path += "resources/" + "upload" + sysList.get(i);
				System.out.print(path);
				File file = new File(path);
				if(file.exists()) {
					if(file.delete()) {
						result="success";
					} else {
						result = "fail";
					}
				}
				path = "";
			}
			//스토어 추가사진 DB 삭제
			stDao.stDeletePhotos(c_num);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//예약 현황 보기
	public ModelAndView stResList() {		
		log.info("stResList()");
		mv = new ModelAndView();

		CeoDto ceo = (CeoDto)session.getAttribute("ceo");

		//상품 재고
		List<ProductDto> prodList = stDao.getProdList(ceo.getC_num());
		
		//결제완료된 예약 리스트
		List<ResTicketDto> resList = new ArrayList<ResTicketDto>();
		Map<Integer, Object> resMap = new HashMap<Integer, Object>();
		
		for(int i = 0; i < prodList.size(); i++) {
			resList = stDao.stTotalResList(prodList.get(i).getPl_num());
			
			if(resList != null) {				
				for(int j = 0; j < resList.size(); j++) {
					resMap.put(resList.get(j).getRes_plnum(), resList.get(j)); //상품번호-상품예약정보
				}										
			}
			resList = null;
		}
		mv.addObject("resMap", resMap);
		mv.addObject("prodList", prodList);
		mv.setViewName("stResList");		

		return mv;
	}
	
	//예약 리스트 - showinfo
	public ModelAndView getResInfo(int res_num) {
		mv = new ModelAndView();
		String plname = null;
		
		ResTicketDto res = stDao.getResInfo(res_num);
		plname = stDao.getPlname(res.getRes_plnum());
		
		try {
			mv.addObject("res", res);
			mv.addObject("res_plname", plname);
			mv.setViewName("stShowInfo");
		} catch(Exception e) {
			e.printStackTrace();
			mv.setViewName("stShowInfo");
		}		
		return mv;
	}
	
	
	
	
	
	
	
	
	
	//인증메일 보내기
	public void stAuthMail(String c_email, String c_num) {
		Properties p = System.getProperties();

		p.put("mail.smtp.starttls.enable", "true");     
		p.put("mail.smtp.host", "smtp.gmail.com");     
		p.put("mail.smtp.auth","true");                 
		p.put("mail.smtp.port", "587");                 

		Authenticator auth = new MyAuthentication();
		Session session = Session.getDefaultInstance(p, auth);
		MimeMessage msg = new MimeMessage(session);

		//authkey 발급
		Random random = new Random();
		String authkey = "";

		for(int i = 0; i<3 ; i++) {
			int rand_char = random.nextInt(26)+65;
			authkey += (char)rand_char;
		}

		int rand_int = random.nextInt(9000)+1000 ;
		authkey += rand_int;


		try{	    
			InternetAddress from = new InternetAddress() ;
			from = new InternetAddress("stepy.tester@gmail.com");
			msg.setFrom(from);

			InternetAddress to = new InternetAddress(c_email);
			msg.setRecipient(Message.RecipientType.TO, to);

			msg.setSubject("STEPY 업체 회원가입 인증메일입니다", "UTF-8");
			msg.setText("안녕하세요 " + c_num + "님, <br>인증하기 버튼을 누르시면 로그인을 하실 수 있습니다."
					+ "<a href='http://localhost" + "/stAuthorized?c_num=" + c_num
					+ "&key=" + authkey + "'>인증하기</a>", "UTF-8");
			msg.setHeader("content-Type", "text/html");			

			javax.mail.Transport.send(msg);

			} catch (AddressException addr_e) {
				addr_e.printStackTrace();
			} catch (MessagingException msg_e) {
				msg_e.printStackTrace();
		}
	}
}

class MyAuthentication extends Authenticator {
	PasswordAuthentication pa;

	public MyAuthentication() {

		String id = "stepy.tester@gmail.com";       
		String pw = "stepy1234!";     

		pa = new PasswordAuthentication(id, pw);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}	
	
}//class end
