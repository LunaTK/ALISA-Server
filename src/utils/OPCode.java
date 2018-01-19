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
    public static final byte OK = 0x00, NOK = 0x01, REQ_LOGIN = 0x02, REQ_REGISTER = 0x03, SENSOR_DATA = 0x04;
    public static final byte ERR = (byte)0xF0, ERR_USER_EXIST = (byte)0xF1;
}
