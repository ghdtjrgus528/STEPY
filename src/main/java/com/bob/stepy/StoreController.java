package com.bob.stepy;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dto.CeoDto;
import com.bob.stepy.dto.StoreDto;
import com.bob.stepy.service.StoreService;

import lombok.extern.java.Log;

@Controller
@Log
public class StoreController {
	
	@Autowired
	private StoreService stServ;
	
	private ModelAndView mv;	

	
	@GetMapping("stMyPage")
	public String stMyPage() {
		log.info("stMyPage()");
		return "stMyPage";
	}
	
	@GetMapping("stResList")
	public ModelAndView stResList() {
		log.info("stResList()");
		mv = stServ.stResList();
		return mv;
	}	

	@GetMapping("stShowInfo")
	public ModelAndView stShowInfo(int res_num) {
		log.info("stShowInfo()");
		mv = stServ.getResInfo(res_num);
		return mv;
	}
	
	/*
	@GetMapping("stReviewList")
	public String stReviewList() {
		log.info("stReviewList()");
		return "stReviewList";
	}
	
	@GetMapping("stReportList")
	public String stReportList() {
		log.info("stReportList()");
		return "stReportList";
	}
	*/
	
	@GetMapping(value="stIdCheck", produces="application/text; charset=utf-8")
	@ResponseBody
	public String stIdCheck(String c_num) {
		log.info("stIdCheck() cnum: " + c_num);
		String result = stServ.stIdCheck(c_num);
		return result;
	}			
	
	@GetMapping("stJoinFrm")
	public String stJoinFrm() {
		log.info("stJoinFrm()");
		return "stJoinFrm";
	}
	
	@PostMapping("stJoinProc")
	public String stJoinProc(CeoDto ceo, StoreDto store, RedirectAttributes rttr, MultipartHttpServletRequest multi) {
		log.info("stJoinProc()");
		String view = stServ.stJoinProc(ceo, store, rttr, multi);
		return view;
	}
	
	@GetMapping("stHome")
	public String stHome() {
		log.info("stHome()");
		return "stHome";
	}
	
	@PostMapping("stLoginProc")
	public String stLoginProc(CeoDto ceo, StoreDto store, RedirectAttributes rttr) {
		log.info("stLoginProc()");
		String view = stServ.stLoginProc(ceo, store, rttr);
		return view;
	}
	
	@GetMapping("stFindPwdFrm")
	public String stFindPwdFrm() {
		log.info("stFindPwdFrm()");
		return "stFindPwdFrm";
	}
	
	@PostMapping("stFindPwdProc")
	public String stFindPwdProc(HttpServletRequest request, RedirectAttributes rttr) {
		log.info("stFindPwdProc()");
		String result = stServ.stFindPwdProc(request, rttr);		
		return result;
	}
	
	@GetMapping("stCheckInfoFrm")
	public String stCheckInfoFrm() {
		log.info("stCheckInfoFrm()");
		return "stCheckInfoFrm";
	}
	
	@PostMapping("stCheckInfoProc")
	public String stCheckInfoProc(HttpServletRequest request, RedirectAttributes rttr) {
		log.info("stCheckInfoProc()");
		String result = stServ.stCheckInfoProc(request, rttr);
		return result;
	}
	
	@GetMapping("stResetPwdFrm")
	public String stResetPwdFrm() {
		log.info("stResetPwdFrm()");
		return "stResetPwdFrm";
	}
	
	@PostMapping("stResetPwdProc")
	public String stResetPwdProc(HttpServletRequest request, RedirectAttributes rttr) {
		log.info("stResetPwdProc()");
		String result = stServ.stResetPwdProc(request, rttr);
		return result;
	}
	
	@GetMapping("stMyProd")
	public ModelAndView getProdList(CeoDto ceo, String pl_cnum) {
		log.info("getProdList()");
		mv = stServ.stMyProd(ceo, pl_cnum);
		return mv;
	}
	
	@GetMapping("stWriteFrm")
	public String stWriteFrm() {
		log.info("stWriteFrm()");
		return "stWriteFrm";
	}	
	
