package com.ssafy.project.review.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.review.dto.CommentDTO;
import com.ssafy.project.review.dto.ReviewDTO;
@Mapper
public interface ReviewDAO {
	
    // 리뷰 추가
	public int insert(ReviewDTO review);

    // 리뷰 이미지 추가
//	public int insertreview_img(@Param("rid")int rid, @Param("img")String img);

    // 리뷰 삭제
	public int deleteReview(int rid);

    // 리뷰 업데이트
	public  int updateReview(ReviewDTO review);

    // 리뷰 조회
	public Map<String, Object> selectReviewByRid(int rid);

    // 리뷰 이미지 조회
//	public List<String> selectReviewImgByRid(int rid);

	// 키워드 리뷰 조회
	public List<Map<String,Object>> selectReview(String keyword);

	// 전체 리뷰 조회
	public List<Map<String, Object>> selectAllReview();

	// 내가 쓴 리뷰 모아보기
	public List<Map<String, Object>> selectReviewByUserId(String user_id);

	// 게시물 좋아요
	public int likeReviewByRid(@Param("rid")int rid, @Param("id")String userId);

	// 게시물 좋아요 취소
	public int deleteLikeReviewByRid(@Param("rid")int rid, @Param("id")String userId);

	// 좋아요한 게시물 모아보기
	public List<Map<String, Object>> likeReviewByUid(String userId);

	public ReviewDTO selectOneReviewByRid(int rid);

	public int getLikeCountByRid(int rid);

	public int getReadCountByRid(int rid);

	public int checkLikeReviewByRid(int rid, String userId);

	public int updateReadCount(int rid);

	
}