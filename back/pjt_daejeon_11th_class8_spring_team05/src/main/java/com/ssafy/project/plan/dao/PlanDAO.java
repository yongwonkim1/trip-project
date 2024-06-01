package com.ssafy.project.plan.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.plan.dto.DetailPlanDTO;
import com.ssafy.project.plan.dto.PlanDTO;
@Mapper
public interface PlanDAO {
	public int insertPlan(PlanDTO item);

	public int deletePlan(@Param("pid") int pid, @Param("userId") String userId);

	public PlanDTO selectOnePlan(int pid);

	public int insertDetailPlan(DetailPlanDTO dpDTO);

	public List<Map<String, Object>> selectOneDayPlan(@Param("pid")int pid, @Param("day") int day);

	public int deleteDetailPlan(@Param("pid")int pid, @Param("day") int day);

	public List<Map<String, Object>> selectUserPlan(String id);

}
