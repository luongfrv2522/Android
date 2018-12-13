package com.example.luong.location.common;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class HttpUtils {
    private final String BASE_URI = "http://demo2100786.mockable.io/";
    private static OkHttpClient client = null;
    public HttpUtils(){
        if(client == null){
            client = new OkHttpClient();
        }
    }
    public String get(String uri){
        Request request = new Request.Builder()
                .url(BASE_URI+uri)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
