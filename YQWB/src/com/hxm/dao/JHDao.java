package com.hxm.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.hxm.model.JH;
import com.hxm.model.PageBean;
import com.hxm.util.StringUtil;
public class JHDao {

	public ResultSet departmentList(Connection con,PageBean pageBean,JH jh) throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_jh where 1=1 ");
		
		if(jh!=null&&!StringUtil.isEmpty(jh.getJhs())){
			sb.append(" and jhs like '%"+jh.getJhs()+"%'");
		}
		if(jh!=null&&!StringUtil.isEmpty(jh.getJhrq())){
			sb.append(" and jhrq = '"+jh.getJhrq()+"'");
		}
		
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();		
	}
	
	public int departmentCount(Connection con,JH jh)throws Exception{
		String sql="select count(*) as total from t_jh  where 1=1 ";
		if(jh!=null&&!StringUtil.isEmpty(jh.getJhs())){
			sql+=(" and jhs like '%"+jh.getJhs()+"%'");
		}
		if(jh!=null&&!StringUtil.isEmpty(jh.getJhrq())){
			sql+=(" and jhrq = '%"+jh.getJhrq()+"%'");
		}
		PreparedStatement pstmt=con.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	public int departmentAdd(Connection con,JH jh) throws Exception{
		String sql="insert into t_jh values(null,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, jh.getJhdd());
		pstmt.setString(2, jh.getJhrq());
		pstmt.setString(3,jh.getHwms() );
		pstmt.setString(4,jh.getJhs() );
		pstmt.setString(5,jh.getJhslxfs() );
		pstmt.setString(6, jh.getJe());
		return pstmt.executeUpdate();		
	}
	
	public int departmentDelete(Connection con,String delIds)throws Exception{
		String sql="delete from t_jh where id in("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
	
	public int departmentModify(Connection con,JH jh)throws Exception{
		String sql="update t_jh set jhdd=?,jhrq=?,hwms=?,jhs=?,jhslxfs=?,je=? where id=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,jh.getJhdd() );
		pstmt.setString(2, jh.getJhrq());
		pstmt.setString(3,jh.getHwms());
		pstmt.setString(4, jh.getJhs());
		pstmt.setString(5, jh.getJhslxfs());
		pstmt.setString(6,jh.getJe());
		pstmt.setInt(7,jh.getId());
		return pstmt.executeUpdate();
	}
}
