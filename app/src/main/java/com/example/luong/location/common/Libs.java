package com.example.luong.location.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

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
    }
}
