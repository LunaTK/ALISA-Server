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
public class Client extends Thread {
    
    private Socket socket;
    private boolean isRunning = false;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    private byte[] buffer;
    
    public Client(Socket soc){
        this.socket = soc;
        try {
            bos = new BufferedOutputStream(soc.getOutputStream());
            bis = new BufferedInputStream(soc.getInputStream());
        } catch (IOException ex) {
            System.err.println("Buffer Stream for Client Creation Failed");
        }
    }
    
    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        
        buffer = new byte[Config.BUFF_SIZE];
        isRunning=true;
        while(isRunning){
            try {
                bis.read(buffer);
                handlePacket();
            } catch (IOException ex) {
                System.err.println("reading from client failed");
            }
        }
    }
    
    private void handlePacket(){
//        System.out.println("Packet received : " + Arrays.toString(buffer));
        switch(buffer[0]){
            case OPCode.REQ_LOGIN:
                handleLoginRequest();
                break;
            case OPCode.REQ_REGISTER:
                break;
        }
    }
    
    public void sendPacket(byte[] packet){
        try {
            bos.write(packet);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendPacket(byte b){
        try {
            bos.write(b);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect(){
        isRunning = false;
        if(socket==null) return;
        try {
            bos.close();
            bis.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("Client socket close failed");
        }
    }

    private void handleLoginRequest() {
        String id, pwd;
        id = new String(Arrays.copyOfRange(buffer, 1, Config.MAX_ID_LEN)); 
        pwd = new String(Arrays.copyOfRange(buffer, Config.MAX_ID_LEN+1, Config.MAX_ID_LEN+64)); //sha-512 는 64byte
        
        if(DBManager.getInstance().authUser(id,pwd)){ // 로그인 성공
            sendPacket(OPCode.OK);
        } else {
            sendPacket(OPCode.NOK);
        }
    }
    
}
