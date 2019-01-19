package com.example.luong.location.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
<<<<<<< HEAD

public class Libs {
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    public static String[] getAndroidHardWare(){
        String androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        String androidVersion = android.os.Build.VERSION.RELEASE;
        String androidBrand = android.os.Build.BRAND;
        String androidManufacturer = android.os.Build.MANUFACTURER;
        String androidModel = android.os.Build.MODEL;
        return new String[]{androidSDK, androidVersion, androidBrand, androidManufacturer, androidModel};
=======
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class Libs {
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
>>>>>>> master
    }
}
