package com.ssafy.project.plan.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.project.plan.dao.PlanDAO;
import com.ssafy.project.plan.dto.DetailPlanDTO;
import com.ssafy.project.plan.dto.PlanDTO;

@Service
public class PlanService {
	@Autowired
	private PlanDAO pdao;

	public int insertPlan(PlanDTO item) {
		pdao.insertPlan(item);
		return item.getPid();
	}

	public int deletePlan(int pid, String userId) {
		return pdao.deletePlan(pid, userId);
	}

	public PlanDTO selectOnePlan(int pid) {
		return pdao.selectOnePlan(pid);
	}

	public int insertDetailPlan(DetailPlanDTO dpDTO) {
		return pdao.insertDetailPlan(dpDTO);
	}

	public List<Map<String, Object>> selectOneDayPlan(int pid, int day) {
		return pdao.selectOneDayPlan(pid,day);
	}

	public int deleteDetailPlan(int pid, int day) {
		return pdao.deleteDetailPlan(pid,day);
	}

	public List<Map<String, Object>> selectUserPlan(String id) {
		return pdao.selectUserPlan(id);
	}
}
