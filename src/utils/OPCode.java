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
    //클라이언트가 서버에게 보내는 OPCode는 0x0로 시작(ACK,NACK은 예외)
    public static final byte OK = 0x00, NOK = 0x01, REQ_LOGIN = 0x02, REQ_REGISTER = 0x03;
    //서버가 클라이언트에게 보내는 OPCode는 0xF로 시작
}
