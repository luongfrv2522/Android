package com.example.luong.location.dataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.luong.location.common.StaticClass;
import com.example.luong.location.models.User;

public class UserConnected {
    private static String USER_SESSION = "UserSession";
    public static void setUserSession(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_SESSION, StaticClass.SingletonGson.getInstance().toJson(user));
    }
    public static User getUserSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        String SessionUserJson = sharedPreferences.getString(USER_SESSION,"");
        return StaticClass.SingletonGson.getInstance().fromJson(SessionUserJson, User.class);
    }
}
