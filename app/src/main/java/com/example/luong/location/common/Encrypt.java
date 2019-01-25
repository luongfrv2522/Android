package com.example.luong.location.common;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encrypt {
    private static String keyCap = "DeathKeyBase";
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String base64Encode(String plainText){
        String rs = null;
        plainText = plainText + keyCap;
        // encode with padding
        try {
            rs = Base64.getEncoder().encodeToString(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rs;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String base64Decode(String base64EncodedData){
        String rs = null;
        try {
            rs = new String(Base64.getDecoder().decode(base64EncodedData), "UTF-8");
            return rs.substring(0, rs.length() - keyCap.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public static String baseMd5Encode(String str){
        str = str + keyCap;
        String result = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInteger = new BigInteger(1,digest.digest());
            result = bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
