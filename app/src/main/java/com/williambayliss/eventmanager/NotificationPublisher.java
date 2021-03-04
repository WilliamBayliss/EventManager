package com.williambayliss.eventmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {
//    Sets notification ID
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    public void onReceive (Context context , Intent intent) {
//        Creates Notification Manager
        NotificationManager notificationManager =
                (NotificationManager)context
                        .getSystemService(
                                Context.NOTIFICATION_SERVICE);
//        Gets notification from given Intent
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
//        Gets id, ensures notification manager not null, sends notify
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 );
        assert notificationManager != null;
        notificationManager.notify(id , notification);
    }
}
