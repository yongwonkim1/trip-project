package com.ssafy.project.review.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.review.dao.CommentDAO;
import com.ssafy.project.review.dao.FileDAO;
import com.ssafy.project.review.dao.ReviewDAO;
import com.ssafy.project.review.dto.CommentDTO;
import com.ssafy.project.review.dto.ReviewDTO;
import com.ssafy.project.review.dto.reviewImageDTO;

@Service
public class ReviewService {
	@Autowired
	private ReviewDAO rdao;
	@Autowired
	private CommentDAO cdao;
	@Autowired
	private FileDAO fdao;

	public int insertReview(ReviewDTO review, String[] files) throws IOException {
		rdao.insert(review); // 파일 첨부해서 디비에 기록하려면 지금 작성한 게시글 번호가 필요함.

		if (files != null) {
			for (String file : files) {
				reviewImageDTO dto = new reviewImageDTO();
				dto.setImg(file); // 파일의 원래 이름
				dto.setRid(review.getRid()); // 어느 게시글에 첨부된 파일인지
				fdao.insert(dto);
			}
		}

		return review.getRid();
	}

	public int deleteReview(int rid) {
		return rdao.deleteReview(rid);
	}

	public int updateReview(ReviewDTO review) {
		return rdao.updateReview(review);
	}

	public Map<String, Object> selectReviewByRid(int rid) {
		if(rdao.updateReadCount(rid)==1)
			return rdao.selectReviewByRid(rid);
		return null;
	}

	public List<String> selectReviewImgByRid(int rid) {
		return fdao.selectReviewImgByRid(rid);
	}

	public List<Map<String, Object>> selectReview(String keyword) {
		return rdao.selectReview(keyword);
	}
	
	public List<Map<String, Object>> selectAllReview() {
		return rdao.selectAllReview();
	}

	public List<Map<String,Object>> selectReviewCommentByRid(int rid) {
		return cdao.selectReviewCommentByRid(rid);
	}

	public List<Map<String, Object>> selectReviewByUserId(String user_id) {
		return rdao.selectReviewByUserId(user_id);
	}

	public int likeReviewByRid(int rid, String userId) {
		return rdao.likeReviewByRid(rid, userId);
	}

	public int deleteLikeReviewByRid(int rid, String userId) {
		return rdao.deleteLikeReviewByRid(rid, userId);
	}

	public List<Map<String, Object>> likeReviewByUid(String userId) {
		return rdao.likeReviewByUid(userId);
	}

	public int insertComment(CommentDTO comment) {
		return cdao.insertComment(comment);
	}

	public int deleteCommentByRid(int cid, String userId) {
		return cdao.deleteComment(cid, userId);
	}

	public int updateComment(CommentDTO comment) {
		return cdao.updateComment(comment);
	}
	
	public CommentDTO selectCommentByCid(int cid) {
        return cdao.selectCommentByCid(cid);
    }

	public ReviewDTO selectOneReviewByRid(int rid) {
		return rdao.selectOneReviewByRid(rid);
	}

	public int getLikeCountByRid(int rid) {
        return rdao.getLikeCountByRid(rid);
    }
	
	public int getCommentCountByRid(int rid) {
        return cdao.getCommentCountByRid(rid);
    }

	public int getReadCountByRid(int rid) {
        return rdao.getReadCountByRid(rid);
	}

	public boolean checkLikeReviewByRid(int rid, String userId) {
		if(rdao.checkLikeReviewByRid(rid,userId)==1)
			return true;
		return false;
	}
}
