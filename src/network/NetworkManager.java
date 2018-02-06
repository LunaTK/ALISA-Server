/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Config;

/**
 *
 * @author LunaTK
 */
public class NetworkManager extends Thread {
    private static NetworkManager instance;
    
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    
    private NetworkManager(){}
    
    public static NetworkManager getInstance(){
        if(instance == null) instance = new NetworkManager();
        return instance;
    }
    
    public void removeRequest(Request c){
        requestList.remove(c);
    }
    
    public void startServer(){
        isRunning = true;
        this.start();
    }
    
    public void stopServer(){
        isRunning = false;
        for(Request c : requestList){
            c.disconnect();
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.err.println("Server Socket already closed");
        }
    }
   
    private void acceptRequest(){
        Socket soc;
        Request request;
        try {
            soc = serverSocket.accept();
        } catch (IOException ex) {
            System.err.println("Client accept failed");
            return;
        }
        request = new Request(soc);
        requestList.add(request);
        request.start();
    }
    
    @Override
    public void run(){
        try {
            serverSocket = new ServerSocket(Config.SERVER_PORT);
        } catch (IOException ex) {
            System.err.println("Port already in use.");
            return;
        }
        
        while(isRunning){
//            System.out.println("Waiting for request...");
            acceptRequest();
        }
    }
}
