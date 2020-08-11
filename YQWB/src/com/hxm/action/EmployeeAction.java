package com.hxm.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.hxm.dao.CzDao;
import com.hxm.dao.EmployeeDao;
import com.hxm.model.Cz;
import com.hxm.model.Employee;
import com.hxm.model.PageBean;
import com.hxm.model.User;
import com.hxm.model.Xfjl;
import com.hxm.util.DateUtil;
import com.hxm.util.DbUtil;
import com.hxm.util.JsonUtil;
import com.hxm.util.PasswordEncoder;
import com.hxm.util.ResponseUtil;
import com.hxm.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EmployeeAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Employee employee;
	private String s_employeeNo;
	private String s_name;
	private String s_bksj;
	public String getS_bksj() {
		return s_bksj;
	}
	public void setS_bksj(String s_bksj) {
		this.s_bksj = s_bksj;
	}

	private String s_sex;
	private String s_bbirthday;
	private String s_ebirthday;
	private String s_nationality;
	private String s_education;
	private String s_departmentId;
	private String s_position;
	private String page;
	private String rows;
	private String delIds;
	private String employeeId;
	private String s_image;//小朋友照片
	public String getS_image() {
		return s_image;
	}
	public void setS_image(String s_image) {
		this.s_image = s_image;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getS_employeeNo() {
		return s_employeeNo;
	}
	public void setS_employeeNo(String s_employeeNo) {
		this.s_employeeNo = s_employeeNo;
	}
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public String getS_sex() {
		return s_sex;
	}
	public void setS_sex(String s_sex) {
		this.s_sex = s_sex;
	}
	public String getS_bbirthday() {
		return s_bbirthday;
	}
	public void setS_bbirthday(String s_bbirthday) {
		this.s_bbirthday = s_bbirthday;
	}
	public String getS_ebirthday() {
		return s_ebirthday;
	}
	public void setS_ebirthday(String s_ebirthday) {
		this.s_ebirthday = s_ebirthday;
	}
	public String getS_nationality() {
		return s_nationality;
	}
	public void setS_nationality(String s_nationality) {
		this.s_nationality = s_nationality;
	}
	public String getS_education() {
		return s_education;
	}
	public void setS_education(String s_education) {
		this.s_education = s_education;
	}
	public String getS_departmentId() {
		return s_departmentId;
	}
	public void setS_departmentId(String s_departmentId) {
		this.s_departmentId = s_departmentId;
	}
	public String getS_position() {
		return s_position;
	}
	public void setS_position(String s_position) {
		this.s_position = s_position;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getDelIds() {
		return delIds;
	}
	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public ResultSet search() throws Exception {
		Connection con=null;
		con=dbUtil.getCon();
		if(page==null){
			page = "1";
			rows = "15";
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		employee=new Employee();
		if(s_employeeNo!=null){
			employee.setEmployeeNo(s_employeeNo);
			employee.setName(s_name);
			employee.setSex(s_sex);
			employee.setEducation(s_education);
			employee.setPosition(s_position);
			employee.setNationality(s_nationality);
			employee.setBksj(s_bksj);
			if(StringUtil.isNotEmpty(s_departmentId)){
				employee.setDepartmentId(Integer.parseInt(s_departmentId));
			}
		    employee.setImage(s_image);
		}
		ResultSet rs = employeeDao.employeeList(con,pageBean,employee,s_bbirthday,s_ebirthday);
		return rs;
	}
	
	DbUtil dbUtil=new DbUtil();
	EmployeeDao employeeDao=new EmployeeDao();
	@Override
	public String execute() throws Exception {
		Connection con=null;
		try {
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			JSONArray jsonArray=JsonUtil.formatRsToJsonArray(search());
			JSONArray retArray = new JSONArray();
			for(int i=0;i<jsonArray.size();i++){
				  JSONObject json =  JSONObject.fromObject(jsonArray.get(i));
				  
				  String sex = json.getString("sex");//性别
				  if(sex.equals("男")){
					  json.put("imgs", "<img title='"+sex+"' style='width:20px;height:20px;' src='images/boy.png'/>");
				  }else{
					  json.put("imgs", "<img  title='"+sex+"'  style='width:20px;height:20px;' src='images/gril.png'/>");
				  }
				  String position = json.getString("position");//家庭住址
				  if(position.length()>8){
					  json.put("position", "<span title='"+position+"' >"+position.substring(0,7)+"...</span>");
				  }else{
					  json.put("position", "<span  title='"+position+"' >"+position+"</span>");
				  }
				  json.put("address", position);
				  String departmentId = json.getString("departmentId");
				  String employeeId = json.getString("employeeId");
				  String ljczje = json.getString("ljczje");
				  json.put("ljczje", ljczje+"元");
				  if(departmentId!=null && departmentId.equals("1")){//次卡
					  String mark = "<a  style=\"text-decoration:none;\" href=\"javascript:opencz("+employeeId+","+departmentId+");\">充值</a>&nbsp;&nbsp;<a  style=\"text-decoration:none;\" href=\"javascript:openczjl("+employeeId+","+departmentId+");\">充值记录</a>&nbsp;&nbsp;<a  style=\"text-decoration:none;\" href=\"javascript:openxfjl("+employeeId+","+departmentId+");\">消费记录</a>";
					  String cs = json.getString("cs");//累计充值次数
					  String xfjl = json.getString("xfjl");//累计消费次数
					  if(cs.equals("0")){
						  json.put("sy", "0次");
					  }else{
						  int sy = Integer.parseInt(cs)-Integer.parseInt(xfjl);
						  json.put("sy",sy+"次");
					  }
					  if(!(cs.equals("0") || (Integer.parseInt(cs)-Integer.parseInt(xfjl))==0)){
						    mark += "&nbsp;&nbsp;<a  style=\"text-decoration:none;\" href=\"javascript:djxf("+employeeId+","+departmentId+");\">点击消费</a>";
					  }
					  json.put("mark", mark);
				  }else   if(departmentId!=null && departmentId.equals("2")){//月卡
					  /*String ts = json.getString("ts");//累计天数
					  String czrq = json.getString("czrq");//获取第一次充值日期
					  if(czrq.equals("0")){
						  json.put("sy","0天");
					  }else{
						  String stopDay =plusDay(Integer.parseInt(ts),czrq);//计算累计截止日期
						  String nowDay = getNowDay();//获取当前日期
						  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					      try{
					        	 Date now = format.parse(nowDay);
					             Date stop = format.parse(stopDay);
					             int sy =  differentDays(now,stop);
					             json.put("sy",sy+"天");
					      }catch (Exception e) {
					             e.printStackTrace();
					      }
					  }*/
					  String sy = "<a style=\"text-decoration:none;\"  href=\"javascript:openczjl("+employeeId+","+departmentId+");\">查看</a>";
					  json.put("sy",sy);
					  String mark = "<a style=\"text-decoration:none;\"  href=\"javascript:opencz("+employeeId+","+departmentId+");\">充值</a>&nbsp;&nbsp;<a style=\"text-decoration:none;\"  href=\"javascript:openczjl("+employeeId+","+departmentId+");\">充值记录</a>";
					  json.put("mark", mark);
				  }
				  retArray.add(json);
			}
			int total=employeeDao.employeeCount(con,employee,s_bbirthday,s_ebirthday);
			result.put("rows", retArray);
			result.put("total", total);
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String delete()throws Exception{
		Connection con=null;
		try {
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			int delNums=employeeDao.employeeDelect(con, delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums", delNums);
			}else{
				result.put("errorMeg", "删除失败");
			}
			result.put("delNum", delNums);
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String save()throws Exception{
		if(StringUtil.isNotEmpty(employeeId)){
			employee.setEmployeeId(Integer.parseInt(employeeId));
		}
		Connection con=null;
		try {
			con=dbUtil.getCon();
			int saveNums=0;
			String result="";
			boolean isDis = false;
			String employeeNo = employee.getEmployeeNo();
			if(employeeNo==null || "".equals(employeeNo) ){
				result= "c";
			}else{
				if(StringUtil.isNotEmpty(employeeId)){
					//saveNums=employeeDao.employeeModify(con, employee);
				}else{
					isDis = employeeDao.getEmployeeByemployeeNo(con, employeeNo);
					if(!isDis){
						saveNums=employeeDao.employeeAdd(con, employee);
					}
				}
				if(isDis){
					result =  employeeNo;
				}else{
					if(saveNums>0){
						result= "a";
					}else{
						result = "b";
					}
				}
			}
			ResponseUtil.writeJSON(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}	
	
	public String djxf()throws Exception{
		Connection con=null;
		HttpServletRequest request=ServletActionContext.getRequest();
		String cz_employeeId = request.getParameter("cz_employeeId");
		try {
			con=dbUtil.getCon();
			con.setAutoCommit(false);//控制事务
			Xfjl xfjl = new Xfjl();
			int saveNums=0;
			JSONObject result=new JSONObject();
			xfjl.setEmployeeId(Integer.parseInt(cz_employeeId));
			saveNums=czDao.xfjl(con,xfjl);
			if(saveNums>0){
				result.put("success", "0");
			}else{
				result.put("success", "1");
			}
			con.commit();
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			con.rollback();//如发现错误立即回滚
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}	
	/*public void ExportEmployee() throws Exception{	
		Connection con = null;
		con=dbUtil.getCon();		
		List<Employee> list = employeeDao.getData(con,search());
		HSSFWorkbook workbook = new HSSFWorkbook();	
		try {
			HSSFSheet sheet = workbook.createSheet("employee");	
			HSSFRow row = sheet.createRow(0);
			String[] cellTitle = {"ID", "编号", "姓名", "性别","出生日期", "民族", "学历", "专业","部门", "职务", "基本工资", "加班费","工龄工资", "考勤费", "旷工费", "保险费"};	
			for (int i = 0; i < cellTitle.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(cellTitle[i]);
			}
			
			for(int rowIndex=0;rowIndex<list.size();rowIndex++){
				row = sheet.createRow(rowIndex+1);	
				HSSFCell cell = row.createCell(rowIndex);
				cell.setCellValue(cellTitle[rowIndex]);
				Employee employee = list.get(rowIndex);
				for(int cellnum = 0; cellnum < 17; cellnum++){
					cell = row.createCell(cellnum);
					switch(cellnum){
					case 0:
						cell.setCellValue(employee.getEmployeeId());
						break;
					case 1:
						cell.setCellValue(employee.getEmployeeNo());
						break;
					case 2:
						cell.setCellValue(employee.getName());
						break;
					case 3:
						cell.setCellValue(employee.getSex());
						break;
					case 4:
						cell.setCellValue(DateUtil.formatDate(employee.getBirthday(), "yyyy-MM-dd"));
						break;
					case 5:
						cell.setCellValue(employee.getNationality());
						break;
					case 6:
						cell.setCellValue(employee.getEducation());
						break;
					case 7:
						cell.setCellValue(employee.getProfession());
						break;
					case 8:
						cell.setCellValue(employee.getDepartmentNameSrc());
						break;
					case 9:
						cell.setCellValue(employee.getPosition());
						break;
					case 10:
						cell.setCellValue(employee.getBaseMoney());
						break;
					case 11:
						cell.setCellValue(employee.getOvertime());
						break;
					case 12:
						cell.setCellValue(employee.getAge());
						break;
					case 13:
						cell.setCellValue(employee.getCheck1());
						break;
					case 14:
						cell.setCellValue(employee.getAbsent());
						break;
					case 15:
						cell.setCellValue(employee.getSafety());
						break;				
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
		String exportFileName = "employee.xls";
		
		ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment;filename=" + new String((exportFileName).getBytes(), "ISO8859-1"));//设定输出文件头
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
		
		
		OutputStream out = ServletActionContext.getResponse().getOutputStream();
		workbook.write(out);
		out.flush();
		out.close();
	}*/
	
	CzDao czDao = new CzDao();
	public String czsave()throws Exception{
		Connection con=null;
		HttpServletRequest request=ServletActionContext.getRequest();
		String cz_czje = request.getParameter("cz_czje");
		String cz_employeeId = request.getParameter("cz_employeeId");
		String cz_ts = request.getParameter("cz_ts");
		String cz_cs = request.getParameter("cz_cs");
		String cz_departmentId = request.getParameter("cz_departmentId");
		Cz cz = new Cz();
		cz.setCzje(cz_czje);
		cz.setEmployddId(Integer.parseInt(cz_employeeId));
		cz.setTs(null);
		cz.setCs(null);
		try {
			con=dbUtil.getCon();
			con.setAutoCommit(false);//控制事务
			if(cz_departmentId!=null && !"".equals(cz_departmentId)){
				   if(cz_departmentId.equals("1")){//次卡
					   cz.setCs(cz_cs);
					   cz.setIsone("0");
				   }else  if(cz_departmentId.equals("2")){//月卡
					   boolean isone = czDao.getCZJL(con, Integer.parseInt(cz_employeeId));
					   if(isone){//表示先前已充值过
						   cz.setIsone("2"); 
					   }else{//表示未充值过
						   cz.setIsone("1");
					   }
					   cz.setTs(cz_ts);
				   }
			 }
			int saveNums=0;
			JSONObject result=new JSONObject();
			saveNums=czDao.czAdd(con,cz);
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
				result.put("errorMeg", "充值失败");
			}
			con.commit();
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		} catch (Exception e) {
			con.rollback();//如发现错误立即回滚
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}	
	
	
	
	
	     /**
	      * 指定日期加上天数后的日期
	      * @param num 为增加的天数
	      * @param newDate 创建时间
	      * @return
	      */
	     public static String plusDay(int num,String newDate) throws Exception{
	         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         Date  currdate = format.parse(newDate);
	         Calendar ca = Calendar.getInstance();
	         ca.setTime(currdate);
	         ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
	         currdate = ca.getTime();
	         String enddate = format.format(currdate);
	         return enddate;
	     }
	     /**
	      * date2比date1多的天数
	      * @param date1    
	      * @param date2
	      * @return    
	      */
	     public static int differentDays(Date date1,Date date2)
	     {
	         Calendar cal1 = Calendar.getInstance();
	         cal1.setTime(date1);
	         
	         Calendar cal2 = Calendar.getInstance();
	         cal2.setTime(date2);
	        int day1= cal1.get(Calendar.DAY_OF_YEAR);
	         int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	         
	         int year1 = cal1.get(Calendar.YEAR);
	         int year2 = cal2.get(Calendar.YEAR);
	         if(year1 != year2)   //同一年
	         {
	             int timeDistance = 0 ;
	             for(int i = year1 ; i < year2 ; i ++)
	             {
	                 if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                 {
	                     timeDistance += 366;
	                 }
	                 else    //不是闰年
	                 {
	                     timeDistance += 365;
	                 }
	             }
	             return timeDistance + (day2-day1) ;
	         }
	         else    //不同年
	         {
	             return day2-day1;
	         }
	     }
	 	public static String getNowDay() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			return (df.format(new Date()));// new Date()为获取当前系统时间
		}
	 	//充值记录
	 	public String searchczjl()throws Exception{
			Connection con=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			String cz_employeeId = request.getParameter("cz_employeeId");
			JSONArray result = new JSONArray();
			try {
				con=dbUtil.getCon();
				result=czDao.getczjl(con,Integer.parseInt(cz_employeeId));
				ResponseUtil.writeJSON(ServletActionContext.getResponse(), result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}	
	 	//消费记录
		public String searchxfjl()throws Exception{
			Connection con=null;
			HttpServletRequest request=ServletActionContext.getRequest();
			String cz_employeeId = request.getParameter("cz_employeeId");
			JSONArray result = new JSONArray();
			try {
				con=dbUtil.getCon();
				result=czDao.getxfjl(con,Integer.parseInt(cz_employeeId));
				ResponseUtil.writeJSON(ServletActionContext.getResponse(), result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}	
		public String quitSys()throws Exception{
				JSONObject result=new JSONObject();
				HttpServletRequest request=ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				session.invalidate();
				ResponseUtil.write(ServletActionContext.getResponse(), result);
			    return null;
		}
		public String updatemm()throws Exception{
			Connection con=null;
			try {
				con=dbUtil.getCon();
				String result=new String("");
				
				HttpServletRequest request=ServletActionContext.getRequest();
				HttpSession session=request.getSession();
				User currentUser = (User) session.getAttribute("currentUser");
				
				String jmm = request.getParameter("jmm");
				String xmm = request.getParameter("xmm");
				String xmmtwo = request.getParameter("xmmtwo");
				
				
			     String salt = "webapp123";
			     PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
			     String encode = encoderMd5.encode(jmm);
			     if(encode.equals(currentUser.getPassword())){//旧密码输入正确
			    	    if(xmm.equals(xmmtwo)){
			    	        PasswordEncoder new1 = new PasswordEncoder(salt, "MD5");
						    String codenew = new1.encode(xmm);
			    	    	currentUser.setPassword(codenew);
			    	    	
			    	    	int delNums=employeeDao.updatemm(con, currentUser);
							if(delNums>0){//修改成功
								result = "0";
							}else{//修改失败
								result = "-1";
							}
			    	    }else{
			    	    	result = "2";//2次密码输入不正确
			    	    }
			     }else{
			    	         result = "1";//旧密码不正确
			     }
			     
				ResponseUtil.writeJSON(ServletActionContext.getResponse(), result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		public String updateMessage()throws Exception{
			Connection con=null;
			try {
				con=dbUtil.getCon();
				HttpServletRequest request=ServletActionContext.getRequest();
				String edit_employeeId = request.getParameter("edit_employeeId");
				String edit_name = request.getParameter("edit_name");
				String edit_sex = request.getParameter("edit_sex");
				String edit_birthday = request.getParameter("edit_birthday");
				String edit_education = request.getParameter("edit_education");
				String edit_profession = request.getParameter("edit_profession");
				String edit_position = request.getParameter("edit_position");
				Employee employee = new Employee();
				    employee.setEmployeeId((edit_employeeId==null || "".equals(edit_employeeId))?-1:Integer.parseInt(edit_employeeId));
					employee.setName(edit_name);
					employee.setSex(edit_sex);
					employee.setBirthday(DateUtil.formatString(edit_birthday, "yyyy-MM-dd"));
					employee.setEducation(edit_education);
					employee.setProfession(edit_profession);
					employee.setPosition(edit_position);
				int result =employeeDao.employeeModify(con, employee);
				ResponseUtil.writeJSON(ServletActionContext.getResponse(), result>0?"y":"n");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
}
