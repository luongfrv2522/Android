package com.example.luong.location.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import com.example.luong.location.common.GlobalConfig;
import com.example.luong.location.dataStorage.UserConnected;
import com.example.luong.location.models.LogHub;
import com.google.gson.JsonObject;

import java.net.ConnectException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class LocationService extends Service{
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
        logHub = new LogHub();
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
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(logHub.isStarted()){
                    boolean flagA = logHub.addInvoke("Send", "luongk","signalr: Invoike truc tiep");
                    Log.d("signalr", "addInvoke: " + flagA);
                }
                else{
                    if(logHub.startHub()){
                        logHub.addInvoke("Send", "luongk","signalr: Invoike sau khi khoi dong lai Hub");
                        Log.d("signalr", "signalr: Invoike sau khi khoi dong lai Hub");
                    }else {
                        Log.e("signalr", "Khong khoi dong lai duocj");
                    }
                }
                Log.e("signalr", "Runable");
                handlerHub.postDelayed(this,3000);
            }
        };
        handlerHub.post(runnable);
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
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Lat", location.getLatitude());
            jsonObject.addProperty("Lng", location.getLongitude());
            //
            Intent i = new Intent("location_update");
            i.putExtra("coordinates", jsonObject.toString());
            sendBroadcast(i);
            //
            Log.d("signalr", UserConnected.getUserSession(LocationService.this).getUserId() + ":" + jsonObject.toString());
            //hubProxy.invoke("Send", UserConnected.getUserSession(LocationService.this).getUserId(), jsonObject.toString());

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
