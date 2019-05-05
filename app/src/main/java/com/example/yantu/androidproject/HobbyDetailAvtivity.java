package com.example.yantu.androidproject;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HobbyDetailAvtivity extends AppCompatActivity {
    private ImageView back;
    private ImageView delete;
    private TextView hobbies;
    private ImageView edit;
    private MyDatabaseHelper dbHelper;
    private MaterialCalendarView materialCalendarView;
    private Hobby hobby;
    private int Total;
    private int Continue;
    private TextView totalday;
    private TextView continueday;
    private ArrayList<String> dates;
    /**************************************************/
    public void addData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbId",1);
        values.put("lgTotal",10);
        values.put("lgContinue",5);
        db.insert("Log",null,values);
        values.clear();
        values.put("hbId",1);
        values.put("ciDate","2019-05-03");
        db.insert("Clockin",null,values);
        values.clear();
        values.put("hbId",1);
        values.put("ciDate","2019-05-04");
        db.insert("Clockin",null,values);
        values.clear();
        values.put("hbId",1);
        values.put("ciDate","2019-05-02");
        db.insert("Clockin",null,values);
        values.clear();
        values.put("hbId",1);
        values.put("ciDate","2019-05-01");
        db.insert("Clockin",null,values);
        values.clear();
        values.put("hbId",1);
        values.put("ciDate","2019-05-05");
        db.insert("Clockin",null,values);
        values.clear();
    }
    /*************************************************/
    public void init(){
        back = findViewById(R.id.back);
        hobbies = findViewById(R.id.hobbies);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        materialCalendarView = findViewById(R.id.calendarView);
        dbHelper = new MyDatabaseHelper(this,"yantu.db",null,1);
        //hobby = (Hobby)getIntent().getSerializableExtra("Hobby");
        hobby = new Hobby();
        hobby.setHbId(1);
        totalday = findViewById(R.id.total);
        continueday = findViewById(R.id.continueday);
        dates = new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobby_detail_avtivity);
        //隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        init();
        addData();
        getTotalAndContinue();
        getAllDates();
        Log.i("2", String.valueOf(dates.size()));
        for (int i = 0; i < dates.size();i++){
            Log.i("2",dates.get(i));
        }
        //materialCalendarView.setSelectionColor(0xff4285f4);
        //hobbies.setText(hobby.getHbName());
        //查询数据库读出相关的天数
        String display_total = "总共签到" + String.valueOf(Total) + "天";
        String display_continue = "连续签到" + String.valueOf(Continue) + "天";
        totalday.setText(display_total);
        continueday.setText(display_continue);
        //删除习惯
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HobbyDetailAvtivity.this,"1111",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(HobbyDetailAvtivity.this);
                builder.setTitle("是否删除习惯")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try{
                            db.delete("Hobby","hbId=?",new String[]{String.valueOf(hobby.getHbId())});
                            db.delete("Log","hbId=?",new String[]{String.valueOf(hobby.getHbId())});
                            db.delete("Clockin","hbId=?",new String[]{String.valueOf(hobby.getHbId())});
                        }catch (Exception e){
                            Toast.makeText(HobbyDetailAvtivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(HobbyDetailAvtivity.this,"删除成功",Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(HobbyDetailAvtivity.this,"1111",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.create().show();
            }
        });
        //编辑习惯
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HobbyDetailAvtivity.this,EditHobbyActivity.class);
                intent1.putExtra("Hobby", hobby);
                intent1.putExtra("choice","edit");
                startActivity(intent1);
            }
        });
        materialCalendarView.addDecorators(new EventDecorator(dates),new SameDayDecorator());
    }
    //给打卡过的日期进行标记
    class EventDecorator implements DayViewDecorator {
        private List<String> datess;
        public EventDecorator(List<String> dates){
            this.datess = dates;
        }
        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return datess.contains(String.valueOf(calendarDay.getDate()));
        }

        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            //dayViewFacade.addSpan(new AnnulusSpan());
            dayViewFacade.addSpan(new ForegroundColorSpan(Color.GREEN));
        }
    }
    //给当天进行标记
    class SameDayDecorator implements DayViewDecorator{

        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String day = String.valueOf(calendar.get(Calendar.DATE));
            if(Calendar.MONTH+1 < 10){
                month = "0" + month;
            }
            if (Calendar.DATE < 10){
                day = "0" + day;
            }
            String parse = year + "-" + month + "-" + day;
            //android.util.Log.i("1", String.valueOf(calendarDay.getDate()));
            //android.util.Log.i("1",parse);
            if (String.valueOf(calendarDay.getDate()).equals(parse)) {
                //android.util.Log.i("1","正确");
                return true;
            }
            return false;
        }
        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            dayViewFacade.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }
    //查询出所有的打卡日期
    public void getAllDates(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Clockin",null,
                "hbId=?",new String[]{String.valueOf(hobby.getHbId())},
                null, null,null);
        if(cursor.moveToFirst()){
            do{
                Log.i("1", String.valueOf(cursor.getColumnIndex("ciDate")));
                String date = cursor.getString(cursor.getColumnIndex("ciDate"));
                dates.add(date);
            }while (cursor.moveToNext());
            cursor.close();
        }
    }
    public void getTotalAndContinue(){
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        Cursor cursor = db1.query("Log",null,
                "hbId=?",new String[]{String.valueOf(hobby.getHbId())},
                null, null,null);
        if(cursor.moveToFirst()){
            do{
                Total = cursor.getInt(cursor.getColumnIndex("lgTotal"));
                Continue = cursor.getInt(cursor.getColumnIndex("lgContinue"));
            }while (cursor.moveToNext());
            cursor.close();
        }
    }
}
