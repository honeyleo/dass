<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="funcs" uri="funcs" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<form id="pagerForm" method="post" action="retention/list">
    <input type="hidden" name="pageNum" value="${requestScope.result.nowPage}" />
    <input type="hidden" name="numPerPage" value="${requestScope.reslut.pageSize}" />
    <input type="hidden" name="gameId" value="${param.gameId}" />
    <input type="hidden" name="audit" value="${param.audit}" />
    <input type="hidden" name="type" value="${param.type}" />
    <input type="hidden" name="startTime" value="${param.startTime}" />
    <input type="hidden" name="endTime" value="${param.endTime}" />
    
    
    
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" action="retention/list" method="post" class="pageForm required-validate">
      <div class="searchBar">
      <table class="searchContent">
        <tr>
        	<td>游戏ID:</td>
			<td>
			  <input type="text" id="queryGameId" name="gameId" value="${param.gameId }"/>
			</td>
			<td>是否已审核:</td>
			<td>
			  <select class="combox" id="queryAudit" name="audit">
			    <option value="-1">无限制</option>
			      <option value="0" ${param.audit == 0 ? "selected" : "" }>未审核</option>
			      <option value="1" ${param.audit == 1 ? "selected" : "" }>已审核</option>
			  </select>
			</td>
			<td>留存率类型:</td>
			<td>
			  <select class="combox" id="queryType" name="type">
			    <option value="-1">无限制</option>
			      <option value="1" ${param.type == 1 ? "selected" : "" }>次日留存</option>
			      <option value="2" ${param.type == 2 ? "selected" : "" }>7日留存</option>
			      <option value="3" ${param.type == 3 ? "selected" : "" }>30日留存</option>
			  </select>
			</td>
			<td>创建时间:</td>
			<td>
			  <input type="text" id="queryBeginTime" name="startTime" value="${param.startTime }" class="date" readonly="true"/>
			</td>
			<td>--</td>
			<td>
			  <input type="text" id="queryEndTime" name="endTime" value="${param.endTime }" class="date" readonly="true"/>
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
            <c:if test="${sessionScope.SESSION_USER.user.roleId <= 2}">
            	<ul class="toolBar">
            	    <li><a class="add" href="retention/audit?id={sid}&audit=1" target="ajaxTodo" title="确定把该条信息设为“已审核”吗?"><span>已审核</span></a></li>
        	        <li><a class="delete" href="retention/audit?id={sid}&audit=0" target="ajaxTodo" title="确定把该条信息设为“未审核”吗?"><span>取消审核</span></a></li>
    	           	<li><a class="edit" href="retention/detail?id={sid}" target="dialog" width="500" height="450" mask="true" close="closedialog"><span>编辑</span></a></li>
	            </ul>
            </c:if>
        </div>

        <table class="table" width="100%" layoutH="138">
            <thead>
            <tr>
                <th width="100">时间</th>
                <th width="50">游戏ID</th>
                <th width="50">游戏名称</th>
                <th width="50">留存率%</th>
                <th width="50">类型</th>
                <c:if test="${sessionScope.SESSION_USER.user.roleId <= 2}">
	                <th width="100">状态</th>
                </c:if>
            </tr>
        </thead>
        <tbody>
        
        <c:forEach items="${requestScope.result.data}" var="rtn" varStatus="var">
            	<tr target="sid" rel="${rtn.id}">
	                <td>${funcs:formatDateTime(rtn.date, 'yyyy-MM-dd')}</td>
	                <td>${rtn.gameId}</td>
	                <td>${rtn.gameName}</td>
	                <td>${rtn.retention}</td>
	                <td><c:choose><c:when test="${rtn.type == 1}">次日留存</c:when><c:when test="${rtn.type == 2}">7日留存</c:when><c:when test="${rtn.type == 3}">30日留存</c:when></c:choose>
	                <c:if test="${sessionScope.SESSION_USER.user.roleId <= 2}">
		                <td>${rtn.audit == 1 ? "已审核" : "未审核"}</td>
	                </c:if>
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
		<script type="text/javascript">
		</script>
</div>


