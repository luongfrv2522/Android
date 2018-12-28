package com.example.luong.location.services;

import android.Manifest;
import android.annotation.SuppressLint;
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


import com.example.luong.location.common.GlobalConfig;
import com.example.luong.location.dataStorage.UserConnected;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class LocationService extends Service {
    private HubConnection hubConnection;
    private HubProxy hubProxy;
    private Handler handlerHub;
    private Handler handler;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint({"WrongConstant"})
    @Override
    public void onCreate() {
        Toast.makeText(this, "onCreate_Service", Toast.LENGTH_SHORT).show();
        //onStartCommand(null,START_STICKY,0);
        startSignalR();

    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand_Service", Toast.LENGTH_SHORT).show();
        //region listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Lat", location.getLatitude());
                jsonObject.addProperty("Lng", location.getLongitude());
                //
                Intent i = new Intent("location_update");
                i.putExtra("coordinates",jsonObject.toString());
                sendBroadcast(i);
                //
                //Log.d("signalr",jsonObject.toString());
                hubProxy.invoke("Send", UserConnected.getUserSession(LocationService.this).getUserId(), jsonObject.toString());
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
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        //endregion
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(handler!= null) handler.removeCallbacksAndMessages(null);
        if(handlerHub!= null) handlerHub.removeCallbacksAndMessages(null);
        hubConnection.disconnect();
        if(locationManager != null) locationManager.removeUpdates(locationListener);

        Log.d("TestService","Run onDestroy");
        Toast.makeText(this, "onDestroy_Service", Toast.LENGTH_SHORT).show();
    }

    private void startSignalR(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        hubConnection = new HubConnection(GlobalConfig.ServerString.BASE_HUB_URL);
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
                Log.d("signalr","hubConnection connected!");
            }
        });
        hubProxy = hubConnection.createHubProxy(GlobalConfig.ServerString.CHAT_HUB_NAME);

        ClientTransport clientTransport = new ServerSentEventsTransport(hubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        }catch (InterruptedException | ExecutionException e){
            Log.e("signalr",e.getMessage());
            return;
        }

        //#region On messages
        hubProxy.on("setLocation", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(String s, String s2) {
                Log.d("signalr",s+" : "+s2);
            }
        },String.class,String.class);
        //#endregion

    }
}
