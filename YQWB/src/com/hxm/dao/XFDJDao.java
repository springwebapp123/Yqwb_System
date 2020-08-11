package com.hxm.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hxm.model.PageBean;
import com.hxm.model.Xfdj;
import com.hxm.util.StringUtil;
public class XFDJDao {

	public ResultSet departmentList(Connection con,PageBean pageBean,Xfdj jh) throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_xfdf where 1=1 ");
		
		if(jh!=null&&!StringUtil.isEmpty(jh.getZffs())){
			sb.append(" and zffs like '%"+jh.getZffs()+"%'");
		}
		if(jh!=null&&!StringUtil.isEmpty(jh.getXfrq())){
			sb.append(" and xfrq  like '%"+jh.getXfrq()+"%'");
		}
		sb.append(" order by xfrq desc ");
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	
	public int departmentCount(Connection con,Xfdj jh)throws Exception{
		String sql="select count(*) as total from t_xfdf  where 1=1 ";
		if(jh!=null&&!StringUtil.isEmpty(jh.getZffs())){
			sql+=(" and zffs like '%"+jh.getZffs()+"%'");
		}
		if(jh!=null&&!StringUtil.isEmpty(jh.getXfrq())){
			sql+=(" and xfrq  like '%"+jh.getXfrq()+"%'");
		}
		PreparedStatement pstmt=con.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int departmentAdd(Connection con,Xfdj jh) throws Exception{
		String sql="insert into t_xfdf values(null,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, jh.getJe());
		pstmt.setString(2, jh.getZffs());
		pstmt.setString(3,getNowDay());
		return pstmt.executeUpdate();		
	}
	public static String getNowDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return (df.format(new Date()));// new Date()为获取当前系统时间
	}
	public int departmentDelete(Connection con,String delIds)throws Exception{
		String sql="delete from t_xfdf where id in("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
}
