/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import utils.Config;

/**
 *
 * @author LunaTK
 */
public class DBManager {
	private Connection connection = null;
	private Statement st = null;
	private ResultSet rs;
	private String sql;
	private static DBManager instance;
	private DBManager(){}

	public static DBManager getInstance(){
		if(instance==null) instance = new DBManager();
		return instance;
	}

	public void disconnectDB(){
		if(st!=null){
			try{
				if(!st.isClosed()) st.close();  
			} catch (SQLException e){
				e.printStackTrace();
			}
			st = null;
		}
		if(connection!=null){
			try{
				if(!connection.isClosed()) connection.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
			connection=null;
		}
	}

	public void connectDB(){
		try {
			if(connection!=null && connection.isValid(0)) System.err.println("DB already connected.");
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",Config.DB_IP,Config.DB_PORT,Config.DB_SCHEME_NAME)
					,Config.DB_USER_ID, Config.DB_USER_PWD);
			st = connection.createStatement();
			System.out.println("DB Connected!");
		} catch (SQLException se1) {
			se1.printStackTrace();
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}   

	public boolean authUser(String id, String pwd){
		System.out.println("id : " + id + ", pwd : " + pwd);
		String sql = String.format("SELECT * FROM qc_user where userid='%s' and pw='%s'", id,pwd);
		try {
			return st.executeQuery(sql).first();
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}


	public boolean addUser(String id, String pwd){
		String sql = String.format("INSERT INTO qc_user (userid, pw) values ('%s','%s')", id, pwd);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
		return false;
	}

	public boolean updateMileage(String id, int mileage){
		String sql = String.format("UPDATE qc_mileage, qc_user SET qc_mileage.mileage = %d WHERE qc_mileage.user_idx = qc_user.idx and qc_user.userid='%s'", mileage, id);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void sendDistance(String id){
		String sql = String.format("SELECT mileage FROM qc_mileage WHERE qc_mileage.user_idx = (SELECT idx FROM qc_user WHERE qc_user.user_id = '%s')", id);
		try {
			rs = st.executeQuery(sql);
			int mileage = rs.getInt(0);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userid", id);
			jsonObj.put("mileage", mileage);
			//return jsonObj
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void sendSubstPart(String id){
		String sql = String.format("SELECT mileage FROM qc_mileage_part WHERE qc_mileage_part.user_idx = (SELECT idx FROM qc_user WHERE qc_user.user_id = '%s')", id);
		try {
			rs = st.executeQuery(sql);
			int mileage = rs.getInt(0);
			while(rs.next()){
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("userid", id);
		jsonObj.put("mileage", mileage);

	}

	public boolean sendLocation(String id){
		String sql = String.format("SELECT latitude FROM qc_location WHERE qc_location.user_idx = (SELECT idx FROM qc_user WHERE qc_user.user_id = '%s')", id);
		
	}
	
	
	
	
	
	
}
