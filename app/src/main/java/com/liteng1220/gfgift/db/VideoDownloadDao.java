package com.liteng1220.gfgift.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface VideoDownloadDao {

    @Query("SELECT * FROM " + VideoDownload.TABLE_NAME + " WHERE n = :name")
    VideoDownload getVideoDownload(String name);

    @Query("SELECT * FROM " + VideoDownload.TABLE_NAME)
    List<VideoDownload> queryVideoDownload();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createVideoDownload(VideoDownload videoDownload);

    @Query("UPDATE " + VideoDownload.TABLE_NAME + " SET u = :url WHERE uu = :uuid")
    void updateVideoDownloadUrl(String url, String uuid);

    @Query("DELETE FROM " + VideoDownload.TABLE_NAME)
    void deleteAllVideoDownload();

    @Query("DELETE FROM " + VideoDownload.TABLE_NAME + " WHERE u = :url")
    void deleteVideoDownload(String url);

}
