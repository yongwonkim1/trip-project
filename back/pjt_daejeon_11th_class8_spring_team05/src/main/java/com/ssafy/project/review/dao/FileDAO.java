package com.ssafy.project.review.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.review.dto.reviewImageDTO;

@Mapper
public interface FileDAO {
	public int insert(reviewImageDTO f);
	public List<String> selectReviewImgByRid(int rid);
//	reviewImageDTO selectOne(int fno);
}
