package com.liteng1220.gfgift.listener;

import com.liteng1220.gfgift.db.VideoDownload;

public interface VideoSqliteDataObserver {

    void onDataAdded(VideoDownload videoDownload);

}
