/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import database.DBManager;
import utils.Config;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.OPCode;

/**
 *
 * @author LunaTK
 */
public class Request extends Thread {
    
    private Socket socket;
    private boolean isRunning = false;
    private DataOutputStream dos;
    private DataInputStream dis;
    
    public Request(Socket soc){
        this.socket = soc;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(soc.getOutputStream()));
            dis = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
        } catch (IOException ex) {
            System.err.println("Buffer Stream for Client Creation Failed");
        }
    }
    
    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        byte opcode;
        isRunning=true;
        while(isRunning){
            try {
//                System.err.println("Request Session Created (" + socket.getInetAddress().getHostAddress() + ")");
                opcode = dis.readByte();
                if(opcode<0) {
                    isRunning = false;
                } else {
                    handlePacket(opcode);
                }
            } catch (IOException ex) {
//                System.err.println("Request Session Closed");
                disconnect();
            }
        }
    }
    
    private void handlePacket(byte opcode) throws IOException{
        switch(opcode){
            case OPCode.REQ_LOGIN:
                handleLoginRequest();
                break;
            case OPCode.REQ_REGISTER:
                handleRegisterRequest();
                break;
            case OPCode.SENSOR_DATA:
                handleSensorData();
                break;
        }
    }
    
    public void disconnect(){
        isRunning = false;
        NetworkManager.getInstance().removeRequest(this);
        if(socket==null) return;
        try {
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("Client socket close failed");
        }
    }

    private void handleLoginRequest() throws IOException {
        String id, pwd;
        id = dis.readUTF();
        pwd = dis.readUTF();
        if(DBManager.getInstance().authUser(id,pwd)){ // 로그인 성공
            dos.writeByte(OPCode.OK);
            System.out.println("Login Success for user : " + id);
        } else {
            dos.writeByte(OPCode.NOK);
            System.out.println("Login Failed for user : " + id);
        }
        dos.flush();
    }
    
    private void handleRegisterRequest() throws IOException {
        String id, pwd;
        id = dis.readUTF();
        pwd = dis.readUTF();
        if(DBManager.getInstance().addUser(id,pwd)){ // 로그인 성공
            dos.writeByte(OPCode.OK);
            System.out.println("Register Success for user : " + id);
        } else {
            dos.writeByte(OPCode.NOK);
            System.out.println("Register Failed for user : " + id);
        }
        dos.flush();
    }
    
    private void handleSensorData() throws IOException {
        System.out.println("Sensor Data (Current Time : " + System.currentTimeMillis() + ")");
        String data = null;
        data = dis.readUTF();
//        System.out.println(data);
        dos.writeByte(OPCode.OK);
        dos.flush();
    }
    
}
