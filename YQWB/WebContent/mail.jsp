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
					s_zffs:$('#s_zffs').combobox('getValue'),
					s_xfrq:$('#s_xfrq').datebox("getValue")
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
						$.post("xfdjaction!delete",{delIds:ids},function(result){
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
				url=("xfdjaction!save");
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
				   pageList="[15,20,25,30]"  pageSize="15"  fit="true" pagination="true" url="xfdjaction" toolbar="#tb"   fitColumns="true"  rownumbers="true" >
			<thead>
				<tr>
					<th field="cb" checkbox="true" ></th>
					<th data-options="field:'id'" width="5"  hidden="true">编号</th>
					<th data-options="field:'je'" width="30">金额(元)</th>
					<th data-options="field:'zffs'" width="30">支付方式</th>
					<th data-options="field:'xfrq'" width="40">消费日期</th>
				</tr>
			</thead>
	</table>
	<div style="padding:5px;" id="tb" >
		<a href="javascript:openDepartmentAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">添加</a>
		<a href="javascript:deleteDepartment()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>
		<a href="javascript:location.reload()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'" plain="true">刷新</a>
		&nbsp;&nbsp;&nbsp;&nbsp;支付方式：&nbsp;
		 <select name="s_zffs" id="s_zffs" data-options="editable:false" class="easyui-combobox" style="width:100px;">
		         <option value="0">全部</option>
		         <option value="支付宝">支付宝</option>
		         <option value="微信">微信</option>
		         <option value="现金">现金</option>
		 </select>&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;消费日期：&nbsp;
		<input type="text" class="easyui-datebox"  name="s_xfrq" id="s_xfrq"  editable="false" required="true" />&nbsp;
		<a href="javascript:searchDepartment()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'" plain="true">搜索</a>
	</div>
	<div id="dlg" class="easyui-dialog" style="width:400px;height:130px;padding:10px 20px;" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" >
			<table>
				<tr>
					<td>消费金额：</td>
					<td><input type="text"  onkeyup="this.value=this.value.replace(/\D/g,'')" style="width:80px;" name="JH.je" id="je" class="easyui-validatebox" required="true" >(元)</td>
					<td>支付方式：</td>
					<td>
							 <select  style="width:80px;"  name="JH.zffs" id="zffs" data-options="editable:false" class="easyui-combobox" style="width:100px;">
							         <option value="支付宝">支付宝</option>
							         <option value="微信">微信</option>
							         <option value="现金">现金</option>
							 </select>
					</td>
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