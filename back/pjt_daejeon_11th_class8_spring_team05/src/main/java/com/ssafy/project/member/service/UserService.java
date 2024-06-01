package com.ssafy.project.member.service;

import com.ssafy.project.member.dto.UserDTO;

public interface UserService {

	public boolean checkDuplicateId(String userId);
	public void signup(UserDTO userDTO);
	UserDTO login(UserDTO userDTO) throws Exception;
	UserDTO userInfo(String userId) throws Exception;
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
	public String selectNicknameImgById(String userId);
	public String selectImgImgById(String userId);
	public int updateInfo(UserDTO user);
	public int updateMtti(String userId, String mtti);
	
}