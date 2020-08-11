<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VIP卡管理</title>
		<%
			//权限验证
			if(session.getAttribute("currentUser")==null){
				response.sendRedirect("index.jsp");
				return;
			}
		%>
 		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/icon.css">
		<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
		<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
		<style type="text/css">
			input{
				width:80px;
			}
		</style>
		<script type="text/javascript">
			var url;
			function searchEmployee(){
				$('#dg').datagrid('load',{
					s_employeeNo:$('#s_employeeNo').val(),
					s_name:$('#s_name').val(),
					s_departmentId:$('#s_departmentId').combobox("getValue"),
					s_bksj:$('#s_bksj').datebox("getValue")
				});				
			};
			function deleteEmployee(){
				var selectedRows=$("#dg").datagrid('getSelections');
				if(selectedRows.length==0){
					$.messager.alert("系统提示","请选择要删除的数据！");
					return;
				}
				var strIds=[];
				for(var i=0;i<selectedRows.length;i++){
					strIds.push(selectedRows[i].employeeId);
				}
				var ids=strIds.join(",");
				$.messager.confirm("系统提示","您确认要删掉这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
					if(r){
						$.post("employee!delete",{delIds:ids},function(result){
							if(result.success){
								$.messager.alert("系统提示","您已成功删除<font color=red>"+result.delNum+"</font>条数据!");
								$("#dg").datagrid("reload");
							}else{
								$.messager.alert("系统提示",'<font color=red>'+selectedRows[result.errorIndex].name+'</font>'+result.errorMsg);
							}
						},"json");
					}
				});
			};
			function openEmployeeAddDialog(){
				$('#dlg').dialog('open').dialog("setTitle","添加小朋友信息");
				 resetValue();
				url=("employee!save");
			};
			//充值
			function opencz(employeeId,departmentId){
				showmask();
				if(departmentId=="1"){//次卡
					$("#cz_employeeId").val(employeeId);
					$("#cz_departmentId").val(departmentId);
					$("#ts").hide();
					$("#cs").show();
					$('#cz').dialog('open').dialog("setTitle","充值");
					//resetValue();
				}else if(departmentId=="2"){//月卡
					$("#cz_employeeId").val(employeeId);
					$("#cz_departmentId").val(departmentId);
					$("#ts").show();
					$("#cs").hide();
					$('#cz').dialog('open').dialog("setTitle","充值");
					//resetValue();
				}
			};
			//充值记录
			function openczjl(employeeId,departmentId){
				var uri = "cz_employeeId="+employeeId;
			    $.ajax({
					url:"employee!searchczjl?"+uri,
					type:"post",
					dataType:"json",
                    async:true,
					success:function(result){
						var len = (result.length);
						var html = "";
						var czjltab  = $("#czjltab");
						if(len>0){
							var isone = result[0].isone;//0:次卡
							if(isone=="0"){
								html+="<tr><td>充值金额</td><td>充值日期</td><td>可消费次数</td></tr>";
							}else{
								html+="<tr><td>充值金额</td><td>充值日期</td><td>可消费天数</td><td>消费截止日期</td></tr>";
							}
							for(var i=0;i<len;i++){
								var isoneX = result[i].isone;//0:次卡
								if(isoneX=="0"){
									html+="<tr><td>"+result[i].czje+"元</td><td>"+result[i].czrq+"</td><td>"+result[i].cs+"次</td></tr>";
								}else{//月卡
									html+="<tr><td>"+result[i].czje+"元</td><td>"+result[i].czrq+"</td><td>"+result[i].ts+"天</td><td style=\"color:red;\">"+result[i].jsrq+"</td></tr>";
								}
							}
						}else{
							html+="<tr><td colspan='3'>无充值记录</td></tr>";
						}
						czjltab.html(html);
						$('#czjl').dialog('open').dialog("setTitle","充值记录详情");
					}
				});
			};
			//消费记录
			function openxfjl(employeeId,departmentId){
				var uri = "cz_employeeId="+employeeId;
			    $.ajax({
					url:"employee!searchxfjl?"+uri,
					type:"post",
					dataType:"json",
                    async:true,
					success:function(result){
						var len = (result.length);
						var html = "";
						var xfjltab  = $("#xfjltab");
						if(len>0){
								html+="<tr><td>消费时间</td></tr>";
							for(var i=0;i<len;i++){
								html+="<tr><td>"+result[i].xfrq+"</td></tr>";
							}
						}else{
							html+="<tr><td>无消费记录</td></tr>";
						}
						xfjltab.html(html);
						$('#xfjl').dialog('open').dialog("setTitle","消费记录详情");
					}
				});
			};
			//点击消费
			function djxf(employeeId,departmentId){
				$.messager.alert('系统提示','确认消费?','question',function(){
					showmask();
					var uri = "cz_employeeId="+employeeId;
				    $.ajax({
						url:"employee!djxf?"+uri,
						type:"post",
						dataType:"json",
	                    async:false,
						success:function(result){
							if(result.success == "0"){
								 $.messager.alert("系统提示","系统已为您完成1次消费登记!");
								 $("#dg").datagrid("reload");
							}else{
								$.messager.alert("系统提示","系统发生异常故障,请暂停使用!","error");
							}
							hidemask();
						}
					});
				});
			};
			function openEmployeeModifyDialog(){
				var selectedRows=$("#dg").datagrid('getSelections');
				if(selectedRows.length!=1){
					$.messager.alert("系统提示","请选择一条要编辑的数据！");
					return;
				}
				var row=selectedRows[0];
				$("#edit_dlg").dialog("open").dialog("setTitle","编辑小朋友信息");
			    $("#edit_employeeId").val(row.employeeId);
				$("#edit_name").val(row.name);
				$("#edit_sex").combobox("setValue",row.sex);
				$('#edit_birthday').datebox("setValue",row.birthday);
				$("#edit_education").val(row.education);
				$("#edit_profession").val(row.profession);
				$("#edit_position").val(row.address);
			};
			function updateMessage(){
				var edit_employeeId = $("#edit_employeeId").val();
				var edit_name = $("#edit_name").val();
				var edit_sex= $("#edit_sex").combobox("getValue");
				var edit_birthday= $('#edit_birthday').datebox("getValue");
				var edit_education = $("#edit_education").val();
				var edit_profession= $("#edit_profession").val();
				var edit_position= $("#edit_position").val();
				if(edit_employeeId==null || edit_employeeId ==undefined ||edit_employeeId == ""){
					$.messager.alert("系统提示","系统可能发生故障,请不要继续使用!!","error");
					return;
				}
				if(edit_name==null || edit_name ==undefined ||edit_name == ""){
					$.messager.alert("系统提示","请输入小朋友姓名!","error");
					return;
				}
				if(edit_birthday==null || edit_birthday == undefined || edit_birthday==""){
					$.messager.alert("系统提示","请输入出生日期!","error");
					return;
				}
				if(edit_education==null || edit_education ==undefined ||edit_education == ""){
					$.messager.alert("系统提示","请输入家长姓名!","error");
					return;
				}
				if(edit_profession==null || edit_profession == undefined || edit_profession==""){
					$.messager.alert("系统提示","请输入家长联系方式!","error");
					return;
				}
				if(edit_position==null || edit_position == undefined || edit_position==""){
					$.messager.alert("系统提示","请输入家庭住址!","error");
					return;
				}
				var uri = "edit_employeeId="+edit_employeeId+"&edit_name="+edit_name+"&edit_sex="+edit_sex+"&edit_birthday="+edit_birthday+"&edit_education="+edit_education+"&edit_profession="+edit_profession+"&edit_position="+edit_position;
				    $.ajax({
						url:"employee!updateMessage?"+uri,
						type:"post",
	                    async:true,
						success:function(result){
							if(result == "n"){
								$.messager.alert("系统提示","更新失败!","error");
								return;
							}else{
								$.messager.alert("系统提示","编辑成功");
								resetValueEdit();
								$("#edit_dlg").dialog("close");
								$("#dg").datagrid("reload");
							}
							hidemask();
						}
					});
			};
			
			
			function closeEmployeeEditDialog(){
				$('#edit_dlg').dialog("close");
				resetValueEdit();
			};
			
			function closeEmployeeDialog(){
				$('#dlg').dialog("close");
				resetValue();
			};
			function czClose(){
				$('#cz').dialog("close");
				resetValuecz();
				hidemask();
			};
			function resetValueEdit(){
				$('#edit_name').val("");
				$('#edit_bbirthday').val("");
				$('#edit_nationality').val("");
				$('#edit_education').val("");
				$('#edit_profession').val("");
				$('#edit_position').val("");
			};
			function resetValuecz(){				
			    $('#cz_czje').val("");
				$('#cz_employeeId').val("");
				$('#cz_departmentId').val("");
				$('#cz_ts').val("");
				$('#cz_cs').val("");
			};
			function resetValue(){
				$('#employeeNo').val("");
				$('#name').val("");
				$('#bbirthday').val("");
				$('#departmentId').combobox("setValue","");
				$('#sex').combobox("setValue","男");
				$('#nationality').val("");
				$('#education').val("");
				$('#profession').val("");
				$('#position').val("");
			};
			
			function saveEmployee(){
				var departmentId = $("#departmentId").combobox("getValue");
				if(departmentId==null || departmentId ==undefined || departmentId ==""){
					 $.messager.alert("操作提示", "请选择办卡方式!","warning");
					 return false;
				}
				$('#fm').form("submit",{
					url:url,
					onSubmit:function(){
						return $(this).form("validate");
					},
					dataType:"json",
					success:function(result){
						if(result == "a"){
							$.messager.alert("系统提示","保存成功");
							resetValue();
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}else if(result == "b"){
							$.messager.alert("系统提示","系统异常,请停止使用!");
							return;
						}else if(result == "c"){
							$.messager.alert("系统提示","请选择办卡方式!");
							return;
						}else{
							$.messager.alert("系统提示","此<span style='color:red;'>"+result+"</span>VIP会员卡系统已有登记,请注意是否填写错误!");
							return;
						}
					}
				});
			};
			function saveCz(){
				var cz_czje = $("#cz_czje").val();
				var cz_employeeId = $("#cz_employeeId").val();
				var cz_ts = $("#cz_ts").val();
				var cz_cs = $("#cz_cs").val();
				var cz_departmentId = $("#cz_departmentId").val();
				if(cz_czje==null || cz_czje ==undefined ||cz_czje == ""){
					$.messager.alert("系统提示","请输入充值金额!","error");
					return;
				}
				if(cz_departmentId!=null && cz_departmentId=="1"){//次卡
					if(cz_cs==null || cz_cs == undefined || cz_cs==""){
						$.messager.alert("系统提示","请输入可消费次数!","error");
						return;
					}
				}else if(cz_departmentId!=null && cz_departmentId=="2"){//月卡
					if(cz_ts==null || cz_ts == undefined || cz_ts==""){
						$.messager.alert("系统提示","请输入可消费天数!","error");
						return;
					}
				}
				var uri = "cz_czje="+cz_czje+"&cz_employeeId="+cz_employeeId+"&cz_ts="+cz_ts+"&cz_cs="+cz_cs+"&cz_departmentId="+cz_departmentId;
				    $.ajax({
						url:"employee!czsave?"+uri,
						type:"post",
	                    async:true,
						success:function(result){
							if(result.errorMsg){
								$.messager.alert("系统提示",result.errorMsg);
								return;
							}else{
								$.messager.alert("系统提示","充值成功");
								resetValuecz();
								$("#cz").dialog("close");
								$("#dg").datagrid("reload");
							}
							hidemask();
						}
					});
			};
			function exportEmployee(){
				$('#search').form("submit",{
					url:"employee!ExportEmployee"
				})
			};
			
			/*
			 * seven.sun
			 * 使用方法：引入此js,
			 * 使用遮罩层时，调用方法showmask();
			 * 关闭遮罩层时，调用方法hidemask();
			 */
			function showmask(){
			    //遮罩层,利用datagrid的遮罩层
			    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
			    //$("<div class=\"datagrid-mask-msg\"></div>").html("").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
			 };
			function hidemask(){
			    $(".datagrid-mask").hide();
			    $(".datagrid-mask-msg").hide();
			};
		</script>
