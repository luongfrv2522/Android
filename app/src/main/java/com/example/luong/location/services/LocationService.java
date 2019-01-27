package com.example.luong.location.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import com.example.luong.location.dataStorage.UserConnected;
import com.example.luong.location.entities.Message;
import com.example.luong.location.entities.User;
import com.example.luong.location.models.LogHub;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;

public class LocationService extends Service{
    private static List<Message> logList = new ArrayList<>();
    private User userSession;

    private LogHub logHub;
    private Handler handlerHub;
    private Handler handler;

    private LocationManager locationManager;
    private MyLocationListener listener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "onCreate_Service", Toast.LENGTH_SHORT).show();
        Log.d("signalr", "In startSignalR()");
        //startSignalR();
        userSession = UserConnected.getUserSession(this);
        String ConnectionQeryString = "userId="+userSession.getUserId()
                    +"&deviceId="+userSession.getDeviceId();
        logHub = new LogHub(ConnectionQeryString, new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {

            }
        });
        logHub.startHub();
        logHub.addOn2("addNewMessageToPage", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(String s, String s2) {
                Log.d("signalr",s +" - "+ s2);
            }
        });
        logHub.addInvoke("send", "Luongk","signalr: Invoike truc tiep");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerHub = new Handler();
        Toast.makeText(this, "onStartCommand_Service", Toast.LENGTH_SHORT).show();
        //region listener
        listener = new MyLocationListener();
        Log.d("signalr", "locationListener Done");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Log.d("signalr", "locationManager Done");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("signalr", "No permission");
        }else{
            Log.d("signalr", "No else permission");
        }
        if(locationManager != null){
            Log.d("signalr", "locationManager Not null");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
        }
        //endregion
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        if(handler!= null) handler.removeCallbacksAndMessages(null);
        if(handlerHub!= null) handlerHub.removeCallbacksAndMessages(null);
        if(logHub.isStarted()) logHub.disconnectHub();
        if(locationManager != null) locationManager.removeUpdates(listener);
        Log.d("TestService","Run onDestroy");
        Toast.makeText(this, "onDestroy_Service", Toast.LENGTH_SHORT).show();
    }
    public class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            //
            int persent = 100000000;
            //
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("lat", location.getLatitude());
            jsonObject.addProperty("lng", location.getLongitude());
            jsonObject.addProperty("created", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));
            Message message = new Message();
            message.lat = (double) Math.round(location.getLatitude() * persent) / persent;
            message.lng = (double) Math.round(location.getLongitude() * persent) / persent;
            message.created = new Date();
            //
            logList.add(message);
            Intent i = new Intent("location_update");
            i.putExtra("coordinates", jsonObject.toString());
            sendBroadcast(i);
            //
            if(logList.size() >= 5){
                logHub.addInvoke("LogLocation", userSession.getUserId(), userSession.getDeviceId(), logList);
                logList.clear();

            }
            Log.d("signalr", userSession.getUserId() + ":" + jsonObject.toString());
            //logHub.addInvoke("Send", userSession.getUserId(), userSession.getDeviceId() + ":" + new Gson().toJson(logList));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intentT = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intentT.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentT);
        }
    }
}
