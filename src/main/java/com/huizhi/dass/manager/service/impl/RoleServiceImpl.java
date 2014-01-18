package com.huizhi.dass.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huizhi.dass.manager.dao.RoleDAO;
import com.huizhi.dass.manager.service.RoleService;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.Role;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;
    
    @Override
    public int countByCriteria(Criteria criteria) {
        return roleDAO.countByExample(criteria);
    }

    @Override
    public int insert(Role record) {
        return roleDAO.insert(record);
    }

    @Override
    public List<Role> getByCriteria(Criteria criteria) {
        return roleDAO.selectByExample(criteria);
    }
    @Override
    public Role getById(Long id) {
        return roleDAO.selectByPrimaryKey(id);
    }
    
    @Cacheable(value = "commonCache", key = "'Role_id_' + #id")
    @Override
    public Role getByIdInCache(Long id) {
        return roleDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByIdSelective(Role record) {
        return roleDAO.updateByPrimaryKeySelective(record);
    }


    @Override
    public PageInfo<Role> getByCriteria(Criteria criteria, int pageNo,
            int pageSize) {
        PageInfo<Role> res = new PageInfo<Role>(pageNo, pageSize);
        int total=this.countByCriteria(criteria);
        res.setTotal(total);
        
        criteria.setOffset(res.getOffset());
        criteria.setRows(pageSize);
        List<Role> list = this.getByCriteria(criteria);
        res.setData(list);
        return res;
    }

}
