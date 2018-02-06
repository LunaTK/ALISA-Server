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
//        System.out.println("id : " + id );
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
    
}
