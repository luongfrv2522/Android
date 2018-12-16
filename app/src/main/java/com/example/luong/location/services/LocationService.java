package com.example.luong.location.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import java.util.Date;

public class LocationService extends Service {
    Handler handler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        Toast.makeText(this, "onCreate_Service", Toast.LENGTH_SHORT).show();
        //onStartCommand(null,START_STICKY,0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand_Service", Toast.LENGTH_SHORT).show();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String date = new Date().toString();
                Log.d("TestService",date);
                handler.postDelayed(this,3000);
            }
        },3000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(handler!= null){
            handler.removeCallbacksAndMessages(null);
        }
        Toast.makeText(this, "onDestroy_Service", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

}
