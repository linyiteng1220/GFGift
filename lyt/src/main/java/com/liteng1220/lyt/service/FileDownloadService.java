package com.liteng1220.lyt.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.listener.FileDownloadSubject;
import com.liteng1220.lyt.manager.DatabaseManager;
import com.liteng1220.lyt.manager.FileDownloadManager;
import com.liteng1220.lyt.manager.HandlerManager;
import com.liteng1220.lyt.manager.NotificationManager;
import com.liteng1220.lyt.manager.ThreadManager;
import com.liteng1220.lyt.utility.FileUtil;
import com.liteng1220.lyt.vo.FileDownloadVo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileDownloadService extends Service {

    public static final String SERVICE_BUNDLE_KEY_TITLE = "title";
    public static final String SERVICE_BUNDLE_KEY_FOLDER_PATH = "folderPath";
    public static final String SERVICE_BUNDLE_KEY_FILENAME = "filename";
    public static final String SERVICE_BUNDLE_KEY_URL = "url";
    public static final String SERVICE_BUNDLE_KEY_ICON_RES_ID = "iconResId";

    private FileDownloadManager fileDownloadManager;
    private Map<String, NotificationData> notificationDataMap;
    private BlockingQueue<DownloadData> downloadQueue;
    private boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isRunning = true;
        notificationDataMap = new HashMap<>();
        downloadQueue = new LinkedBlockingQueue<>();

        fileDownloadManager = new FileDownloadManager(getApplicationContext(), new FileDownloadManager.FileDownloadListener() {
            @Override
            public void onProgressChange(String url, String percent) {
                NotificationData notificationData = notificationDataMap.get(url);
                if (notificationData != null) {
                    String desc = getString(R.string.download_progress, percent);
                    Notification notification = NotificationManager.createNormalNotification(notificationData.title, desc, notificationData.iconResId, true, null);
                    NotificationManager.showNotification(notificationData.id, notification);
                    // TODO: 2017/12/4 点击弹框给用户确认是否停止下载
                }
            }

            @Override
            public void onSucceed(final String url, String filePath, String mimeType) {
                HandlerManager.getInstance().postThread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseManager.deleteFileDownloadRecord(url);
                    }
                });

                FileDownloadSubject.getInstance().notifyFinished(url, filePath, mimeType);
                NotificationData notificationData = notificationDataMap.get(url);
                if (notificationData != null) {
                    String desc = getString(R.string.download_success);
                    Intent resultIntent = FileUtil.getIntentFromFile(getApplicationContext(), new File(filePath), mimeType);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    Notification notification = NotificationManager.createNormalNotification(notificationData.title, desc, notificationData.iconResId, true, pendingIntent);
                    NotificationManager.showNotification(notificationData.id, notification);
                }
            }

            @Override
            public void onFail(String url) {
                FileDownloadSubject.getInstance().notifyFail(url);
                NotificationData notificationData = notificationDataMap.get(url);
                if (notificationData != null) {
                    String desc = getString(R.string.download_failed);
                    Notification notification = NotificationManager.createNormalNotification(notificationData.title, desc, notificationData.iconResId, true, null);
                    NotificationManager.showNotification(notificationData.id, notification);
                }
            }
        });

        startDownloading();
    }

    private void startDownloading() {
        ThreadManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        DownloadData downloadData = downloadQueue.take();
                        handleDownloadRecord(downloadData);
                        fileDownloadManager.download(downloadData.url, downloadData.folderPath, downloadData.filename);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void handleDownloadRecord(DownloadData downloadData) {
        File file = new File(downloadData.folderPath, downloadData.filename);
        FileDownloadVo fileDownloadVo = DatabaseManager.queryFileDownloadRecord(downloadData.url);
        if (fileDownloadVo != null) {
            if (fileDownloadVo.filePath != null) {
                if (!fileDownloadVo.filePath.equals(file.getAbsolutePath())) {
                    DatabaseManager.deleteFileDownloadRecord(downloadData.url);
                    DatabaseManager.createFileDownloadRecord(new FileDownloadVo(downloadData.url, downloadData.filename, file.getAbsolutePath()));
                }
            }
        } else {
            DatabaseManager.createFileDownloadRecord(new FileDownloadVo(downloadData.url, downloadData.filename, file.getAbsolutePath()));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String title = bundle.getString(SERVICE_BUNDLE_KEY_TITLE);
            final String folderPath = bundle.getString(SERVICE_BUNDLE_KEY_FOLDER_PATH);
            final String filename = bundle.getString(SERVICE_BUNDLE_KEY_FILENAME);
            final String url = bundle.getString(SERVICE_BUNDLE_KEY_URL);
            final int iconResId = bundle.getInt(SERVICE_BUNDLE_KEY_ICON_RES_ID);

            int notificationId = createNotificationId();
            NotificationData notificationData = new NotificationData(notificationId, title, iconResId);
            notificationDataMap.put(url, notificationData);

            addDownloadTaskIfNew(new DownloadData(url, folderPath, filename));
        }
        return START_REDELIVER_INTENT;
    }

    private void addDownloadTaskIfNew(DownloadData downloadData) {
        try {
            if (!existDownloadData(downloadData)) {
                downloadQueue.put(downloadData);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean existDownloadData(DownloadData downloadData) {
        for (DownloadData data : downloadQueue) {
            if (data.url != null && data.url.equals(downloadData.url)) {
                return true;
            }
        }
        return false;
    }

    private int createNotificationId() {
//        long systemTime = System.currentTimeMillis();
//        String strSystemTime = String.valueOf(systemTime);
//        int len = strSystemTime.length();
//        return Integer.parseInt(strSystemTime.substring(len - 8, len));
        return NotificationManager.NOTIFICATION_DEFAULT_ID * 10;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isRunning = false;
        removeAllNotification();
        notificationDataMap.clear();
        downloadQueue.clear();
        fileDownloadManager.stop();
    }

    private void removeAllNotification() {
//        Collection<NotificationData> notificationDataCollection = notificationDataMap.values();
//        for (NotificationData notificationData : notificationDataCollection) {
//            if (notificationData != null) {
//                NotificationManager.removeNotification(notificationData.id);
//            }
//        }
        NotificationManager.removeNotification(createNotificationId());
    }

    private static class NotificationData {
        public int id;
        public String title;
        public int iconResId;

        public NotificationData(int id, String title, int iconResId) {
            this.id = id;
            this.title = title;
            this.iconResId = iconResId;
        }
    }

    private static class DownloadData {
        public String url;
        public String folderPath;
        public String filename;

        public DownloadData(String url, String folderPath, String filename) {
            this.url = url;
            this.folderPath = folderPath;
            this.filename = filename;
        }
    }
}
