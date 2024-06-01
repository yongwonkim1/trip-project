package com.ssafy.project.attraction.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.attraction.service.AttractionService;
import com.ssafy.project.member.dto.UserDTO;
import com.ssafy.project.member.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/attraction")
@CrossOrigin
@Tag(name = "관광지 검색 컨트롤러", description = "관광지 검색, 상세페이지 처리하는 클래스")
public class AttractionController {

	@Autowired
	private AttractionService aservice;
	
	@Autowired
	private UserService userService;

	@Operation(summary = "관광지 검색", description = "지역,키워드,콘텐츠로 관광지들과 각각의 평균 평점 검색")
	@GetMapping("/search")
	public List<Map<String, Object>> selectAttractionInfo(@RequestParam("region") int region,
	        @RequestParam("keyword") String keyword,
	        @RequestParam(value = "typeNumbers", required = false) String typeNumbers) throws UnsupportedEncodingException {
		if(typeNumbers==null || typeNumbers.equals("")) {
			return aservice.selectAttractionInfoByKeyword(region,keyword);
		}
		String[] numbers = typeNumbers.trim().split(",");
		return aservice.selectAttractionInfo(region,keyword, numbers);
	}
	
	@Operation(summary = "MTTI 관광지 검색", description = "지역,mtti로 관광지들과 각각의 평균 평점 검색 -> MTTI 페이지용")
	@GetMapping("/mtti")
	public List<Map<String, Object>> selectMTTIAttractionInfo(
	    @RequestParam("userId") String userId, @RequestParam("region") int region) throws Exception {

	    UserDTO user = userService.userInfo(userId);  // 사용자 정보를 가져오는 메소드
	    String mtti = user.getMtti();

	    return aservice.selectMTTIAttractionInfo(mtti, region);
	}


	@Operation(summary = "관광지 상세페이지", description = "관광지 번호로 특정 관광지와 평균 평점 조회")
	@GetMapping("/detail")
	public Map<String, Object> selectAttractionDetail(@RequestParam("attractionId") int attractionId) {
		return aservice.selectAttractionDetail(attractionId);
	}
	
	@Operation(summary = "관광지 후기", description = "관광지 번호로 특정 관광지의 후기조회")
	@GetMapping("/reviewList")
	public List<Map<String, Object>> selectReviewByAttractionId(@RequestParam("attractionId") int attractionId) {
		return aservice.selectReviewByAttractionId(attractionId);
	}
	
	@Operation(summary = "MTTI 여행지 추천", description = "MTTI 별 여행지 추천")
	@GetMapping("/recommend")
	public List<Map<String, Object>> recommendAttraction(@RequestParam("userId") String userId) throws Exception {
		if(userId==null||userId.equals(""))
			return aservice.recommendAttractionNoMTTI();
	    UserDTO user = userService.userInfo(userId);  // 사용자 정보를 가져오는 메소드
	    if(user.getMtti()==null||user.getMtti().equals("")) {
			return aservice.recommendAttractionNoMTTI();
	    }
	    return aservice.recommendAttraction(user.getMtti(),user.getSido_code());
	}
	 
	@Operation(summary = "리뷰 인기순", description = "리뷰 갯수와 평점으로 리뷰 조회")
	@GetMapping("/highRating")
	public List<Map<String, Object>> selectByReview() {
		return aservice.selectByReview();
	}
	
	@Operation(summary = "찜 많은 순", description = "찜 갯수로 리뷰 조회")
	@GetMapping("/manyJjim")
	public List<Map<String, Object>> selectByJJIM() {
		return aservice.selectByJJIM();
	}
}
