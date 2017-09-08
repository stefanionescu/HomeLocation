package com.location.home.app.di.modules;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.location.home.R;
import com.location.home.app.di.scopes.ServiceScope;
import com.location.home.ui.activities.MainActivity;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.NOTIFICATION_SERVICE;

@Module
public class ServiceModule {

    private Context context;

    public ServiceModule(Context context) {

        this.context = context;

    }

    @Provides
    @ServiceScope
    public Notification.Builder provideNotification() {

        Intent showTaskIntent = new Intent(context, MainActivity.class);

        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                showTaskIntent,
                0);

        return new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent);


    }

    @Provides
    @ServiceScope
    public android.app.NotificationManager provideManager() {

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        return notificationManager;

    }

}
