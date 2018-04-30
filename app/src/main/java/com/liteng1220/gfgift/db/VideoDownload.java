package com.liteng1220.gfgift.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = VideoDownload.TABLE_NAME)
public class VideoDownload {

    public static final String TABLE_NAME = "VD";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    public long id;

    @ColumnInfo(name = "n")
    public String name;

    @ColumnInfo(name = "uu")
    public String uuid;

    @ColumnInfo(name = "i")
    public int index;

    @ColumnInfo(name = "tn")
    public int totalNum;

    @ColumnInfo(name = "u")
    public String url;

    @ColumnInfo(name = "f")
    public String filename;

    @ColumnInfo(name = "fp")
    public String folderPath;

    @ColumnInfo(name = "ct")
    public long createTime;
}
