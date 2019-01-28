package com.example.luong.location;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.luong.location.models.LogHub;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;

public class TestSignalrActivity extends AppCompatActivity {
    Button button;
    LogHub logHub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_signalr);
        button = findViewById(R.id.button);
        logHub = new LogHub();
        try {
            logHub.startHub();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        logHub.addOn2("addNewMessageToPage", new SubscriptionHandler2<String, String>() {
            @Override
            public void run(String s, String s2) {
            Log.d("signalr",s + " + " + s2);
            }
        });
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logHub.addInvoke("Send","android","Oke");
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(runnable);

    }

}
