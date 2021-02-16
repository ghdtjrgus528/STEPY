package com.bob.stepy;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dto.MailDto;
import com.bob.stepy.dto.MemberDto;
import com.bob.stepy.dto.MessageDto;
import com.bob.stepy.dto.ReplyDto;
import com.bob.stepy.service.MemberService;

import lombok.extern.java.Log;
import oracle.jdbc.proxy.annotation.Post;



@Controller
@Log
public class MemberController {
	
	@Autowired
	private MemberService mServ;
	private ModelAndView mv;

//-----
	
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	
	@ResponseBody
	@PostMapping(value="mPaiedInFull", produces = "application/text; charset=utf-8")
	public void mPaiedInFull(Integer resnum) {
		
		mServ.mPaiedInFull(resnum);
		
		return;
	}
	
	@GetMapping("mMyPayment")
	public ModelAndView mMyPayment(Integer sort) {
	
		mv = mServ.mMyPayment(sort);
		
		return mv;
	}

	
	@GetMapping("mMyLikedPages")
	public ModelAndView mMyLikedPages(Integer pageNum) {

		mv = mServ.mMyLikedPages(pageNum);
		
		return mv;
	}
	
	
	@GetMapping("mMyCartPages")
	public ModelAndView mMyCartPages() {

		mv = mServ.mGetMyCartItems();
		
		return mv;
	}
	

	@ResponseBody
	@PostMapping(value="mUpdatePwd", produces = "application/text; charset=utf-8")
	public String mUpdatePwd(String m_pwd, String m_id) {
		
		String set = mServ.mUpdatePwd(m_pwd, m_id);
		
		return set;
	}
	
	@ResponseBody
	@PostMapping(value="mGetCrypPwd", produces = "application/text; charset=utf-8")
	public String mGetCrypPwd(String pwd, String m_id) {
		
		String set = mServ.mGetCrypPwd(pwd, m_id);
		
		return set;
	}
	
	@GetMapping("mModifyPwd")
	public String mModifyPwd() {
		
		return "mModifyPwd";
	}
	
		
	@ResponseBody
	@PostMapping(value="mRetrieveByUsername", produces = "application/json; charset = utf-8")
	public Map<String, List<MessageDto>> mRetrieveByUsername(String userid, String m_id ){
		log.info("mRetrieveByUsername");
		
		Map<String, List<MessageDto>> map = mServ.mRetrieveByUsername(userid, m_id);

		return map;
	}

	@ResponseBody
	@PostMapping(value = "mRetrieveByContents", produces = "application/json; charset = utf-8")
	public Map<String, List<MessageDto>> mRetrieveByContents(String contents, String m_id){
		log.info("mRetrieveByContents");

		Map<String, List<MessageDto>> map = mServ.mRetrieveByContents(contents, m_id);

		return map;
	}

	@ResponseBody
	@PostMapping(value="mProfileUpdate")
	public String mProfileUpdate(@RequestParam("pfile") MultipartFile multi) {

		String remsg = mServ.mProfileUpdate(multi);

		return remsg;
	}


	@ResponseBody
	@GetMapping(value="mUploadAfterView", produces = "application/text; charset=utf-8")
	public String mUploadAfterView(String ms_mid){

		mServ.mUploadAfterView(ms_mid);

		return "success";
	}


	@ResponseBody
	@GetMapping(value="mNewMsgCount", produces = "application/json; charset=utf-8")
	public MessageDto mNewMsgCount(String ms_mid){

		MessageDto msg = mServ.mNewMsgCount(ms_mid);

		return msg;
	}

	@GetMapping("mReceiveOverview")
	public ModelAndView mReceiveOverview(String hostid, Integer pageNum) {

		mv = mServ.mReceiveOverview(hostid, pageNum);

		return mv;
	}

	@GetMapping("mMeSendOverview")
	public ModelAndView mMeSendOverview(String hostid, Integer pageNum) {

		mv = mServ.mMeSendOverview(hostid, pageNum);

		return mv;
	}

	@PostMapping("mSendMessageProc")
	public String mSendMessageProc(MessageDto msg, RedirectAttributes rttr) {

		String path = mServ.mSendMessageProc(msg,rttr);

		return path;
	}

	@GetMapping("mSendMessage")
	public ModelAndView mMessage(String toid, String fromid) {

		mv = mServ.mSendMessage(toid, fromid);

		return mv;
	}

	@GetMapping("mMyLittleBlog")
	public ModelAndView mMyLittleBlog(String blog_id, Integer sort) {

		mv = mServ.mMyLittleBlog(blog_id,sort);

		return mv;
	}


	@PostMapping("mModifyProc")
	public String mModifyProc(MemberDto member, RedirectAttributes rttr) {

		mServ.mModifyProc(member, rttr);
		return "redirect:mModifyMyinfo";
	}


	@GetMapping("mModifyMyinfo")
	public ModelAndView mModifyMyinfo() {

		mv = mServ.mModifyMyinfo();

		return mv; 
	}

	@GetMapping("mMyPage")
	public ModelAndView mMyPage(HttpServletRequest hsr) {
		log.info("mMyPage");
		mv = mServ.mMyPage();

		return mv;
	}

	@ResponseBody
	@PostMapping(value = "mAuthMail", 
	produces = "application/json; charset=utf-8")
	public Map<String, String> mAuthMail(String mailaddr) {
		
		MailDto mail = new MailDto();
		mail.setSetSubject("STEPY 회원가입 인증메일입니다");
		mail.setSetText("인증번호를 입력해주세요\n");
		
		Map<String, String> map = mServ.mAuthMail(mailaddr, mail);

		return map;
	}
	
	
	@GetMapping("mLogoutProc")
	public String mLogoutProc() {

		return mServ.mLogoutProc();
	}

	@PostMapping("mLoginProc")
	public String mLoginProc(MemberDto member, RedirectAttributes rttr) {
		logger.info("mLoginProc()");

		String path = mServ.mLoginProc(member, rttr);
		return path;
	}

	@GetMapping("mLoginFrm")
	public String mLoginFrm() {
		logger.info("mLoginFrm()");

		return "mLoginFrm";
	}


	@GetMapping(value = "mIdDuplicationCheck", produces = "application/text; charset=utf-8")
	@ResponseBody
	public String mIdDuplicationCheck(String tempid) {

		logger.info("mIdDuplicationCheck()");

		String id = mServ.mIdDuplicationCheck(tempid);

		return id;
	}



	@PostMapping("mJoinProc")
	public String mJoinProc(MemberDto member) {
		logger.info("mJoinProc()");

		String path = mServ.mJoinProc(member);

		return path;
	}


	@GetMapping("mJoinFrm")
	public String mJoinFrm() {
		logger.info("mJoinFrm()");

		return "mJoinFrm";
	}


	static String restApi = "3a7921c9c86e805003cd07a8e548f149";
	static String redirect_uri= "http://localhost/stepy/kakaoLogInProc";
	

	@GetMapping(value = "kakaoLogInProc", produces = "application/json; charset=utf8")
	public String kakaoLogInProc(String code,RedirectAttributes rttr) {

		String path = mServ.mKakaoLogInProc(code,rttr);

		return path;
	}


	@GetMapping("kakaoLogin")
	public String mGetAuthorizationUrl(RedirectAttributes rttr) { 

		String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?" + "client_id=" + restApi 
				+ "&redirect_uri=" + redirect_uri + "&response_type=code";

		return "redirect:"+kakaoUrl; 
	}



}
