package com.liteng1220.lyt.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.liteng1220.lyt.AppContext;

public class NotificationManager {
    public static final int NOTIFICATION_DEFAULT_ID = 666;
    public static final String NOTIFICATION_GROUP_KEY = "LYT_NOTIFICATION";

    public static void showNotification(int notificationId, String title, String content, int iconResId, boolean isSilent, PendingIntent pendingIntent) {
        Notification notification = createNormalNotification(title, content, iconResId, isSilent, pendingIntent);
        showNotification(notificationId, notification);
    }

    public static void showNotification(int notificationId, Notification notification) {
        NotificationManagerCompat.from(AppContext.getContext()).notify(notificationId, notification);
    }

    public static void removeNotification(final int notificationId) {
        Context context = AppContext.getContext();
        if (context != null) {
            NotificationManagerCompat.from(context).cancel(notificationId);
        }
    }

    public static Notification createServiceNotification(String title, String content, int iconResId, boolean isSilent, PendingIntent pendingIntent) {
        Notification notification = createNormalNotification(title, content, iconResId, isSilent, pendingIntent);

        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }

    public static Notification createNormalNotification(String title, String content, int iconResId, boolean isSilent, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.getContext())
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(iconResId)
                .setLocalOnly(true)
                .setTicker(title)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (!isSilent) {
            builder.setVibrate(new long[]{100, 300, 200, 300, 200, 300});
        } else {
            builder.setVibrate(null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setGroupSummary(true);
            builder.setGroup(NOTIFICATION_GROUP_KEY);
        }

        return builder.build();
    }
}
