package com.ssafy.project.plan.dto;

import java.util.Date;

public class DetailPlanDTO { // 여행 세부일정

	private int dpid;
	private int pid;
	private String userId;
	private int aid;
	private int day; // 첫째날, 둘째날 등등
	private int porder; // 여행지 순서

	public DetailPlanDTO() {
	}

	public DetailPlanDTO(int dpid, int pid, String userId, int aid, int day, int porder) {
		super();
		this.dpid = dpid;
		this.pid = pid;
		this.userId = userId;
		this.aid = aid;
		this.day = day;
		this.porder = porder;
	}

	public int getDpid() {
		return dpid;
	}

	public void setDpid(int dpid) {
		this.dpid = dpid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getPorder() {
		return porder;
	}

	public void setPorder(int porder) {
		this.porder = porder;
	}

	@Override
	public String toString() {
		return "DetailPlanDTO [dpid=" + dpid + ", pid=" + pid + ", userId=" + userId + ", aid=" + aid + ", day=" + day
				+ ", porder=" + porder + "]";
	}
}
