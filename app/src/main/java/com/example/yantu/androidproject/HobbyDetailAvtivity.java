package com.example.yantu.androidproject;
/*臧博浩*/
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.example.yantu.androidproject.Fragment.DailyFragment;
import com.example.yantu.androidproject.Util.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HobbyDetailAvtivity extends AppCompatActivity {
    private ImageView delete;
    private ImageView edit;
    private ImageView btnDetailBack;
    private MyDatabaseHelper dbHelper;
    private MaterialCalendarView materialCalendarView;
    private Hobby hobby;
    private int Total;
    private int Continue;
    private TextView totalday;
    private TextView continueday;
    private ArrayList<String> dates;

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**************************************************/
    //添加模拟数据
    public void addData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hbId", 1);
        values.put("lgTotal", 10);
        values.put("lgContinue", 5);
        db.insert("Log", null, values);
        values.clear();
        values.put("hbId", 1);
        values.put("ciDate", "2019-05-03");
        db.insert("Clockin", null, values);
        values.clear();
        values.put("hbId", 1);
        values.put("ciDate", "2019-05-04");
        db.insert("Clockin", null, values);
        values.clear();
        values.put("hbId", 1);
        values.put("ciDate", "2019-05-02");
        db.insert("Clockin", null, values);
        values.clear();
        values.put("hbId", 1);
        values.put("ciDate", "2019-05-01");
        db.insert("Clockin", null, values);
        values.clear();
        values.put("hbId", 1);
        values.put("ciDate", "2019-05-05");
        db.insert("Clockin", null, values);
        values.clear();
    }

    /*************************************************/
    //初始化控件
    public void init() {
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        totalday = findViewById(R.id.total);
        btnDetailBack = findViewById(R.id.btnDetailBack);
        continueday = findViewById(R.id.continueday);
        materialCalendarView = findViewById(R.id.calendarView);
        dbHelper = new MyDatabaseHelper(this, "yantu.db", null, 1);
        hobby = (Hobby)getIntent().getSerializableExtra("Hobby");
        dates = new ArrayList<>();

        btnDetailBack.setOnClickListener(backListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobby_detail_avtivity);
        Utils.setStatusBar(this, false, false);

        init();
        //addData();
        getTotalAndContinue();
        getAllDates();
        Log.i("2", String.valueOf(dates.size()));
        for (int i = 0; i < dates.size(); i++) {
            Log.i("2", dates.get(i));
        }
        materialCalendarView.setSelectionColor(0xff4285f4);
        //hobbies.setText(hobby.getHbName());
        //查询数据库读出相关的天数

        delete.setOnClickListener(deletelistener);
        edit.setOnClickListener(editlistener);
        materialCalendarView.addDecorators(new EventDecorator(dates), new SameDayDecorator());
    }

    //给打卡过的日期进行标记
    class EventDecorator implements DayViewDecorator {
        private List<String> datess;

        public EventDecorator(List<String> dates) {
            this.datess = dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return datess.contains(String.valueOf(calendarDay.getDate()));
        }

        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            dayViewFacade.addSpan(new AnnulusSpan());
            //dayViewFacade.addSpan(new ForegroundColorSpan(Color.GREEN));

        }
    }

    //给当天进行标记
    class SameDayDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String day = String.valueOf(calendar.get(Calendar.DATE));
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                month = "0" + month;
            }
            if (calendar.get(Calendar.DATE) < 10) {
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
            //dayViewFacade.addSpan(new ForegroundColorSpan(Color.RED));
            dayViewFacade.addSpan(new CircleBackGroundSpan());
        }
    }

    //查询出所有的打卡日期
    public void getAllDates() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Clockin", null,
                "hbId=?", new String[]{String.valueOf(hobby.getHbId())},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.i("1", String.valueOf(cursor.getColumnIndex("ciDate")));
                String date = cursor.getString(cursor.getColumnIndex("ciDate"));
                dates.add(date);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    //从数据库中获取当前习惯的总共打卡天数和坚持天数
    public void getTotalAndContinue() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Log", null,
                "hbId=?", new String[]{String.valueOf(hobby.getHbId())},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Total = cursor.getInt(cursor.getColumnIndex("lgTotal"));
                Continue = cursor.getInt(cursor.getColumnIndex("lgContinue"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        //将数据库中获取的信息展示在页面上
        String display_total = "总共签到" + String.valueOf(Total) + "天";
        String display_continue = "连续签到" + String.valueOf(Continue) + "天";
        totalday.setText(display_total);
        continueday.setText(display_continue);
    }

    //查询是否删除
    public View.OnClickListener deletelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HobbyDetailAvtivity.this);
            builder.setTitle("是否删除习惯");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    try {
                        Log.i("result", "hobbyid delete : " + hobby.getHbId());
                        db.delete("Hobby", "hbId=?", new String[]{String.valueOf(hobby.getHbId())});
                        db.delete("Log", "hbId=?", new String[]{String.valueOf(hobby.getHbId())});
                        db.delete("Clockin", "hbId=?", new String[]{String.valueOf(hobby.getHbId())});
                    } catch (Exception e) {
                        Toast.makeText(HobbyDetailAvtivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(HobbyDetailAvtivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HobbyDetailAvtivity.this, MainActivity.class);
                    intent.putExtra("id",1);
                    startActivity(intent);

                }
            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(HobbyDetailAvtivity.this,"1111",Toast.LENGTH_SHORT).show();
                }
            });
            builder.create().show();
        }
    };

    //向习惯编辑页面跳转
    public View.OnClickListener editlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent(HobbyDetailAvtivity.this, EditHobbyActivity.class);
            intent1.putExtra("choice", "update");
            intent1.putExtra("Hobby", hobby);
            Log.i("result", "123 " + hobby.getHbName());
            Log.i("result", "123 " + hobby.getHbCycle());
            startActivity(intent1);
        }
    };

    //今天设置红色圆环背景
    class CircleBackGroundSpan implements LineBackgroundSpan {

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setAntiAlias(true); //消除锯齿
            paint.setStyle(Paint.Style.STROKE);//绘制空心圆或 空心矩形
            int ringWidth = dip2px(HobbyDetailAvtivity.this, 1);//圆环宽度
            //绘制圆环
            paint.setColor(Color.parseColor("#FF3D62"));
            paint.setStrokeWidth(ringWidth);
            c.drawCircle((right - left) / 2, (bottom - top) / 2, dip2px(HobbyDetailAvtivity.this, 18), paint);
        }
    }

    //打卡过的天设置淡蓝色圆环背景
    class AnnulusSpan implements LineBackgroundSpan {
        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            Paint paint = new Paint();
            paint.setAntiAlias(true); //消除锯齿
            paint.setStyle(Paint.Style.STROKE);//绘制空心圆或 空心矩形
            int ringWidth = dip2px(HobbyDetailAvtivity.this, 1);//圆环宽度
            //绘制圆环
            paint.setColor(Color.parseColor("#00bcbe"));
            paint.setStrokeWidth(ringWidth);
            c.drawCircle((right - left) / 2, (bottom - top) / 2, dip2px(HobbyDetailAvtivity.this, 18), paint);
        }
    }

    //向前一页面跳转
    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
