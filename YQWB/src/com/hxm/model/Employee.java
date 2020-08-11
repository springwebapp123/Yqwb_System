package com.hxm.model;

import java.util.Date;


public class Employee {

	private int employeeId;//流水编号
	private String employeeNo;//VIP会员卡编码
	private String name;//小朋友姓名
	private String sex;//年龄
	private Date birthday;//出生年月
	private String nationality;//名族
	private String education;//家长姓名
	private String profession;//家长联系方式
	private int departmentId=-1;//办卡方式
	private String position;//家庭住址
	private String image;//小朋友照片
    private String isdel;//0:正常,1:已删除
	private String bksj;//办卡时间
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public String getIsdel() {
		return isdel;
	}
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}
	public String getBksj() {
		return bksj;
	}
	public void setBksj(String bksj) {
		this.bksj = bksj;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
