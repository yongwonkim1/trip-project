package com.ssafy.project.plan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.plan.dto.DetailPlanDTO;
import com.ssafy.project.plan.dto.PlanDTO;
import com.ssafy.project.plan.service.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/plan")
@CrossOrigin
@Tag(name = "여행 계획 컨트롤러", description = "여행 일정 관리 및 계획 처리 컨트롤러")
public class PlanController {
	@Autowired
	private PlanService pservice;

	@Operation(summary = "여행 계획 추가 시작", description = "여행 계획 생성")
	@PostMapping("/add")
	public ResponseEntity<?> insertPlan(@RequestBody PlanDTO plan) throws IllegalStateException, IOException {
		System.out.println(plan);
		return ResponseEntity.ok(pservice.insertPlan(plan));
	}
	
	@Operation(summary = "여행 계획 삭제", description = "여행 계획 삭제")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePlan(@RequestBody Map<String, Object> data) throws IllegalStateException, IOException {
		String userId = (String) data.get("id");
		int pid = Integer.parseInt((String) data.get("pid"));
		PlanDTO plan = pservice.selectOnePlan(pid);
		if (plan == null || !userId.equals(plan.getUser_id())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
		}
		pservice.deletePlan(pid, userId);
		return ResponseEntity.ok("Plan deleted successfully");
	}
	
	@Operation(summary = "여행 계획 조회", description = "user id에 따른 계획 조회")
	@GetMapping("/search")
	public List<Map<String, Object>> selectUserPlan(@RequestParam String id) {
		return pservice.selectUserPlan(id);
	}

	@Operation(summary = "여행 세부 계획 추가", description = "여행 세부 계획 추가")
	@PostMapping("/detail/add")
	public ResponseEntity<?> insertDetailPlan(@RequestBody Map<String, Object> data) throws IllegalStateException, IOException {
//		System.out.println(data);
		int pid = Integer.parseInt((String) data.get("pid"));
		String userId = (String) data.get("id");
		String aidArray = (String) data.get("aids");
		String[] aid = aidArray.split(",");
		int day = Integer.parseInt((String) data.get("day"));
		
		for (int i = 0; i < aid.length; i++) {
			DetailPlanDTO dpDTO = new DetailPlanDTO();
			dpDTO.setPid(pid);
			dpDTO.setUserId(userId);
			dpDTO.setAid(Integer.parseInt(aid[i]));
			dpDTO.setDay(day);
			dpDTO.setPorder(i+1);
			pservice.insertDetailPlan(dpDTO);
		}
		return ResponseEntity.ok("Detail Plan inserted successfully");
	}
	
	@Operation(summary = "여행 세부 계획 삭제", description = "pid에 따른 해당 day의 세부 계획 전체 삭제")
	@DeleteMapping("/detail/delete")
	public ResponseEntity<?> deleteDetailPlan(@RequestParam int pid, @RequestParam int day) {
//		System.out.println(pid+" "+day);
		if(pservice.deleteDetailPlan(pid,day)==1)
			return ResponseEntity.ok("Plan deleted successfully");
		return ResponseEntity.ok("Plan deleted fail");
	}
	
	@Operation(summary = "여행 세부 계획 조회", description = "pid에 따른 해당 day의 세부 계획 조회")
	@GetMapping("/detail/search")
	public List<Map<String, Object>> selectOneDayPlan(@RequestParam int pid, @RequestParam int day) {
		return pservice.selectOneDayPlan(pid,day);
	}
	
}
