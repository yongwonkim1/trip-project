package com.ssafy.project.review.dto;

import java.util.Date;

public class ReviewDTO {

	private int rid;
	private String user_id;
	private int aid;
	private String title;
	private String contents;
	private int rating;
	private int view_count;
	private Date date;

	public ReviewDTO() {
		super();
	}

	public ReviewDTO(int rid, String user_id, int aid, String title, String contents, int rating, int view_count,
			Date date) {
		super();
		this.rid = rid;
		this.user_id = user_id;
		this.aid = aid;
		this.title = title;
		this.contents = contents;
		this.rating = rating;
		this.view_count = view_count;
		this.date = date;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}