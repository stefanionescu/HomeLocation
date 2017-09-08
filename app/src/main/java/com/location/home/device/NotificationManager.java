package com.location.home.device;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.location.home.R;
import com.location.home.ui.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManager {

    Context context;

    public NotificationManager(Context context){

        this.context = context;

    }

    public void startNotification(){

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, createNotificationBuilder());

    }

    public PendingIntent getPendingIntent(){

        Intent showTaskIntent = new Intent(context, MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                showTaskIntent,
                0);

        return contentIntent;

    }

    public Notification createNotificationBuilder(){

        return new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(getPendingIntent())
                .build();

    }

    public void stopNotification(){

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

    }


}
