package com.liteng1220.gfgift.repository;

import com.liteng1220.gfgift.db.VideoDownload;
import com.liteng1220.gfgift.db.VideoDownloadDataSource;
import com.liteng1220.lyt.manager.HandlerManager;

import java.util.List;

public class VideoDownloadRepository {

    private VideoDownloadDataSource dataSource;

    public VideoDownloadRepository(VideoDownloadDataSource userDataSource) {
        dataSource = userDataSource;
    }

    public void getVideoDownload(final String name, final VideoDownloadQueryCallback callback) {
        HandlerManager.getInstance().postThread(new Runnable() {
            @Override
            public void run() {
                final VideoDownload videoDownload = dataSource.getVideoDownload(name);

                HandlerManager.getInstance().postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onVideoDownloadQuery(videoDownload);
                        }
                    }
                });
            }
        });
    }

    public void createVideoDownload(final VideoDownload videoDownload, final VideoDownloadCreateCallback callback) {
        HandlerManager.getInstance().postThread(new Runnable() {
            @Override
            public void run() {
                dataSource.createVideoDownload(videoDownload);
                HandlerManager.getInstance().postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onVideoDownloadCreate();
                        }
                    }
                });
            }
        });
    }

    public void queryVideoDownloadList(final VideoDownloadListQueryCallback callback) {
        HandlerManager.getInstance().postThread(new Runnable() {
            @Override
            public void run() {
                final List<VideoDownload> videoDownloadList = dataSource.queryVideoDownload();

                HandlerManager.getInstance().postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onVideoDownloadListQuery(videoDownloadList);
                        }
                    }
                });
            }
        });
    }

    public void updateVideoDownloadUrl(final String url, final String uuid) {
        HandlerManager.getInstance().postThread(new Runnable() {
            @Override
            public void run() {
                dataSource.updateVideoDownloadUrl(url, uuid);
            }
        });
    }

    public void deleteVideoDownload(final String url) {
        HandlerManager.getInstance().postThread(new Runnable() {
            @Override
            public void run() {
                dataSource.deleteVideoDownload(url);
            }
        });
    }

    public void deleteAllVideoDownload() {
        HandlerManager.getInstance().postThread(new Runnable() {
            @Override
            public void run() {
                dataSource.deleteAllVideoDownload();
            }
        });
    }

    public interface VideoDownloadCreateCallback {
        void onVideoDownloadCreate();
    }

    public interface VideoDownloadQueryCallback {
        void onVideoDownloadQuery(VideoDownload videoDownload);
    }

    public interface VideoDownloadListQueryCallback {
        void onVideoDownloadListQuery(List<VideoDownload> videoDownloadList);
    }
}
