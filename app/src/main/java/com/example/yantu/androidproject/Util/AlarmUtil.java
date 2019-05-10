package com.example.yantu.androidproject.Util;
/*李洋*/
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

import com.example.yantu.androidproject.Broadcast.AlarmReceiver;

import java.util.Calendar;

import static com.example.yantu.androidproject.Broadcast.AlarmReceiver.INTENT_ALARM_LOG;

public class AlarmUtil {

    // 设置Alarm
    private void setAlarmTime(Context context, long timeInMillis, String action, int requestCode) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        android.util.Log.i("result", "startAlarm");
    }

    // 销毁Alarm
    private void canalAlarm(Context context, String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        android.util.Log.i("result", "closeAlarm" + requestCode);
    }

    public void openAlarm(Context context, int alarmChoice) {
        Calendar currCalendar = Calendar.getInstance();
        android.util.Log.i("result", "alarmchoice" + alarmChoice);
        if(alarmChoice == 4 || alarmChoice == 1){
            Calendar calendarMorning = Calendar.getInstance();
            calendarMorning.set(Calendar.HOUR_OF_DAY, 8);
            calendarMorning.set(Calendar.MINUTE, 0);
            calendarMorning.set(Calendar.SECOND, 0);

            // 如果当前时间大于闹钟时间，则为过去的闹钟。定义新的闹钟需要加上一天，因此使用add方法
            // 如果alarmChoice不为4，说明闹钟刚刚响过，应该定义新一天的闹钟。
            if(currCalendar.compareTo(calendarMorning) > 0 || alarmChoice == 1){
                calendarMorning.add(Calendar.DATE, 1);
            }
            setAlarmTime(context, calendarMorning.getTimeInMillis(), "1", 1);
            android.util.Log.i("result", "morning");
        }
        if(alarmChoice == 4 || alarmChoice == 2){
            Calendar calendarAfternoon = Calendar.getInstance();
            calendarAfternoon.set(Calendar.HOUR_OF_DAY, 14);
            calendarAfternoon.set(Calendar.MINUTE, 31);
            calendarAfternoon.set(Calendar.SECOND, 0);

            if(currCalendar.compareTo(calendarAfternoon) > 0 || alarmChoice == 2){
                calendarAfternoon.add(Calendar.DATE, 1);
            }
            setAlarmTime(context, calendarAfternoon.getTimeInMillis(), "2", 2);
            android.util.Log.i("result", "afternoon");
        }
        if(alarmChoice == 4 || alarmChoice == 3){
            Calendar calendarNoon = Calendar.getInstance();
            calendarNoon.set(Calendar.HOUR_OF_DAY, 20);
            calendarNoon.set(Calendar.MINUTE, 0);
            calendarNoon.set(Calendar.SECOND, 0);

            if(currCalendar.compareTo(calendarNoon) > 0 || alarmChoice == 3){
                calendarNoon.add(Calendar.DATE, 1);
            }
            setAlarmTime(context, calendarNoon.getTimeInMillis(), "3",  3);
            android.util.Log.i("result", "noon");
        }
        if(alarmChoice == 0){
            Toast.makeText(context, "无法开启通知！",Toast.LENGTH_SHORT).show();
        }
    }

    public void closeAlarm(Context context){
        canalAlarm(context, INTENT_ALARM_LOG,1);
        canalAlarm(context, INTENT_ALARM_LOG,2);
        canalAlarm(context, INTENT_ALARM_LOG,3);
    }

}
