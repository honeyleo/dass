package com.huizhi.dass.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huizhi.dass.manager.dao.AdminMenuDAO;
import com.huizhi.dass.manager.dao.MenuDAO;
import com.huizhi.dass.manager.dao.RoleDefaultMenuDAO;
import com.huizhi.dass.manager.service.MenuService;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.Menu;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDAO menuDAO;
    
    @Autowired
    private AdminMenuDAO adminMenuDAO;
    
    @Autowired
    private RoleDefaultMenuDAO roleDefaultMenuDAO;
    
    @Override
    public int save(Menu record) {
        if(record.getId() == null || record.getId().intValue() < 1){
            return menuDAO.insert(record);
        }else{
            Menu old=this.getById(record.getId());
            int cnt=this.updateByIdSelective(record);
            if(!old.getParentId().equals(record.getParentId())){
                String oldParentIdPath = old.getParentIdPath() + old.getId() + "$";
                String newParentIdPath = record.getParentIdPath() + record.getId() + "$";
                menuDAO.updateChildParentPath(oldParentIdPath, newParentIdPath);
            }
            return cnt;
        }
    }

    @Override
    public Menu getById(Long id) {
        return menuDAO.selectByPrimaryKey(id);
    }
    
    @Cacheable(value = "commonCache", key = "'Menu_id_' + #id")
    @Override
    public Menu getByIdInCache(Long id) {
        return menuDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByIdSelective(Menu record) {
        return menuDAO.updateByPrimaryKeySelective(record);
    }
    
    @Override
    public void deleteById(Long id){
        menuDAO.deleteByPrimaryKey(id);
        adminMenuDAO.deleteByMenuId(id);
        roleDefaultMenuDAO.deleteByMenuId(id);
    }

	@Override
	public List<Menu> findMenuList() {

		return menuDAO.selectByExample(new Criteria());
	}

}
