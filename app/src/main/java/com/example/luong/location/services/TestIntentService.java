package com.example.luong.location.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class TestIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Toast.makeText(this, "onHandleIntent_Intent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "onCreate_Intent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand_Intent", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "onDestroy_Intent", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
