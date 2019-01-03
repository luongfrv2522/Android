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
import com.google.gson.JsonObject;

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
    private HubConnection hubConnection;
    private HubProxy hubProxy;
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
        Log.d("signalr", "Out startSignalR()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand_Service", Toast.LENGTH_SHORT).show();
        //region listener
        listener = new MyLocationListener(LocationManager.NETWORK_PROVIDER);
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);
        }
        //endregion
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(handler!= null) handler.removeCallbacksAndMessages(null);
        if(handlerHub!= null) handlerHub.removeCallbacksAndMessages(null);
        if(hubConnection != null) hubConnection.disconnect();
        if(locationManager != null) locationManager.removeUpdates(listener);
        Log.d("TestService","Run onDestroy");
        Toast.makeText(this, "onDestroy_Service", Toast.LENGTH_SHORT).show();
    }

    protected void startSignalR(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        hubConnection = new HubConnection(GlobalConfig.ServerString.BASE_HUB_URL);
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
                Log.d("signalr","hubConnection connected!");
            }
        });
        hubConnection.setCredentials(new Credentials() {
            @Override
            public void prepareRequest(Request request) {

            }
        });
        hubProxy = hubConnection.createHubProxy(GlobalConfig.ServerString.CHAT_HUB_NAME);
        Log.e("signalr","Begin startSignalR()");
        ClientTransport clientTransport = new ServerSentEventsTransport(hubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        }catch (InterruptedException | ExecutionException e){
            Log.e("signalr",e.getMessage());
            return;
        }
        Log.d("signalr","Mid startSignalR()");
        //#region On messages
        hubProxy.on("setLocation", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(String s, String s2) {
                Log.d("signalr",s+" : "+s2);
            }
        },String.class,String.class);
        //hubProxy.invoke("Send", "android","Oke");
        //#endregion
        Log.d("signalr","End startSignalR()");
    }
    public class MyLocationListener implements LocationListener{
        Location mLastLocation;

        MyLocationListener(String provider)
        {
            Log.e("signalr", "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
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
