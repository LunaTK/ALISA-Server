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
    public static final byte ERR = (byte)0xFF, OK = 0x01, NOK = 0x02;
    public static final byte REQ_LOGIN = 0x03, REP_LOGIN = 0x04, REQ_REGISTER = 0x05, REP_REGISTER = 0x06, REQ_LOGOUT = 0x23, REP_LOGOUT = 0x24;
    public static final byte 
    		//센서데이터
    		NEW_SENSOR_DATA = 0x07, REQ_SENSOR_HISTORY = 0x08, REP_SENSOR_HISTORY = 0x09,
    		
            //이벤트발생 데이터
            UPDATE_AC = 0x10, REQ_AC = 0x11, REP_AC = 0x12,
            UPDATE_DC = 0x13, REQ_DC = 0x13, REP_DC = 0x14,
            REQ_AC_STATISTIC = 0x15, REP_AC_STATISTIC = 0x16,
            REQ_DC_STATISTIC = 0x17, REP_DC_STATISTIC = 0x18,
            UPDATE_FILTER_THR = 0x19, REQ_FILTER_THR = 0x1a, REP_FILTER_THR = 0x1b,
            REQ_FILTER_STATISTIC = 0x1c, REP_FILTER_STATISTIC = 0x1d,
            
            //부품별 주행거리 데이터
            UPDATE_COMP_MILEAGE = 0x1e, REQ_COMP_MILEAGE = 0x1f, REP_COMP_MILEAGE = 0x20,
            REQ_COMP_STATISTIC = 0x21, REP_COMP_STATISTIC = 0x22,
            
            NEW_EVENT_DATA = 0x25, REQ_EVENT_DATA = 0x26, REP_EVENT_DATA = 0x27,
    
    		//디바이스 데이터
    		NEW_DEVICE_DATA = 0x28, REQ_DEVICE_DATA = 0x29, REP_DEVICE_DATA = 0x2a, 
    
    		//판매자 정보 데이터
    		NEW_AGENCY_DATA = 0x2b, REQ_AGENCY_DATA = 0x2c, REP_AGENCY_DATA = 0x2d,
    
    		//판매정보 데이터
    		NEW_SALEINFO_DATA = 0X2e, REQ_SALEINFO_DATA = 0x2f, REP_SALEINFO_DATA = 0x30,
    		
    		//회원 정보 데이터
    		NEW_USER_DATA = 0x31, REQ_USER_DATA = 0x32, REP_USER_DATA = 0x33,
    		
    		//위치데이터
    	    NEW_LOCATION_DATA = 0x0a, REQ_LOCATION_HISTORY = 0x0b, REP_LOCATION_HISTORY = 0x0c,         
    	           
    		//차량 이용 데이터
    		NEW_DRIVE_DATA = 0x34, REQ_DRIVE_DATA = 0x35, REP_DRIVE_DATA = 0x36,
    		
    		//주행거리데이터
    		UPDATE_MILEAGE_DATA = 0x0d, REQ_MILEAGE_DATA = 0x0e, REP_MILEAGE_DATA = 0x0f,
    
    		//부품종류 데이터
    		NEW_COMPONENT_DATA = 0x37, REQ_COMPONENT_DATA = 0x38, REP_COMPONENT_DATA = 0x39;
    
    public static final byte ERR_USER_EXIST = (byte)0xF1;
}
