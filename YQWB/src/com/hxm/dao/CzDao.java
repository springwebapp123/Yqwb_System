package com.hxm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hxm.model.Cz;
import com.hxm.model.Employee;
import com.hxm.model.PageBean;
import com.hxm.model.Xfjl;
import com.hxm.util.StringUtil;

public class CzDao {

	public ResultSet employeeList(Connection con,PageBean pageBean,Employee employee,String bbirthday,String ebirthday) throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_employee s,t_department g where s.departmentId=g.departmentId");
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.employeeNo like '%"+employee.getEmployeeNo()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.image = '"+employee.getEmployeeNo()+"'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.detail = '"+employee.getEmployeeNo()+"'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.cz = '"+employee.getEmployeeNo()+"'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getName())){
			sb.append(" and s.name like '%"+employee.getName()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getSex())){
			sb.append(" and s.sex = '"+employee.getSex()+"'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getNationality())){
			sb.append(" and s.nationality like '%"+employee.getNationality()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEducation())){
			sb.append(" and s.education like '%"+employee.getEducation()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getPosition())){
			sb.append(" and s.position like '%"+employee.getPosition()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEducation())){
			sb.append(" and s.education like '%"+employee.getEducation()+"%'");
		}
		if(employee.getDepartmentId()!=-1){
			sb.append(" and s.departmentId ='"+employee.getDepartmentId()+"'");			
		}
		if(StringUtil.isNotEmpty(bbirthday)){
			sb.append(" and TO_DAYS(s.birthday)>=TO_DAYS('"+bbirthday+"')");			
		}
		if(StringUtil.isNotEmpty(ebirthday)){
			sb.append(" and TO_DAYS(s.birthday)<=TO_DAYS('"+ebirthday+"')");			
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	
	public int employeeCount(Connection con,Employee employee,String bbirthday,String ebirthday)throws Exception{
		StringBuffer sb=new StringBuffer("select count(*) as total from t_employee s,t_department g where s.departmentId=g.departmentId");
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.employeeNo like '%"+employee.getEmployeeNo()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getName())){
			sb.append(" and s.name like '%"+employee.getName()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getSex())){
			sb.append(" and s.sex = '"+employee.getSex()+"'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getNationality())){
			sb.append(" and s.nationality like '%"+employee.getNationality()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEducation())){
			sb.append(" and s.education like '%"+employee.getEducation()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getPosition())){
			sb.append(" and s.position like '%"+employee.getPosition()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getEducation())){
			sb.append(" and s.education like '%"+employee.getEducation()+"%'");
		}
		if(employee.getDepartmentId()!=-1){
			sb.append(" and s.departmentId ='"+employee.getDepartmentId()+"'");			
		}
		if(StringUtil.isNotEmpty(bbirthday)){
			sb.append(" and TO_DAYS(s.birthday)>=TO_DAYS('"+bbirthday+"')");			
		}
		if(StringUtil.isNotEmpty(ebirthday)){
			sb.append(" and TO_DAYS(s.birthday)<=TO_DAYS('"+ebirthday+"')");			
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	public int employeeDelect(Connection con,String delIds)throws Exception{
		String sql="delete from t_employee where employeeId in("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public String getMaxJSRQ(Connection con,int empId)throws Exception{
		String sql="select max(jsrq) as maxjsrq from t_cz where employddId="+empId;
		PreparedStatement pstmt=con.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getString("maxjsrq");
		}else{
			return "";
		}
	}
	
	
	public int czAdd(Connection con,Cz cz)throws Exception{
		if("1".equals(cz.getIsone())){//第一次来(月卡)
			//结束日期等于当前日期加天数
			String jsrq = plusDay(new Integer(cz.getTs()),getNowDay());
			String sql="insert into t_cz (employddId,czje,czrq,cs,ts,isone,jsrq) values(?,?,?,?,?,?,?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, cz.getEmployddId());
			pstmt.setString(2, cz.getCzje());
			pstmt.setString(3, getNowDay());
			pstmt.setString(4,cz.getCs());
			pstmt.setString(5, cz.getTs());
			pstmt.setString(6, cz.getIsone());
			pstmt.setString(7, jsrq);
			return pstmt.executeUpdate();
		}else if("2".equals(cz.getIsone())){//之前来过了(月卡)
			 //先查询结果集
			 String maxjsrq = getMaxJSRQ(con,cz.getEmployddId());
			 if(!maxjsrq.equals("")){
				  String stopDay =maxjsrq;//计算累计截止日期
				  String nowDay = getNowDay();//获取当前日期
				  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        	  Date now = format.parse(nowDay);
	              Date stop = format.parse(stopDay);
	              int sy =  differentDays(now,stop);
	              if(sy>=0){//表示没过期(用最后一次时间)
	            	  String jsrq = plusDay(new Integer(cz.getTs()),maxjsrq);
	            	  cz.setJsrq(jsrq);
	              }else{//已经过期了,用当前时间吧
	            	  String jsrq = plusDay(new Integer(cz.getTs()),nowDay);
	            	  cz.setJsrq(jsrq);
	              }
	              String sql="insert into t_cz (employddId,czje,czrq,cs,ts,isone,jsrq) values(?,?,?,?,?,?,?)";
	  			  PreparedStatement pstmt=con.prepareStatement(sql);
	  			  pstmt.setInt(1, cz.getEmployddId());
	  			  pstmt.setString(2, cz.getCzje());
	  			  pstmt.setString(3, getNowDay());
	  			  pstmt.setString(4,cz.getCs());
	  			  pstmt.setString(5, cz.getTs());
	  			  pstmt.setString(6, cz.getIsone());
	  			  pstmt.setString(7, cz.getJsrq());
	  			  return pstmt.executeUpdate();
			 }else{
				 return -1;
			 }
			//第一如果最后一次消费时间这当前时间之前那么就用再后一次时间加上天数
			//第二如果最后一次消费时间在今天之后那么就用当前时间加上充值天数
		}else{//次卡
			String sql="insert into t_cz (employddId,czje,czrq,cs,ts,isone) values(?,?,?,?,?,?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, cz.getEmployddId());
			pstmt.setString(2, cz.getCzje());
			pstmt.setString(3, getNowDay());
			pstmt.setString(4,cz.getCs());
			pstmt.setString(5, cz.getTs());
			pstmt.setString(6, cz.getIsone());
			return pstmt.executeUpdate();
		}
		
	}
	public static void main(String[] args)throws Exception {
		  String stopDay ="2017-10-17 00:51:02";//计算累计截止日期
		  String nowDay = getNowDay();//获取当前日期
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  	      Date now = format.parse(nowDay);
          Date stop = format.parse(stopDay);
          int sy =  differentDays(stop,now);
          System.out.println(sy);
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return (df.format(new Date()));// new Date()为获取当前系统时间
	}
	public boolean getEmployeeByDepartmentId(Connection con,String departmentId)throws Exception{
		String sql="select * from t_employee where departmentId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, departmentId);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	public int xfjl(Connection con,Xfjl xfjl)throws Exception{
		String sql="insert into t_xfjl values(null,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,xfjl.getEmployeeId());
		pstmt.setString(2, getNowDay());
		return pstmt.executeUpdate();
	}
	public JSONArray getczjl(Connection con,int employddId)throws Exception{
		String sql="select czje,czrq,cs,ts,isone,jsrq from t_cz where employddId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, employddId);
		ResultSet rs=pstmt.executeQuery();
		JSONArray ret = new JSONArray();
		while(rs.next()){
			JSONObject json = new JSONObject();
			String czje = rs.getString("czje");
			String czrq = rs.getString("czrq");
			String cs = rs.getString("cs");
			String ts = rs.getString("ts");
			String isone = rs.getString("isone");
			String jsrq = rs.getString("jsrq");
			json.put("czje", czje);
			json.put("czrq", czrq);
			json.put("cs", cs);
			json.put("ts", ts);
			json.put("isone", isone);
			json.put("jsrq", jsrq);
			ret.add(json);
		}
		return ret;
	}
	//获取消费记录
	public JSONArray getxfjl(Connection con,int employddId)throws Exception{
		String sql="select employeeId,xfrq from t_xfjl where employeeId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, employddId);
		ResultSet rs=pstmt.executeQuery();
		JSONArray ret = new JSONArray();
		while(rs.next()){
			JSONObject json = new JSONObject();
			String xfrq = rs.getString("xfrq");
			json.put("xfrq", xfrq);
			ret.add(json);
		}
		return ret;
	}
	//获取充值记录
	public boolean getCZJL(Connection con,int employeeId)throws Exception{
		String sql="select id from t_cz where employddId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, employeeId);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	
	
	public static List<Employee> getData(Connection con,ResultSet rs) throws Exception{
		List<Employee> list = new ArrayList<Employee>();
		try {
			con.setAutoCommit(false);
			while(rs.next()){
				int employeeId = rs.getInt("employeeId");
				String employeeNo = rs.getString("employeeNo");
				String name = rs.getString("name");
				String sex = rs.getString("sex");
				Date birthday = rs.getDate("birthday");
				String nationality = rs.getString("nationality");
				String education = rs.getString("education");
				String profession = rs.getString("profession");
				String position = rs.getString("position");
				Employee employee = new Employee();
				employee.setEmployeeId(employeeId);
				employee.setEmployeeNo(employeeNo);
				employee.setName(name);
				employee.setSex(sex);
				employee.setBirthday(birthday);
				employee.setNationality(nationality);
				employee.setEducation(education);
				employee.setProfession(profession);
				employee.setPosition(position);
				list.add(employee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public ResultSet backemp(Connection con) throws Exception{
		String sql = "select * from t_employee";
		StringBuffer sb=new StringBuffer(sql);
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	public ResultSet backcz(Connection con) throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_cz");
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	public ResultSet backxfjl(Connection con) throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_xfjl");
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	public ResultSet backjh(Connection con) throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_jh");
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
}
