package com.ssafy.project.attraction.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttractionDAO {

	// 지역,키워드,콘텐츠로 검색
	public List<Map<String, Object>> selectAttractionInfo(@Param("region") int region,@Param("keyword") String keyword,@Param("typeNumbers") String[] numbers);

	// 지역,키워드로 검색
	public List<Map<String, Object>> selectAttractionInfoByKeyword(@Param("region")int region,@Param("keyword") String keyword);

	// 관광지 상세페이지
	public Map<String, Object> selectAttractionDetail(int attractionId);

	// 특정 관광지와 해당하는 리뷰 조회
	public List<Map<String, Object>> selectReviewByAttractionId(int attractionId);

	public List<Map<String, Object>> recommendAttraction(@Param("mtti") String mtti, @Param("userSidoCode")int sido_code);

	public List<Map<String, Object>> selectMTTIAttractionInfo(@Param("mtti") String mtti, @Param("region")int region);

	public List<Map<String, Object>> recommendAttractionNoMTTI();

	public List<Map<String, Object>> selectByReview();

	public List<Map<String, Object>> selectByJJIM();

}
