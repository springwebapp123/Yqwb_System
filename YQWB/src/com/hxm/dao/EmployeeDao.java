package com.hxm.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.hxm.model.Employee;
import com.hxm.model.PageBean;
import com.hxm.model.User;
import com.hxm.util.DateUtil;
import com.hxm.util.StringUtil;
public class EmployeeDao {

	public ResultSet employeeList(Connection con,PageBean pageBean,Employee employee,String bbirthday,String ebirthday) throws Exception{
		String sql = "select "
				+ "s.bksj,s.employeeId,s.employeeNo,s.name,s.sex,"
				+ "s.birthday,s.education,"
				+ "s.profession, s.departmentId,s.position,g.departmentName,"
				+ "(select ifnull(sum(cs),\"0\") from t_cz where employddId=s.employeeId) as cs,"
				+ "(select ifnull(sum(ts),\"0\") from t_cz where employddId=s.employeeId) as ts,"
				+ "(select ifnull(min(czrq),\"0\") from t_cz where employddId=s.employeeId) as czrq,"
				+ "(select ifnull(sum(czje),\"0\") from t_cz where employddId=s.employeeId) as ljczje,"
				+ "(select ifnull(count(*),\"0\") from t_xfjl where employeeId=s.employeeId) as xfjl"
				+ " from t_employee s,t_department g where s.departmentId=g.departmentId and s.isdel='0'";
		StringBuffer sb=new StringBuffer(sql);
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.employeeNo like '%"+employee.getEmployeeNo()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getName())){
			sb.append(" and s.name like '%"+employee.getName()+"%'");
		}
		if(employee.getDepartmentId()!=-1){
			sb.append(" and s.departmentId ='"+employee.getDepartmentId()+"'");			
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getBksj())){
			sb.append(" and s.bksj like '%"+employee.getBksj()+"%'");
		}
		
		sb.append(" order by s.employeeId desc ");
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	
	public int employeeCount(Connection con,Employee employee,String bbirthday,String ebirthday)throws Exception{
		StringBuffer sb=new StringBuffer("select count(*) as total from t_employee s,t_department g where s.departmentId=g.departmentId and s.isdel='0'");
		if(employee!=null&&!StringUtil.isEmpty(employee.getEmployeeNo())){
			sb.append(" and s.employeeNo like '%"+employee.getEmployeeNo()+"%'");
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getName())){
			sb.append(" and s.name like '%"+employee.getName()+"%'");
		}
		if(employee.getDepartmentId()!=-1){
			sb.append(" and s.departmentId ='"+employee.getDepartmentId()+"'");			
		}
		if(employee!=null&&!StringUtil.isEmpty(employee.getBksj())){
			sb.append(" and s.bksj like '%"+employee.getBksj()+"%'");
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
		String sql="update  t_employee set isdel ='1',employeeNo='' where employeeId in("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int updatemm(Connection con,User user)throws Exception{
		String sql="update  t_user  set password='"+user.getPassword()+"' where userName ='"+user.getUserName()+"'";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int employeeAdd(Connection con,Employee employee)throws Exception{
		String sql="insert into t_employee values(null,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, employee.getEmployeeNo());
		pstmt.setString(2, employee.getName());
		pstmt.setString(3, employee.getSex());
		pstmt.setString(4, DateUtil.formatDate(employee.getBirthday(), "yyyy-MM-dd"));
		pstmt.setString(5, employee.getEducation());
		pstmt.setString(6, employee.getProfession());
		pstmt.setInt(7, employee.getDepartmentId());
		pstmt.setString(8, employee.getPosition());
		pstmt.setString(9, "0");
		pstmt.setString(10, getNowDay());
		return pstmt.executeUpdate();
	}
	
	public static String getNowDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return (df.format(new Date()));// new Date()为获取当前系统时间
	}
	public int employeeModify(Connection con,Employee employee)throws Exception{
		String sql="update t_employee set name=?,sex=?,birthday=?,education=?,profession=?,position=? where employeeId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, employee.getName());
		pstmt.setString(2, employee.getSex());
		pstmt.setString(3, DateUtil.formatDate(employee.getBirthday(), "yyyy-MM-dd"));
		pstmt.setString(4, employee.getEducation());
		pstmt.setString(5, employee.getProfession());
		pstmt.setString(6, employee.getPosition());
		pstmt.setInt(7, employee.getEmployeeId());
		return pstmt.executeUpdate();
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
	public boolean getEmployeeByemployeeNo(Connection con,String employeeNo)throws Exception{
		String sql="select * from t_employee where employeeNo=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, employeeNo);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	
}
