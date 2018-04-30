package com.liteng1220.gfgift.manager;

import android.app.Notification;
import android.content.Intent;
import android.text.TextUtils;

import com.liteng1220.gfgift.GfApplication;
import com.liteng1220.gfgift.R;
import com.liteng1220.gfgift.cmd.Cmd;
import com.liteng1220.gfgift.cmd.CmdGetSactionInfo;
import com.liteng1220.gfgift.db.LocalVideoDownloadDataSource;
import com.liteng1220.gfgift.db.VideoDownload;
import com.liteng1220.gfgift.listener.VideoSqliteDataObserver;
import com.liteng1220.gfgift.listener.VideoSqliteDataSubject;
import com.liteng1220.gfgift.repository.VideoDownloadRepository;
import com.liteng1220.lyt.BuildConfig;
import com.liteng1220.lyt.listener.FileDownloadObserver;
import com.liteng1220.lyt.listener.FileDownloadSubject;
import com.liteng1220.lyt.manager.HandlerManager;
import com.liteng1220.lyt.manager.NotificationManager;
import com.liteng1220.lyt.service.FileDownloadService;
import com.liteng1220.lyt.task.BaseHttpTask;
import com.liteng1220.lyt.utility.ILog;

import java.util.List;

public class VideoDownloadManager implements FileDownloadObserver, VideoSqliteDataObserver {

    private volatile static VideoDownloadManager instance;

    private VideoDownloadRepository repository;

    private VideoDownloadManager() {
        repository = new VideoDownloadRepository(LocalVideoDownloadDataSource.getInstance(GfApplication.getContext()));
    }

    public static VideoDownloadManager getInstance() {
        if (instance == null) {
            synchronized (VideoDownloadManager.class) {
                if (instance == null) {
                    instance = new VideoDownloadManager();
                }
            }
        }
        return instance;
    }

    public void start() {
        FileDownloadSubject.getInstance().registerObserver(this);
        VideoSqliteDataSubject.getInstance().registerObserver(this);

        GfApplication.getContext().startService(new Intent(GfApplication.getContext(), FileDownloadService.class));

        repository.queryVideoDownloadList(new VideoDownloadRepository.VideoDownloadListQueryCallback() {
            @Override
            public void onVideoDownloadListQuery(List<VideoDownload> videoDownloadList) {
                for (final VideoDownload videoDownload : videoDownloadList) {
                    HandlerManager.getInstance().postUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(videoDownload.url)) {
                                getVideoRealUrl(videoDownload);
                            } else {
                                startService2DownloadVideo(createDownloadTitle(videoDownload), videoDownload.folderPath, videoDownload.filename, videoDownload.url);
                            }
                        }
                    }, 1000);
                }
            }
        });
    }

    private void showAllDownloadFinishNotification() {
        String title = GfApplication.getContext().getString(R.string.notification_title);
        String desc = GfApplication.getContext().getString(R.string.notification_desc_downloaded);
        int iconResId = R.drawable.ic_logo_small;

        Notification notification = NotificationManager.createNormalNotification(title, desc, iconResId, false, null);
        NotificationManager.showNotification(NotificationManager.NOTIFICATION_DEFAULT_ID, notification);
    }

    private void getVideoRealUrl(final VideoDownload videoDownload) {
        final Cmd cmd = new CmdGetSactionInfo();
        cmd.sendHttpRequest(new BaseHttpTask.HttpCallback() {
            @Override
            public void onResponse(String responseResult) {
                if (responseResult != null) {
                    CmdGetSactionInfo.Results results = (CmdGetSactionInfo.Results) cmd.parseResponseJson(responseResult);
                    if (results.status) {
                        if (results.data != null) {
                            if (results.data instanceof CmdGetSactionInfo.InfoData) {
                                CmdGetSactionInfo.InfoData infoData = (CmdGetSactionInfo.InfoData) results.data;
                                videoDownload.url = infoData.oriUrl;
                                repository.updateVideoDownloadUrl(infoData.oriUrl, videoDownload.uuid);
                                startService2DownloadVideo(createDownloadTitle(videoDownload), videoDownload.folderPath, videoDownload.filename, infoData.oriUrl);
                            }
                        }
                    } else {
                        if (BuildConfig.DEBUG) {
                            ILog.error("VideoDownloadManager.onResponse()", new Exception(results.msg));
                        }
                    }
                }
            }

            @Override
            public void onNetworkException(Exception exception) {
            }

            @Override
            public void onFail(Exception exception) {
            }
        }, videoDownload.uuid);
    }

    private String createDownloadTitle(VideoDownload curVideoDownload) {
        if (curVideoDownload == null) {
            return null;
        }

        return curVideoDownload.name + " (" + curVideoDownload.index + " / " + curVideoDownload.totalNum + ")";
    }

    private void startService2DownloadVideo(String title, String folderPath, String filename, String url) {
        Intent intent = new Intent(GfApplication.getContext(), FileDownloadService.class);
        intent.putExtra(FileDownloadService.SERVICE_BUNDLE_KEY_TITLE, title);
        intent.putExtra(FileDownloadService.SERVICE_BUNDLE_KEY_FOLDER_PATH, folderPath);
        intent.putExtra(FileDownloadService.SERVICE_BUNDLE_KEY_FILENAME, filename);
        intent.putExtra(FileDownloadService.SERVICE_BUNDLE_KEY_URL, url);
        intent.putExtra(FileDownloadService.SERVICE_BUNDLE_KEY_ICON_RES_ID, R.drawable.ic_logo_small);
        GfApplication.getContext().startService(intent);
    }

    @Override
    public void onFinished(String url, String filePath, String mimeType) {
        repository.deleteVideoDownload(url);
    }

    @Override
    public void onFailed(String url) {
        if (BuildConfig.DEBUG) {
            ILog.warn("VideoDownloadManager.onFailed() >>> can not download video: " + url);
        }
    }

    @Override
    public void onAllFinished() {
        showAllDownloadFinishNotification();
    }

    @Override
    public void onDataAdded(VideoDownload videoDownload) {
        if (TextUtils.isEmpty(videoDownload.url)) {
            getVideoRealUrl(videoDownload);
        } else {
            startService2DownloadVideo(createDownloadTitle(videoDownload), videoDownload.folderPath, videoDownload.filename, videoDownload.url);
        }
    }

    public void stop() {
        FileDownloadSubject.getInstance().unregisterObserver(this);
        VideoSqliteDataSubject.getInstance().unregisterObserver(this);

        GfApplication.getContext().stopService(new Intent(GfApplication.getContext(), FileDownloadService.class));
    }
}
