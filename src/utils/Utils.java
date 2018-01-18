/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LunaTK
 */
public class Utils {
    public byte[] getSHA512(String str){
        byte[] encodedhash = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encodedhash;
    }
}
