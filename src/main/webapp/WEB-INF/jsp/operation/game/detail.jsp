<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="funcs" uri="funcs" %>

<div class="pageContent">
    <form method="post" action="game/update" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
        <div class="pageFormContent" layoutH="56">
            <p> 
                <label>游戏ID：</label>
                <input name="gameId" type="text" size="30" value="${game.gameId}"  class="required"/>
            </p>
            <p>
                <label>游戏名称：</label>
                <input name="gameName" type="text" size="30" value="${game.gameName}" class="required" />
            </p>
            <p style="width: 450px;">
                <label>游戏秘钥：</label>
                <input name="appSecret" type="text" size="50" value="${game.appSecret}" class="required" readonly/>
            </p>
            
        <input name="id" type="hidden" value="${game.id}"/>
        </div>
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
