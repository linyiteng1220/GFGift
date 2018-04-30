package com.liteng1220.gfgift.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.liteng1220.gfgift.R;
import com.liteng1220.gfgift.manager.VideoDownloadManager;
import com.liteng1220.lyt.manager.NotificationManager;

public class VideoDownloadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        VideoDownloadManager.getInstance().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();

        return START_REDELIVER_INTENT;
    }

    private void showNotification() {
        String title = getString(R.string.notification_title);
        String desc = getString(R.string.notification_desc_downloading);
        int iconResId = R.drawable.ic_logo_small;

        Notification notification = NotificationManager.createServiceNotification(title, desc, iconResId, false, null);
        startForeground(NotificationManager.NOTIFICATION_DEFAULT_ID, notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.showNotification(NotificationManager.NOTIFICATION_DEFAULT_ID, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoDownloadManager.getInstance().stop();
    }
}
