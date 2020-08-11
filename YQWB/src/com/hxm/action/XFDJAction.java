package com.hxm.action;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.hxm.dao.XFDJDao;
import com.hxm.model.JH;
import com.hxm.model.PageBean;
import com.hxm.model.Xfdj;
import com.hxm.util.DbUtil;
import com.hxm.util.JsonUtil;
import com.hxm.util.ResponseUtil;
import com.hxm.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
public class XFDJAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Xfdj JH;
	private String page;
	private String rows;
	private String idv;
	private String delIds;
	
	public Xfdj getJH() {
		return JH;
	}
	public void setJH(Xfdj jH) {
		JH = jH;
	}
	public String getIdv() {
		return idv;
	}
	public void setIdv(String idv) {
		this.idv = idv;
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
	DbUtil dbUtil=new DbUtil();
	XFDJDao jhdao=new XFDJDao();
	
	public String save()throws Exception{
		
		Connection con=null;
		try {
			con=dbUtil.getCon();
			int saveNums=0;
			JSONObject result=new JSONObject();
		    saveNums=jhdao.departmentAdd(con, JH);
			if(saveNums>0){
				result.put("success", "true");
			}else{
				result.put("success", "true");
			}
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
	private String s_zffs;
	private String s_xfrq;
	
	public String getS_zffs() {
		return s_zffs;
	}
	public void setS_zffs(String s_zffs) {
		this.s_zffs = s_zffs;
	}
	public String getS_xfrq() {
		return s_xfrq;
	}
	public void setS_xfrq(String s_xfrq) {
		this.s_xfrq = s_xfrq;
	}
	@Override
	public String execute() throws Exception {
		Connection con=null;
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			Xfdj jh = new Xfdj();
			if(s_zffs!=null && !"".equals(s_zffs) && !"0".equals(s_zffs)){
				jh.setZffs(s_zffs);
			}if(s_xfrq!=null && !"".equals(s_xfrq)){
				jh.setXfrq(s_xfrq);
			}
			ResultSet rs=jhdao.departmentList(con,pageBean,jh);
			JSONArray jsonArray=JsonUtil.formatRsToJsonArray(rs);
			int total=jhdao.departmentCount(con,jh);
			result.put("rows", jsonArray);
			result.put("total", total);
			HttpServletResponse response=ServletActionContext.getResponse();
			ResponseUtil.write(response, result);
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
			//String str[]=delIds.split(",");
			int delNums=jhdao.departmentDelete(con, delIds);
			if(delNums>0){
				result.put("success", "true");
				result.put("delNums", delNums);
			}else{
				result.put("success", "true");
				result.put("errorMsg", "删除失败");
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
}
