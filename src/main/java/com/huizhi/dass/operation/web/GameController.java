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
import com.huizhi.dass.model.operation.Game;
import com.huizhi.dass.operation.service.GameService;

@Controller
@RequestMapping("/manager/game")
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpSession session) {
		Integer nowPage = RequestUtil.getInteger(request, "pageNum");
		Integer pageSize = RequestUtil.getInteger(request, "numPerPage");
		nowPage = nowPage == null ? 1 : nowPage;
		pageSize = pageSize == null ? Constants.LIST_PAGESIZE : pageSize;
		
		Integer gameId = RequestUtil.getInteger(request, "gameId");
		request.setAttribute("gameId", gameId);
		
		Criteria criteria = new Criteria();
		criteria.put("gameId", gameId);
		
		nowPage = (nowPage == 0) ? 1 : nowPage;
		pageSize = (pageSize == 0) ? Constants.LIST_PAGESIZE : pageSize;
		int mysqlOffset = (nowPage - 1) * pageSize;
		criteria.setOffset(mysqlOffset);
		criteria.setRows(pageSize);
		
		PageInfo<Game> pager = gameService.queryByCriteria(criteria);
		request.setAttribute("result", pager);
		pager.setNowPage(nowPage);
		pager.setPageSize(pageSize);
		
		return "/operation/game/list";
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
        
        Game game = gameService.queryById(id);
        request.setAttribute("game", game);
        
        return "/operation/game/detail";
    }

    /**
     * 删除
     * @param request
     * @return
     * @throws AdminException
     */
    @RequestMapping("/delete")
    public ModelAndView delete(HttpServletRequest request) throws AdminException {
    	Long id = RequestUtil.getLong(request, "id");
    	if(id == null) {
    		throw new AdminException("请选择要修改的数据");
    	}
    	
    	boolean isDel = gameService.remove(id);
    	
    	if (isDel) {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getOkStatusMsg("删除成功"));
		} else {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getErrorStatusMsg("删除失败"));
		}
    	
    }

    /**
     * 新增页面
     * @param request
     * @return
     * @throws AdminException
     */
    @RequestMapping("/goadd")
    public String goAdd(HttpServletRequest request) throws AdminException {
    	
    	return "/operation/game/add";
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
		Integer gameId = RequestUtil.getInteger(request, "gameId");
		String gameName = RequestUtil.getString(request, "gameName");
		
        Game game = new Game();
        game.setGameId(gameId);
        game.setGameName(gameName);
        game.setId(id);
        
		boolean isUpdate = gameService.edit(game);
		if (isUpdate) {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getOkStatusMsg("更新成功"));
		} else {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getErrorStatusMsg("更新失败"));
		}
		
	}

	/**
	 * 新增数据
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request, HttpSession session) {
		Integer gameId = RequestUtil.getInteger(request, "gameId");
		String gameName = RequestUtil.getString(request, "gameName");
		
		Game game = new Game();
		game.setGameId(gameId);
		game.setGameName(gameName);
		
		boolean isAdd = gameService.add(game);
		if (isAdd) {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getOkStatusMsg("添加成功"));
		} else {
			return new ModelAndView(Constants.JSON_VIEW, Constants.JSON_ROOT, DwzJsonUtil.getErrorStatusMsg("添加失败"));
		}
		
	}

}
