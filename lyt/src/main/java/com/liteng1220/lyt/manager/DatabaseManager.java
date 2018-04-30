package com.liteng1220.lyt.manager;

import android.content.ContentValues;
import android.database.Cursor;

import com.liteng1220.lyt.sqlite.DatabaseHelper;
import com.liteng1220.lyt.sqlite.TableFileDownload;
import com.liteng1220.lyt.vo.FileDownloadVo;

public class DatabaseManager {

    public static long createFileDownloadRecord(FileDownloadVo fileDownloadVo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFileDownload.COLUMN_URL, fileDownloadVo.url);
        contentValues.put(TableFileDownload.COLUMN_FILE_PATH, fileDownloadVo.filePath);
        contentValues.put(TableFileDownload.COLUMN_FILENAME, fileDownloadVo.filename);
        return DatabaseHelper.getInstance().getDatabase().insert(TableFileDownload.TABLE_NAME, null, contentValues);
    }

    public static boolean deleteFileDownloadRecord(String url) {
        int line = DatabaseHelper.getInstance().getDatabase().delete(TableFileDownload.TABLE_NAME, TableFileDownload.COLUMN_URL + " = ?", new String[]{url});
        return line > 0;
    }

    public static FileDownloadVo queryFileDownloadRecord(String url) {
        FileDownloadVo fileDownloadVo = null;
        Cursor cursor = DatabaseHelper.getInstance().getDatabase().query(TableFileDownload.TABLE_NAME, null, TableFileDownload.COLUMN_URL + " = ?", new String[]{url}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToNext()) {
                fileDownloadVo = new FileDownloadVo();
                fileDownloadVo.url = cursor.getString(cursor.getColumnIndex(TableFileDownload.COLUMN_URL));
                fileDownloadVo.filePath = cursor.getString(cursor.getColumnIndex(TableFileDownload.COLUMN_FILE_PATH));
                fileDownloadVo.filename = cursor.getString(cursor.getColumnIndex(TableFileDownload.COLUMN_FILENAME));
            }
            cursor.close();
        }
        return fileDownloadVo;
    }
}


