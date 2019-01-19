package com.example.luong.location.dataStorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.luong.location.common.StaticClass;
import com.example.luong.location.entities.User;

/**
 * Class chứa User hiện tại đang đăng nhập
 * @author LuongHV5
 */
public class UserConnected {
    private static User userSession;
    /**
     * Key lưu dữ liệu USER
     */
    private static String USER_SESSION = "UserSession";

    /**
     * Lưu User vào bộ nhớ trong
     * @param context Context hiện tại
     * @param user User cần lưu
     */
    public static void setUserSession(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_SESSION, StaticClass.SingletonGson.getInstance().toJson(user));
        editor.apply();
        //
        userSession = user;
    }
    /**
     * Lấy User hiện tại
     * @param context Context hiện tại
     * @return Đối tượng User
     */
    private static User getUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String SessionUserJson = sharedPreferences.getString(USER_SESSION,"");
        return StaticClass.SingletonGson.getInstance().fromJson(SessionUserJson, User.class);
    }
    public static User getUserSession(Context context){
        if(userSession == null){
            userSession = getUser(context);
        }
        return userSession;
    }
    /**
     * Xóa User hiện tại trong bộ nhớ
     * @param context Context hiện tại
     */
    public static void removeUserSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_SESSION);
        editor.apply();
        //
        userSession = null;
    }
}
