<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>一祺玩吧会员管理系统</title>
<%
	//权限验证
	if(session.getAttribute("currentUser")!=null){
		response.sendRedirect("main.jsp");
		return;
	}
%>
<link href="CSS/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	function check(form){
		if ($("#userName").val()==""){
			$.messager.alert("系统提示","请输入账号!","error");
			return false;
		}
		if ($("#password").val()==""){
			$.messager.alert("系统提示","请输入密码!","error");
			return false;
		}	
	}; 
	function keyLogin(){
		 if (event.keyCode==13) {
		       if ($("#userName").val()==""){
					$.messager.alert("系统提示","请输入账号!","error");
					return false;
				}
				if ($("#password").val()==""){
					$.messager.alert("系统提示","请输入密码!","error");
					return false;
			    }	
		         document.getElementById("form1").submit();
		 }
	};
	$(document).ready(function(){
		window.flags = 0;
		window.setInterval(function(){
			if(window.flags == 0){
				$("#yqwbImg").attr("src","Cmages/n.jpg");
				window.flags = 1;
			}else{
				$("#yqwbImg").attr("src","Cmages/y.jpg");
				window.flags = 0;
			}
		},2000);
	});
</script>
</head>
<body style="background-color:E0ECFF;"  onkeydown="keyLogin();" >
<table width="778" height="217" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
    <td height="189" colspan="2"></td>
  </tr>
</table>
<table width="800" height="350"  border="0" align="center" style="border-radius: 10px;background:url(Cmages/bc.jpg) no-repeat;background-size:100%;" cellpadding="0" cellspacing="0" >
          <tr>
            <td height="72" valign="top"><table width="100%" height="63"  border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="2%">&nbsp;</td>
                <td width="97%" align="center" valign="top">
                    <form autoComplete="off" name="form1" id="form1" action="login" method="post">
                      <table width="100%"  border="0" cellpadding="0" cellspacing="0" bordercolorlight="#FFFFFF" bordercolordark="#D2E3E6">
                        <tr>
                        <td width="48%" height="36">&nbsp;</td>
                        <td colspan="3" style="text-align:right;"><font color="red">${error}</font></td>
                        </tr>
                        <tr>
                          <td width="48%" height="36">&nbsp;</td>
	                      <td width="8%">账号：</td>
	                      <td width="32%">
	                        <input type="text" class="logininput" style="text-indent:1em"   name="user.userName" id="userName"  size="25">                      
	                      </td>
	                      <td rowspan="2"><img style="border-top-left-radius:5px;border-top-right-radius:5px;border-bottom-left-radius:5px;border-bottom-right-radius:5px;" id="yqwbImg" height="70px" width="140px" src="Cmages/y.jpg"></td>
                   		 </tr>
	                     <tr>
	                          <td height="38">&nbsp;</td>
		                      <td  width="8%">密码：</td>
		                      <td><input type="password"  style="text-indent:1em" class="logininput"   name="user.password" id="password"  size="25"></td>
		                      <td>&nbsp;</td>
	                     </tr>
	                     <tr>
	                          <td height="31">&nbsp;</td>
		                      <td height="31" colspan="2" align="center"><input name="Submit" style="width:40px;height:25px;"  type="submit" class="btn_grey" value="登录" onClick="return check(form1)">
		                        &nbsp;
		                       <input name="Submit3"  style="width:40px;height:25px;" type="reset" class="btn_grey" value="重置">&nbsp;
	                     </tr>
                      </table> 
			  </form>				   </td>
                  <td width="1%">&nbsp;</td>
              </tr>
              </table>
              </td>
  </tr>
  <tr align="center">
   <td>
         软件说明:该软件所有权归属 《一祺玩吧》商家所有,其他第三方均没有使用和操控权!
   </td>
  </tr>
  
</table>
</body>
</html>
