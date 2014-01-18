<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="funcs" uri="funcs" %>

<div class="pageContent">
    <form method="post" action="rechargeInfo/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
        <div class="pageFormContent" layoutH="56">
            <p> 
                <label>时间：</label>
                <input name="date" id="date" type="text" size="30" value="${funcs:formatDateTime(recharge.date, 'yyyy-MM-dd')}"  class="required" readonly/>
            </p>
            <p> 
                <label>游戏ID：</label>
                <input name="gameId" id="gameId" type="text" size="30" value="${recharge.gameId}" class="required" readonly/>
            </p><%--
            <p> 
                <label>游戏名称：</label>
                <input name="gameName" id="gameName" type="text" size="30" value="${recharge.gameName}" class="required"  readonly/>
            </p>
            --%><p>
                <label>金额(元)：</label>
                <input name="money" id="money" type="text" size="30" value="${recharge.money}" class="required" />
            </p>
        </div>
        <input name="id" type="hidden" value="${recharge.id}"/>
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