	@PostMapping("stWriteProc")
	public String stWriteProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stWriteProc()");
		String view = stServ.stWriteProc(multi, rttr);		
		return view;
	}
	
	@GetMapping("stModifyInfoFrm")
	public String stModifyInfoFrm() {
		log.info("stModifyInfoFrm()");
		return "stModifyInfoFrm";
	}
	
	@PostMapping("stModifyInfo")
	public String stModifyInfo(CeoDto ceo, StoreDto store, RedirectAttributes rttr) {
		String view = stServ.stModifyInfo(ceo, store, rttr);		
		return view;
	}
	
	@GetMapping("stModifyPwdFrm")
	public String stModifyPwdFrm() {
		log.info("stModifyPwdFrm()");
		return "stModifyPwdFrm";
	}
	
	@PostMapping("stModifyPwd")
	public String stModifyPwd(RedirectAttributes rttr, HttpServletRequest request) {
		log.info("stModifyPwd()");
		String view = stServ.stModifyPwd(rttr, request);
		return view;
	}
	
	@GetMapping("stLogout")
	public String stLogout() {
		log.info("stLogout()");
		return stServ.stLogout();
	}
	
	@GetMapping("stModifyProdFrm")
	public String stModifyProdFrm() {
		log.info("stModifyProdFrm()");
		return "stModifyProdFrm";
	}
	
	@GetMapping("stMyThumb")
	public ModelAndView stMyThumb() {
		log.info("stMyThumb()");
		mv = stServ.stGetThumb();
		return mv;
	}
	
	@PostMapping("stMyThumbProc")
	public String stMyThumbProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stMyThumbProc");
		String view = stServ.stMyThumbProc(multi, rttr);
		return view;		
	}
	
	@PostMapping(value = "delThumb", produces = "application/text; charset=utf-8")
	@ResponseBody
	public String stDeleteThumb(String f_sysname, String f_cnum) {
		log.info("stDeleteThumb()");
		String result = null;
		result = stServ.stDeleteThumb(f_sysname);
		log.info(result);
		
		return result;
	}
	
	@GetMapping("stExtraPhoto")
	public ModelAndView stExtraPhoto() {
		log.info("stExtraPhoto()");
		mv = stServ.stGetPhoto();	
		return mv;
	}
	
	@GetMapping("stIntro")
	public String stIntro() {
		log.info("stIntro()");
		return "stIntro";
	}
	
	@PostMapping("stExtraPhotoProc")
	public String stExtraPhotoProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stExtraPhotoProc()");
		String view = stServ.stExtraPhotoProc(multi, rttr);
		return view;
	}
	
	@GetMapping("stAddProdPhotos")
	public ModelAndView stAddProdPhotos(Integer pl_num) {
		log.info("stAddProdPhotos()");
		mv = stServ.stAddProdPhotos(pl_num);		
		return mv;
	}

	@PostMapping("stAddProdPhotoProc")
	public String stAddProdPhotoProc(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("stAddProdPhotoProc()");
		String view = stServ.stAddProdPhotoProc(multi, rttr);
		return view;
	}
	
	@GetMapping("stDetail")
	public ModelAndView stGetDetail(Integer pl_num, String s_num) {
		log.info("stGetDetail() pl_num : " + pl_num);
		mv = stServ.stGetDetail(pl_num, s_num);
		return mv;
	}
	
	@GetMapping("plProductList")
	public ModelAndView plProductList(String c_num) {
		log.info("plProductList()");
		mv = stServ.plProductList(c_num);
		return mv;
	}
	
	@GetMapping("stAuthMail")
	public ModelAndView stAuthMail() {
		log.info("stAuthMail()");
		mv = stServ.getAuthList();
		return mv;
	}
	
	@GetMapping(value="inCartHeart", produces="application/text; charset=utf-8")
	@ResponseBody
	public void inCartHeart(String ic_mid, String ic_cnum) {
		log.info("inCartHeart()");
		stServ.stIncart(ic_mid, ic_cnum);
		
	}
	
	@GetMapping(value="inCartHeartEmpty", produces="application/text; charset=utf-8")
	@ResponseBody
	public void inCartHeartEmpty(String ic_mid, String ic_cnum) {
		log.info("inCartHeartEmpty()");
		stServ.stIncartEmpty(ic_mid, ic_cnum);
		
	}
	
	@PostMapping(value = "delProd", produces = "application/text; charset=utf-8")
	@ResponseBody
	public String stDeleteProd(Integer pl_num) {
		log.info("stDeleteProd()");
		String result = null;
		result = stServ.stDeleteProd(pl_num);		
		return result;
	}
	
	@GetMapping("stAuthorized")
	public String stAuthorized(String c_num, String key) {
		log.info("stAuthorized()");
		String view = stServ.stAuthorized(c_num, key);
		return view;
	}
	
	@PostMapping(value = "stAuthMail", produces = "application/json; charset=utf-8")
	@ResponseBody
	public void stAuthMail(String c_email, String c_num, HttpServletRequest request) throws Exception {	
		stServ.stAuthMail(c_email, c_num);
	}	
	
	@PostMapping(value = "stDelPhotos", produces = "application/text; charset=utf-8")
	@ResponseBody
	public String stDeletePhotos(String c_num) {
		log.info("stDeletePhotos()");
		String result = null;
		result = stServ.stDeletePhotos(c_num);	
		return result;
	}


}//class end
