<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="funcs" uri="funcs" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<form id="pagerForm" method="post" action="game/list">
    <input type="hidden" name="pageNum" value="${params.pageNo}" />
    <input type="hidden" name="numPerPage" value="${params.pageSize}" />
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" action="game/list" method="post" class="pageForm required-validate">
      <div class="searchBar">
      <table class="searchContent">
        <tr>
            <td>
            	游戏ID：<input type="text" name="gameId" value="${gameId}"/> 
            </td>
        </tr>
      </table>
      <div class="subBar">
          <ul>
              <li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
          </ul>
      </div>
    </div>
    </form>
</div>

<div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <li><a class="add" href="game/goadd" rel="lookup2organization" target="dialog" width="500" height="450" mask="true" close="closedialog"><span>添加</span></a></li>
                <li><a class="delete" href="game/delete?id={sid}" target="ajaxTodo" title="确定删除该条渠道信息吗?"><span>删除</span></a></li>
                <li><a class="edit" href="game/detail?id={sid}" rel="lookup2organization" target="dialog"  width="500" height="370" mask="true" close="closedialog"><span>修改</span></a></li>         
            </ul>
        </div>
        
        <table class="table" width="100%" layoutH="138">
            <thead>
            <tr>
                <th width="120">游戏ID</th>
                <th width="120">游戏名称</th>
                <th width="120">游戏秘钥</th>
            </tr>
        </thead>
        <tbody>
        
        <c:forEach items="${requestScope.result.data}" var="game" varStatus="var">
            <tr target="sid" rel="${game.id}">
                <td>${game.gameId}</td>
                <td>${game.gameName}</td>
                <td>${game.appSecret}</td>
            </tr>
        </c:forEach>
        </tbody>
        </table>
        <div class="panelBar">
          <div class="pages">
            <span>共${requestScope.result.total}条</span>
          </div>
        <div class="pagination" targetType="navTab" totalCount="${requestScope.result.total}" numPerPage="${requestScope.result.pageSize}" currentPage="${requestScope.result.nowPage}"></div>
    </div>
</div>

