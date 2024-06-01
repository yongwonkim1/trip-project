package com.ssafy.project.review.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.global.util.JWTUtil;
import com.ssafy.project.review.dto.CommentDTO;
import com.ssafy.project.review.dto.ReviewDTO;
import com.ssafy.project.review.service.ReviewService;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/review")
@CrossOrigin
@Tag(name = "후기 게시판 컨트롤러", description = "게시판의 후기, 댓글 등 처리하는 클래스")
public class ReviewController {

	@Autowired
	private ReviewService rservice;

	@Operation(summary = "후기 작성", description = "후기 글과 사진 등록")
	@PostMapping("/add")
	public ResponseEntity<?> insertReview(@RequestBody Map<String, Object> data)
			throws IllegalStateException, IOException {
//		System.out.println(data);
		Map<String, Object> reviewData = (Map<String, Object>) data.get("review");
		ReviewDTO review = new ReviewDTO();
		review.setAid(Integer.parseInt((String) reviewData.get("aid")));
		review.setTitle((String) reviewData.get("title"));
		review.setContents((String) reviewData.get("contents"));
		review.setRating((int) reviewData.get("rating"));

		// uploadedFiles 정보 추출
		String uploadedFilesString = (String) data.get("uploadedFiles");
		String[] uploadedFiles = uploadedFilesString.split(",");

		// id 추출
		String userId = (String) data.get("id");
		review.setUser_id(userId);

		return ResponseEntity.ok(rservice.insertReview(review, uploadedFiles));
	}

	@Operation(summary = "후기 삭제", description = "후기 글의 번호로 게시물 삭제")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteReview(@RequestBody Map<String, Object> data) {
		String userId = (String) data.get("id");
		int rid = Integer.parseInt((String) data.get("rid"));
		ReviewDTO review = rservice.selectOneReviewByRid(rid);
		if (review == null || !userId.equals(review.getUser_id())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
		}
		System.out.println(review.getRid());
		rservice.deleteReview(review.getRid());
		return ResponseEntity.ok("Review deleted successfully");
	}

	@Operation(summary = "후기 수정", description = "새로운 후기로 재등록하여 수정 처리")
	@PutMapping("/update")
	public ResponseEntity<?> updateReview(@RequestBody Map<String, Object> data) {
		int rid = Integer.parseInt((String) data.get("rid"));
		ReviewDTO review = rservice.selectOneReviewByRid(rid);
		String userId = (String) data.get("id");

		if (review == null || !userId.equals(review.getUser_id())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
		}
		rservice.updateReview(review);
		return ResponseEntity.ok("Review updated successfully");
	}

	@Operation(summary = "후기글 한개 조회", description = "후기글의 id로 글 조회")
	@GetMapping("/{rid}/detail")
	public Map<String, Object> selectReviewByRid(@PathVariable int rid) {
		return rservice.selectReviewByRid(rid);
	}

	@Operation(summary = "후기의 댓글 조회", description = "후기글의 id로 해당 글의 댓글 조회")
	@GetMapping("/{rid}/comment")
	public List<Map<String,Object>> selectReviewCommentByRid(@PathVariable int rid) {
		return rservice.selectReviewCommentByRid(rid);
	}

	@Operation(summary = "후기의 사진 조회", description = "후기들의 id로 해당 글의 사진 조회")
	@GetMapping("/{rid}/images")
	public List<String> selectReviewImgByRid(@PathVariable int rid) {
		return rservice.selectReviewImgByRid(rid);
	}

	@Operation(summary = "후기 검색", description = "키워드로 후기 검색(전체 조회 가능)")
	@GetMapping("/search")
	public List<Map<String, Object>> selectReviewByKeyword(
			@RequestParam(value = "keyword", defaultValue = "") String keyword) {
		if (keyword.equals("") || keyword.isEmpty()) {
			return rservice.selectAllReview();
		} else {
			return rservice.selectReview(keyword);
		}
	}

	@Operation(summary = "내가 쓴 후기 모아보기", description = "사용자의 id로 사용자가 작성한 후기 조회")
	@GetMapping("myList")
	public List<Map<String, Object>> selectReviewByUserId(@RequestParam String user_id) {
		return rservice.selectReviewByUserId(user_id);
	}

