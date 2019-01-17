package com.example.luong.location.DataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.luong.location.common.StaticClass;
import com.example.luong.location.models.User;
<<<<<<< HEAD
=======
import com.google.gson.Gson;
>>>>>>> parent of e02d819... signal simple

public class UserConnected {
<<<<<<< HEAD
    /**
     * Key lưu dữ liệu USER
     */
=======
>>>>>>> parent of e02d819... signal simple
    private static String USER_SESSION = "UserSession";
    public static void setUserSession(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_SESSION, StaticClass.SingletonGson.getInstance().toJson(user));
<<<<<<< HEAD
        editor.apply();
    }
    /**
     * Lấy User hiện tại
     * @param context Context hiện tại
     * @return Đối tượng User
     */
=======
    }
>>>>>>> parent of e02d819... signal simple
    public static User getUserSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        String SessionUserJson = sharedPreferences.getString(USER_SESSION,"");
        return StaticClass.SingletonGson.getInstance().fromJson(SessionUserJson, User.class);
    }
<<<<<<< HEAD

    /**
     * Xóa User hiện tại trong bộ nhớ
     * @param context Context hiện tại
     */
    public static void removeUserSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_SESSION);

    }
=======
>>>>>>> parent of e02d819... signal simple
}
