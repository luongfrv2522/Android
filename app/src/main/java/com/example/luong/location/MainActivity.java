package com.example.luong.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.luong.location.services.LocationService;

public class MainActivity extends AppCompatActivity {
    Switch swtService;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "onCreate_MainMenu", Toast.LENGTH_SHORT).show();
        innitControl();
        excuteControl();
    }

    private void innitControl(){
        swtService = findViewById(R.id.swt_service);
    }

    private void excuteControl() {
        swtService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intent = new Intent(MainActivity.this, LocationService.class);
                if(isChecked){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Toast.makeText(this, "onPostResume_MainMenu", Toast.LENGTH_SHORT).show();
    }
}
