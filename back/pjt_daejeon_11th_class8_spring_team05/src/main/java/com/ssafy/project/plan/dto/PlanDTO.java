package com.ssafy.project.plan.dto;

import java.util.Date;

public class PlanDTO { // 여행 대일정
	int pid;
	String user_id;
	String title;
	String style;
	String image;
	Date startDate;
	int duration;

	public PlanDTO() {
	}

	public PlanDTO(int pid, String user_id, String title, String style, String image, Date startDate, int duration) {
		super();
		this.pid = pid;
		this.user_id = user_id;
		this.title = title;
		this.style = style;
		this.image = image;
		this.startDate = startDate;
		this.duration = duration;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "PlanDTO [pid=" + pid + ", user_id=" + user_id + ", title=" + title + ", style=" + style + ", image="
				+ image + ", startDate=" + startDate + ", Duration=" + duration + "]";
	}
}
