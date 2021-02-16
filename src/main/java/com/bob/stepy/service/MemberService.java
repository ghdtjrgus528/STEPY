package com.bob.stepy.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dao.MemberDao;
import com.bob.stepy.dto.FileUpDto;
import com.bob.stepy.dto.MailDto;
import com.bob.stepy.dto.MemberDto;
import com.bob.stepy.dto.MemberKaKaoTokenDto;
import com.bob.stepy.dto.MemberKakaoProfileDto;
import com.bob.stepy.dto.MemberPaymentDto;
import com.bob.stepy.dto.MemberPostDto;
import com.bob.stepy.dto.MessageDto;
import com.bob.stepy.dto.PostDto;
import com.bob.stepy.dto.PostDto2;
import com.bob.stepy.dto.ResTicketDto;
import com.bob.stepy.util.Paging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Log
@Service
public class MemberService {

	static String restApi = "3a7921c9c86e805003cd07a8e548f149";
	static String redirect_uri= "http://localhost/stepy/kakaoLogInProc";

	@Autowired
	private MemberDao mDao;
	@Autowired
	private HttpSession session;

	private ModelAndView mv;

	//_______________





	public String mKakaoLogInProc(String code,RedirectAttributes rttr) {

		String path;
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add( "Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String,String>params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", restApi);
		params.add("redirect_uri", redirect_uri);
		params.add("code", code);

		HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
				);


		ObjectMapper objectMapper = new ObjectMapper();
		MemberKaKaoTokenDto tokenDto = null;

		try {
			tokenDto = objectMapper.readValue(response.getBody()
					, MemberKaKaoTokenDto.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("Get access token for kakato : "+tokenDto.getAccess_token());

		MemberDto member = mKakaoProfileRequest(tokenDto);

		if(member.getM_email().equals("no_email_detected")) {
			rttr.addFlashAttribute("msgforlogin", "eamil 정보수집에 동의해주세요");
			//로그아웃처리 
			mKakaoDisconnect(tokenDto.getAccess_token());
			return "redirect:mLoginFrm";
		}

		session.setAttribute("member", member);

		return "redirect:/";
	}



	@Transactional
	public MemberDto mKakaoProfileRequest(MemberKaKaoTokenDto tokenDto) {


		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+tokenDto.getAccess_token());
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = 
				new HttpEntity<>(headers);

		ResponseEntity<String> response = rt.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest,
				String.class
				);


		ObjectMapper objectMapper = new ObjectMapper();
		MemberKakaoProfileDto kakaoProfile = null;

		try {
			kakaoProfile = objectMapper.readValue(response.getBody()
					, MemberKakaoProfileDto.class);

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("The format of user id for stepy is "
				+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());

		String idPreCheck = kakaoProfile.getKakao_account().getEmail()+"_"
		+kakaoProfile.getId();

		MemberDto memberDto = new MemberDto();

		if(kakaoProfile.getKakao_account().getEmail() == "" 
				|| kakaoProfile.getKakao_account().getEmail() == null) {
			memberDto.setM_email("no_email_detected");
			return memberDto;
		}

		String dbCheckResult = mIdDuplicationCheck(idPreCheck);

		if(dbCheckResult == "0") {
			memberDto.setM_id(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
			memberDto.setM_email(kakaoProfile.getKakao_account().getEmail());
			memberDto.setM_name(kakaoProfile.getKakao_account().getProfile().getNickname());
			memberDto.setM_nickname(kakaoProfile.getKakao_account().getProfile().getNickname());
			memberDto.setM_pwd("mustbekeptwithoutanyleak11!!");
			//default password for users who logged in from kakaotalk

			try {
				mJoinProc(memberDto);
				mKakaoProfileUp(kakaoProfile);

			}catch(Exception e){
				System.out.println("mKakaoProfileRequest Member insult failure" );
			}

		}
		else {
			memberDto = mDao.getMemeberInfo(idPreCheck);

		}

		return memberDto;
	}


	public void mKakaoDisconnect(String accessToken) {

		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+accessToken);

		HttpEntity<MultiValueMap<String,String>> forDisconnection = 
				new HttpEntity<>(headers);

		ResponseEntity<String> response = rt.exchange(

				"https://kapi.kakao.com/v1/user/unlink",
				HttpMethod.POST,
				forDisconnection,
				String.class
				);
	}


