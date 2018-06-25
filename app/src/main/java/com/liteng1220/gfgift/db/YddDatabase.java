package com.liteng1220.gfgift.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {VideoDownload.class}, version = 1)
public abstract class YddDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "YDD";
    private static YddDatabase instance;

    public abstract VideoDownloadDao videoDownloadDaoDao();

    public static YddDatabase getInstance(Context context) {
        synchronized (YddDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), YddDatabase.class, DATABASE_NAME).build();
            }
            return instance;
        }
    }

}
