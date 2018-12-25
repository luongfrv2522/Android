package com.example.luong.location.common;

import com.google.gson.Gson;

public class StaticClass {
    public static class SingletonGson {
        private static Gson instance;

        private SingletonGson() {

        }
        public static Gson getInstance() {
            if(instance == null) {
                synchronized(SingletonGson.class) {
                    if(null == instance) {
                        instance  = new Gson();
                    }
                }
            }
            return instance;
        }
    }
}
