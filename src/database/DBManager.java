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
    private String sql;
    private static DBManager instance;
    private DBManager(){}
    
    public static DBManager getInstance(){
        if(instance==null) instance = new DBManager();
        return instance;
    }
    
    public void disconnectDB(){
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
            System.out.println("DB Connected!");
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    private Statement createStatement(){
     
        try {
            return connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private void closeStatement(Statement st){
        if(st!=null){
            try{
                if(!st.isClosed()) st.close();  
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    private void closeResultSet(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public int authUser(String id, String pwd){
        Statement st = createStatement();
//        System.out.println("id : " + id );
        ResultSet rs = null;
        String sql = String.format("SELECT idx FROM qc_user where user_id='%s' and user_password='%s'", id,pwd);
        try {
            rs = st.executeQuery(sql);
            if(rs.first()) return rs.getInt("idx");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeResultSet(rs);
        closeStatement(st);
        return -1;
    }
    
    public boolean addUser(String id, String pwd){
        Statement st = createStatement();
        String sql = String.format("INSERT INTO qc_user (user_id, user_password) values ('%s','%s')", id, pwd);
        try {
            if(st.executeUpdate(sql)>0) return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeStatement(st);
        return false;
    }
    
    public boolean newSensorData(long hall, double temp, long accX, long accY, long accZ, long gyroX, long gyroY, long gyroZ, long geoX, long geoY, long geoZ){
       
        Statement st = createStatement();
        String sql = String.format("INSERT INTO qc_sensor_data (user_idx, hall, temp, accX, accY, accZ, gyroX, gyroY, gyroZ, geoX, geoY, geoZ) "
                + "values (%d, %d, %f, %d, %d, %d, %d, %d, %d, %d, %d, %d)",0, hall, temp, accX, accY, accZ, gyroX, gyroY, gyroZ, geoX, geoY, geoZ);
        try {
            if(st.executeUpdate(sql)>0) return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        closeStatement(st);
        return false;
    }
    
    public boolean newEventData(int userIdx, int deviceIdx, int eventType, double lon, double lat){    
        Statement st = createStatement();
        String sql = String.format("INSERT INTO qc_event_history (user_idx, device_idx, event_type, longtitude, latitude) values (%d, %d, %d, %lf, %lf)", userIdx, deviceIdx, eventType, lon, lat);
        try {
            if(st.executeUpdate(sql)>0) return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeStatement(st);
        return false;
    }
    

    //여기부터
	public boolean newDeviceData(String deviceId, String deviceType, String macAddress, String time, String examiner){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_device (device_id, device_type, ble_mac_add, manu_date ,examiner) values ('%s', '%s', '%s', '%s', '%s')", deviceId, deviceType, macAddress, time, examiner);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newAgencyData(String agenName, String agenPnumber, String agenPlace, String agenId, String agenTime){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_seller (agency_name, agency_pnumber, agency_place, agency_id, agency_date) values ('%s', '%s', '%s', '%s', '%s')", agenName, agenPnumber, agenPlace, agenId, agenTime);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newSaleInfoData(String userId, String agenId, String deviceId, String date, int contract){
		Statement st = createStatement();
		ResultSet rs = null;
		String sql_useridx = String.format("SELECT idx FROM qc_user WHERE userid = '%s'", userId);
		String sql_agencyidx = String.format("SELECT idx FROM qc_agency WHERE agency_id = '%s'", agenId);
		int user_idx, agency_idx;
		try {
			rs = st.executeQuery(sql_useridx);
			user_idx = rs.getInt(0);
			rs = st.executeQuery(sql_agencyidx);
			agency_idx = rs.getInt(0);
		} catch (SQLException e1) {
			return false;
		}
		String sql = String.format("INSERT INTO qc_sales_information (user_idx, agency_idx, device_id, date, contract_type) values (%d, %d, '%s', '%s', %d)", user_idx, agency_idx, deviceId, date, contract);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		closeStatement(st);
		closeResultSet(rs);
		return false;
	}
	
	public boolean newUserData(String userId, String userPw, String deviceId, Byte userLevel, String email, String phone, String birth, Byte gender, String place, String osVersion, Byte agreeLi, Byte agreePr, Byte agreeMa, String regdt){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_user (user_id, user_password, user_device_idx, user_level, email, phone, birth, gender, place, osversion, agree_license, agree_protection, agree_marketing, regdt) values ('%s', '%s', '%s', %c, '%s', '%s', '%s', %c, '%s', '%s', %c, %c, %c, '%s')", userId, userPw, deviceId, userLevel, email, phone, birth, gender, place, osVersion, agreeLi, agreePr, agreeMa, regdt);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newLocationData(int userIdx, double lat, double lon, String time){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_gps_history (user_idx, latitude, longitude, time) values (%d, %lf, %lf, '%s')", userIdx, lat, lon, time);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newDriveData(int userIdx, String sensorId, String start, String finish){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_drive_history (user_idx, sensor_id, start_time, finish_time) values (%d, '%s', '%s', '%s')", userIdx, sensorId, start, finish);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException e) {
			return false;
		}
		closeStatement(st);
		return false;
	}
	
	public boolean updateMileageData(String userId, int mileage){
		Statement st = createStatement();
		String sql = String.format("UPDATE qc_mileage, qc_user SET qc_mileage.mileage = %d WHERE qc_mileage.user_idx = qc_user.idx and qc_user.userid='%s'", mileage, userId);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newEventData(int userIdx, int sensorIdx, double acc, double dcc, double accthres, double dccthres, String time, double lat, double lon, double mileage, double filterThres){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_event (user_idx, sensor_idx, acceleration, deceleration, ac_threshold, dc_threshold, time, latitude, longitude, mileage, filter_threshold) values (%d, %d, %lf, %lf, %lf, %lf, '%s', %lf, %lf, %lf, %lf)", userIdx, sensorIdx, acc, dcc, accthres, dccthres, time, lat, lon, mileage, filterThres);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newComponentData(String name){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_component_mileage (name) values ('%s')",name);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeStatement(st);
		return false;
	}
	
	public boolean newComponentMileageData(int userIdx, int componentIdx, int mileage){
		Statement st = createStatement();
		String sql = String.format("INSERT INTO qc_component_mileage (user_idx, component_idx, mileage) values (%d, %d, %d)", userIdx, componentIdx, mileage);
		try {
			if(st.executeUpdate(sql)>0) return true;
		} catch (SQLException ex) {
			Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeStatement(st);
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject sendDistance(String userId){
		Statement st = createStatement();
		ResultSet rs = null;
		JSONObject jsonObj = new JSONObject();
		String sql = String.format("SELECT mileage FROM qc_mileage WHERE qc_mileage.user_idx = (SELECT idx FROM qc_user WHERE qc_user.user_id = '%s')", userId);
		try {
			rs = st.executeQuery(sql);
			int mileage = rs.getInt(0);
			jsonObj.put("userid", userId);
			jsonObj.put("mileage", mileage);
			return jsonObj;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public JSONObject sendSubstPart(String userId){
		Statement st = createStatement();
		ResultSet rs = null;
		JSONObject jsonObj = new JSONObject();
		String sql = String.format("", userId);
		return jsonObj;
	}

	@SuppressWarnings("unchecked")
	public JSONObject sendLocationData(String userId){
		Statement st = createStatement();
		ResultSet rs = null;
		JSONObject jsonObj = new JSONObject();
		String sql = String.format("SELECT latitude, longitude, time FROM qc_location WHERE qc_location.user_idx = (SELECT idx FROM qc_user WHERE qc_user.user_id = '%s')", userId);
		try {
			rs = st.executeQuery(sql);
			double lat = rs.getDouble("latitude");
			double lon = rs.getDouble("longitude");
			jsonObj.put("latitude", lat);
			jsonObj.put("longitude", lon);
			return jsonObj;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
}