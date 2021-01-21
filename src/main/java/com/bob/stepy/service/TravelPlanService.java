package com.bob.stepy.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bob.stepy.dao.TravelPlanDao;
import com.bob.stepy.dto.AccompanyPlanDto;
import com.bob.stepy.dto.CheckListDto;
import com.bob.stepy.dto.ChecklistViewDto;
import com.bob.stepy.dto.HouseholdDto;
import com.bob.stepy.dto.StoreDto;
import com.bob.stepy.dto.TravelPlanDto;

import lombok.extern.java.Log;

@Service
@Log
public class TravelPlanService {
	@Autowired
	private HttpSession session;
	@Autowired
	private TravelPlanDao tDao;
	
	ModelAndView mv;
	
	//여행 등록
	@Transactional
	public ModelAndView pRegPlan(TravelPlanDto plan, RedirectAttributes rttr) {
		log.info("service - pRegPlan()");
		long planNum = System.currentTimeMillis();
		
		mv = new ModelAndView();
		
		try {
			plan.setT_plannum(planNum);
			tDao.pRegPlan(plan);
			
			/*
			mv.addObject("planNum", planNum);
			mv.addObject("planName", plan.getT_planname());
			mv.addObject("leader", plan.getT_id());
			mv.addObject("spot", plan.getT_spot());
			mv.addObject("start", plan.getT_stdate());
			mv.addObject("end", plan.getT_bkdate());
			mv.addObject("member1", plan.getT_member1());
			mv.addObject("member2", plan.getT_member2());
			mv.addObject("member3", plan.getT_member3());
			mv.addObject("member4", plan.getT_member4());
			mv.addObject("member5", plan.getT_member5());
			*/
			
			mv.addObject("plan", plan);
			
			//시작일과 종료일의 차이 계산
			long days = getTime(plan.getT_stdate(), plan.getT_bkdate());
			mv.addObject("days", days);
			
			AccompanyPlanDto acPlan = new AccompanyPlanDto();
			
			for(int i = 1; i <= days; i++) {
				acPlan.setAp_plannum(planNum);
				acPlan.setAp_mid(plan.getT_id());
				acPlan.setAp_day(i);
				
				//초기 여행 일정 설정
				tDao.regPlanContents(acPlan);
			}
			session.setAttribute("curPlan", planNum);
			mv.setViewName("pPlanFrm");
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("redirect:pMakePlanFrm");
			rttr.addFlashAttribute("msg", "아이디를 확인해주세요!");
		}
		
		return mv;
	}
	
	//날짜 차이 계산
	public long getTime(String start, String end) {
		long days = 0;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//날짜를 Date형으로 변환
			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			
			//두 날짜간의 차이(1970년 기준 00:00:00부터 몇 초가 흘렀는지)
			long calDate = startDate.getTime() - endDate.getTime();
			
			//(일*시*분*초) = 일수
			days = calDate / (24*60*60*1000);
			//절대값 변환
			days = Math.abs(days);
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return days;
	}

	//여행 목록 가져오기
	public ModelAndView pPlanList(String id) {
		log.info("service - pPlanList() - id: " + id);
		mv = new ModelAndView();
		
		List<TravelPlanDto> planList = tDao.getPlanList(id);
		
		//여행 일정 모델에 추가
		mv.addObject("planList", planList);
		
		mv.setViewName("pPlanList");
		
		return mv;
	}

