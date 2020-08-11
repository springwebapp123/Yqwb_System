package com.hxm.model;

public class User {

	private int userId;
	private String userName;//账户名
	private String password;//用户密码
	private String nameval;//用户姓名
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNameval() {
		return nameval;
	}
	public void setNameval(String nameval) {
		this.nameval = nameval;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
