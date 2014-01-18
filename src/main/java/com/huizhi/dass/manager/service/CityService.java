package com.huizhi.dass.manager.service;

import java.util.List;

import com.huizhi.dass.model.City;

public interface CityService {
    
    City getById(Long id);
    
    List<City> getAllProvince();
}
