package com.example.luong.location.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import java.util.Date;

import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class LocationService extends Service {
    private HubConnection hubConnection;
    private HubProxy hubProxy;
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
                Log.d("TestService","Run Background");
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
        Log.d("TestService","Run onDestroy");
        Toast.makeText(this, "onDestroy_Service", Toast.LENGTH_SHORT).show();
    }

}
