package com.hxm.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONArray;

import com.hxm.dao.CzDao;
import com.hxm.dao.EmployeeDao;
public class BackSql extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    public void init() throws ServletException {
    	DbUtil dbUtil=new DbUtil();
    	String backUp  = getServletContext().getRealPath("/upload");
    	CzDao czDao = new CzDao();
    	try {
    		Connection con = dbUtil.getCon();
    		//备份t_employee表
			ResultSet emp = czDao.backemp(con);
			JSONArray jaemp=JsonUtil.formatRsToJsonArray(emp);
			String t_employee = jaemp.toString();
			saveFile(t_employee,backUp+"/t_employee.sql");
			//备份t_employee表
			ResultSet cz = czDao.backcz(con);
			JSONArray jacz=JsonUtil.formatRsToJsonArray(cz);
			String t_cz = jacz.toString();
			saveFile(t_cz,backUp+"/t_cz.sql");
			//备份t_xfjl表
			ResultSet xfjl = czDao.backxfjl(con);
			JSONArray jaxfjl=JsonUtil.formatRsToJsonArray(xfjl);
			String t_xfjl = jaxfjl.toString();
			saveFile(t_xfjl,backUp+"/t_xfjl.sql");
			//备份t_jh表
			ResultSet jh = czDao.backjh(con);
			JSONArray jajh=JsonUtil.formatRsToJsonArray(jh);
			String t_jh = jajh.toString();
			saveFile(t_jh,backUp+"/t_jh.sql");
			dbUtil.closeCon(con);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    } 
    public void saveFile(String val,String fileUrl){
    	PrintWriter pw = null;
		try{
		   File file = new File(fileUrl);
		   if(!file.exists()){
			    try {
			        //创建文件
			        file.createNewFile();
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
		   }
		   FileOutputStream fos=new FileOutputStream(file);
		   pw=new PrintWriter(fos);
		   pw.write(val);
		   pw.flush();
		}catch(FileNotFoundException e){
		      e.printStackTrace();
		}finally{
		      try{
		         pw.close();
		      }catch(Exception e){
		          e.printStackTrace();
		      }
		}
    }

}
