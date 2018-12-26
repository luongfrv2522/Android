package com.example.luong.location.dataStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.luong.location.models.User;

public class UserManager extends SQLiteOpenHelper{
    private static final String TABLE_NAME ="user";
    private static final String USER_ID ="user_id";
    private static final String CONNECTION_ID ="connection_id";
    private static final String USER_NAME ="user_name";
    private static final String PASSWORD ="password";
    private static final String FULL_NAME ="full_name";
    private static final String EMAIL ="email";
    private static final String USER_TYPE ="user_type";
    private static final String DESCRIPTION ="description";
    private static final String CREATED ="created";

    private Context context;

    public UserManager(Context context){
        super(context, Manager.DATABASE_NAME, null, 1);
        this.context = context;
        Log.d("UserManager","Constructor.");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME +
                "( "+ USER_ID + " INT, " +
                CONNECTION_ID + " TEXT, " +
                USER_NAME     + " TEXT, " +
                PASSWORD      + " TEXT, " +
                FULL_NAME     + " TEXT, " +
                EMAIL         + " TEXT, " +
                USER_TYPE     + " TEXT, " +
                DESCRIPTION   + " TEXT, " +
                CREATED       + " TEXT)";
        db.execSQL(sqlQuery);
        Log.d("UserManager","onCreate: Create successfully!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.d("UserManager","onUpgrade: Drop successfully!");
    }
    //Add new a User
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserId());
        values.put(CONNECTION_ID, user.getConnectionId());
        values.put(USER_NAME, user.getUserName());
        values.put(PASSWORD, user.getPassword());
        values.put(FULL_NAME, user.getFullName());
        values.put(EMAIL, user.getEmail());
        values.put(USER_TYPE, user.getUserType());
        values.put(DESCRIPTION, user.getDescription());
        values.put(CREATED, user.getCreated());
        //Neu de null thi khi value bang null thi loi
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    //Update a User
    public int Update(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserId());
        values.put(CONNECTION_ID, user.getConnectionId());
        values.put(USER_NAME, user.getUserName());
        values.put(PASSWORD, user.getPassword());
        values.put(FULL_NAME, user.getFullName());
        values.put(EMAIL, user.getEmail());
        values.put(USER_TYPE, user.getUserType());
        values.put(DESCRIPTION, user.getDescription());
        values.put(CREATED, user.getCreated());
        //Neu de null thi khi value bang null thi loi
        db.insert(TABLE_NAME,null,values);
        return db.update(TABLE_NAME,values,USER_ID +"=?",new String[] { String.valueOf(user.getUserId())});
    }
    //Delete a User
    public void deleteStudent(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, USER_ID + "=?",
                new String[] { String.valueOf(user.getUserId()) });
        db.close();
    }
}
