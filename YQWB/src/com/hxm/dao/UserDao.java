package com.hxm.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.hxm.model.User;
import com.hxm.util.PasswordEncoder;
public class UserDao {

	public User login(Connection con,User user) throws Exception{
		User currentUser=null;
		
		 String salt = "webapp123";
	     PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
	     String encode = encoderMd5.encode(user.getPassword());
		 user.setPassword(encode);
		
		String sql="select * from t_user where userName=? and password=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserName());
		pstmt.setString(2, user.getPassword());
		ResultSet rs=pstmt.executeQuery();
		
		if(rs.next()){
			currentUser = new User();
			currentUser.setUserName(rs.getString("userName"));
			currentUser.setPassword(rs.getString("password"));
			currentUser.setNameval(rs.getString("nameval"));
		}
		return currentUser;
	}
}
