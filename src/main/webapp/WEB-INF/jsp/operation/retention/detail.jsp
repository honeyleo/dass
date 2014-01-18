<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="funcs" uri="funcs" %>

<div class="pageContent">
    <form method="post" action="retention/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
        <div class="pageFormContent" layoutH="56">
            <p> 
                <label>时间：</label>
                <input name="date" id="date" type="text" size="30" value="${funcs:formatDateTime(retention.date, 'yyyy-MM-dd')}"  class="required" readonly/>
            </p>
            <p> 
                <label>游戏ID：</label>
                <input name="gameId" id="gameId" type="text" size="30" value="${retention.gameId}" class="required" readonly/>
            </p><%--
            <p> 
                <label>游戏名称：</label>
                <input name="gameName" id="gameName" type="text" size="30" value="${retention.gameName}" class="required"  readonly/>
            </p>
            --%><p>
                <label>留存率%：</label>
                <input name="retention" id="retention" type="text" size="30" value="${retention.retention}" class="required" />%
            </p>
            <p>
                <label>类型：</label>
                <input name="type" id="type" type="text" size="30" value="<c:choose><c:when test="${retention.type == 1}">次日留存</c:when><c:when test="${retention.type == 2}">7日留存</c:when><c:when test="${retention.type == 3}">30日留存</c:when></c:choose>"  class="required" readonly/>
            </p>            
            <p> 
                <label>状态：</label>
                <input name="audit" id="audit" type="text" size="30" value="<c:choose><c:when test="${retention.audit == 0}">未审核</c:when><c:when test="${retention.audit == 1}">已审核</c:when></c:choose>"  class="required" readonly/>
            </p>
        </div>
        <input name="id" type="hidden" value="${retention.id}"/>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                <li>
                    <div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
                </li>
            </ul>
        </div>
    </form>
</div>
<script type="text/javascript">


</script>
