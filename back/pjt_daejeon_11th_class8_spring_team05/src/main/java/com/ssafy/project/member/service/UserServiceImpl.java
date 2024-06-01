package com.ssafy.project.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.project.member.dao.UserDAO;
import com.ssafy.project.member.dto.UserDTO;


@Service
public class UserServiceImpl implements UserService {
	
	private UserDAO udao;

	public UserServiceImpl(UserDAO udao) {
		super();
		this.udao = udao;
	}
	
	@Override
	public boolean checkDuplicateId(String userId) {
		return udao.checkDuplicateId(userId) > 0;
	}
	
	@Override
	public void signup(UserDTO userDTO) {
		udao.insertUser(userDTO);
	}

	@Override
	public UserDTO login(UserDTO userDTO) throws Exception {
		return udao.login(userDTO);
	}
	
	@Override
	public UserDTO userInfo(String userId) throws Exception {
		return udao.userInfo(userId);
	}

	@Override
	public void saveRefreshToken(String userId, String refreshToken) throws Exception {
		udao.saveRefreshToken(userId, refreshToken);
	}

	@Override
	public Object getRefreshToken(String userId) throws Exception {
		return udao.getRefreshToken(userId);
	}

	@Override
	public void deleRefreshToken(String userId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", null);
		udao.deleteRefreshToken(map);
	}

	@Override
	public String selectNicknameImgById(String userId) {
		return udao.selectNicknameImgById(userId);
	}

	@Override
	public String selectImgImgById(String userId) {
		return udao.selectImgImgById(userId);
	}

	@Override
	public int updateInfo(UserDTO user) {
		return udao.updateInfo(user);
	}

	@Override
	public int updateMtti(String userId, String mtti) {
		return udao.updateMtti(userId,mtti);
	}

}
