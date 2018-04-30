package com.liteng1220.lyt.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.liteng1220.lyt.AppContext;
import com.liteng1220.lyt.BuildConfig;
import com.liteng1220.lyt.utility.ILog;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sd435651"; // lyt
    public static final int DB_VERSION = 1;
    private static DatabaseHelper instance;
    private static SQLiteDatabase db;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(AppContext.getContext());
                    db = instance.getWritableDatabase();
                }
            }
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG) {
            ILog.warn("DatabaseHelper.onUpgrade() >>> oldVersion = " + oldVersion + ", newVersion = " + newVersion);
        }
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(TableFileDownload.getCreateTableSql());
    }
}
