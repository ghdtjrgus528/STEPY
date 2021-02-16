package com.bob.stepy.dao;

import java.util.List;
import java.util.Map;

import javax.mail.Store;

import com.bob.stepy.dto.CeoDto;
import com.bob.stepy.dto.FileUpDto;
import com.bob.stepy.dto.InCartDto;
import com.bob.stepy.dto.ProductDto;
import com.bob.stepy.dto.ResTicketDto;
import com.bob.stepy.dto.StoreDto;


public interface StoreDao {
	
	//회원가입
	public void cJoinProc(CeoDto ceo);
	public void stJoinProc(StoreDto store);	

	//사업자등록증 저장
	public boolean stFileUp(Map<String, String> fmap);
	
	//사업자번호 중복체크
	public int stIdCheck(String c_num);	
	
	//로그인 전 c_join 정보 가져오기
	public String getCjoin(String c_num);
	//비밀번호 구하기
	public String getStEncPwd(String c_num);
	
	//로그인 후 업체 정보 가져오기
	public CeoDto getCeoInfo(String c_num);
	public StoreDto getStoreInfo(String c_num);
	
	//비밀번호 찾기
	//1단계는 중복체크 메소드, 2단계는 업체정보 가져오기 메소드 활용
	public boolean stResetPwdProc(CeoDto ceo);	
	
	//상품 리스트 가져오기
	public List<ProductDto> getProdList(String pl_cnum);
	//상품 정보 가져오기
	public ProductDto getProdInfo(int pl_num);
	
	//상품 등록
	public boolean stProdInsert(ProductDto product);
	//상품 메인사진 등록
	public boolean stProdThumbUp(Map<String, String> tmap);
	//상품 추가사진 등록
	public boolean stProdFileUp(Map<String, String> pmap);
	//상품 메인사진 가져오기
	public FileUpDto getProdThumb(int f_plnum);
	//상품 추가사진 가져오기
	public List<FileUpDto> getProdPhotos(int f_plnum);
	
	//사업주 매장 기본정보 변경하기
	public boolean stModifyCeo(CeoDto ceo);
	public boolean stModifyStore(StoreDto store);
	
	//사업주 비밀번호 가져오기
	public String stGetPwd(String c_num);
	//사업주 비밀번호 변경
	public boolean stModifyPwd(CeoDto ceo);
	
	//스토어 메인사진 등록
	public boolean stThumbUp(Map<String, String> smap);
	//스토어 추가사진 등록
	public boolean stPhotoUp(Map<String, String> smap);
	//스토어 메인사진 불러오기
	public FileUpDto getThumb(String f_cnum);
	//스토어 메인사진 삭제하기
	public void stDeleteThumb(String f_sysname);
	//스토어 추가사진 불러오기
	public List<FileUpDto> getPhotos(String f_cnum);
	//스토어 추가사진 삭제
	public void stDeletePhotos(String f_cnum);
	//삭제할 스토어 추가사진들의 f_sysname 가져오기
	public List<String> getPtsSys(String f_cnum);
	
	//가입대기(미인증) 업체 불러오기
	public List<CeoDto> getAuthList();
	
	//찜
	public boolean stIncart(InCartDto incart);
	//찜 해제
	public boolean stIncartEmpty(InCartDto incart);
	//찜 정보 가져오기
	public int GetIncart(InCartDto incart);
	
	//상품 삭제하기(상품정보와 사진모두 삭제)
	public List<FileUpDto> stGetProdPhotos(int pl_num);
	public void stDeleteProd(int pl_num); 
	public void stDeleteProdPhoto(int pl_num);
	
	//업체 메일 인증 업데이트
	public void stUpdateJoin(String c_num);
		
	//사업자등록증 가져오기
	public FileUpDto stGetBiz(String c_num);
	
	
	//전체 예약 정보 불러오기
	public List<ResTicketDto> stTotalResList(int res_plnum);	
	//예약 번호-정보 불러오기
	public ResTicketDto getResInfo(int res_num);
	//해당 상품 이름 가져오기
	public String getPlname(int res_plnum);
	

}
