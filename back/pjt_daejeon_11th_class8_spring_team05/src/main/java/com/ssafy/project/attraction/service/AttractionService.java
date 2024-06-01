package com.ssafy.project.attraction.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.project.attraction.dao.AttractionDAO;

@Service
public class AttractionService {
	@Autowired
	private AttractionDAO adao;
	
	public List<Map<String, Object>> selectAttractionInfo(int region, String keyword, String[] numbers) {
		return adao.selectAttractionInfo(region, keyword, numbers);
	}


	public List<Map<String, Object>> selectAttractionInfoByKeyword(int region, String keyword) {
		return adao.selectAttractionInfoByKeyword(region, keyword);
	}

	public Map<String, Object> selectAttractionDetail(int attractionId) {
		return adao.selectAttractionDetail(attractionId);
	}

	public List<Map<String, Object>> selectReviewByAttractionId(int attractionId){
		return adao.selectReviewByAttractionId(attractionId);
	}


	public List<Map<String, Object>> recommendAttraction(String mtti, int sido_code) {
		return adao.recommendAttraction(mtti, sido_code);
	}

	public List<Map<String, Object>> selectMTTIAttractionInfo(String mtti, int region) {
		return adao.selectMTTIAttractionInfo(mtti, region);
	}


	public List<Map<String, Object>> recommendAttractionNoMTTI() {
		return adao.recommendAttractionNoMTTI();
	}


	public List<Map<String, Object>> selectByReview() {
		return adao.selectByReview();
	}


	public List<Map<String, Object>> selectByJJIM() {
		return adao.selectByJJIM();
	}

}
