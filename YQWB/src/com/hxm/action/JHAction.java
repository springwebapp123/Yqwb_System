package com.hxm.action;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.hxm.dao.JHDao;
import com.hxm.model.JH;
import com.hxm.model.PageBean;
import com.hxm.util.DbUtil;
import com.hxm.util.JsonUtil;
import com.hxm.util.ResponseUtil;
import com.hxm.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class JHAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JH JH;
	private String page;
	private String rows;
	private String idv;
	private String delIds;
	private String s_jhs;
	private String s_jhrq;
	
	
	public String getS_jhs() {
		return s_jhs;
	}
	public void setS_jhs(String s_jhs) {
		this.s_jhs = s_jhs;
	}
	public String getS_jhrq() {
		return s_jhrq;
	}
	public void setS_jhrq(String s_jhrq) {
		this.s_jhrq = s_jhrq;
	}
	public JH getJH() {
		return JH;
	}
	public String getIdv() {
		return idv;
	}
	public void setIdv(String idv) {
		this.idv = idv;
	}
	public void setJH(JH jH) {
		JH = jH;
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
	JHDao jhdao=new JHDao();
	
	public String save()throws Exception{
		if(StringUtil.isNotEmpty(idv)){
			JH.setId(Integer.parseInt(idv));
		}
		Connection con=null;
		try {
			con=dbUtil.getCon();
			int saveNums=0;
			JSONObject result=new JSONObject();
			if(StringUtil.isNotEmpty(idv)){
				saveNums=jhdao.departmentModify(con, JH);
			}else{
				saveNums=jhdao.departmentAdd(con, JH);
			}
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
	@Override
	public String execute() throws Exception {
		Connection con=null;
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		try {
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			JH jh = new JH();
			if(s_jhs!=null && !"".equals(s_jhs)){
				jh.setJhs(s_jhs);
			}if(s_jhrq!=null && !"".equals(s_jhrq)){
				jh.setJhrq(s_jhrq);
			}
			ResultSet rs=jhdao.departmentList(con,pageBean,jh);
			JSONArray jsonArray=JsonUtil.formatRsToJsonArray(rs);
			
			JSONArray retArray = new JSONArray();
			for(int i=0;i<jsonArray.size();i++){
				  JSONObject json =  JSONObject.fromObject(jsonArray.get(i));
				  String hwms = json.getString("hwms");//性别
				  if(hwms.length()>30){
					  json.put("hwmsT", "<span title='"+hwms+"'>"+hwms.substring(0,29)+"......</span>");
				  }else{
					  json.put("hwmsT",hwms);
				  }
				  String je = json.getString("je");//性别
				  json.put("jeT",je+"(元)");
				  retArray.add(json);
			}
			
			int total=jhdao.departmentCount(con,jh);
			result.put("rows", retArray);
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
