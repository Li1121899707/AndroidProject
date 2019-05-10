package com.example.yantu.androidproject.Broadcast;
/*李洋*/
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.yantu.androidproject.R;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import com.example.yantu.androidproject.Util.AlarmUtil;


public class AlarmReceiver extends BroadcastReceiver {
    public static final String INTENT_ALARM_LOG = "intent_alarm_log";
    private String title = "研途--早上好!";
    private String content = "不要忘记去看一下早上任务哦！";
    private Boolean vibBool = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        init(context);

        String action = intent.getAction();
        assert action != null;
        int alarmChoice = Integer.valueOf(action);

        CharSequence name = "研途";
        String description = "desc" + +alarmChoice;
        //渠道id
        String channelId = "channelId" + +alarmChoice;

        switch (alarmChoice) {
            case 1:
                title = "研途--早上好!";
                content = "不要忘记去看一下早上任务哦！";
                break;
            case 2:
                title = "研途--下午好!";
                content = "不要忘记去看一下下午任务哦！";
                break;
            case 3:
                title = "研途--晚上好!";
                content = "不要忘记去看一下晚上任务哦！";
                break;
        }
        // 安卓版本高于8.0需要使用 “渠道”
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道样例
            Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);//渠道描述
            mChannel.enableLights(true);//是否显示通知指示灯
            mChannel.enableVibration(vibBool);//是否振动
            PendingIntent pIntent = PendingIntent.getActivity(context, 1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            Notification.Builder builder =
                    new Notification.Builder(context, channelId);
            builder.setSmallIcon(android.R.mipmap.sym_def_app_icon).
                    setContentTitle(title).
                    setContentText(content).
                    setNumber(3);
            builder.setContentIntent(pIntent);
            builder.setFullScreenIntent(pIntent, true);
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            builder.setContentIntent(pIntent);
            builder.setAutoCancel(true);
            notificationManager.notify(1, builder.build());
        } else {
            Toast.makeText(context, "Alarm2", Toast.LENGTH_SHORT).show();
            Notification notification =
                    new NotificationCompat.Builder(context).
                            setContentTitle(title).
                            setContentText(content).
                            setWhen(System.currentTimeMillis()).
                            setSmallIcon(R.drawable.ic_launcher_background).
                            setLargeIcon(BitmapFactory.
                                    decodeResource(context.getResources(), R.drawable.ic_launcher_foreground)).
                            build();
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(alarmChoice, notification);
        }

        //因为set函数只执行一次，所以要重新定义闹钟实现循环。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmUtil alarmUtil = new AlarmUtil();
            alarmUtil.openAlarm(context, alarmChoice);
        }
    }

    public void init(Context context){
        SharedPreferences sp = context.getSharedPreferences("settings", MODE_PRIVATE);
        String vibrateStr = sp.getString("vibrate_setting","0");
        assert vibrateStr != null;
        if(vibrateStr.equals("1"))
            vibBool = true;
    }

}
