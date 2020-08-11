<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>进货记录</title>
 		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/icon.css">
		<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
		<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript">
			var url;
			function searchDepartment(){
				$('#dg').datagrid('load',{
					s_jhs:$('#s_jhs').val(),
					s_jhrq:$('#s_jhrq').datebox("getValue")
				});				
			};
			function deleteDepartment(){
				var selectedRows=$("#dg").datagrid('getSelections');
				if(selectedRows.length==0){
					$.messager.alert("系统提示","请选择要删除的数据！");
					return;
				}
				var strIds=[];
				for(var i=0;i<selectedRows.length;i++){
					strIds.push(selectedRows[i].id);
				}
				var ids=strIds.join(",");
				$.messager.confirm("系统提示","您确认要删掉这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
					if(r){
						$.post("jhaction!delete",{delIds:ids},function(result){
							if(result.success){
								$.messager.alert("系统提示","您已成功删除<font color=red>"+result.delNum+"</font>条数据!");
								$("#dg").datagrid("reload");
							}else{
								$.messager.alert("系统提示",'<font color=red>'+selectedRows[result.errorIndex].departmentName+'</font>'+result.errorMsg);
							}
						},"json");
					}
				});
			};
			function openDepartmentAddDialog(){
				$('#dlg').dialog('open').dialog("setTitle","添加进货信息");
				url=("jhaction!save");
			};
			function openDepartmentModifyDialog(){
				var selectedRows=$("#dg").datagrid('getSelections');
				if(selectedRows.length!=1){
					$.messager.alert("系统提示","请选择一条要编辑的数据！");
					return;
				}
				var row=selectedRows[0];
				$("#dlg").dialog("open").dialog("setTitle","编辑进货信息");
				$("#idv").val(row.id);
				$('#jhdd').val(row.jhdd);
				$('#jhrq').datebox("setValue",row.jhrq);
				$('#hwms').html(row.hwms);
				$('#jhs').val(row.jhs);
				$('#jhslxfs').val(row.jhslxfs);
				$('#je').val(row.je);
				url="jhaction!save?id="+row.id;
			};
			function closeDepartmentDialog(){
				$('#dlg').dialog("close");
				resetValue();
			};
			function resetValue(){
				$("#idv").val("");
				$('#jhdd').val("");
				$('#jhrq').datebox("setValue","");
				$('#hwms').html("");
				$('#jhs').val("");
				$('#jhslxfs').val("");
				$('#je').val("");
			};
			function saveDepartment(){
				$('#fm').form("submit",{
					url:url,
					onSubmit:function(){
						return $(this).form("validate");
					},
					success:function(result){
						if(result.errorMsg){
							$.messager.alert("系统提示",result.errorMsg);
							return;
						}else{
							$.messager.alert("系统提示","保存成功");
							resetValue();
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					}
				});
			};
		</script>
</head>
<body>
	<table id="dg" title="" class="easyui-datagrid" style="height:250px"
				   pageList="[15,20,25,30]"  pageSize="15"    fit="true" pagination="true" url="jhaction" toolbar="#tb"   fitColumns="true"  rownumbers="true" >
			<thead>
				<tr>
					<th field="cb" checkbox="true" ></th>
					<th data-options="field:'departmentId'" width="5"  hidden="true">编号</th>
					<th data-options="field:'hwmsT'" width="40">进货详情</th>
					<th data-options="field:'jhrq'" width="10">进货日期</th>
					<th data-options="field:'jhdd'" width="20">进货地点</th>
					<th data-options="field:'jhs'" width="20">进货商</th>
					<th data-options="field:'jhslxfs'" width="10">进货商联系方式</th>
					<th data-options="field:'jeT'" width="10">进货金额</th>
				</tr>
			</thead>
	</table>
	<div style="padding:5px;" id="tb" >
		<a href="javascript:openDepartmentAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">添加</a>
		<a href="javascript:openDepartmentModifyDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true">修改</a>
		<a href="javascript:deleteDepartment()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		<a href="javascript:location.reload()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'" plain="true">刷新</a>
		&nbsp;&nbsp;&nbsp;&nbsp;进货商：&nbsp;<input type="text" name="s_jhs" id="s_jhs" />&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;进货日期：&nbsp;<input type="text" class="easyui-datebox"  name="s_jhrq" id="s_jhrq"  editable="false" required="true" />&nbsp;
		<a href="javascript:searchDepartment()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" plain="true">搜索</a>
	</div>
	<div id="dlg" class="easyui-dialog" style="width:600px;height:280px;padding:10px 20px;" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" >
			<table>
				<tr>
					<td>进货日期：</td>
					<td>
					<input type="text" class="easyui-datebox"  name="JH.jhrq" id="jhrq"  editable="false" required="true" />
					<input type="hidden" name="idv" id="idv"  >
					</td>
					<td>进货地点：</td>
					<td><input type="text" name="JH.jhdd" id="jhdd" class="easyui-validatebox" required="true" ></td>
				</tr>
				<tr>
					<td>进货商：</td>
					<td><input type="text" name="JH.jhs" id="jhs" class="easyui-validatebox" required="true" ></td>
					<td>进货商联系方式：</td>
					<td><input type="text" name="JH.jhslxfs" id="jhslxfs" class="easyui-validatebox" ></td>
				</tr>
				<tr>
					<td valign="top">进货详情：</td>
					<td colspan="3"><textarea rows="4" cols="60" name="JH.hwms" id="hwms" ></textarea></td>
				</tr>
				<tr>
					<td>进货金额：</td>
					<td><input type="text" name="JH.je" id="je" class="easyui-validatebox" required="true" >(元)</td>
					<td></td>
					<td></td>
				</tr>
			</table>			
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveDepartment()" class="easyui-linkbutton" iconCls="icon-ok" >保存</a>
		<a href="javascript:closeDepartmentDialog()" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
	</div>
</body>
</html>