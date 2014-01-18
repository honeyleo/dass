package com.huizhi.dass.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huizhi.dass.manager.dao.CityDAO;
import com.huizhi.dass.manager.service.CityService;
import com.huizhi.dass.model.City;
import com.huizhi.dass.model.Criteria;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDAO cityDAO;
    
    @Cacheable(value = "commonCache", key = "'City_id_' + #id")
    @Override
    public City getById(Long id) {
        return cityDAO.selectById(id);
    }

    @Cacheable(value = "commonCache", key = "'CityService_provinces'")
    @Override
    public List<City> getAllProvince() {
        Criteria cir = new Criteria();
        return cityDAO.selectListByCriteria(cir);
    }

}
