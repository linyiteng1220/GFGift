package com.liteng1220.gfgift.util;

import android.os.Environment;

import com.liteng1220.gfgift.conf.Constant;

import java.io.File;

public class YddUtil {

    public static String getFileDownloadPath() {
        return new File(Environment.getExternalStorageDirectory(), Constant.SDCARD_FOLDER_NAME).getAbsolutePath();
    }

}