	@Transactional
	public void mKakaoProfileUp(MemberKakaoProfileDto profile) throws IOException{

		String imageUrl = profile.getProperties().getProfile_image();
		String userid = profile.getKakao_account().getEmail()+"_"+profile.getId();
		URL url = new URL(imageUrl);

		String dirpath = session.getServletContext().getRealPath("/");
		dirpath += "resources/profile/";

		File dir = new File(dirpath);
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}

		String sourcefileName = url.getFile();
		String sysName = System.currentTimeMillis() 
				+ sourcefileName.substring(sourcefileName.lastIndexOf("."));
		String oriName = profile.getId() +"_"
				+ sourcefileName.substring(sourcefileName.lastIndexOf("/")+1);
		String fileName = dirpath + oriName;
		ReadableByteChannel readChannel = Channels.newChannel(url.openStream());

		FileOutputStream fileOS = new FileOutputStream(fileName);
		FileChannel writeChannel = fileOS.getChannel();

		writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);

		FileUpDto fileDto = new FileUpDto();
		fileDto.setF_oriname(oriName);
		fileDto.setF_sysname(sysName);
		fileDto.setF_mnum(userid);

		mDao.mKakaoThumbUpload(fileDto);

	}



	// join stepy
	@Transactional
	public String mJoinProc(MemberDto member) {

		String path = null;
		String actualpwd;
		String realaddr = null;

		BCryptPasswordEncoder pwdEncrypt = new BCryptPasswordEncoder();

		actualpwd = pwdEncrypt.encode(member.getM_pwd());
		member.setM_pwd(actualpwd);


		if(member.getAddress_without_specific() != "" 
				&& member.getAddress_without_specific() != null ) {
			if(member.getAddress_with_specific() !="") {
				realaddr = member.getAddress_without_specific() +" "
			+ member.getAddress_with_specific();
				member.setM_addr(realaddr);
			}
			else if(member.getAddress_with_specific()== ""){
				realaddr = member.getAddress_without_specific();
				member.setM_addr(realaddr);
			}
		}

		try {
			mOnceCreateProfile();

			mDao.memberInsert(member);

			String sysName = System.currentTimeMillis() + "_profile.png";
			String oriName = "stepydefaultprofile.png";

			FileUpDto fileDto = new FileUpDto();
			fileDto.setF_oriname(oriName);
			fileDto.setF_sysname(sysName);
			fileDto.setF_mnum(member.getM_id());

			mDao.mThumbUpload(fileDto);
			//mShutFirstMsg(member.getM_id());

			path="redirect:/";
		}catch(Exception e) {
			path="redirect:mJoinFrm";
			e.printStackTrace();
		}

		return path;
	}


	private void mOnceCreateProfile() {

		String dirpath = session.getServletContext().getRealPath("/");
		String loadpath = dirpath + "resources/images/";
		String destpath = dirpath + "resources/profile/";

		File fork = new File(destpath + "stepydefaultprofile.png");
		if (fork.exists()) {
			return;
		}
		File dir = new File(destpath);
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}

		File folderInput = new File(loadpath+"stepydefaultprofile.png");
		try {
			BufferedImage image = ImageIO.read(folderInput);
			ImageIO.write(image, "png", fork);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}



	public String mIdDuplicationCheck(String tempid) {

		String msg;

		int i = mDao.duplicationCheck(tempid);
		if (i == 1) {
			msg="1";
			//"이미 존재하는 아이디입니다. 다른 아이디를 선택해주세요 (already existing id)";
		}else {
			msg = "0";
			//"사용가능한 아이디입니다. (usable id)";
		}

		return msg;
	}


	public String mLoginProc(MemberDto member, RedirectAttributes rttr) {

		String path;
		String encryptPass = mDao.getEncryptizedPass(member.getM_id());

		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();		

		if(encryptPass != null) {
			if(pwdEncoder.matches(member.getM_pwd(), encryptPass)) {

				member = mDao.getMemeberInfo(member.getM_id());
				session.setAttribute("member", member);
				path = "redirect:/";

			}else {
				// right id, but pwd typo
				path="redirect:mLoginFrm";
				rttr.addFlashAttribute("loginMsg", "비밀번호 입력 오류");
				rttr.addFlashAttribute("resultNum", "1");
			}

		}else {
			//no memberinfo exist
			path="redirect:mLoginFrm";
			rttr.addFlashAttribute("loginMsg", "존재하지 않는 아이디입니다.");
			rttr.addFlashAttribute("resultNum", "2");
		}

		return path; 
	}


	public String mLogoutProc() {

		session.invalidate();

		return "home";
	}



	public ModelAndView mMyPage() {

		MemberDto member = (MemberDto)session.getAttribute("member");
		System.out.println(member);
		String userid = member.getM_id();
		mv = new ModelAndView();

		FileUpDto profile = mDao.mGetProfile(userid);
		System.out.println(profile);
		mv.addObject("profile", profile);
		mv.setViewName("mMyPage");

		return mv;
	}



	public Map<String, String> mAuthMail(String mailaddr, MailDto mail) {

		Map<String, String> map = new HashMap<>();

		final String user = "stepy.tester@gmail.com";
		final String password = "stepy1234!";
		final String host ="smtp.gmail.com";// "smtp.naver.com";
		int port = 587;
		final String to = mailaddr;


		Properties prop = new Properties();

		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable","true");
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);

		Session mailsession = Session.getInstance(prop, 
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});


		//random
		Random random = new Random();
		String authkey = "";

		for(int i = 0; i<3 ; i++) {
			int rand_char = random.nextInt(26)+65;
			authkey += (char)rand_char;
		}

		int rand_int = random.nextInt(9000)+1000 ;
		authkey += rand_int;

		try {
			Message message = new MimeMessage(mailsession);
			message.setFrom(new InternetAddress(user));
			message.setRecipients(Message.RecipientType.TO, 
					InternetAddress.parse(to));
			message.setSubject(mail.getSetSubject());
			message.setText(mail.getSetText()+authkey);
			Transport.send(message);
			log.info("Sent a key , which is : " + authkey);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}

		map.put("msg","success");
		map.put("authkey",authkey);

		return map;

	}


	@Transactional
	public ModelAndView mModifyMyinfo() {

		mv = new ModelAndView();
		MemberDto member = (MemberDto)session.getAttribute("member");
		FileUpDto profile = mDao.mGetProfile(member.getM_id());

		mv.addObject("profile", profile);
		mv.setViewName("mModifyMyinfo");

		return mv;

	}


	public ModelAndView mMyLittleBlog(String blog_id, Integer sort) {

		int sortnum = (sort == null) ? 1:sort;

		mv = new ModelAndView();

		FileUpDto profile = mDao.mGetProfile(blog_id);
		mv.addObject("hostprofile", profile);
		MemberDto member = mDao.getMemeberInfo(blog_id);
		mv.addObject("hostaccount", member);

		if(sortnum == 1) {
			List<MemberPostDto> mpListori = mDao.mGetMyPostList(blog_id);
			List<MemberPostDto> mpList = new ArrayList<MemberPostDto>();

			int round = mpListori.size();

			for(int i = 0;  i < round;  i++ ) {
				int j = 1;
				int limit = round - (round - j);

				MemberPostDto mp1 = mpListori.get(i);

				MemberPostDto mp2 = mpListori.get(limit);	
				if(mp1.getP_num() != mp2.getP_num()) {
					if(i == 0) {
						mpList.add(mp1);
					}else {
						mp2 = mpListori.get(i-1); //실상 mp0
						if(mp1.getP_num() != mp2.getP_num()) {
							mpList.add(mp1);
						}
					}
				}else if(mp1.getP_num() == mp2.getP_num()) {
					if(i == 0) {
						mpList.add(mp1);
					}else {
						mp2 = mpListori.get(i-1);
						if(mp1.getP_num() != mp2.getP_num()) {
							mpList.add(mp1);
						}
					}
				}
				j++;
			}
			mv.addObject("mpList", mpList);
			mv.addObject("sort",1);
		}else {
			List<MemberPostDto> mrListori = mDao.mGetMyReplyList(blog_id);
			List<MemberPostDto> mrList = new ArrayList<MemberPostDto>();

			int round = mrListori.size();

			for(int i = 0;  i < round;  i++ ) {
				int j = 1;
				int limit = round - (round - j);

				MemberPostDto mp1 = mrListori.get(i);

				MemberPostDto mp2 = mrListori.get(limit);	
				if(mp1.getP_num() != mp2.getP_num()) {
					if(i == 0) {
						mrList.add(mp1);
					}else {
						mp2 = mrListori.get(i-1); //실상 mp0
						if(mp1.getP_num() != mp2.getP_num()) {
							mrList.add(mp1);
						}
					}
				}else if(mp1.getP_num() == mp2.getP_num()) {
					if(i == 0) {
						mrList.add(mp1);
					}else {
						mp2 = mrListori.get(i-1);
						if(mp1.getP_num() != mp2.getP_num()) {
							mrList.add(mp1);
						}
					}
				}
				j++;
			}
			mv.addObject("mrList", mrList);
			mv.addObject("sort",2);
		}
		return mv;
	}


	private void mShutFirstMsg(String m_id) {
		System.out.println("여기 들어오긴 했니..?");
		MessageDto msg = new MessageDto();

		msg.setMs_mid(m_id);
		msg.setMs_smid("admin");
		msg.setMs_contents("안녕하세요! Stepy와 함께 전국 방방 곳곳! 발도장을 찍어볼까요? 여행 일정을 짜고, 동행인을 구하고, 소통하고 즐겨봐요!");
		msg.setMs_bfread(1);
		msg.setMs_afread(0);

		mDao.mSetMessage(msg);

	}


	@Transactional
	public void mModifyProc(MemberDto member, RedirectAttributes rttr) {

		String msg;

		if(member.getM_nickname() == "" && member.getM_nickname() == null) {
			msg = "닉네임을 입력해주세요";
			rttr.addFlashAttribute("msg",msg);
			return;
		}
		String realaddr;
		
		if(member.getAddress_without_specific() != ""
				&& member.getAddress_without_specific() != null ) {
			if(member.getAddress_with_specific() !="") {
				realaddr = member.getAddress_without_specific() +" "
			+ member.getAddress_with_specific();
				member.setM_addr(realaddr);
			}
			else if(member.getAddress_with_specific()== ""){
				realaddr = member.getAddress_without_specific();
				member.setM_addr(realaddr);
			}
		}

		mDao.mModifyUserInfo(member);	
		session.setAttribute("member", member);
		msg = "변경사항이 저장되었습니다!";
		rttr.addFlashAttribute("msgFromModify", msg);

		return;
	} 



	public ModelAndView mSendMessage(String toid, String fromid) {
		//toid : 보내는이 	fromid : 받는이( 현재 세션에 있는 접속 유저  = launch )  
		mv = new ModelAndView();

		MemberDto guest = mDao.getMemeberInfo(fromid);
		mv.addObject("guest", guest);

		MemberDto host = mDao.getMemeberInfo(toid);

		mv.addObject("host", host);

		mv.setViewName("mSendMessage");

		return mv;
	}


	@Transactional
	public String mSendMessageProc(MessageDto msg, RedirectAttributes rttr) {

		mv = new ModelAndView();
		String path;
		String ms_mid = msg.getMs_mid();
		String ms_smid = msg.getMs_smid();

		if(mDao.duplicationCheck(ms_mid) == 0) {
			rttr.addAttribute("fromid", ms_smid);
			rttr.addAttribute("toid", "");
			rttr.addFlashAttribute("nouserfound", "존재하지 않는 유저입니다!");
			return "redirect:mSendMessage";
		}

		MessageDto before = mDao.mGetBfMsg(ms_mid);

		if(before == null) {
			msg.setMs_bfread(1);
			msg.setMs_afread(0);
			mDao.mSetMessage(msg);
		}else {
			msg.setMs_bfread(before.getMs_bfread()+1);
			msg.setMs_afread(before.getMs_afread());

			mDao.mSetMessage(msg);
		}

		rttr.addAttribute("hostid", ms_smid);
		path= "redirect:mMeSendOverview";
		return path;
	}



	public ModelAndView mMeSendOverview(String hostid, Integer pageNum) {

		mv = new ModelAndView();

		String link = "./mMeSendOverview?hostid="+hostid+"&";
		int num = (pageNum == null) ? 1 : pageNum;

		List<MessageDto> smList = mDao.mGetSendList(hostid, num);
		int forMaxNum = mDao.mCountSendMsg(hostid);

		mv.addObject("smList", smList);
		mv.addObject("paging", getPaging(forMaxNum, 5, 5, hostid, num, link));

		mv.setViewName("mMeSendOverview");

		return mv;
	}



	public ModelAndView mReceiveOverview(String hostid, Integer pageNum) {

		mv = new ModelAndView();
		System.out.println("pageNum is " + pageNum + " ( null is expected)");

		String link = "./mReceiveOverview?hostid="+hostid+"&";
		int num = (pageNum == null) ? 1 : pageNum;

		List<MessageDto> rmList = mDao.mGetReceiveList(hostid, num);
		int forMaxNum = mDao.mCountReceivedMsg(hostid);

		mv.addObject("rmList", rmList);

		mv.addObject("paging", getPaging(forMaxNum, 5, 5, hostid,num,link));
		session.setAttribute("pageNum", num);

		mv.setViewName("mReceiveOverview");

		return mv;
	}

	private String getPaging(int forMaxNum , int listC, int pageC, String hostid, int num, String link){

		int maxNum = forMaxNum;
		int listCnt = listC;
		int pageCnt = pageC;
		String linkName = link;

		Paging paging = new Paging(maxNum, num, listCnt, pageCnt, linkName);

		String pageTag = paging.makePageBtnForMulti();

		return pageTag;
	}


	public MessageDto mNewMsgCount(String ms_mid) {

		MessageDto fork = mDao.mGetBfMsg(ms_mid);
		MessageDto msg = new MessageDto();
		if(fork == null) {
			msg.setMs_bfread(0);
			msg.setMs_afread(0);
			System.out.println("hope no problem" + msg);
		}else {
			msg = fork;
		}

		return msg;
	}


	@Transactional
	public void mUploadAfterView(String ms_mid) {

		MessageDto msg = mDao.mGetBfMsg(ms_mid);
		if(msg == null) {
			return;
		}else {
			mDao.mUploadAfterView(msg);
		}

		return;
	}


	@Transactional
	public String mProfileUpdate(MultipartFile multi) {

		String msg;

		if(multi.isEmpty()) {
			msg = "선택된 파일 없음";

			return msg;
		}

		MemberDto member = (MemberDto)session.getAttribute("member");
		String dirpath = session.getServletContext().getRealPath("/");
		dirpath += "resources/profile/";

		String oriname = multi.getOriginalFilename();
		String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
		System.out.println(sysname);

		File file = new File(dirpath + oriname);

		try {
			multi.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		msg = "프로필 사진 변경 완료";

		FileUpDto fileDto  = new FileUpDto();
		fileDto.setF_oriname(oriname);
		fileDto.setF_sysname(sysname);
		fileDto.setF_mnum(member.getM_id());


		mDao.mKakaoThumbUpload(fileDto);

		return msg; 
	}



	public Map<String, List<MessageDto>> mRetrieveByContents(String contents, String m_id) {

		MessageDto msg = new MessageDto();
		Map<String, List<MessageDto>> map = new HashMap<>();

		msg.setMs_contents(contents);
		msg.setMs_mid(m_id);

		List<MessageDto> searchList = mDao.mRetrieveByContents(msg);

		map.put("searchList", searchList);


		return map;
	}


	public Map<String, List<MessageDto>> mRetrieveByUsername(String userid, String m_id) {

		MessageDto msg = new MessageDto();
		msg.setMs_smid(userid);
		msg.setMs_mid(m_id);
		List<MessageDto> searchList = mDao.mRetrieveByUsername(msg);

		Map<String, List<MessageDto>> map = new HashMap<>();
		map.put("searchList", searchList);

		return map;

	}


	public String mGetCrypPwd(String pwd, String m_id){

		String set;

		BCryptPasswordEncoder pwdEncrypt = new BCryptPasswordEncoder();

		String userInput = pwd;

		String cryptFromDB = mDao.getEncryptizedPass(m_id);

		if(pwdEncrypt.matches(userInput, cryptFromDB)){

			set = "1";
		}else{
			set = "0";
		}

		return set;
	}

	public String mUpdatePwd(String m_pwd, String m_id) {

		BCryptPasswordEncoder pwdEncrypt = new BCryptPasswordEncoder();

		m_pwd = pwdEncrypt.encode(m_pwd);
		mDao.mUpdatePwd(m_pwd, m_id);
		String msg = "success";

		return msg;
	}



	public ModelAndView mGetMyCartItems() {

		MemberDto member = (MemberDto)session.getAttribute("member");

		List<FileUpDto> cartList = mDao.mMyCartItems(member.getM_id());

		mv = new ModelAndView();

		mv.addObject("cartList", cartList);
		mv.setViewName("mMyCartPages");

		return mv;
	}



	public ModelAndView mMyLikedPages(Integer pageNum) {

		int num = (pageNum == null) ? 1 : pageNum;
		String link = "./mMyLikedPages?";

		mv = new ModelAndView();
		MemberDto member = (MemberDto)session.getAttribute("member");
		String userid = member.getM_id();

		int forMaxNum = mDao.mGetWholeLiked(userid);
		List<PostDto> likedPost = mDao.mGetLikedPost(userid, num);

		mv.addObject("pList", likedPost);

		mv.addObject("paging", getPaging(forMaxNum, 10, 10, userid, num, link));

		session.setAttribute("paging", num);

		mv.setViewName("mMyLikedPages");

		return mv;
	}



	public ModelAndView mMyPayment(Integer sort) {

		int sortnum = (sort == null) ? 1 : sort;
		mv = new ModelAndView();

		MemberDto member = (MemberDto)session.getAttribute("member");
		if(sortnum == 1) {
			List<MemberPaymentDto> ticket = mDao.mGetPayPendingList(member.getM_id());

			mv.addObject("sort", sortnum);
			mv.addObject("tkList", ticket);
		}else {
			List<MemberPaymentDto> ticket = mDao.mGetPaidList(member.getM_id());
			mv.addObject("sort", sortnum);
			mv.addObject("tkList", ticket);
		}

		mv.setViewName("mMyPayment");

		return mv;
	}


	public void mPaiedInFull(Integer resnum) {

		mDao.mUpdateToPaidStatement(resnum);

		return;
	}








}


