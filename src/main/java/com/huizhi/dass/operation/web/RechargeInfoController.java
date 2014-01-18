package com.huizhi.dass.operation.web;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.exception.AdminException;
import com.huizhi.dass.common.util.DwzJsonUtil;
import com.huizhi.dass.common.util.RequestUtil;
import com.huizhi.dass.common.web.Funcs;
import com.huizhi.dass.model.Admin;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.DayRechargeInfo;
import com.huizhi.dass.operation.service.RechargeInfoService;

@Controller
@RequestMapping("/manager/rechargeInfo")
public class RechargeInfoController {
	
	@Autowired
	private RechargeInfoService riService;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpSession session) {
		Integer nowPage = RequestUtil.getInteger(request, "pageNum");
		Integer pageSize = RequestUtil.getInteger(request, "numPerPage");
		nowPage = nowPage == null ? 1 : nowPage;
		pageSize = pageSize == null ? Constants.LIST_PAGESIZE : pageSize;
		
		Date startTime = RequestUtil.getDate(request, "startTime", "yyyy-MM-dd");
		Date endTime = RequestUtil.getDate(request, "endTime", "yyyy-MM-dd");
		if (startTime != null) {
			request.setAttribute("startTime", startTime);
		}
		if (endTime != null) {
			request.setAttribute("endTime", endTime);
		}
		
		Integer audit = RequestUtil.getInteger(request, "audit");
		request.setAttribute("audit", audit);

		Integer gameId = RequestUtil.getInteger(request, "gameId");
		request.setAttribute("gameId", gameId);
		
		Admin user = Funcs.getSessionLoginUser(session);
		
		Criteria criteria = new Criteria();
		criteria.put("audit", audit);
		criteria.put("gameId", gameId);
		criteria.put("startTime", startTime);
		criteria.put("endTime", endTime);
		
		if (user.getRoleId() > 2) {
			criteria.put("audit", 1);
			criteria.put("gameId", user.getGameId());
		}
		
		nowPage = (nowPage == 0) ? 1 : nowPage;
		pageSize = (pageSize == 0) ? Constants.LIST_PAGESIZE : pageSize;
		int mysqlOffset = (nowPage - 1) * pageSize;
		criteria.setOffset(mysqlOffset);
		criteria.setRows(pageSize);
		
		PageInfo<DayRechargeInfo> pager = riService.queryByDateAndGame(criteria);
		request.setAttribute("result", pager);
		pager.setNowPage(nowPage);
		pager.setPageSize(pageSize);
		
		return "/operation/recharge/list";
	}
	
	@RequestMapping("/audit")
	public ModelAndView updateStatus(HttpServletRequest request, HttpSession session) {
		Long id = RequestUtil.getLong(request, "id");
		Integer audit = RequestUtil.getInteger(request, "audit");
		
		DayRechargeInfo info = new DayRechargeInfo();
		info.setId(id);
		info.setAudit(audit);
		
		boolean isUpdate = riService.updateAudit(info);
		if (isUpdate) {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getOkStatusMsg("更新成功"));
		} else {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getErrorStatusMsg("更新失败"));
		}
		
	}
	
    /**
     * 详情
     * @param request
     * @return
     * @throws AdminException
     */
    @RequestMapping("/detail")
    public String detail(HttpServletRequest request) throws AdminException {
        Long id = RequestUtil.getLong(request, "id");
        if(id == null) {
            throw new AdminException("请选择要修改的数据");
        }
        
        DayRechargeInfo info = riService.queryById(id);
        request.setAttribute("recharge", info);
        
        return "/operation/recharge/detail";
    }
	
	
	/**
	 * 修改数据
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, HttpSession session) {
		Long id = RequestUtil.getLong(request, "id");
        Double money = RequestUtil.getDouble(request, "money");
		
        DayRechargeInfo info = new DayRechargeInfo();
        info.setId(id);
        info.setMoney(new BigDecimal(money));
        
		boolean isUpdate = riService.updateById(info);
		if (isUpdate) {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getOkStatusMsg("更新成功"));
		} else {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getErrorStatusMsg("更新失败"));
		}
		
	}
}
