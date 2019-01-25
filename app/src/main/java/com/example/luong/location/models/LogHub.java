package com.example.luong.location.models;

import android.content.Intent;
import android.util.Log;

import com.example.luong.location.common.GlobalConfig;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.StateChangedCallback;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class LogHub {
    private  HubConnection hubConnection;
    private  HubProxy hubProxy;
    private  ClientTransport clientTransport;
    private SignalRFuture<Void> signalRFuture;
    private boolean started = false;
    public LogHub(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        String CONNECTION_QUERYSTRING = "userId=1&deviceId=3";

        hubConnection = new HubConnection(GlobalConfig.ServerString.BASE_HUB_URL, CONNECTION_QUERYSTRING, false, new Logger() {
            @Override
            public void log(String message, LogLevel level) {
                //System.out.println(message);
            }
        });
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
                Log.d("signalr","hubConnection connected!");
            }
        });
        hubProxy = hubConnection.createHubProxy(GlobalConfig.ServerString.CHAT_HUB_NAME);
        Log.d("signalr","Begin startSignalR()");
        clientTransport = new ServerSentEventsTransport(hubConnection.getLogger());
    }

    /**
     * Contructor với query string và logger
     * @param CONNECTION_QUERYSTRING "x=A&y=B"
     * @param logger callback logging
     */
    public LogHub(String CONNECTION_QUERYSTRING, Logger logger){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        hubConnection = new HubConnection(GlobalConfig.ServerString.BASE_HUB_URL, CONNECTION_QUERYSTRING, false, logger);
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
                Log.d("signalr","hubConnection connected!");
            }
        });
        hubProxy = hubConnection.createHubProxy(GlobalConfig.ServerString.CHAT_HUB_NAME);
        Log.d("signalr","Begin startSignalR()");
        clientTransport = new ServerSentEventsTransport(hubConnection.getLogger());
    }
    public boolean isStarted(){
        return started;
    }
    public boolean startHub(){
        try {
            signalRFuture  = hubConnection.start(clientTransport);
            signalRFuture.get(); 
            Log.d("signalr","state: " + hubConnection .getState());
            hubConnection.stateChanged(new StateChangedCallback() {
                @Override
                public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {
                    Log.d("signalr","connectionState: " + connectionState + " - connectionState1: " + connectionState1);
                    Log.d("signalr","stateChanged - state: " + hubConnection .getState());
                }
            });

        }catch ( InterruptedException | ExecutionException e){
            Log.e("signalr","startHub: " + e.getMessage());
            started = false;
            return false;
        }
        started = true;
        return true;
    }
    public void connected(Runnable haRunnable){
        hubConnection.connected(haRunnable);
    }
    public void addOn2(String name, SubscriptionHandler2<String, String> subscriptionHandler2){
        hubProxy.on(name, subscriptionHandler2, String.class,String.class);
    }
    public boolean addInvoke(String name, Object... params){
        SignalRFuture<Void> rs = hubProxy.invoke(name,params);
        try {
            rs.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
        return true;
    }

    public void disconnectHub(){
        started = false;
        if(hubConnection != null) hubConnection.disconnect();
    }
}
