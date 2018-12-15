package com.example.luong.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luong.location.common.HttpUtils;
import com.example.luong.location.models.ReturnObj;
import com.example.luong.location.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class LoginActivity extends Activity {
    Button btnLogin;
    LinearLayout prsLogin;
    ScrollView layoutScroll;
    TextView txtRs;
    TextInputEditText txtUser,txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        innitControl();
        excuteControl();
    }
    private void innitControl(){
        btnLogin = findViewById(R.id.btn_login);
        prsLogin = findViewById(R.id.login_progress);
        layoutScroll = findViewById(R.id.layout_scroll);
        txtRs = findViewById(R.id.txtRs);
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
    }
    private void excuteControl(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                //show loading
                layoutScroll.setVisibility(View.GONE);
                prsLogin.setVisibility(View.VISIBLE);
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        HttpUtils httpUtils = new HttpUtils();
                        String val = httpUtils.get("Register");
                        ReturnObj<User> rs = new Gson().fromJson(val,new TypeToken<ReturnObj<User>>(){}.getType());
                        return rs;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        txtRs.setText(o.toString());
                        ReturnObj<User> u = (ReturnObj<User>) o;
                        String user = txtUser.getText().toString().trim();
                        String pass = txtPass.getText().toString().trim();
                        //
                        String userC = u.Data.UserName.toString().trim();
                        String passC = u.Data.Password.toString().trim();
                        if( userC.equals(user) && passC.equals(pass)){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            layoutScroll.setVisibility(View.VISIBLE);
                            prsLogin.setVisibility(View.GONE);
                            finish();
                        }else{
                            //Hide Loading
                            layoutScroll.setVisibility(View.VISIBLE);
                            prsLogin.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
        }
        });
    }
}
