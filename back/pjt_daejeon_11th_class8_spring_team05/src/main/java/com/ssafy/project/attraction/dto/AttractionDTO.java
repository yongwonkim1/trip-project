package com.ssafy.project.attraction.dto;

import java.math.BigDecimal;

public class AttractionDTO {
	private int content_id;
	private String overview;
	private int content_type_id;
	private String title;
	private int rating;
	private String first_image;
	private String sido;
	private String gugun;
	private BigDecimal latitude;
	private BigDecimal longitude;

	public AttractionDTO(int content_id, String overview, int content_type_id, String title, int rating,
			String first_image, String sido, String gugun, BigDecimal latitude, BigDecimal longitude) {
		super();
		this.content_id = content_id;
		this.overview = overview;
		this.content_type_id = content_type_id;
		this.title = title;
		this.rating = rating;
		this.first_image = first_image;
		this.sido = sido;
		this.gugun = gugun;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getContent_id() {
		return content_id;
	}

	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public int getContent_type_id() {
		return content_type_id;
	}

	public void setContent_type_id(int content_type_id) {
		this.content_type_id = content_type_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFirst_image() {
		return first_image;
	}

	public void setFirst_image(String first_image) {
		this.first_image = first_image;
	}

	public String getSido() {
		return sido;
	}

	public void setSido(String sido) {
		this.sido = sido;
	}

	public String getGugun() {
		return gugun;
	}

	public void setGugun(String gugun) {
		this.gugun = gugun;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}
