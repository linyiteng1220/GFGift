package com.liteng1220.gfgift.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

public class LocalVideoDownloadDataSource implements VideoDownloadDataSource {

    private static volatile LocalVideoDownloadDataSource instance;

    private VideoDownloadDao videoDownloadDao;

    @VisibleForTesting
    LocalVideoDownloadDataSource(VideoDownloadDao videoDownloadDao) {
        this.videoDownloadDao = videoDownloadDao;
    }

    public static LocalVideoDownloadDataSource getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (LocalVideoDownloadDataSource.class) {
                if (instance == null) {
                    YddDatabase database = YddDatabase.getInstance(context);
                    instance = new LocalVideoDownloadDataSource(database.videoDownloadDaoDao());
                }
            }
        }
        return instance;
    }

    @Override
    public VideoDownload getVideoDownload(String name) {
        return videoDownloadDao.getVideoDownload(name);
    }

    @Override
    public List<VideoDownload> queryVideoDownload() {
        return videoDownloadDao.queryVideoDownload();
    }

    @Override
    public void createVideoDownload(VideoDownload videoDownload) {
        videoDownloadDao.createVideoDownload(videoDownload);
    }

    @Override
    public void updateVideoDownloadUrl(String url, String uuid) {
        videoDownloadDao.updateVideoDownloadUrl(url, uuid);
    }

    @Override
    public void deleteAllVideoDownload() {
        videoDownloadDao.deleteAllVideoDownload();
    }

    @Override
    public void deleteVideoDownload(String url) {
        videoDownloadDao.deleteVideoDownload(url);
    }

}
