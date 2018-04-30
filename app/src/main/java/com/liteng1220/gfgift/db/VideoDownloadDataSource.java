package com.liteng1220.gfgift.db;

import java.util.List;

public interface VideoDownloadDataSource {

    VideoDownload getVideoDownload(String name);

    List<VideoDownload> queryVideoDownload();

    void createVideoDownload(VideoDownload videoDownload);

    void updateVideoDownloadUrl(String url, String uuid);

    void deleteAllVideoDownload();

    void deleteVideoDownload(String url);
}
