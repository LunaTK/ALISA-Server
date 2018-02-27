/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author LunaTK
 */
public class OPCode {
	//로그인, 에러 관련 서비스
    public static final byte OK = 0x00, NOK = 0x01, REQ_LOGIN = 0x02, REQ_REGISTER = 0x03;
    public static final byte ERR = (byte)0xF0, ERR_USER_EXIST = (byte)0xF1;
    
    //센서데이터 제공,저장 서비스
    public static final byte UPDATE_SENSOR_DATA = 0x04, UPDATE_DISTANCE = 0x05, REQ_DISTANCE = 0x06; 
    		
    //위치정보 제공 서비스		
    public static final byte UPDATE_LOCATION = 0x07, REQ_LOCATION = 0x08;
    
    //급가속,급감속 경고서비스
    public static final byte CHANGE_AC_THRES = 0x09, CHANGE_DC_THRES = 0x0A, UPDATE_AC_THRES = 0x0B, UPDATE_DC_THRES = 0x0C, REQ_AC = 0x0D, REQ_DC = 0x0E;
    
    //필터상태 경고서비스
    public static final byte REQ_WARNING_THRES = 0x10, CHANGE_WARNING_THRES = 0x11, UPDATE_WARNING_DATA = 0x12;   
    
    //부품별 주행거리 초과 경고 서비스
    public static final byte DEVICE_DISTANCE_THRES = 0x13, UPDATE_DEVICE_DISTANCE = 0x14, REQ_DEVICE_DISTANCE = 0x15;
   
}
