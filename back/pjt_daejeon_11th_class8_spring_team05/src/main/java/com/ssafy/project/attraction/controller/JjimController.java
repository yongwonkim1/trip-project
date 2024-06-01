package com.ssafy.project.attraction.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.attraction.dto.JjimDTO;
import com.ssafy.project.attraction.service.JjimService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/jjim")
@Tag(name = "찜 컨트롤러", description = "찜 관련 기능 처리하는 클래스")
@CrossOrigin
public class JjimController {

    @Autowired
    private JjimService jservice;

    @Operation(summary = "찜 추가", description = "찜 목록에 관광지 추가")
    @PostMapping("/add")
    public int insertJjim(@RequestBody Map<String, Object> data) {
    	int aid = 0;
    	try{
    		aid = Integer.parseInt((String)data.get("aid"));
    	} catch (ClassCastException e){
    		aid = (int)data.get("aid");
    	}
    	String userId = (String) data.get("user_id");
        return jservice.insertJjim(userId, aid);
    }

    @Operation(summary = "찜 모아보기", description = "찜 리스트들 모아보기")
    @GetMapping("/list")
    public List<Map<String,Object>> selectJjimList(@RequestParam("userId") String userId) {
        return jservice.selectJjimList(userId);
    }

    @Operation(summary = "찜 삭제", description = "찜 목록에서 관광지 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteJjim(@RequestParam String user_id, @RequestParam int aid) {
		return ResponseEntity.ok(jservice.deleteJjim(aid, user_id));
	}
    
    @Operation(summary = "찜 불러오기 (컨텐츠 선택ver)", description = "지역과 컨텐츠 타입으로 해당 찜 리스트들 불러보기")
    @GetMapping("/list/plan")
    public List<Map<String,Object>> selectJjimByContent(@RequestParam("region") int region,
	        @RequestParam("user_id") String user_id,
	        @RequestParam(value = "typeNumbers", required = false) String typeNumbers) {
    	if(typeNumbers==null || typeNumbers.equals("")) {
			return jservice.selectJjim(region, user_id);
		}
		String[] numbers = typeNumbers.trim().split(",");
		return jservice.selectJjimByContent(region, user_id, numbers);
    }
    
    @Operation(summary = "찜 모아보기", description = "찜 리스트들 모아보기")
    @GetMapping("/check")
    public boolean isJjim(@RequestParam("user_id") String user_id, @RequestParam("aid") String aid) {
        return jservice.isJjim(user_id, aid);
    }
}
