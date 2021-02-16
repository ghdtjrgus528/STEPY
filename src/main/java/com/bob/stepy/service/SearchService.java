package com.bob.stepy.service;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bob.stepy.dao.BoardDao;
import com.bob.stepy.dao.SearchDao;
import com.bob.stepy.dto.FileUpDto;
import com.bob.stepy.dto.PostDto;
import com.bob.stepy.dto.StoreDto;
import com.bob.stepy.util.Paging;

import lombok.extern.java.Log;

@Log
@Service
public class SearchService {
	
	// SearchDao
	@Autowired
	private SearchDao sDao;
	
	// BoardDao
	@Autowired
	private BoardDao bDao;
	
	// ModelAndView 객체
	private ModelAndView mv;
	
	// 세션 객체
	@Autowired
	private HttpSession session;
	
	

	// 숙소 검색
	public ModelAndView searchHotel(String keyword) {
		log.info("searchHotel() keyword : " + keyword);
		
		mv = new ModelAndView();

		if(keyword != "") {
			List<StoreDto> sList = sDao.searchHotel(keyword); // 스토어 리스트
			Map<String, Object> tMap = new LinkedHashMap<String, Object>(); // 썸네일과 스토어 매칭
			
			for(int i = 0; i < sList.size(); i++) {
				FileUpDto thumbnail = new FileUpDto();
				thumbnail = sDao.getThumbnail(sList.get(i).getS_num());
				//tList.add(i, thumbnail);
				
				tMap.put(thumbnail.getF_sysname(), sList.get(i));
			
				thumbnail = null;
			}
			
			mv.addObject("sList", sList); // DB에서 조회한 데이터 목록을 모델에 추가
			mv.addObject("tMap", tMap);
			mv.addObject("keyword", keyword);
			
			mv.setViewName("sSearchHotel");
		}
		
		else {
			mv.addObject("msg", "검색할 키워드를 입력해주세요.");
			mv.setViewName("sSearchFrm");
		}
		
		return mv;
	}
	
	
	// 음식점 검색
	public ModelAndView searchRestaurant(String keyword) {
		log.info("searchRestaurant()");
		
		mv = new ModelAndView();
		
		if(keyword != "") {
			List<StoreDto> sList = sDao.searchRestaurant(keyword);
			Map<String, Object> tMap = new LinkedHashMap<String, Object>(); // 썸네일과 스토어 매칭
			
			for(int i = 0; i < sList.size(); i++) {
				FileUpDto thumbnail = new FileUpDto();
				thumbnail = sDao.getThumbnail(sList.get(i).getS_num());
				//tList.add(i, thumbnail);
				
				tMap.put(thumbnail.getF_sysname(), sList.get(i));
			
				thumbnail = null;
			}
			
			mv.addObject("sList", sList); // DB에서 조회한 데이터 목록을 모델에 추가
			mv.addObject("tMap", tMap);
			mv.addObject("keyword", keyword);
			
			mv.setViewName("sSearchRestaurant");
		}
		
		else {
			mv.addObject("msg", "검색할 키워드를 입력해주세요.");
			mv.setViewName("sSearchFrm");
		}
		
		return mv;
	}
	
	
	// 지역시설 검색
	public ModelAndView searchPlay(String keyword) {
		log.info("searchPlay()");
		
		mv = new ModelAndView();
		
		if(keyword != "") {
			List<StoreDto> sList = sDao.searchPlay(keyword);
			Map<String, Object> tMap = new LinkedHashMap<String, Object>(); // 썸네일과 스토어 매칭
			
			for(int i = 0; i < sList.size(); i++) {
				FileUpDto thumbnail = new FileUpDto();
				thumbnail = sDao.getThumbnail(sList.get(i).getS_num());
				//tList.add(i, thumbnail);
				
				tMap.put(thumbnail.getF_sysname(), sList.get(i));
			
				thumbnail = null;
			}
			
			mv.addObject("sList", sList); // DB에서 조회한 데이터 목록을 모델에 추가
			mv.addObject("tMap", tMap);
			mv.addObject("keyword", keyword);
			
			mv.setViewName("sSearchPlay");
		}
		
		else {
			mv.addObject("msg", "검색할 키워드를 입력해주세요.");
			mv.setViewName("sSearchFrm");
		}
		
		return mv;
	}
	
		
	/* ***** */
		
		
	// 여행 후기 게시판 검색
	public ModelAndView searchTravelReview(@RequestParam(defaultValue = "all") String searchOption, 
			@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer pageNum) {
		log.info("searchTravelReview() searchOption : " + searchOption + ", keyword : " + keyword
				+ ", pageNum : " + pageNum);
		
		mv = new ModelAndView();
		
		int num = (pageNum == null) ? 1: pageNum;
		
		List<PostDto> pList = sDao.searchTravelReview(searchOption, keyword, pageNum);
		Map<String, Object> pMap = new HashMap<String, Object>();

		pMap.put("pList", pList);
		pMap.put("searchOption", searchOption);
		pMap.put("keyword", keyword);

		mv.addObject("pMap", pMap);
		mv.addObject("pList", pList);
		mv.addObject("paging", getPaging(num,"bReviewList"));

		session.setAttribute("pageNum", num);

		mv.setViewName("bSearchTravelReview");
		
		return mv;
	}
	
	
	// 메이트 게시판 검색
	public ModelAndView searchMate(@RequestParam(defaultValue = "all") String searchOption, 
			@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer pageNum) {
		log.info("searchMate() searchOption : " + searchOption + ", keyword : " + keyword);
		
		mv = new ModelAndView();
			
		int num = (pageNum == null) ? 1: pageNum;
			
		List<PostDto> pList = sDao.searchMate(searchOption, keyword, pageNum);
		Map<String, Object> pMap = new HashMap<String, Object>();

		pMap.put("pList", pList);
		pMap.put("searchOption", searchOption);
		pMap.put("keyword", keyword);

		mv.addObject("pMap", pMap);
		mv.addObject("pList", pList);
		mv.addObject("paging", getPaging(num,"bMateList"));

		session.setAttribute("pageNum", num);

		mv.setViewName("bSearcMate");
		
		return mv;
	}
	
	
	// 자유 게시판 검색
	public ModelAndView searchFree(@RequestParam(defaultValue = "all") String searchOption, 
			@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer pageNum) {
		log.info("searchFree() searchOption : " + searchOption + ", keyword : " + keyword);

		mv = new ModelAndView();

		int num = (pageNum == null) ? 1: pageNum;


		List<PostDto> pList = sDao.searchFree(searchOption, keyword, pageNum);
		Map<String, Object> pMap = new HashMap<String, Object>();

		pMap.put("pList", pList);
		pMap.put("searchOption", searchOption);
		pMap.put("keyword", keyword);

		mv.addObject("pMap", pMap);
		mv.addObject("pList", pList);
		mv.addObject("paging", getPaging(num,"bFreeList"));

		session.setAttribute("pageNum", num);

		mv.setViewName("bSearchFree");
		
		return mv;
	}
	
	
	// 페이징 처리
	private String getPaging(int num, String str) {
		// 전체 글 개수 구하기(from DB)
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

		// 설정값(페이지 당 글 개수, 그룹 당 페이지 개수)
		int listCnt = 6;
		int pageCnt = 5;
		String listName = str;

		Paging paging = new Paging(maxNum, num, listCnt, pageCnt, listName);

		String pagingHtml = paging.makePaging();

		return pagingHtml;
	}
	
}
