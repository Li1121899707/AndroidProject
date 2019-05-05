package com.example.yantu.androidproject.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREAT_HOBBY = "create table Hobby("+
            "hbId integer primary key autoincrement,"+"hbName text,"
            +"hbTime text,hbCycle integer,hbImg text)";
    private static final String CREAT_LOG = "create table Log("+
            "lgId integer primary key autoincrement,"+" hbId integer,"+
            "lgTotal integer,lgContinue integer)";
    private static final String CREAT_ClOCKIN = "create table Clockin("+
            "siId integer primary key autoincrement,"+" hbId integer,"+
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
        db.execSQL(CREAT_ClOCKIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
