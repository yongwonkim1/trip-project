package com.ssafy.project.attraction.dto;

public class JjimDTO {
	private int jid;
	private int aid;
	private String userId;
	private int sidoCode;
	private int gugunCode;
	private String sidoName;
	private String gugunName;

	public JjimDTO(int jid, int aid, String userId, int sidoCode, int gugunCode, String sidoName, String gugunName) {
		super();
		this.jid = jid;
		this.aid = aid;
		this.userId = userId;
		this.sidoCode = sidoCode;
		this.gugunCode = gugunCode;
		this.sidoName = sidoName;
		this.gugunName = gugunName;
	}

	public int getJid() {
		return jid;
	}

	public void setJid(int jid) {
		this.jid = jid;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSidoCode() {
		return sidoCode;
	}

	public void setSidoCode(int sidoCode) {
		this.sidoCode = sidoCode;
	}

	public int getGugunCode() {
		return gugunCode;
	}

	public void setGugunCode(int gugunCode) {
		this.gugunCode = gugunCode;
	}

	public String getSidoName() {
		return sidoName;
	}

	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}

	public String getGugunName() {
		return gugunName;
	}

	public void setGugunName(String gugunName) {
		this.gugunName = gugunName;
	}

	// Getters and Setters

}
