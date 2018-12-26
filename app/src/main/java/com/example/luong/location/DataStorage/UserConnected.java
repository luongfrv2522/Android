package com.example.luong.location.dataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.luong.location.common.StaticClass;
import com.example.luong.location.models.User;

/**
 * Class chứa User hiện tại đang đăng nhập
 * @author LuongHV5
 */
public class UserConnected {
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
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_SESSION, StaticClass.SingletonGson.getInstance().toJson(user));
        editor.apply();
    }
    /**
     * Lấy User hiện tại
     * @param context Context hiện tại
     * @return Đối tượng User
     */
    public static User getUserSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        String SessionUserJson = sharedPreferences.getString(USER_SESSION,"");
        return StaticClass.SingletonGson.getInstance().fromJson(SessionUserJson, User.class);
    }

    /**
     * Xóa User hiện tại trong bộ nhớ
     * @param context Context hiện tại
     */
    public static void removeUserSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Manager.SHARE_PREFERENCES_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_SESSION);

    }
}