	//일정 페이지 이동
	public ModelAndView pPlanFrm(long planNum) {
		log.info("service - pPlanFrm() - planNum : " + planNum);
		
		long num = (planNum == 0)? (long)session.getAttribute("curPlan") : planNum;
		
		mv = new ModelAndView();
		
		TravelPlanDto plan = tDao.getPlan(num);
		
		
		mv.addObject("plan", plan);
		
		//시작일과 종료일의 차이 계산
		long days = getTime(plan.getT_stdate(), plan.getT_bkdate());
		mv.addObject("days", days);
		
		//여행일별로 데이터 저장
		//Map<String, Long> planContentsMap = new HashMap<String, Long>();
		
		//for(long i = 1; i <= days; i++) {
		//	planContentsMap.put("planNum", planNum);
		//	planContentsMap.put("days", i);
		
		//여행 내용 가져오기	
		List<AccompanyPlanDto> planContentsList = tDao.getPlanContents(num);
		mv.addObject("planContentsList", planContentsList);
		//}
		
		//세션에 현재 여행 번호 저장
		session.setAttribute("curPlan", num);
		
		//가게 목록 불러오기
		List<StoreDto> sList = tDao.getStoreList();
		mv.addObject("sList", sList);
		
		mv.setViewName("pPlanFrm");
		
		return mv;
	}

	//장소 검색 페이지 이동
	public ModelAndView pStoreSearch(long day, long planCnt) {
		log.info("service - pStoreSearch()");
		
		mv = new ModelAndView();
		//가게 목록 불러오기
		List<StoreDto> sList = tDao.getStoreList();
		mv.addObject("sList", sList);
		mv.addObject("day", day);
		mv.addObject("planCnt", planCnt);
		
		return mv;
	}
	
