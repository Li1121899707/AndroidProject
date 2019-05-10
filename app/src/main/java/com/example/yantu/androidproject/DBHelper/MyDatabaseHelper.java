package com.example.yantu.androidproject.DBHelper;
/*姚越*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yantu.androidproject.EditHobbyActivity;
import com.example.yantu.androidproject.HobbyDetailAvtivity;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_HOBBY = "create table Hobby("+
            "hbId integer primary key autoincrement,"+"hbName text,"
            +"hbTime text,hbCycle integer,hbIcon text)";
    private static final String CREATE_LOG = "create table Log("+
            "lgId integer primary key autoincrement,"+" hbId integer,"+
            "lgTotal integer,lgContinue integer)";
    private static final String CREATE_ClOCKIN = "create table Clockin("+
            "ciId integer primary key autoincrement,"+" hbId integer,"+
            "ciDate text)";
    private Context mContext;


    public MyDatabaseHelper(Context context, String name, Object factory, int version) {
        super(context,name, (SQLiteDatabase.CursorFactory) factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOBBY);
        db.execSQL(CREATE_LOG);
        db.execSQL(CREATE_ClOCKIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
