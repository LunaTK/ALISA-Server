/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import database.DBManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    private int userIdx = -1;
    
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
        int sessionId;
        isRunning=true;
        while(isRunning){
            try {
//                System.err.println("Request Session Created (" + socket.getInetAddress().getHostAddress() + ")");
                opcode = dis.readByte();
                sessionId = dis.readInt();
                userIdx = SessionManager.getInstance().getUserIdx(sessionId);
                SessionManager.getInstance().updateTimestamp(sessionId);
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
            case OPCode.NEW_SENSOR_DATA:
                handleSensorData();
                break;
            case OPCode.NEW_EVENT_DATA:
                handleEventData();
                break;
        }
    }
    
    public void disconnect(){
        isRunning = false;
        RequestManager.getInstance().removeRequest(this);
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
        int sessionId = -1;
        id = dis.readUTF();
        pwd = dis.readUTF();
        userIdx = DBManager.getInstance().authUser(id,pwd);
        if(userIdx>=0){ // 로그인 성공
            System.out.println("Login Success for user : " + id);
            sessionId = SessionManager.getInstance().createSession(id, userIdx);
        } else {
            System.out.println("Login Failed for user : " + id);
        }
        dos.writeInt(sessionId);
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
        long hall, accX, accY, accZ, gyroX, gyroY, gyroZ, geoX, geoY, geoZ;
        double temp;
        
        data = dis.readUTF();
//        System.out.println(data);
        
        String[] split = data.split("\n");

        hall = Long.parseLong(split[0].split(" : ")[1]);
        temp = Double.parseDouble(split[1].split(" : ")[1]);
        accX = Long.parseLong(split[2].split(" : ")[1]);
        accY = Long.parseLong(split[3].split(" : ")[1]);
        accZ = Long.parseLong(split[4].split(" : ")[1]);
        gyroX = Long.parseLong(split[5].split(" : ")[1]);
        gyroY = Long.parseLong(split[6].split(" : ")[1]);
        gyroZ = Long.parseLong(split[7].split(" : ")[1]);
        geoX = Long.parseLong(split[8].split(" : ")[1]);
        geoY = Long.parseLong(split[9].split(" : ")[1]);
        geoZ = Long.parseLong(split[10].split(" : ")[1]);
        
//        DBManager.getInstance().newSensorData(hall, temp, accX, accY, accZ, gyroX, gyroY, gyroZ, geoX, geoY, geoZ);
        
        dos.writeByte(OPCode.OK);
        dos.flush();
    }
    
    private void handleEventData() throws IOException {
        int eventType;
        double longtitude, latitude;
        
        eventType = dis.readInt();
        longtitude = dis.readDouble();
        latitude = dis.readDouble();
        
        if(DBManager.getInstance().newEventData(userIdx, 1, eventType, longtitude, latitude)==true){
            dos.writeByte(OPCode.OK);
        } else {
            dos.writeByte(OPCode.NOK);
        }
        dos.flush();
        System.out.println("handle event data : " + eventType + ", " + userIdx);
    }
    
}