package com.williambayliss.eventmanager;

import android.app.Notification;
import android.app.NotificationChannel;
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
//        Checks that os build is >= Oreo
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES.O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
//            Creates notification channel with ID from NotificationChannelBuilder
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            NotificationChannelBuilder.CHANNEL_1_ID,
                            "NOTIFICATION_CHANNEL_NAME",
                            importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
//        Gets id, ensures notification manager not null, sends notify
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 );
        assert notificationManager != null;
        notificationManager.notify(id , notification);
    }
}
