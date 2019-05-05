package com.example.yantu.androidproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yantu.androidproject.DBHelper.MyDatabaseHelper;
import com.example.yantu.androidproject.Entity.Hobby;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

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
    public void init(){
        back = findViewById(R.id.back);
        hobbies = findViewById(R.id.hobbies);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        materialCalendarView = findViewById(R.id.calendarView);
        dbHelper = new MyDatabaseHelper(this,"yantu.db",null,1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        init();
        //materialCalendarView.setSelectionColor(0xff4285f4);
        Intent intent = getIntent();
        Hobby hobby = new Hobby();
        hobbies.setText(hobby.getHbName());
        List<String> dates = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        for(int i = 0;i < dates.size();i++){
            android.util.Log.i("1", dates.get(i));
        }
        materialCalendarView.addDecorators(new EventDecorator(dates),new SameDayDecorator());
    }
    //给打卡过的日期进行标记
    class EventDecorator implements DayViewDecorator {
        private List<String> dates;
        public EventDecorator(List<String> dates){
            this.dates = dates;
        }
        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return dates.contains(String.valueOf(calendarDay.getDate()));
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
}
