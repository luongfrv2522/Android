package com.example.luong.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luong.location.dataStorage.UserConnected;
import com.example.luong.location.services.LocationService;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Switch swtService;
    Intent intentService;
    Button btnAutoRunActive
          ,btnLogout;
    TextView txtCheckServiceRuning
            ,txtStateService;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiver2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "onCreate_MainMenu", Toast.LENGTH_SHORT).show();
        innitControl();
        eventControl();
    }

    private void innitControl(){
        swtService = findViewById(R.id.swt_service);
        //txt
        txtCheckServiceRuning = findViewById(R.id.txtCheckServiceRuning);
        txtStateService = findViewById(R.id.txtStateService);
        //btn
        btnAutoRunActive = findViewById(R.id.btn_auto_run_active);
        btnLogout = findViewById(R.id.btn_logout);
    }

    @SuppressLint("SetTextI18n")
    private void eventControl() {
        updateShowState();
        intentService = new Intent(MainActivity.this, LocationService.class);

        //#region Set sự kiện cho logout button
        //region Xin cấp quyền truy cập vị trí
        if(!runTimePermission()){
            enableSwitch();
        }
        //#endregion
        //endregion
    }

    @Override
    protected void onResume() {
        super.onResume();
        //region Mở menu active cấp quyền tự động chạy
        btnAutoRunActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAutoStartup();
            }
        });
        //endregion
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    txtStateService.setText(intent.getExtras().getString("coordinates",""));
                    //Nếu là update status thì update
                    boolean status = intent.getExtras().getBoolean("status", false);
                    if(status){
                        swtService.setChecked(false);
                    }
                }
            };
            registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
        }
        //Toast.makeText(this, "onResume_MainMenu", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableSwitch();
            }else{
                runTimePermission();
            }

        }
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
        boolean serviceIsRuning = isMyServiceRunning(LocationService.class);
        txtCheckServiceRuning.setText("isMyServiceRunning: "+ serviceIsRuning);
        swtService.setChecked(serviceIsRuning);
    }
    private void enableSwitch(){
        swtService.setClickable(true);
        // set giá trị ban đầu cho switch
        swtService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startServices();
                }else{
                    stopServices();
                }
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Đóng service
                stopServices();
                //Xóa dữ liệu đăng nhập
                UserConnected.removeUserSession(MainActivity.this);
                Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public void startServices(){

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                startService(intentService);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                updateShowState();
            }
        }.execute();

    }
    public void stopServices(){
        if(intentService!=null) stopService(intentService);
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
    public boolean runTimePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }
}
