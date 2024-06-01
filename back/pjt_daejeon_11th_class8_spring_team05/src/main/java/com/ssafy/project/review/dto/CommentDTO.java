package com.ssafy.project.review.dto;

import java.util.Date;

public class CommentDTO {
	private int cid;
	private int rid;
	private String user_id;
	private String content;
	private Date date;
	
	public CommentDTO() {}
	
	public CommentDTO(int cid, int rid, String user_id, String content, Date birth) {
		super();
		this.cid = cid;
		this.rid = rid;
		this.user_id = user_id;
		this.content = content;
		this.date = birth;
	}
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getBirth() {
		return date;
	}

	public void setBirth(Date birth) {
		this.date = birth;
	}

	@Override
	public String toString() {
		return "CommentDTO [cid=" + cid + ", rid=" + rid + ", user_id=" + user_id + ", content=" + content + ", date="
				+ date + "]";
	}
	
	
}
