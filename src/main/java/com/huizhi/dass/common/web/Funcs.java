package com.huizhi.dass.common.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.stereotype.Component;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.util.RequestUtil;
import com.huizhi.dass.manager.service.CityService;
import com.huizhi.dass.manager.service.MenuService;
import com.huizhi.dass.manager.service.RoleService;
import com.huizhi.dass.model.Admin;
import com.huizhi.dass.model.City;
import com.huizhi.dass.model.LoginAccount;
import com.huizhi.dass.model.Menu;
import com.huizhi.dass.model.Role;

@Component
public class Funcs implements Constants {
    private static RoleService roleService;

    private static MenuService menuService;

    private static CityService cityService;
    

    public static Admin getSessionLoginUser(HttpSession session) {
        if (null == session) {
            return null;
        }
        LoginAccount account = (LoginAccount) session.getAttribute(SESSION_LOGIN_USER);
        if (account != null) {
        	return account.getUser();
        }
        return null;
    }

    public static LoginAccount getSessionLoginAccount(HttpSession session) {
    	if (null == session) {
    		return null;
    	}
    	LoginAccount account = (LoginAccount) session.getAttribute(SESSION_LOGIN_USER);
    	return account;
    }

    public static String formatDateTime(Date date, String parten) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(parten);
        return format.format(date);
    }
    
    //返回给定时间往后推n天的日期
    public static String getNextNumDate(Date date, Integer dayNum, String parten) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(parten);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, dayNum);
        return format.format(c.getTime());
    }

    //根据数字表示的年周返回特定周的日期
    public static String getDayOfWeekDate(Integer yearWeek, String parten, Integer dayOfWeek) 
    {
        if (0 == yearWeek) 
        {
            return "";
        }
        String tmp = String.valueOf(yearWeek);
        //获取年
        int year = Integer.valueOf(tmp.substring(0, 4));
        int week = Integer.valueOf(tmp.substring(4));
        Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, week);
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		
        SimpleDateFormat format = new SimpleDateFormat(parten);
        return format.format(c.getTime());
    }
    
    public static String roundDouble(Double num, Integer r) {
        if (null == num) {
            return "";
        }
        BigDecimal b = new BigDecimal(num);
        num = b.setScale(r, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num.toString();
    }

    public static boolean isContains(Object id, List<Object> ids) {
        if (ids == null || ids.size() == 0 || id == null) {
            return false;
        }
        if (ids.contains(id)) {
            return true;
        }
        return false;
    }

    public static String getRoleName(Long roleId) {
        if (roleId == null) {
            return "";
        }
        Role r = roleService.getByIdInCache(roleId);
        if (r == null) {
            return "";
        }
        return r.getName();
    }

    public static String getMenuName(Long menuId) {
        Menu m = menuService.getByIdInCache(menuId);
        if (m != null) {
            return m.getName();
        }
        return "";
    }

    public static String getCityName(Long cityId){
        City city = cityService.getById(cityId);
        return city == null ? "" : city.getName();
    }
    
    public static String formatPrice(Double price){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price / 100);
    }
    
    public static String formatDoubleNumber(Double num, String fmtStr){
        DecimalFormat df = new DecimalFormat(fmtStr);
        return df.format(num);
    }
    
    @Autowired
    public void setRoleService(RoleService roleService) {
        Funcs.roleService = roleService;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        Funcs.menuService = menuService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        Funcs.cityService = cityService;
    }

}
