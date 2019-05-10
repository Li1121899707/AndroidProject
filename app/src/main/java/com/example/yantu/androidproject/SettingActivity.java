package com.example.yantu.androidproject;
/*张立才*/
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.yantu.androidproject.Util.AlarmUtil;
import com.example.yantu.androidproject.Util.Utils;

public class SettingActivity extends AppCompatActivity {

    private Switch notiSwitch;
    private Switch vibrateSwitch;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        Utils.setStatusBar(this, false, false);

        init();
    }

    public void init() {
        BottomNavigationView navigation = findViewById(R.id.navigationSetting);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_notifications);

        notiSwitch = findViewById(R.id.notiSwitch);
        vibrateSwitch = findViewById(R.id.vibrateSwitch);

        sp = getSharedPreferences("settings", MODE_PRIVATE);

        String notiChoice = sp.getString("notification_setting","0");
        String vibChoice = sp.getString("vibrate_setting","0");

        if(notiChoice != null && notiChoice.equals("1"))
            notiSwitch.setChecked(true);
        else
            vibrateSwitch.setClickable(false);
        if(vibChoice != null && vibChoice.equals("1"))
            vibrateSwitch.setChecked(true);

        notiSwitch.setOnCheckedChangeListener(switchListener);
        vibrateSwitch.setOnCheckedChangeListener(vibListener);
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
                vibrateSwitch.setClickable(true);
                OpenNotification();
                Toast.makeText(SettingActivity.this, "您已开启通知!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingActivity.this, "您已关闭通知!", Toast.LENGTH_SHORT).show();
                CloseNotification();
                vibrateSwitch.setClickable(false);
            }
        }
    };

    public CompoundButton.OnCheckedChangeListener vibListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                VibrateController("1");
                Toast.makeText(SettingActivity.this, "您已开启震动!", Toast.LENGTH_SHORT).show();
            }else{
                VibrateController("0");
                Toast.makeText(SettingActivity.this, "您已关闭震动!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void OpenNotification(){
        AlarmUtil alarmUtil = new AlarmUtil();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("notification_setting","1");
        editor.apply();
        alarmUtil.openAlarm(SettingActivity.this, 4);
    }

    public void CloseNotification(){
        AlarmUtil alarmUtil = new AlarmUtil();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("notification_setting","0");
        editor.apply();
        alarmUtil.closeAlarm(SettingActivity.this);
    }

    public void VibrateController(String open){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vibrate_setting",open);
        editor.apply();
    }

}
