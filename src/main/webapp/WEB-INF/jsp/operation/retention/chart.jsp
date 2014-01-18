<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="funcs" uri="funcs" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<form id="pagerForm" method="post" action="retention/chartPage">
    <input type="hidden" name="gameId" value="${param.gameId}" /> 
    <input type="hidden" name="startTime" value="${param.startTime}" />
    <input type="hidden" name="endTime" value="${param.endTime}" />
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" action="retention/chartPage" method="post" class="pageForm required-validate">
      <div class="searchBar">
      <table class="searchContent">
        <tr>
        	<td>游戏ID:</td>
			<td>
			  <select class="combox" id="queryGameId" name="gameId">
			  	<c:forEach items="${requestScope.games}" var="game">
			  		<option value="${game.gameId}" ${param.gameId == game.gameId ? "selected" : "" }>${game.gameName}</option>
			  	</c:forEach>
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

	<div id="canvasDiv" width="900" height="700"></div>

    </div>
    <script src="<%= basePath%>static/manager/js/ichart.1.2.js" type="text/javascript"></script>
	<script type="text/javascript">

		
		$(function(){
			
			$.ajax({
				type: "post",
				dataType: "json",
				url: "<%= basePath%>manager/retention/chart",
				data: {
					gameId: $('#queryGameId').val(),
					startTime: $('#queryBeginTime').val(),
					endTime: $('#queryEndTime').val()
				},
				success: function(data){
					var tmrRtn = data.tmrRetentions;	
					var day7Rtn = data.day7Retentions;
					var day30Rtn = data.day30Retentions;
					var dateLabels = data.dates;
					var startDate = data.startDate;
					var endDate = data.endDate;
					
					var gameName = $('#queryGameId').find("option:selected").text();
					
					showChart(tmrRtn, day7Rtn, day30Rtn, dateLabels, startDate, endDate, gameName);
				}
				
			});
			
			
		});
			

			
		
		var showChart = function(tmrRtn, day7Rtn, day30Rtn, dates, startDate, endDate, gameName) {

			
			var data = [
			         	{
			         		name : '次日留存',
			         		value: tmrRtn,
			         		color:'#0d8ecf',
			         		line_width:2
			         	},
			         	{
			         		name : '7日留存',
			         		value: day7Rtn,
			         		color:'yellow',
			         		line_width:2
			         	},
			         	{
			         		name : '30日留存',
			         		value: day30Rtn,
			         		color:'#ef7707',
			         		line_width:2
			         	}
			         ];
	        
			var labels = dates;
			
			var chart = new iChart.LineBasic2D({
				render : 'canvasDiv',
				data: data,
				align:'center',
				title : {
					text: '《' + gameName + '》玩家留存',
					font : '微软雅黑',
					fontsize:24,
					color:'#2e2e2e'
				},
				subtitle : {
					text:'日期：' + startDate + " 至 " + endDate,
					font : '微软雅黑',
					color:'#2e2e2e'
				},
				footnote : {
					text:'汇智手游平台',
					font : '微软雅黑',
					fontsize:11,
					fontweight:600,
					padding:'0 28',
					color:'#2e2e2e'
				},
				width : 1550,
				height : 700,
				shadow:true,
				shadow_color : '#202020',
				shadow_blur : 2,
				shadow_offsetx : 0,
				shadow_offsety : 0,
				background_color:'white',
				legend : {
					enable : true,
					row:1,//设置在一行上显示，与column配合使用
					column : 'max',
					valign:'top',
					sign:'bar',
					background_color:null,//设置透明背景
					offsetx:-80,//设置x轴偏移，满足位置需要
					border : true
				},
				tip:{
					enable:true,
					shadow:true,
					listeners:{
						 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
						parseText:function(tip,name,value,text,i){
							return "<span style='color:#005268;font-size:12px;'>"+labels[i]+" 留存率 约:<br/>"+
							"</span><span style='color:#005268;font-size:20px;'>"+value+"%</span>";
						}
					}
				},
				crosshair:{
					enable:true,
					line_color:'#62bce9'
				},
				sub_option : {
					smooth : true,
					label:false,
					hollow:false,
					hollow_inside:false,
					point_size:8
				},
				coordinate:{
					width:1400,
					height:500,
					striped_factor : 0.18,
					grid_color:'#b2b2b2',
					axis:{
						color:'#9f9f9f',
						width:[0,0,4,4]
					},
					scale:[{
						 position:'left',	
						 start_scale:0,
						 end_scale:100,
						 scale_space:10,
						 scale_size:2,
						 scale_enable : false,
						 label : {color:'#9d987a',font : '微软雅黑',fontsize:11,fontweight:600},
						 scale_color:'#9f9f9f'
					},{
						 position:'bottom',	
						 label : {color:'#9d987a',font : '微软雅黑',fontsize:11,fontweight:600},
						 scale_enable : false,
						 labels:labels
					}]
				}
			});
			//利用自定义组件构造左侧说明文本
			chart.plugin(new iChart.Custom({
					drawFn:function(){
						//计算位置
						var coo = chart.getCoordinate(),
							x = coo.get('originx'),
							y = coo.get('originy'),
							w = coo.width,
							h = coo.height;
						//在左上侧的位置，渲染一个单位的文字
						chart.target.textAlign('start')
						.textBaseline('bottom')
						.textFont('600 11px 微软雅黑')
						.fillText('留存率(%)',x-40,y-12,false,'#9d987a')
						.textBaseline('top')
						.fillText('(日期)',x+w+22,y+h+10,false,'#9d987a');
						
					}
				}));
				//开始画图
				chart.draw();

		};
	</script>
</div>