	@Operation(summary = "후기 좋아요 기능", description = "특정 게시물의 좋아요 기능")
	@GetMapping("/like/add")
	public ResponseEntity<?> likeReviewByRid(@RequestParam String user_id, @RequestParam int rid) {
		return ResponseEntity.ok(rservice.likeReviewByRid(rid, user_id));
	}
	
	@Operation(summary = "후기 좋아요 체크", description = "현재 user_id로 좋아요 눌렀는지 안눌렀는지 체크")
	@GetMapping("/like/check")
	public boolean checkLikeReviewByRid(@RequestParam String user_id, @RequestParam int rid) {
		return rservice.checkLikeReviewByRid(rid, user_id);
	}

	@Operation(summary = "후기 좋아요 취소 기능", description = "특정 게시물의 좋아요 취소 기능")
	@DeleteMapping("/like/delete")
	public ResponseEntity<?> deleteLikeReviewByRid(@RequestParam String user_id, @RequestParam int rid) {
		return ResponseEntity.ok(rservice.deleteLikeReviewByRid(rid, user_id));
	}

	@Operation(summary = "좋아요한 후기 모아보기", description = "좋아요한 게시물을 모두 보기")
	@GetMapping("/likes")
	public List<Map<String, Object>> LikeReviewByUid(@RequestParam String user_id) {
		return rservice.likeReviewByUid(user_id);
	}

	@Operation(summary = "댓글 작성", description = "새로운 댓글 등록")
	@PostMapping("/comment/add")
	public ResponseEntity<?> insertComment(@RequestBody CommentDTO comment)
			throws IllegalStateException, IOException {
//		Map<String, Object> commentData = (Map<String, Object>) data.get("comment");
//		CommentDTO comment = new CommentDTO();
//		comment.setRid(Integer.parseInt((String) commentData.get("rid")));
//		comment.setContent((String) commentData.get("contents"));
//		System.out.println(comment);
		return ResponseEntity.ok(rservice.insertComment(comment));
	}

	@Operation(summary = "댓글 삭제", description = "사용자와 작성자가 동일한 경우 삭제 하기")
	@DeleteMapping("/comment/delete")
	public ResponseEntity<?> deleteCommentByRid(@RequestBody Map<String, Object> data) {
		String user_id = (String) data.get("id");
		int cid = Integer.parseInt((String) data.get("cid"));
		CommentDTO comment = rservice.selectCommentByCid(cid);
		if (comment == null || !user_id.equals(comment.getUser_id())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
		}

		rservice.deleteCommentByRid(cid, user_id);
		return ResponseEntity.ok("Comment deleted successfully");
	}

	@Operation(summary = "댓글 수정", description = "사용자와 작성자가 동일한 경우 댓글 업데이트")
	@PutMapping("/comment/update")
	public ResponseEntity<?> updateComment(@RequestBody Map<String, Object> data)
			throws IllegalStateException, IOException {
		int cid = Integer.parseInt((String) data.get("cid"));
		CommentDTO comment = rservice.selectCommentByCid(cid);
		String userId = (String) data.get("id");

		if (comment == null || !userId.equals(comment.getUser_id())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
		}
		rservice.updateComment(comment);
		return ResponseEntity.ok("Comment updated successfully");
	}
	
	@Operation(summary = "후기글의 좋아요 댓글수 조회수", description = "후기글의 id로 글 조회")
	@GetMapping("/{rid}/count")
	public ResponseEntity<?> selectReviewCandLByRid (@PathVariable int rid) {
	    Map<String, Object> reviewData = new HashMap<>();

	    // 좋아요 수 가져오기
	    int likeCount = rservice.getLikeCountByRid(rid);
	    reviewData.put("likeCount", likeCount);

	    // 댓글 수 가져오기
	    int commentCount = rservice.getCommentCountByRid(rid);
	    reviewData.put("commentCount", commentCount);
	    
	 // 조회 수 가져오기
	    int readCount = rservice.getReadCountByRid(rid);
	    reviewData.put("readCount", readCount);

	    return ResponseEntity.ok(reviewData);
	}
}
