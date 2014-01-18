package com.huizhi.dass.manager.web.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.exception.AdminException;
import com.huizhi.dass.common.util.DwzJsonUtil;
import com.huizhi.dass.common.util.MessageDigestUtil;
import com.huizhi.dass.common.util.RequestUtil;
import com.huizhi.dass.common.web.Funcs;
import com.huizhi.dass.manager.service.AdminMenuService;
import com.huizhi.dass.manager.service.AdminService;
import com.huizhi.dass.manager.service.RoleService;
import com.huizhi.dass.model.Admin;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.Role;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.model.type.StateType;
import com.huizhi.dass.operation.service.GameService;

@Controller
@RequestMapping("/manager/system/admin")
public class AdminController implements Constants {

    public static final String ADMIN_LIST = "/manager/system/admin/list";

    public static final String ADD_ADMIN = "/manager/system/admin/add";

    public static final String UPDATE_ADMIN = "/manager/system/admin/detail";

    public static final int listPageSize = 20;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private AdminMenuService adminMenuService;
    
    @Autowired
    private GameService gameService;

    /**
     * 用户详情列表
     * 
     * @param request
     * @return ModelAndView
     * @throws AdminException
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request) throws AdminException {
        Admin user = Funcs.getSessionLoginUser(request.getSession());
        if (!DEV_ADMIN_ROLE_ID.equals(user.getRoleId())
                && !SUPER_ADMIN_ROLE_ID.equals(user.getRoleId())) {
            throw new AdminException("您的角色身份不能使用该功能！");
        }

        String realName = RequestUtil.getString(request, "realName");
        String username = RequestUtil.getString(request, "username");
        Integer pageNum = RequestUtil.getInteger(request, "pageNum");
        if (pageNum == null || pageNum <= 0) {// 判断页码是否为空
            pageNum = 1;
        }
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(username)) {
            criteria.put("usernameLike", username);
        }
        if (StringUtils.isNotBlank(realName)) {
            criteria.put("realNameLike", realName);
        }
        PageInfo<Admin> result = adminService.findListByCriteria(criteria, pageNum, listPageSize);
        request.setAttribute("result", result);
        return new ModelAndView(ADMIN_LIST);
    }

    /**
     * 删除用户
     * 
     * @param request
     * @return ModelAndView
     * @throws AdminException
     */
    @RequestMapping("/del")
    public ModelAndView del(HttpServletRequest request) throws AdminException {
        Long id = RequestUtil.getLong(request, "id");
        Admin record = new Admin();
        record.setId(id);
        record.setState(StateType.INACTIVE.getId());
        adminService.updateByIdSelective(record);
        return new ModelAndView(JSON_VIEW, JSON_ROOT,
                DwzJsonUtil.getOkStatusMsg("删除成功"));
    }

    /**
     * 添加
     * 
     * @param request
     * @return
     * @throws AdminException
     */
    @RequestMapping("/goadd")
    public ModelAndView goAdd(HttpServletRequest request) throws AdminException {
        List<Role> roles = roleService.getByCriteria(new Criteria());
        request.setAttribute("roles", roles);
        
        List<Game> games = gameService.queryAllGame();
        request.setAttribute("games", games);
        
        return new ModelAndView(ADD_ADMIN);
    }

    /**
     * 详情
     * 
     * @param request
     * @return
     * @throws AdminException
     */
    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request) throws AdminException {
        Long id = RequestUtil.getLong(request, "id");
        Admin admin = adminService.findById(id);
        request.setAttribute("admin", admin);
        request.setAttribute("gameId", admin.getGameId());

        List<Role> roles = roleService.getByCriteria(new Criteria());
        request.setAttribute("roles", roles);
        
        List<Game> games = gameService.queryAllGame();
        request.setAttribute("games", games);
        
        return new ModelAndView(UPDATE_ADMIN);
    }

    /**
     * 添加
     * 
     * @param request
     * @return ModelAndView
     * @throws AdminException
     */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request) throws AdminException {
        String username = RequestUtil.getString(request, "username");
        Admin extAdmin = adminService.findByUsername(username);
        if (extAdmin != null) {
            throw new AdminException("该用户名已被注册！");
        }
        String password = RequestUtil.getString(request, "passward");
        try {
            password = MessageDigestUtil.getSHA256(password).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realName = RequestUtil.getString(request, "realName");
        String email = RequestUtil.getString(request, "email");
        String phone = RequestUtil.getString(request, "phone");
        String address = RequestUtil.getString(request, "address");
        Long roleId = RequestUtil.getLong(request, "roleId");
        Integer gameId = RequestUtil.getInteger(request, "gameId");

        Admin record = new Admin();
        record.setUsername(username);
        record.setPassword(password);
        record.setRealName(realName);
        record.setEmail(email);
        record.setPhone(phone);
        record.setAddress(address);
        record.setRoleId(roleId);
        record.setState(1);
        if (roleId > 2) {
        	record.setGameId(gameId);
        }
        Long adminId=adminService.add(record);
        
        // 根据角色添加默认权限
        adminMenuService.addAdminDefaultMenu(adminId, roleId);
        return new ModelAndView(JSON_VIEW, JSON_ROOT, DwzJsonUtil.getOkStatusMsg("添加成功"));
    }

    /**
     * 更新
     * 
     * @param request
     * @return ModelAndView
     * @throws AdminException
     */
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request,
            HttpServletResponse response) throws AdminException {
        Long id = RequestUtil.getLong(request, "id");
        String password = RequestUtil.getString(request, "password");
        String realName = RequestUtil.getString(request, "realName");
        String email = RequestUtil.getString(request, "email");
        String phone = RequestUtil.getString(request, "phone");
        String address = RequestUtil.getString(request, "address");
        Long roleId = RequestUtil.getLong(request, "roleId");
        Integer state = RequestUtil.getInteger(request, "state");
        Integer gameId = RequestUtil.getInteger(request, "gameId");

        Admin record = new Admin();
        record.setId(id);
        if (StringUtils.isNotBlank(password)) {// 判断password是否为空
            try {
                password = MessageDigestUtil.getSHA256(password).toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            record.setPassword(password);
        }
        if (StringUtils.isNotBlank(realName)) {// 判断realname是否为空
            record.setRealName(realName);
        }
        record.setEmail(email);
        record.setPhone(phone);
        record.setAddress(address);
        record.setRoleId(roleId);
        record.setState(state);
        record.setGameId(gameId);
        adminService.updateByIdSelective(record);
        return new ModelAndView(JSON_VIEW, JSON_ROOT, DwzJsonUtil.getOkStatusMsg("更新成功"));
    }
    
    

}
