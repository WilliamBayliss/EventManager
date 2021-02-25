package com.williambayliss.eventmanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Build notification based on intent data
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(intent.getStringExtra("EventTitle"))
                .setContentText(intent.getStringExtra("EventTiming") + ", " + intent.getStringExtra("EventDate"))
                .build();
//        Show notification
        int id = intent.getIntExtra("id", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleNotification(Context context, Event event, long time){
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("id", event.id);
        intent.putExtra("EventTitle", event.title);
        String eventTiming = event.startTime + " - " + event.endTime;
        intent.putExtra("EventTiming", eventTiming);
        intent.putExtra("EventDate", event.date);
        intent.putExtra("EventAlertType", event.alertType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, event.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Schedule notification
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