</head>
<body>
			<table id="dg"    class="easyui-datagrid"  style="height:100%"  pageList="[15,20,25,30]"  pageSize="15"     fitColumns="true"  rownumbers="true" fit="true" pagination="true"  url="employee" toolbar="#tb" >
					<thead>
						<tr>
							<th field="cb" checkbox="true" ></th>
							<th data-options="field:'employeeId'" width="1" hidden="true">ID</th>
							<th data-options="field:'mark'" width="21" >操作</th>
							<th data-options="field:'employeeNo'" width="10">VIP卡编号</th>
							<th data-options="field:'departmentName'" width="7">办卡方式</th>
							<th data-options="field:'bksj'" width="14">办卡时间</th>
							<th data-options="field:'imgs'" width="5">性别</th>
							<th data-options="field:'name'" width="8">小朋友姓名</th>
							<th data-options="field:'birthday'" width="10">出生日期</th>
							<th data-options="field:'education'" width="8">家长姓名</th>
							<th data-options="field:'profession'" width="10">家长联系方式</th>
							<th data-options="field:'sy'" width="6">剩余</th>
							<th data-options="field:'ljczje'" width="8">累计充值金额</th>
							<th data-options="field:'position'" width="10" >家庭住址</th>
						</tr>
					</thead>
			</table>
	 <div id="tb" >
		<div>
			<a href="javascript:openEmployeeAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">添加</a>
			<a href="javascript:openEmployeeModifyDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true">修改</a>
			<a href="javascript:deleteEmployee()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
			<a href="javascript:location.reload()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'" plain="true">刷新</a>
		</div>
		<div>
			<form id="search" method="post" >
				&nbsp;VIP卡编号:&nbsp;<input type="text" name="s_employeeNo" style="width:150px;" id="s_employeeNo" size="10" />
			    &nbsp;小朋友姓名:&nbsp;<input type="text" name="s_name" style="width:150px;"  id="s_name" size="10" />
				&nbsp;办卡方式:&nbsp;<input class="easyui-combobox" style="width:150px;"  id="s_departmentId" name="s_departmentId" size="8" panelHeight="auto" data-options="editable:false,valueField:'departmentId',textField:'departmentName',url:'department!departmentComboList'" />				
				&nbsp;办卡日期：&nbsp;<input type="text"  style="width:150px;" class="easyui-datebox"  name="s_bksj" id="s_bksj"  editable="false" required="true" />&nbsp;
				<a href="javascript:searchEmployee()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" plain="true">搜索</a>
			</form>
		</div>
		<br />
	</div>
	<!-- 添加start -->
	<div id="dlg" class="easyui-dialog" style="width:600px;height:250px;padding:20px 10px 0;" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" style="width:100%;">
			<table style="width:95%;margin:auto;">
				<tr>
					<td>&nbsp;VIP卡编号:&nbsp;</td>
					<td><input type="text" name="employee.employeeNo" id="employeeNo" style="width:150px;" class="easyui-validatebox" required="true" ></td>
					<td>&nbsp;小朋友姓名:&nbsp;</td>
					<td><input type="text" name="employee.name" id="name" style="width:150px;" class="easyui-validatebox" required="true" ></td>
				</tr>			
				<tr>
					<td>&nbsp;性别:&nbsp;</td>
					<td><select class="easyui-combobox" name="employee.sex" id="sex" style="width:150px;"  editable="false" panelHeight="auto" style="width:85px;">
								<option value="男" >男</option>
								<option value="女" >女</option>
						</select>
					</td>
					<td>&nbsp;出生日期:&nbsp;</td>
					<td><input type="text" class="easyui-datebox" name="employee.birthday" style="width:150px;" id="birthday" editable="false" required="true" /></td>
				</tr>
				<tr>
					<td>&nbsp;家长姓名:&nbsp;</td>
					<td><input type="text" name="employee.education" id="education" style="width:150px;"  class="easyui-validatebox" required="true" ></td>
					<td>&nbsp;家长联系方式:&nbsp;</td>
					<td><input type="text" name="employee.profession" id="profession" style="width:150px;"  class="easyui-validatebox" required="true" ></td>
				</tr>
				<tr>
				    <td>&nbsp;家庭住址:&nbsp;</td>
					<td><input type="text" name="employee.position" id="position" style="width:150px;"  class="easyui-validatebox" required="true" ></td>
					<td>&nbsp;办卡方式:&nbsp;</td>
					<td><input class="easyui-combobox" id="departmentId"  required="true" name="employee.departmentId" style="width:150px;"  size="8" panelHeight="auto" data-options="editable:false,valueField:'departmentId',textField:'departmentName',url:'department!departmentComboList'" /></td>
				</tr>
			</table>			
		</form>
	</div>
	 <div id="dlg-buttons">
		<a href="javascript:saveEmployee()" class="easyui-linkbutton" iconCls="icon-ok" >保存</a>
		<a href="javascript:closeEmployeeDialog()" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
	</div>
	<!-- 添加stop -->
    <!-- 编辑start -->
	<div id="edit_dlg" class="easyui-dialog" style="width:600px;height:250px;padding:20px 10px 0;" closed="true" buttons="#edit_buttons">
			<table style="width:95%;margin:auto;">
				<tr>
				 	  <td>&nbsp;小朋友姓名:&nbsp;</td>
					  <td>
					  <input type="text"  id="edit_name" style="width:150px;" class="easyui-validatebox" required="true" >
					   <input type="hidden"  id="edit_employeeId" style="width:150px;" >
					  </td>
					 <td>&nbsp;家庭住址:&nbsp;</td>
					 <td><input type="text"  id="edit_position" style="width:150px;"  class="easyui-validatebox" required="true" ></td>
				</tr>			
				<tr>
					<td>&nbsp;性别:&nbsp;</td>
					<td><select class="easyui-combobox"  id="edit_sex" style="width:150px;"  editable="false" panelHeight="auto" style="width:85px;">
								<option value="男" >男</option>
								<option value="女" >女</option>
						</select>
					</td>
					<td>&nbsp;出生日期:&nbsp;</td>
					<td><input type="text" class="easyui-datebox"   style="width:150px;" id="edit_birthday" editable="false" required="true" /></td>
				</tr>
				<tr>
					<td>&nbsp;家长姓名:&nbsp;</td>
					<td><input type="text"  id="edit_education" style="width:150px;"  class="easyui-validatebox" required="true" ></td>
					<td>&nbsp;家长联系方式:&nbsp;</td>
					<td><input type="text"  id="edit_profession" style="width:150px;"  class="easyui-validatebox" required="true" ></td>
				</tr>
			</table>			
	</div>
	 <div id="edit_buttons">
		<a href="javascript:updateMessage()" class="easyui-linkbutton" iconCls="icon-ok" >保存</a>
		<a href="javascript:closeEmployeeEditDialog()" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
	</div>
	<!-- 编辑stop -->
	<!-- 充值start -->
	<div id="cz" class="easyui-dialog" style="width:350px;height:150px;padding:20px 10px 0;" closed="true" closable="false"  buttons="#cz-buttons">
			<table style="width:95%;margin:auto;">
				<tr>
					<td>&nbsp;充值金额:&nbsp;</td>
					<td>
					 <input type="text" name="cz.czje" id="cz_czje" style="width:120px;" class="easyui-validatebox" required="true"  onkeyup="this.value=this.value.replace(/\D/g,'')">(元)
					  <input type="hidden"  id="cz_employeeId"   required="true" >
					  <input type="hidden"  id="cz_departmentId"   required="true" >
					</td>
				</tr>		
				<tr id="ts" style="display:none;">
					<td >&nbsp;天数:&nbsp;</td>
					<td><input type="text"  id="cz_ts" style="width:120px;" class="easyui-validatebox" required="true"   onkeyup="this.value=this.value.replace(/\D/g,'')">(天)</td>
				</tr>	
			    <tr id="cs" style="display:none;">
			    	<td>&nbsp;次数:&nbsp;</td>
					<td><input type="text"  id="cz_cs" style="width:120px;" class="easyui-validatebox" required="true"   onkeyup="this.value=this.value.replace(/\D/g,'')">(次)</td>
				</tr>	
			</table>			
	</div>
	<div id="cz-buttons">
		<a href="javascript:saveCz();" class="easyui-linkbutton" iconCls="icon-ok" >充值</a>
		<a href="javascript:czClose();" class="easyui-linkbutton" iconCls="icon-cancel" >取消</a>
	</div>
	<!-- 充值stop -->
	<!-- 充值记录start -->
	<div id="czjl" class="easyui-dialog" style="width:500px;height:200px;padding:5px 5px 5px;" closed="true" buttons="#czjl-buttons">
			<table style="width:100%;margin:auto;padding:5px 5px 5px;" border="1px" id="czjltab">
				
			</table>			
	</div>
	<!-- 充值记录stop -->
	<!-- 充值记录start -->
	<div id="xfjl" class="easyui-dialog" style="width:500px;height:200px;padding:5px 5px 5px;" closed="true" buttons="#xfjl-buttons">
			<table style="width:100%;margin:auto;padding:5px 5px 5px;" border="1px" id="xfjltab">
				
			</table>			
	</div>
	<!-- 充值记录stop -->
</body>
</html>