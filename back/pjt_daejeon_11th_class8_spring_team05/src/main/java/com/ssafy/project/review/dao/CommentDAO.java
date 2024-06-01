package com.ssafy.project.review.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.review.dto.CommentDTO;

@Mapper
public interface CommentDAO {
	// 댓글 작성
	public int insertComment(CommentDTO comment);

	// 댓글 삭제
	public int deleteComment(@Param("cid") int cid, @Param("id") String userId);

	// 댓글 수정
	public int updateComment(CommentDTO comment);

	// 리뷰 별 댓글 조회
	public List<Map<String,Object>> selectReviewCommentByRid(int rid);

	public CommentDTO selectCommentByCid(int cid);

	public int getCommentCountByRid(int rid);

}