	//여행 내용 등록
	@Transactional
	public String RegAccompanyPlan(AccompanyPlanDto acPlan, RedirectAttributes rttr) {
		log.info("service - RegAccompanyPlan()");
		
		try {
			//여행 내용 등록
			tDao.regAccompanyPlan(acPlan);
			
			//목록 다시 불러오기
			//mv = pPlanFrm(acPlan.getAp_plannum());
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return "redirect:pPlanFrm?planNum=0";
	}

	//여행 내용 삭제
	@Transactional
	public String delAccompanyPlan(long planNum, long day, long num, RedirectAttributes rttr) {
		log.info("service - delAccompanyPlan()");
		
		Map<String, Long> apMap = new HashMap<String, Long>();
		apMap.put("planNum", planNum);
		apMap.put("day", day);
		apMap.put("num", num);
		try {
			//데이터 삭제
			tDao.delAccompanyPlan(apMap);
			//남은 데이터 카운트 정렬
			tDao.reduceNumCnt(apMap);
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return "redirect:pPlanFrm?planNum=0";
	}
	
	//여행 내용 수정 페이지 이동
	public ModelAndView pEditAccompanyPlanFrm(long day, long planCnt) {
		log.info("service - pEditAccompanyPlanFrm()");
		
		return pStoreSearch(day, planCnt);
	}
	
	//여행 내용 수정
	@Transactional
	public String pEditAccompanyPlan(AccompanyPlanDto acPlan, RedirectAttributes rttr) {
		log.info("service - pEditAccompanyPlan()");
		
		try {
			tDao.pEditAccompanyPlan(acPlan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:pPlanFrm?planNum=0";
	}
	
	//가계부 페이지 이동
	public ModelAndView pHouseholdFrm(long planNum) {
		log.info("service - pHouseholdFrm() - planNum : " + planNum);
		
		long num = (planNum == 0)? (long)session.getAttribute("curPlan") : planNum;
		
		mv = new ModelAndView();
		
		TravelPlanDto plan = tDao.getPlan(num);
		
		mv.addObject("plan", plan);
		
		//시작일과 종료일의 차이 계산
		long days = getTime(plan.getT_stdate(), plan.getT_bkdate());
		mv.addObject("days", days);
		
		//가계부 내용 가져오기
		List<HouseholdDto> hList = tDao.getHouseholdList(num);
		mv.addObject("hList", hList);
		
		mv.setViewName("pHouseholdFrm");
		
		return mv;
	}

	//가계부 내용 작성페이지 이동
	public ModelAndView pWriteHousehold(long householdCnt, long days, long dayCnt) {
		log.info("service - pWriteHousehold() - householdCnt : " + householdCnt + ", days : " + days  + " , dayCnt : " + dayCnt);
		
		long num = (days == 0)? (long)session.getAttribute("curDays") : days;
		session.setAttribute("cruDays", days);
		
		mv = new ModelAndView();
		
		mv.addObject("householdCnt", householdCnt);
		mv.addObject("days", num);
		mv.addObject("dayCnt", dayCnt);
		
		//가게 목록 불러오기
		List<StoreDto> sList = tDao.getStoreList();
		mv.addObject("sList", sList);
				
		mv.setViewName("pWriteHousehold");
		
		return mv;
	}

	//가계부 내용 등록
	@Transactional
	public String regHousehold(HouseholdDto household, RedirectAttributes rttr) {
		log.info("service - regHousehold()");
		String view = null;
	
		try {
			tDao.regHousehold(household);
			view = "redirect:pHouseholdFrm?planNum=0";
			
		} catch (Exception e) {
			e.printStackTrace();
			String cnt = Long.toString(household.getH_cnt());
			view = "redirect:pWriteHousehold?householdCnt=" + cnt + "&days=0";
		}
		
		return view;
	}

	//가계부 내용 수정 페이지 이동
	public ModelAndView pModHouseholdFrm(long planNum, long days, long dayCnt, long householdCnt) {
		log.info("service - pModHouseholdFrm()");
		
		mv = new ModelAndView();
		
		Map<String, Long> hList = new HashMap<String, Long>();
		hList.put("planNum", planNum);
		hList.put("day", dayCnt);
		hList.put("householdCnt", householdCnt);
		
		HouseholdDto household = tDao.getHouseholdContentes(hList);
		
		mv.addObject("dayCnt", dayCnt);
		mv.addObject("days", days);
		mv.addObject("contents", household);
		mv.setViewName("pModHouseholdFrm");
		
		//가게 목록 불러오기
		List<StoreDto> sList = tDao.getStoreList();
		mv.addObject("sList", sList);
		
		return mv;
	}
	
	//가계부 내용 수정
	@Transactional
	public String modHousehold(HouseholdDto household, RedirectAttributes rttr) {
		log.info("service - modHousehold()");
		String view = null;
		
		Map<String, Long> hMap = new HashMap<String, Long>();
		hMap.put("planNum", household.getH_plannum());
		hMap.put("curDay", household.getH_curday());
		hMap.put("householdCnt", household.getH_cnt());
		
		try {
			//가계부 내용 수정
			tDao.ModHousehold(household);
			//남은 데이터 카운트 정렬
			tDao.reduceHouseholdCnt(hMap);
			view = "redirect:pHouseholdFrm?planNum=0";
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:pPlanFrm?planNum=0";
		}
		
		return view;
	}

	//가계부 내용 삭제
	@Transactional
	public String delHousehold(long planNum, long day, long householdCnt, RedirectAttributes rttr) {
		log.info("service - delHousehold()");
		String view = null;
		
		Map<String, Long> hMap = new HashMap<String, Long>();
		hMap.put("planNum", planNum);
		hMap.put("curDay", day);
		hMap.put("householdCnt", householdCnt);
		
		try {
			//데이터 삭제
			tDao.delHousehold(hMap);
			//남은 데이터 카운트 정렬
			tDao.reduceHouseholdCnt(hMap);
			
			view = "redirect:pHouseholdFrm?planNum=0";
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:pPlanFrm?planNum=0";
		}
		
		return view;
	}

	//예산 등록
	public Map<String, Long> pRegBudget(long planNum, long budget) {
		log.info("service - pRegBudget() - planNum : " + planNum + ", budget : " + budget);
		Map<String, Long> rbMap = new HashMap<String, Long>();
		rbMap.put("planNum", planNum);
		rbMap.put("budget", budget);
		
		try {
			//예산 등록
			tDao.pRegBudget(rbMap);
			//여행 정보 다시불러오기
			Long totalCost = tDao.getBalance(planNum);
			if(totalCost == null) {
				rbMap.put("totalCost", 0L);
				rbMap.put("balance", budget);
			} else {
				rbMap.put("totalCost", totalCost);
				rbMap.put("balance", (budget - totalCost));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rbMap;
	}

	//체크리스트 페이지로 이동
	public ModelAndView pCheckSupFrm(long planNum) {
		log.info("service - pCheckSupFrm() - planNum : " + planNum);

		long num = (planNum == 0)? (long)session.getAttribute("curPlan") : planNum;
		
		mv = new ModelAndView();
		//여행 정보 가져오기
		TravelPlanDto plan = tDao.getPlan(num);
		//체크리스트 내용 가져오기
		List<CheckListDto> checklist = tDao.getCheckList(num);
		//체크리스트 카테고리 개수 가져오기
		int categoryNum = tDao.getCategoryNum(num);
		//레이아웃 생성용 뷰 가져오기
		List<ChecklistViewDto> cvList = tDao.getCV(num);
		
		mv.addObject("plan", plan);
		mv.addObject("checklist", checklist);
		mv.addObject("categoryNum", categoryNum);
		mv.addObject("cvList", cvList);
		
		return mv;
	}

	//체크리스트 상태 변경
	public CheckListDto pChangeCheck(long planNum, long category, long itemCnt, long check) {
		log.info("service - pChangeCheck() - planNum : " + planNum + ", category : " + category + ", itemCnt : " + itemCnt + ", check : " + check);
		String result = null;
		
		Map<String, Long> clMap = new HashMap<String, Long>();
		clMap.put("planNum", planNum);
		clMap.put("category", category);
		clMap.put("itemCnt", itemCnt);
		clMap.put("check", check);
		try {
			tDao.pChangeCheck(clMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CheckListDto checklist = tDao.getACheck(clMap);
		
		return checklist;
	}

	//준비물 추가 페이지 이동
	public ModelAndView pAddCheckItemFrm(long category, String categoryName, long itemCnt) {
		log.info("service - pAddCheckItemFrm() - category : " + category + ", categoryName : " + categoryName + ", itemCnt : " + itemCnt);
		
		mv = new ModelAndView();
		
		mv.addObject("planNum", (long)session.getAttribute("curPlan"));
		mv.addObject("category", category);
		mv.addObject("categoryName", categoryName);
		mv.addObject("itemCnt", itemCnt);
		
		mv.setViewName("pAddCheckItemFrm");
		
		return mv;
	}

	//준비물 추가
	@Transactional
	public String pAddCheckItem(long planNum, long category, String categoryName, long itemCnt, String itemName, RedirectAttributes rttr) {
		log.info("service - pAddCheckItem() - planNum : " + ", category : " + category + ", categoryName : " + categoryName + ", itemCnt : " + itemCnt + ", itemName : " + itemName);

		CheckListDto checklist = new CheckListDto();
		checklist.setCl_plannum(planNum);
		checklist.setCl_category(category);
		checklist.setCl_categoryname(categoryName);
		checklist.setCl_cnt(itemCnt);
		checklist.setCl_item(itemName);
		
		mv = new ModelAndView();
		
		try {
			tDao.pAddCheckItem(checklist);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return "redirect:pCheckSupFrm?planNum=0";
	}

	//카테고리 추가 페이지 이동
	public ModelAndView pAddCheckCategoryFrm(long category) {
		log.info("service - pAddCheckCategoryFrm() - category : " + category);
		
		mv = new ModelAndView();
		
		mv.addObject("category", category);
		mv.setViewName("pAddCheckCategoryFrm");
		
		return mv;
	}

	//카테고리 추가
	@Transactional
	public String pAddCheckCategory(CheckListDto checklist, RedirectAttributes rttr) {
		log.info("service - pAddCheckCategory()");
		String view = null;
		
		try {
			tDao.pAddCheckItem(checklist);
			
			view = "redirect:pCheckSupFrm?planNum=0";
		} catch (Exception e) {
			e.printStackTrace();
			
			view = "redirect:pAddCheckCategoryFrm?category=" + checklist.getCl_category();
		}
		
		return view;
	}
}
