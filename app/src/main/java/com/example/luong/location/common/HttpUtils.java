package com.example.luong.location.common;

import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class HttpUtils {
    private static OkHttpClient client = null;
    public HttpUtils(){
        if(client == null){
            client = new OkHttpClient();
        }
    }
    public String get(String uri, Map<String,String> params){
        HttpUrl.Builder httpBuilder = HttpUrl.parse(uri).newBuilder();
        if(params != null){
            for(Map.Entry<String,String> param : params.entrySet()){
                httpBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(uri)
                .get()
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public String get(String uri){
        Request request = new Request.Builder()
                .url(uri)
                .get()
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public String post(String uri, String json){
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8;");
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(requestBody)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            Log.e("HttpUtils/post: ",e.getMessage());
        }
        return "";
    }
}
