package com.example.luong.location.common;

public class GlobalConfig {
    public static class ServerString{
        //server
        public static final String BASE_SERVER = "http://192.168.137.1:8088/";
        //api
        public static final String BASE_API_URL = BASE_SERVER + "api/";
        //hub
        public static final String BASE_HUB_URL = BASE_SERVER + "signalr/";
        public static final String CHAT_HUB_NAME = "MyHub1";
    }
}
