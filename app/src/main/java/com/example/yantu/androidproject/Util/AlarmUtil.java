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
        // AlarmManager是系统服务中的定时服务
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        // Android O以上版本需要显式声明Broadcast发送方与接收方。
        intent.setClass(context, AlarmReceiver.class);
        // PendingIntent是延时Intent，getBroadcast为打开广播组件
        // requestCode为唯一标识，否则alarm会覆盖。
        // 如果有两个通知，FLAG_CANCEL_CURRENT会令第一个失效，显示第二个。FLAG_UPDATE_CURRENT会覆盖第一个。
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        android.util.Log.i("alarm", "startAlarm " + action);
    }

    // 销毁Alarm
    private void canalAlarm(Context context, String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        android.util.Log.i("alarm", "closeAlarm" + requestCode);
    }

    public void openAlarm(Context context, int alarmChoice) {
        Calendar currCalendar = Calendar.getInstance();
        if(alarmChoice == 4 || alarmChoice == 1){
            Calendar calendarMorning = Calendar.getInstance();
            calendarMorning.set(Calendar.HOUR_OF_DAY, 7);
            calendarMorning.set(Calendar.MINUTE, 50);
            calendarMorning.set(Calendar.SECOND, 0);

            // 如果当前时间大于闹钟时间，则为过去的闹钟。定义新的闹钟需要加上一天，因此使用add方法
            // 如果alarmChoice不为4，说明闹钟刚刚响过，应该定义新一天的闹钟。
            if(currCalendar.compareTo(calendarMorning) > 0 || alarmChoice == 1){
                calendarMorning.add(Calendar.DATE, 1);
                android.util.Log.i("alarm", "早上 增加一天");
            }
            setAlarmTime(context, calendarMorning.getTimeInMillis(), "1", 1);
            android.util.Log.i("alarm", "morning");
        }
        if(alarmChoice == 4 || alarmChoice == 2){
            Calendar calendarAfternoon = Calendar.getInstance();
            calendarAfternoon.set(Calendar.HOUR_OF_DAY, 13);
            calendarAfternoon.set(Calendar.MINUTE, 50);
            calendarAfternoon.set(Calendar.SECOND, 0);

            if(currCalendar.compareTo(calendarAfternoon) > 0 || alarmChoice == 2){
                calendarAfternoon.add(Calendar.DATE, 1);
                android.util.Log.i("alarm", "下午 增加一天");

            }
            setAlarmTime(context, calendarAfternoon.getTimeInMillis(), "2", 2);
            android.util.Log.i("alarm", "afternoon");
        }
        if(alarmChoice == 4 || alarmChoice == 3){
            Calendar calendarNoon = Calendar.getInstance();
            calendarNoon.set(Calendar.HOUR_OF_DAY, 19);
            calendarNoon.set(Calendar.MINUTE, 50);
            calendarNoon.set(Calendar.SECOND, 0);

            if(currCalendar.compareTo(calendarNoon) > 0 || alarmChoice == 3){
                calendarNoon.add(Calendar.DATE, 1);
                android.util.Log.i("alarm", "晚上 增加一天");
            }
            setAlarmTime(context, calendarNoon.getTimeInMillis(), "3",  3);
            android.util.Log.i("alarm", "noon");
        }
        if(alarmChoice == 0){
            Toast.makeText(context, "无法开启通知！",Toast.LENGTH_SHORT).show();
        }
    }

    public void closeAlarm(Context context){
        canalAlarm(context, "1",1);
        canalAlarm(context, "2",2);
        canalAlarm(context, "3",3);
    }

}
