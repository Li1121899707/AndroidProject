package com.example.yantu.androidproject.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREAT_HOBBY = "create table Hobby("+
            "hbId integer primary key autoincrement,"+"hbName text,"
            +"hbTime integer,hbCycle text)";
    private static final String CREAT_LOG = "create table Log("+
            "lgId integer primary key autoincrement,"+" hobby Hobby,"+
            "lgTotal integer,lgContinue integer)";
    private static final String CREAT_SIGNIN = "create table Signin("+
            "siId integer primary key autoincrement,"+" hobby Hobby,"+
            "siDate text)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_HOBBY);
        db.execSQL(CREAT_LOG);
        db.execSQL(CREAT_SIGNIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
