package com.example.luong.location.models;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.luong.location.common.GlobalConfig;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.ConnectionState;
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
import microsoft.aspnet.signalr.client.transport.LongPollingTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class LogHub {

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!handlerDisconnect && getCurentState() == ConnectionState.Disconnected){
                Log.d("signalr", "closed Step 2");
                try {
                    startHub();
                } catch (ExecutionException | InterruptedException e) {
                    handler.postDelayed(this, 5000);
                }
            }
        }
    };

    private  HubConnection hubConnection;
    private  HubProxy hubProxy;
    private  ClientTransport clientTransport;
    private boolean handlerDisconnect = false;
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
                handlerDisconnect = false;
                Log.d("signalr","hubConnection connected!");
            }
        });
        hubProxy = hubConnection.createHubProxy(GlobalConfig.ServerString.CHAT_HUB_NAME);
        Log.d("signalr","Begin startSignalR()");
        clientTransport = new ServerSentEventsTransport(hubConnection.getLogger());
        Log.d("signalr","Begin clientTransport()");
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
                hubConnection.stateChanged(new StateChangedCallback() {
                    @Override
                    public void stateChanged(ConnectionState oldState, ConnectionState newState) {
                        Log.d("signalr","connectionState: " + oldState + " - connectionState1: " + newState);
                        Log.d("signalr","stateChanged - state: " + hubConnection .getState());
                    }
                });
                Log.d("signalr","hubConnection connected!");
            }
        });
        hubConnection.closed(new Runnable() {
            @Override
            public void run() {
                Log.d("signalr", "closed Step 1");
                handler.postDelayed(runnable, 5000);
            }
        });
        hubProxy = hubConnection.createHubProxy(GlobalConfig.ServerString.CHAT_HUB_NAME);
        Log.d("signalr","Begin startSignalR()");
        clientTransport = new LongPollingTransport(hubConnection.getLogger());
        //#region test runable
        //#endregion
    }
    public ConnectionState getCurentState(){
        return hubConnection.getState();
    }
    public void connectionStateChanged(StateChangedCallback stateChangedCallback){
        hubConnection.stateChanged(stateChangedCallback);
    }
    public void startHub() throws ExecutionException, InterruptedException {
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);
            signalRFuture.get();
    }
    public void connected(Runnable haRunnable){
        hubConnection.connected(haRunnable);
    }

    public void addOn2(String name, SubscriptionHandler2<String, String> subscriptionHandler2){
        hubProxy.on(name, subscriptionHandler2, String.class,String.class);
    }
    public boolean addInvoke(String name, Object... params){

        try {
            SignalRFuture<Void> rs = null;
            if(hubConnection.getState() == ConnectionState.Connected){
                rs = hubProxy.invoke(name,params);
            }
            if (rs != null) {
                rs.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.d("signalr","addInvoke: Không invoke được" );
            return false;
        }
        return true;
    }

    public void disconnectHub(){
        handlerDisconnect = true;
        if(hubConnection != null) hubConnection.disconnect();
    }
}