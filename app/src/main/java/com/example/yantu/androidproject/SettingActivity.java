package com.example.yantu.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.yantu.androidproject.Broadcast.AlarmReceiver;
import com.example.yantu.androidproject.Entity.Log;
import com.example.yantu.androidproject.Util.AlarmUtil;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_DAY;
import static com.example.yantu.androidproject.Broadcast.AlarmReceiver.INTENT_ALARM_LOG;

public class SettingActivity extends AppCompatActivity {

    private Switch notiSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        init();
    }

    public void init(){
        BottomNavigationView navigation = findViewById(R.id.navigationSetting);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_notifications);

        notiSwitch = findViewById(R.id.notiSwitch);
        notiSwitch.setOnCheckedChangeListener(switchListener);
    }


    // 底部状态栏监听事件（lysuzy）
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(SettingActivity.this, TodayHobbyActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(SettingActivity.this, DailyHobbyActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    public CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                OpenNotification();
                Toast.makeText(SettingActivity.this, "您已打开通知!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingActivity.this, "您已关闭通知!", Toast.LENGTH_SHORT).show();
                CloseNotification();
            }
        }
    };

    public void OpenNotification(){
        AlarmUtil alarmUtil = new AlarmUtil();
        alarmUtil.openAlarm(SettingActivity.this, 1);
    }

    public void CloseNotification(){
        AlarmUtil alarmUtil = new AlarmUtil();
        alarmUtil.canalAlarm(SettingActivity.this, INTENT_ALARM_LOG,1);
//        alarmUtil.canalAlarm(SettingActivity.this, INTENT_ALARM_LOG,2);
//        alarmUtil.canalAlarm(SettingActivity.this, INTENT_ALARM_LOG,3);
    }

}
