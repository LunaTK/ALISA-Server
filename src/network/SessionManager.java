/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Config;

/**
 *
 * @author LunaTK
 */
public class SessionManager extends Thread{
    
    private static SessionManager instance;
    
    private boolean isRunning = false;
    
    private HashMap<Integer,Integer> sessions;//Key : Session Id, Value : User idx
    private HashMap<Integer,Long> sessions_timestamp;//Key : Session-Id, Value : Last Timestamp
    
    
    
    private SessionManager(){
        sessions = new HashMap();
        sessions_timestamp = new HashMap();
    }
    
    public static SessionManager getInstance(){
        if(instance==null) instance = new SessionManager();
        return instance;
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        isRunning = true; 
        while(isRunning){
            try {
                Thread.sleep(Config.SESSION_TIMEOUT);
            } catch (InterruptedException ex) {
                Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            clearTimeoutedSessions();
        }
    }
    
    private void clearTimeoutedSessions(){
//        System.out.println("clearTimeoutedSessions");
        long currentTime = System.currentTimeMillis();
        for(int sessionId : sessions_timestamp.keySet()){
//            System.out.println("sessionId : " + sessionId + ", timestamp : " + sessions_timestamp.get(sessionId) + ", current time : " + currentTime);
            if(currentTime - sessions_timestamp.get(sessionId) > Config.SESSION_TIMEOUT){
                System.err.println("Session Timeout : " + sessionId);
                removeSession(sessionId);
            }
        }
    }
    
    public int createSession(String id, int idx){
        int sessionId;
        long startTime = System.currentTimeMillis();
        do{
            if(System.currentTimeMillis() - startTime>Config.CREATE_SESSION_TIMEOUT) return -1;

            sessionId = Math.abs((id+ System.currentTimeMillis()).hashCode());
            try{
                Thread.sleep(10);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        } while(sessions.containsKey(sessionId));
        
        sessions.put(sessionId, idx);
        sessions_timestamp.put(sessionId, System.currentTimeMillis());
        
        System.out.println("Session Created, Session list : " + sessions);
        
        return sessionId;
    }
    
    public void removeSession(int sessionId){
        if(!isAlive(sessionId)) return;
        sessions.remove(sessionId);
        sessions_timestamp.remove(sessionId);
        System.out.println("Session Removed, Session list : " + sessions);
    }
    
    public int getUserIdx(int sessionId){
        Integer idx = sessions.get(sessionId);
        if(idx==null) return -1;
        else return idx;
    }
    
    public boolean isAlive(int sessionId){
        return sessions.get(sessionId)==null?false:true;
    }
    
    public void updateTimestamp(int sessionId){
        if(!isAlive(sessionId))return;
        sessions_timestamp.put(sessionId, System.currentTimeMillis());
    }
    
    public void stopRunning(){
        isRunning = false;
        sessions.clear();
        sessions_timestamp.clear();
        instance = new SessionManager();
    }
    
    
}