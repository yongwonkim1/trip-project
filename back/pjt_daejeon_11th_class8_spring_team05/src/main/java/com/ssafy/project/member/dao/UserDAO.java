package com.ssafy.project.member.dao;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.ssafy.project.member.dto.UserDTO;

@Mapper
public interface UserDAO {

	int checkDuplicateId(String userId);
	void insertUser(UserDTO userDTO);
	UserDTO login(UserDTO userDTO) throws SQLException;
	UserDTO userInfo(String userId) throws SQLException;
	void saveRefreshToken(@Param("userId") String userId,@Param("refreshToken") String refreshToken) throws SQLException;
	Object getRefreshToken(String userid) throws SQLException;
	void deleteRefreshToken(Map<String, String> map) throws SQLException;
	String selectNicknameImgById(String userId);
	String selectImgImgById(String userId);
	int updateInfo(UserDTO user);
	int updateMtti(@Param("userId") String userId,@Param("mtti") String mtti);
}
