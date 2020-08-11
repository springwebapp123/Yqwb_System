package com.hxm.model;

public class Cz {

	private int id;
	private int employddId;
	private String czje;
    private String czrq;
    private String cs;
    private String ts;
    private String isone;//0:表示次卡，1:表示月卡第一次,2:表示月卡第二次
    private String jsrq;//结束日期
	public String getJsrq() {
		return jsrq;
	}
	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getIsone() {
		return isone;
	}
	public void setIsone(String isone) {
		this.isone = isone;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEmployddId() {
		return employddId;
	}
	public void setEmployddId(int employddId) {
		this.employddId = employddId;
	}
	public String getCzje() {
		return czje;
	}
	public void setCzje(String czje) {
		this.czje = czje;
	}
	public String getCzrq() {
		return czrq;
	}
	public void setCzrq(String czrq) {
		this.czrq = czrq;
	}
	
	
}
