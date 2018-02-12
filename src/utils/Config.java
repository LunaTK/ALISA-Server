package utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LunaTK
 */
public class Config {
    //서버설정
    public static final int BUFF_SIZE = 1024;
    public static final String DB_IP = "115.145.177.64", DB_SCHEME_NAME="qualcdb",DB_USER_ID="qualcuser",DB_USER_PWD="gkdl28$*";
    public static final int DB_PORT = 4501, SERVER_PORT = 9949;
    
    //로그인
    public static final int MAX_ID_LEN = 255, MAX_PWD_LEN = 255;
    
    //네트워크 설정
    public static final int SESSION_TIMEOUT = 10* 60 * 1000, CREATE_SESSION_TIMEOUT = 3*1000;
}
