package com.bob.stepy.dao;

import java.util.List;
import java.util.Map;

import com.bob.stepy.dto.*;

public interface AdminDao {

	public String getPwd(String id);
	public MemberDto getMemberInfo(String id);

	public List<MemberDto> getMemberList(int num);
	public int getMemberCnt();

	public List<CeoDto> getAllCeoList(int num2);
	public int getAllCeoCnt();

	public List<CeoDto> getApproveList(int num3);
	public int getApproveCeoCnt();

	public List<CeoDto> getPendingList(int num4);
	public int getPendingCeoCnt();

	public int permitStore (String c_num);

	public int deleteMember(String m_id);

	public int deleteStore(String c_num);

	public List<String> getMailList_M ();
	public List<String> getMailList_C ();

	public List<EventDto> getEventList (Integer pageNum);
	public int getEventCnt();

	public EventDto getEventRecord (Integer e_num);
	public List<FileUpDto> getEventFiles(Integer e_num);

	public String getOriName(String sysName);

	public int InsertEvent(EventDto event);
	public boolean fileInsert(Map<String, String> fmap);

	public int deleteEvent(int e_num);

	public int updateEvent(EventDto event);
	public void deleteFile(int f_num);

	public List<ReportDto> getReportList_C (Integer pageNum);
	public int getReportCnt();

	public List<PostDto> getReportList_P (Integer pageNum);
	public int getReportCnt_P();
	
	public List<ReplyDto> getReportList_R (Integer pageNum);
	public int getReportCnt_R();

	public ReportDto getReportRecord(Integer rp_num);
	public CeoDto getCeoRecord(String rp_cnum);

	public int deletePost(Integer num);

	public int deleteReply(Integer num);

	public void updateReport(int rp_num);

	public List<SuggestDto> getSuggestList(Integer num9);
	public int getSuggestCnt();

	public SuggestDto getSuggestRecord(int sug_num);

	public void deleteSuggest(int sug_num);
}//DAO 인터페이스 끝
