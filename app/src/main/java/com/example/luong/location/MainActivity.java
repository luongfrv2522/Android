package com.example.luong.location;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luong.location.common.StaticState;
import com.example.luong.location.services.LocationService;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Switch swtService;
    Intent intent;
    Button btnAutoRunActive;
    TextView txtCheckServiceRuning
            ,txtStateService;
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
        txtCheckServiceRuning = findViewById(R.id.txtCheckServiceRuning);
        txtStateService = findViewById(R.id.txtStateService);
        btnAutoRunActive = findViewById(R.id.btn_auto_run_active);
    }

    @SuppressLint("SetTextI18n")
    private void excuteControl() {
        //region Mở menu active cấp quyền tự động chạy
        btnAutoRunActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAutoStartup();
            }
        });
        //endregion
        updateShowState();
        // set giá trị ban đầu cho switch
        StaticState.ServiceLocationStarted = isMyServiceRunning(LocationService.class);
        swtService.setChecked(StaticState.ServiceLocationStarted);
        swtService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intent = new Intent(MainActivity.this, LocationService.class);
                if(isChecked){
                    startServices();
                }else{
                    stopServices();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume_MainMenu", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Toast.makeText(this, "onPostResume_MainMenu", Toast.LENGTH_SHORT).show();
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @SuppressLint("SetTextI18n")
    private void updateShowState(){
        //test
        txtCheckServiceRuning.setText("isMyServiceRunning: "+ isMyServiceRunning(LocationService.class));
        txtStateService.setText("txtStateService: "+ StaticState.ServiceLocationStarted);
    }
    @SuppressLint("StaticFieldLeak")
    private void startServices(){
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                startService(intent);
                StaticState.ServiceLocationStarted = true;
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                updateShowState();
            }
        }.execute();

    }
    private void stopServices(){
        stopService(intent);
        StaticState.ServiceLocationStarted = false;
        updateShowState();
    }
    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }
}
