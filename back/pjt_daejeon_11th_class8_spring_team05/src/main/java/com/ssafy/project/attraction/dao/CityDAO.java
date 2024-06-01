package com.ssafy.project.attraction.dao;

import java.util.List;
import java.util.Map;

public interface CityDAO {
    List<Map<String, Object>> selectSidoMap();
    List<Map<String, Object>> selectGugunMap(int sidoCode);
}
