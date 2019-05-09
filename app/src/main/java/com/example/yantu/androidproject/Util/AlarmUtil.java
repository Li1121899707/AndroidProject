package com.example.yantu.androidproject.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.yantu.androidproject.Broadcast.AlarmReceiver;
import com.example.yantu.androidproject.Entity.Log;
import com.example.yantu.androidproject.SettingActivity;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_DAY;

public class AlarmUtil {

    // 设置Alarm
    public void setAlarmTime(Context context, long timeInMillis, String action, long delay, int requestCode) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // 安卓4.4以上
            am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, delay, sender);
        else
            am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, delay, sender);

        android.util.Log.i("result", "startAlarm");
    }

    // 销毁Alarm
    public void canalAlarm(Context context, String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);

        android.util.Log.i("result", "closeAlarm");
    }

    public void openAlarm(Context context, int alarmChoice) {
        android.util.Log.i("result", "alarmchoice" + alarmChoice);
        if(alarmChoice == 4 || alarmChoice == 1){
            Calendar calendarMorning = Calendar.getInstance();
            calendarMorning.set(Calendar.HOUR_OF_DAY, 7);
            calendarMorning.set(Calendar.MINUTE, 0);
            calendarMorning.set(Calendar.SECOND, 0);
            setAlarmTime(context, calendarMorning.getTimeInMillis(), "1", 60*60*24*1000, 1);
            android.util.Log.i("result", "morning");
        }
//        if(alarmChoice == 4 || alarmChoice == 2){
//            Calendar calendarAfternoon = Calendar.getInstance();
//            calendarAfternoon.set(Calendar.HOUR_OF_DAY, 13);
//            calendarAfternoon.set(Calendar.MINUTE, 0);
//            calendarAfternoon.set(Calendar.SECOND, 0);
//            setAlarmTime(context, calendarAfternoon.getTimeInMillis(), "2", INTERVAL_DAY, 2);
//            android.util.Log.i("result", "afternoon");
//        }
//        if(alarmChoice == 4 || alarmChoice == 3){
//            Calendar calendarNoon = Calendar.getInstance();
//            calendarNoon.set(Calendar.HOUR_OF_DAY, 19);
//            calendarNoon.set(Calendar.MINUTE, 0);
//            calendarNoon.set(Calendar.SECOND, 0);
//            setAlarmTime(context, calendarNoon.getTimeInMillis(), "3", INTERVAL_DAY, 3);
//            android.util.Log.i("result", "noon");
//        }
        if(alarmChoice == 0){
            Toast.makeText(context, "无法开启通知！",Toast.LENGTH_SHORT).show();
        }
    }

}
