package com.ssafy.project.attraction.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.attraction.dto.JjimDTO;
@Mapper
public interface JjimDAO {

    // 찜 추가
	public int insertJjim(@Param("userId") String userId, @Param("aid") int aid);

    // 사용자의 찜 목록 조회
	public List<Map<String, Object>> selectJjimList(String userId);

    // 찜 삭제
	public  int deleteJjim(@Param("aid") int aid,@Param("uid") String user_id);

	// 찜 불러오기 (컨텐츠 선택ver)
	public List<Map<String,Object>> selectJjimByContent(@Param("region") int region, @Param("id")String user_id, @Param("typeNumbers") String[] numbers);

	// 찜 불러오기 
	public List<Map<String,Object>> selectJjim(@Param("region") int region, @Param("id")String user_id);

	public int isJjim(@Param("aid")String aid, @Param("uid") String user_id);
}
