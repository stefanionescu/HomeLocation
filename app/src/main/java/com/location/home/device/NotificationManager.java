package com.location.home.device;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.location.home.R;
import com.location.home.ui.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManager {

    private Context context;
    private Notification notification;
    private android.app.NotificationManager manager;

    public NotificationManager(Context context,
                               Notification notification,
                               android.app.NotificationManager manager){

        this.context = context;
        this.manager = manager;
        this.notification = notification;

    }

    public void startNotification(){

        manager.notify(1, notification);

    }

    public void stopNotification(){

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(1);

    }


}
