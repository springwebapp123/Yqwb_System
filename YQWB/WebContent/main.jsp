<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/icon.css">
		<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
		<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">
			$(function(){
				var treeData=[{
					text:"系统管理",
					children:[
					{
						text:"单次消费登记",
						attributes:{
							url:"mail.jsp"
						}
					},{
						text:"会员管理登记",
						attributes:{
							url:"EmployeeManage.jsp"
						}
					},{
						text:"进货入库登记",
						attributes:{
							url:"DepartmentManage.jsp"
						}
					}]
				}];
				//实例化树型
				$("#tree").tree({
					data:treeData,
					lines:true,
					onClick:function(node){
						if(node.attributes){
							openTab(node.text,node.attributes.url);
						}
					}
				});
				//新增tab
				function openTab(text,url){
					if($("#tabs").tabs('exists',text)){
						$("#tabs").tabs('select',text);
					}else{
						var content="<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%;' src="+url+"></iframe>";
						$("#tabs").tabs('add',{
							title:text,
							closable:true,
							content:content			
						});				
					}
				}
			});
			
			//退出
			function quitSys(){
				$.post("employee!quitSys",{},function(result){
					window.location.href = "index.jsp";
				},"json");
			};
			//充值
			function openxgmm(){
				  $('#xgmm').dialog('open').dialog("setTitle","修改密码");
			};
			function updatemm(){
				var jmm = $("#jmm").val();
				var xmm = $("#xmm").val();
				var xmmtwo = $("#xmmtwo").val();
				var uri = "jmm="+jmm+"&xmm="+xmm+"&xmmtwo="+xmmtwo;
				    $.ajax({
						url:"employee!updatemm?"+uri,
						type:"post",
	                    async:true,
						success:function(result){
							if(result == "-1"){
								$.messager.alert("系统提示","密码修改失败!","error");
							}else if(result == "0"){
								$.messager.alert("系统提示","密码修改成功!");
								resetValuemm();
								xgmmClose();
								quitSys();
								$("#xgmm").dialog("close");
							}else if(result == "1"){
								$.messager.alert("系统提示","旧密码输入不正确!","error");
							}else if(result == "2"){
								$.messager.alert("系统提示","2次新密码输入不正确!","error");
							}
						}
					});
			};
			function xgmmClose(){
				$('#xgmm').dialog("close");
				resetValuemm();
			};
			function resetValuemm(){				
			    $('#jmm').val("");
				$('#xmm').val("");
				$('#xmmtwo').val("");
			};
		</script>
<title>一祺玩吧管理系统</title>
<%
	//权限验证
	if(session.getAttribute("currentUser")==null){
		response.sendRedirect("index.jsp");
		return;
	}
%>
</head>
<body class="easyui-layout">
        <div data-options="region:'north'" style="height:50px;background-color:#F9F7F8;background:url(images/banner.jpg) repeat;background-size:100%;" >
        	<div style="margin-top:15px;margin-right:20px;float:right;">
	        	<img   alt="" src="images/log.jpg" style="height:30px;width:30px;border-radius:50px;vertical-align:middle;"  >
	        	当前登录用户：&nbsp;<font color="red">${currentUser.nameval }</font>&nbsp;&nbsp;
	        	<a   href="javascript:quitSys();">退出</a>&nbsp;&nbsp;
	        	<a   href="javascript:openxgmm();">修改密码</a>
        	</div>
        </div>
        <div data-options="region:'south'" style="height:30px;padding:5px;" align="center"  >版权所有&nbsp;&nbsp;一祺玩吧</div>
        <div data-options="region:'west',split:true" title="导航菜单" style="width:140px;">
        	<ul id="tree" class="easyui-tree" ></ul>
        </div>
        <div data-options="region:'center'">
            <div class="easyui-tabs" fit="true" border="false" id="tabs" >
                <div title="会员管理" style="padding:10px">
				    <iframe frameborder='0'  scrolling='auto' style='width:100%;height:100%;' src="mail.jsp"></iframe>
				</div>
			</div>	
        </div>
        <!-- 充值start -->
	<div id="xgmm" class="easyui-dialog" style="width:350px;height:200px;padding:20px 10px 0;" closed="true" buttons="#xgmm-buttons">
			<table style="width:95%;margin:auto;">
				<tr>
					<td>&nbsp;旧密码:&nbsp;</td>
					<td> <input type="password" name="jmm" id="jmm"   style="width:120px;" class="easyui-validatebox" required="true" ></td>
				</tr>		
				<tr>
					<td >&nbsp;新密码:&nbsp;</td>
					<td><input type="password"  name="xmm"  id="xmm"   style="width:120px;" class="easyui-validatebox" required="true" ></td>
				</tr>	
			    <tr>
			    	<td>&nbsp;再次输入新密码:&nbsp;</td>
					<td><input type="password"  name="xmmtwo"  id="xmmtwo"  style="width:120px;" class="easyui-validatebox" required="true" ></td>
				</tr>	
			</table>			
	</div>
	<div id="xgmm-buttons">
		<a href="javascript:updatemm()" class="easyui-linkbutton" iconCls="icon-ok" >确认修改</a>
		<a href="javascript:xgmmClose()" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	</div>
	<!-- 充值stop -->
</body>
</html>