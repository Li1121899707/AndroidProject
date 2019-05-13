package com.example.yantu.androidproject.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yantu.androidproject.Entity.Hobby;

import java.util.ArrayList;

public class MyDB {
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public MyDB(){

    }

    public void createDatabase(Context context){
        dbHelper = new MyDatabaseHelper(context, "yantu.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    // 添加/修改习惯数据库表
    public int editHobby(Integer hbId, String hbName, String hbIcon, String hbTime, Integer hbCycle, String choice) {
        ContentValues values = new ContentValues();
        values.put("hbName", hbName);
        values.put("hbIcon", hbIcon);
        values.put("hbTime", hbTime);
        values.put("hbCycle", hbCycle);
        int result = 0;
        if (choice.equals("insert"))
            result = (int) db.insert("Hobby", null, values);
        else if (choice.equals("update"))
            result = db.update("Hobby", values, "hbId=?", new String[]{String.valueOf(hbId)});
        values.clear();
        return result;
    }

    // 添加/修改日志数据库表，返回自增主键ID
    public int editLog(Integer hbId, Integer lgTotal, Integer lgContinue) {
        ContentValues values = new ContentValues();
        values.put("hbId", hbId);
        values.put("lgTotal", lgTotal);
        values.put("lgContinue", lgContinue);
        long logid = db.insert("Log", null, values);
        values.clear();
        return (int) logid;
    }

    // 查询所有hobby，存入hobbyList
    public ArrayList<Hobby> queryAllFromHobby(){
        ArrayList<Hobby> hobbyList = new ArrayList<>();
        Cursor cursor = db.query("Hobby", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Hobby hobby = new Hobby();
                hobby.setHbId(cursor.getInt(cursor.getColumnIndex("hbId")));
                hobby.setHbName(cursor.getString(cursor.getColumnIndex("hbName")));
                hobby.setHbImg(cursor.getString(cursor.getColumnIndex("hbIcon")));
                hobby.setHbTime(cursor.getString(cursor.getColumnIndex("hbTime")));
                hobby.setHbCycle(cursor.getInt(cursor.getColumnIndex("hbCycle")));
                hobbyList.add(hobby);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hobbyList;
    }

}
