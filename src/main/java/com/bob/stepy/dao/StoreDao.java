package com.bob.stepy.dao;

import java.util.List;
import java.util.Map;

import com.bob.stepy.dto.CeoDto;
import com.bob.stepy.dto.FileUpDto;
import com.bob.stepy.dto.ProductDto;
import com.bob.stepy.dto.StoreDto;



public interface StoreDao {
	
	//회원가입
	public void cJoinProc(CeoDto ceo);
	public void stJoinProc(StoreDto store);
	
	//사업자번호 중복체크
	public int stIdCheck(String c_num);	
	//사업자등록증 저장
	public boolean stFileUp(Map<String, String> fmap);
	
	//비밀번호 구하기
	public String getStEncPwd(String c_num);	
	
	//로그인 후 업체 정보 가져오기
	public CeoDto getCeoInfo(String c_num);
	public StoreDto getStoreInfo(String c_num);
	
	//비밀번호 찾기
	//1단계는 중복체크 메소드 활용
	//2단계는 업체정보 가져오기 메소드 활용x
	public boolean stResetPwdProc(CeoDto ceo);	
	
	//상품 리스트 가져오기
	public List<ProductDto> getProdList(String pl_cnum);
	//상품 정보 가져오기
	public ProductDto getProdInfo(int pl_num);
	
	//상품 등록하기
	public boolean stProdInsert(ProductDto product);
	//상품 메인사진 등록
	public boolean stProdThumbUp(Map<String, String> tmap);
	//상품 추가사진 업로드
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
	
	//스토어 메인사진 업로드
	public boolean stThumbUp(Map<String, String> smap);
	//스토어 사진 추가
	public boolean stPhotoUp(Map<String, String> smap);
	//스토어 메인사진 불러오기
	public FileUpDto getThumb(String f_cnum);
	//스토어 메인사진 삭제하기
	public void stDeleteThumb(String f_sysname);
	//스토어 추가사진 불러오기
	public List<FileUpDto> getPhotos(String f_cnum);
	
	
	
	
	//우리 가게 후기 불러오기
	
	//우리 가게 신고 불러오기
	

}
