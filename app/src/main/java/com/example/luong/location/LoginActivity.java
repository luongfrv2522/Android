package com.example.luong.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import com.example.luong.location.common.Libs;
import com.example.luong.location.dataStorage.ErrorCodes;
=======
import com.example.luong.location.common.ErrorCodes;
import com.example.luong.location.common.GlobalConfig;
>>>>>>> master
import com.example.luong.location.common.HttpUtils;
import com.example.luong.location.common.Libs;
import com.example.luong.location.dataStorage.UserConnected;
import com.example.luong.location.entities.ReturnObj;
import com.example.luong.location.entities.User;
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
        /*innitControl();
        checkIsLogin();//nếu đã đăng nhập thì chuyển màn
        excuteControl();*/
        //
        startActivity(new Intent(LoginActivity.this, TestSignalrActivity.class));
        finish();
        //
    }
    private void checkIsLogin(){
        showLoading(true);
        User userSession = UserConnected.getUserSession(LoginActivity.this);
        if(Objects.nonNull(userSession)){
<<<<<<< HEAD
            //new LoginAsynTask(userSession.getUserName(), userSession.getPassword()).execute();
=======
>>>>>>> master
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        showLoading(false);
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
                //Ẩn bàn phím
                hideKeyBoard();
                //show loading
                showLoading(true);
                //Login Asyntask
                String user = Objects.requireNonNull(txtUser.getText()).toString().trim();
                String pass = Objects.requireNonNull(txtPass.getText()).toString().trim();
                new LoginAsynTask(user, pass).execute();

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
    private void hideKeyBoard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            Log.d("LoginActivity/hideKeyBoard",e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected class LoginAsynTask extends AsyncTask<Objects, Void, Object> {
        private String user;
        private String pass;

        LoginAsynTask(String user, String pass) {
            this.user = user.trim();
            this.pass = pass.trim();
        }

        @Override
        protected Object doInBackground(Objects... objects) {
            HttpUtils httpUtils = new HttpUtils();
            JSONObject jsonObject = new JSONObject();
            try {
                String androidId = Libs.getAndroidId(LoginActivity.this);
                String[] info = Libs.getAndroidHardWare();
                jsonObject.put("UserName", user);
                jsonObject.put("Password", pass);
                jsonObject.put("DeviceCode", androidId);
                jsonObject.put("DeviceName", info[4]+"-"+info[2]+"-"+info[0]);
                jsonObject.put("DeviceVersion", info[1]);
                Log.d("jsonObject",jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
<<<<<<< HEAD
            String val = httpUtils.post("Account/LoginMobile", jsonObject.toString());
            ReturnObj<User> rs = new Gson().fromJson(val, new TypeToken<ReturnObj<User>>(){}.getType());
=======
            String val = httpUtils.post(GlobalConfig.ServerString.BASE_API_URL+"Account/Login", jsonObject.toString());
            ReturnObj<User> rs = null;
            if(Libs.isJSONValid(val)){
                rs = new Gson().fromJson(val, new TypeToken<ReturnObj<User>>(){}.getType());
            }
>>>>>>> master
            return rs;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (Objects.nonNull(o)) {
                ReturnObj<User> u = (ReturnObj<User>) o;
                //
                if (u.ErrorCode.equals(ErrorCodes.SUCCESS) && u.hasData()) {
                    UserConnected.setUserSession(LoginActivity.this,u.Data);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Log.e("LoginActivity/onPostExecute", u.ErrorCode + " - " + u.ErrorMessage);
                    Toast.makeText(LoginActivity.this, u.ErrorMessage, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Lỗi Api", Toast.LENGTH_LONG).show();
            }
            showLoading(false);
        }
    }
}
