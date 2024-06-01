package com.ssafy.project.attraction.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.project.attraction.dao.JjimDAO;
import com.ssafy.project.attraction.dto.JjimDTO;

@Service
public class JjimService {
	@Autowired
	private JjimDAO jdao;

	public int insertJjim(String userId, int aid) {
        return jdao.insertJjim(userId, aid);
    }

	public List<Map<String, Object>> selectJjimList(String userId) {
		return jdao.selectJjimList(userId);
	}

	public int deleteJjim(int aid, String user_id) {
		return jdao.deleteJjim(aid, user_id);
	}

	public List<Map<String,Object>> selectJjimByContent(int region, String user_id, String[] numbers) {
		return jdao.selectJjimByContent(region, user_id, numbers);
	}

	public List<Map<String,Object>> selectJjim(int region, String user_id) {
		return jdao.selectJjim(region, user_id);
	}

	public boolean isJjim(String user_id, String aid) {
		if(jdao.isJjim(aid, user_id)==1)
			return true;
		return false;
	}

}
