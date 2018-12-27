package com.example.luong.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.example.luong.location.DataStorage.ErrorCodes;
import com.example.luong.location.DataStorage.UserConnected;
import com.example.luong.location.common.HttpUtils;
import com.example.luong.location.common.StaticClass;
import com.example.luong.location.models.ReturnObj;
import com.example.luong.location.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


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
        checkIsLogin();//nếu đã đăng nhập thì chuyển màn
        innitControl();
        excuteControl();
    }
    private void checkIsLogin(){
        
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
                showLoading(true);
                final String user = Objects.requireNonNull(txtUser.getText()).toString().trim();
                final String pass = Objects.requireNonNull(txtPass.getText()).toString().trim();
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        HttpUtils httpUtils = new HttpUtils();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserName", user);
                            jsonObject.put("Password", pass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String val = httpUtils.post("Account/Login",jsonObject.toString());
                        ReturnObj<User> rs = null;
                        if(Objects.nonNull(val)){
                             rs = new Gson().fromJson(val,new TypeToken<ReturnObj<User>>(){}.getType());
                        }
                        return rs;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        txtRs.setText(o.toString());
                        if(Objects.nonNull(o)){
                            ReturnObj<User> u = (ReturnObj<User>) o;
                            //
                            if(u.hasData() && u.ErrorCode.equals(ErrorCodes.SUCCESS)){
                                String userC = u.Data.getUserName();
                                String passC = u.Data.getPassword();
                                if(user.equals(userC) && pass.equals(passC)){
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    showLoading(false);
                                    finish();
                                }
                            }else{
                                showLoading(false);
                                Log.e("onPostExecute",u.ErrorCode + " - " + u.Message);
                            }

                        }else {
                            Toast.makeText(LoginActivity.this, "Lỗi Api",Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute();
        }
        });
    }
    private void showLoading(Boolean show){
        if(show){
            layoutScroll.setVisibility(View.GONE);
            prsLogin.setVisibility(View.VISIBLE);
        }else {
            layoutScroll.setVisibility(View.VISIBLE);
            prsLogin.setVisibility(View.GONE);
        }
    }
}
