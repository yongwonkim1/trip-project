package com.ssafy.project.attraction.service;

import com.ssafy.project.attraction.dao.CityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityService {

    @Autowired
    private CityDAO cado;

    public Map<Integer, String> getSidoMap() {
        List<Map<String, Object>> sidoList = cado.selectSidoMap();
        Map<Integer, String> sidoMap = new HashMap<>();
        
        for (Map<String, Object> map : sidoList) {
            Integer code = (Integer) map.get("sido_code");
            String name = (String) map.get("sido_name");
            sidoMap.put(code, name);
        }

        return sidoMap;
    }

    public Map<Integer, String> getGugunMap(int sidoCode) {
        List<Map<String, Object>> gugunList = cado.selectGugunMap(sidoCode);
        Map<Integer, String> gugunMap = new HashMap<>();
        
        for (Map<String, Object> map : gugunList) {
            Integer code = (Integer) map.get("gugun_code");
            String name = (String) map.get("gugun_name");
            gugunMap.put(code, name);
        }

        return gugunMap;
    }
}
