package com.ssafy.project.attraction.controller;

import com.ssafy.project.attraction.service.CityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/city")
@Tag(name = "시도 컨트롤러", description = "시도 관련 클래스")
@CrossOrigin
public class CityController {

    @Autowired
    private CityService cService;

    @Operation(summary = "시도 리스트", description = "시도 리스트 반환")
    @GetMapping("/sidoList")
    public Map<Integer, String> getSidoMap() {
        return cService.getSidoMap();
    }
    
    @Operation(summary = "구군 리스트", description = "시도에 따른 구군 리스트 반환")
    @GetMapping("/gugunList/{sidoCode}")
    public Map<Integer, String> getGugunList(@PathVariable int sidoCode) {
        return cService.getGugunMap(sidoCode);
    }
}
