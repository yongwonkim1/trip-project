package com.ssafy.project.review.dto;

public class reviewImageDTO {
	private int fid;
	private int rid;
	private String img;
	
	public reviewImageDTO() {
	}
	
	public reviewImageDTO(int fid, int rid, String img) {
		super();
		this.fid = fid;
		this.rid = rid;
		this.img = img;
	}

	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
