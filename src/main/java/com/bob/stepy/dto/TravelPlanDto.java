package com.bob.stepy.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TravelPlanDto {
	private int t_plannum;
	private String t_planname;
	private String t_id;
	private String t_spot;
	private String t_stdate;
	private String t_bkdate;
	private String t_member1;
	private String t_member2;
	private String t_member3;
	private String t_member4;
	private String t_member5;
}
