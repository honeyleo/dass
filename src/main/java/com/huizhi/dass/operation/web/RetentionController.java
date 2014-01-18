package com.huizhi.dass.operation.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.exception.AdminException;
import com.huizhi.dass.common.util.DwzJsonUtil;
import com.huizhi.dass.common.util.JsonUtils;
import com.huizhi.dass.common.util.RequestUtil;
import com.huizhi.dass.common.web.Funcs;
import com.huizhi.dass.model.Admin;
import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.PageInfo;
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.model.operation.RetentionChartInfo;
import com.huizhi.dass.model.operation.RetentionInfo;
import com.huizhi.dass.operation.service.GameService;
import com.huizhi.dass.operation.service.RetentionInfoService;

@Controller
@RequestMapping("/manager/retention")
public class RetentionController {
    
	@Autowired
	private RetentionInfoService riService;
	
	@Autowired
	private GameService gameService;
	
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

		Integer type = RequestUtil.getInteger(request, "type");
		request.setAttribute("type", type);

		Integer gameId = RequestUtil.getInteger(request, "gameId");
		request.setAttribute("gameId", gameId);
		
		Admin user = Funcs.getSessionLoginUser(session);
		
		Criteria criteria = new Criteria();
		criteria.put("type", type);
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
		
		PageInfo<RetentionInfo> pager = riService.queryRetentionInfoByDateAndGame(criteria);
		request.setAttribute("result", pager);
		pager.setNowPage(nowPage);
		pager.setPageSize(pageSize);
		
		return "/operation/retention/list";
	}
	
	@RequestMapping("/audit")
	public ModelAndView updateStatus(HttpServletRequest request, HttpSession session) {
		Long id = RequestUtil.getLong(request, "id");
		Integer audit = RequestUtil.getInteger(request, "audit");
		
		RetentionInfo info = new RetentionInfo();
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
     * 留存率详情
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
        
        RetentionInfo info = riService.queryById(id);
        request.setAttribute("retention", info);
        
        return "/operation/retention/detail";
    }
	
	
	/**
	 * 修改留存率数据
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/update")
	public ModelAndView update(HttpServletRequest request, HttpSession session) {
		Long id = RequestUtil.getLong(request, "id");
        Double retention = RequestUtil.getDouble(request, "retention");
		
        RetentionInfo info = new RetentionInfo();
        info.setId(id);
        info.setRetention(retention);
        
		boolean isUpdate = riService.updateById(info);
		if (isUpdate) {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getOkStatusMsg("更新成功"));
		} else {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getErrorStatusMsg("更新失败"));
		}
		
	}
	
	@RequestMapping(value = "/chartPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String chartPage(HttpServletRequest request, HttpSession session) {
		Admin user = Funcs.getSessionLoginUser(session);
		List<Game> games = gameService.queryAllGame();
		if (user.getRoleId() > 2) {
			List<Game> copyGames = new ArrayList<Game>();
			if (games != null && games.size() > 0) {
				copyGames.add(games.get(0));
			}
			request.setAttribute("games", copyGames);
		} else {
			request.setAttribute("games", games);
		}
		return "/operation/retention/chart";
	}
	
	@RequestMapping(value = "/chart", method = {RequestMethod.GET, RequestMethod.POST})
	public Object chart(HttpServletRequest request, HttpSession session) {
		Criteria criteria = new Criteria();
		
		Admin user = Funcs.getSessionLoginUser(session);
		if (user.getRoleId() > 2) {
			criteria.put("audit", 1);
			criteria.put("gameId", user.getGameId());
		} else {
			List<Game> games = gameService.queryAllGame();
			
			Integer gameId = RequestUtil.getInteger(request, "gameId");
			if (gameId == null) {
				if (games != null && games.size() > 0) {
					gameId = games.get(0).getGameId();
				}
			}
			criteria.put("gameId", gameId);
			request.setAttribute("gameId", gameId);
		}
		
		Date startTime = RequestUtil.getDate(request, "startTime", "yyyy-MM-dd");
		Date endTime = RequestUtil.getDate(request, "endTime", "yyyy-MM-dd");
		if (startTime != null) {
			request.setAttribute("startTime", startTime);
		}
		if (endTime != null) {
			request.setAttribute("endTime", endTime);
		}
		criteria.put("startTime", startTime);
		criteria.put("endTime", endTime);
		
		RetentionChartInfo rci = riService.queryRetentionChartInfo(criteria);
		System.out.println(JsonUtils.beanToJson(rci));
		
		return new ResponseEntity<String>(JsonUtils.beanToJson(rci), HttpStatus.OK);
	}
    
}
