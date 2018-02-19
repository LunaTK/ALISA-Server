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
        String sql = String.format("INSERT INTO qc_event_history (user_idx, device_idx, event_type, longtitude, latitude) values (%d,%d,%d,%f,%f)", userIdx, deviceIdx, eventType, lon, lat);
        try {
            if(st.executeUpdate(sql)>0) return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeStatement(st);
        return false;
    }
}