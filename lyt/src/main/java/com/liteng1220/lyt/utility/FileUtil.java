package com.liteng1220.lyt.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.liteng1220.lyt.BuildConfig;

import java.io.File;

public class FileUtil {

    public static File getFileCacheFolder(String type, String folder, Context context) {
        String sdcardPath = getSdcardFilePath(type, folder, context);
        String internalStoragePath = getInternalStorageFilePath(folder, context);
        return getCacheFolder(sdcardPath, internalStoragePath);
    }

    private static String getSdcardFilePath(String type, String folder, Context context) {
        String path = null;
        File cacheFolder = context.getExternalFilesDir(type);
        if (cacheFolder != null) {
            path = cacheFolder.getAbsolutePath();
            if (folder != null) {
                path += (File.separator + folder);
            }
        }
        return path;
    }

    private static String getInternalStorageFilePath(String folder, Context context) {
        String path = context.getFilesDir().getAbsolutePath();
        if (folder != null) {
            path += (File.separator + folder);
        }
        return path;
    }

    private static File getCacheFolder(String sdcardPath, String internalStoragePath) {
        File cacheFolder = null;
        if (isSDCardAvailable() && sdcardPath != null) {
            cacheFolder = new File(sdcardPath);
        } else {
            cacheFolder = new File(internalStoragePath);
        }

        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
        return cacheFolder;
    }

    private static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String createFilename(String url, String name) {
        if (url == null || name == null) {
            return null;
        }

        int index = url.lastIndexOf(".");
        if (index == -1) {
            return null;
        }

        String fileExt = url.substring(index + 1, url.length());
        return name + "." + fileExt;
    }

    public static String createFilePath(String folder, String filename) {
        if (folder == null || filename == null) {
            return null;
        }

        return new File(folder, filename).getAbsolutePath();
    }

    public static String getFilename(String url) {
        if (url == null)
            return null;

        int index = url.lastIndexOf("/");
        String filename = url.substring(index + 1);
        return filename;
    }

    public static Intent getIntentFromFile(Context context, File file, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.liteng1220.lyt.provider", file);
            intent.setDataAndType(contentUri, mimeType);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), mimeType);
        }
        return intent;
    }

    public static String getMimeType(String filename) {
        final String DEFAULT_TYPE = "*/*";
        if (filename == null) {
            return DEFAULT_TYPE;
        }

        int index = filename.lastIndexOf(".");
        if (index == -1) {
            return DEFAULT_TYPE;
        }

        String ext = filename.substring(index + 1);
        if ("apk".equalsIgnoreCase(ext)) {
            return "application/vnd.android.package-archive";
        } else if ("doc".equalsIgnoreCase(ext) || "docx".equalsIgnoreCase(ext)) {
            return "application/msword";
        } else if ("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext)) {
            return "application/msexcel";
        } else if ("ppt".equalsIgnoreCase(ext) || "pptx".equalsIgnoreCase(ext)) {
            return "application/mspowerpoint";
        } else if ("pdf".equalsIgnoreCase(ext)) {
            return "application/pdf";
        } else if ("bmp".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext)) {
            return "image/*";
        } else if ("flac".equalsIgnoreCase(ext) || "mp3".equalsIgnoreCase(ext) || "m3u".endsWith(ext) || "m4p".equalsIgnoreCase(ext) || "m4a".equalsIgnoreCase(ext)
                || "m4b".equalsIgnoreCase(ext) || "ape".equalsIgnoreCase(ext) || "mp2".equalsIgnoreCase(ext) || "wav".equalsIgnoreCase(ext)) {
            return "audio/*";
        } else if ("3gp".equalsIgnoreCase(ext) || "asf".equalsIgnoreCase(ext) || "avi".equalsIgnoreCase(ext) || "flv".equalsIgnoreCase(ext) || "m4u".equalsIgnoreCase(ext)
                || "m4v".equalsIgnoreCase(ext) || "mkv".equalsIgnoreCase(ext) || "mov".equalsIgnoreCase(ext) || "mp4".equalsIgnoreCase(ext) || "mpe".equalsIgnoreCase(ext)
                || "mpeg".equalsIgnoreCase(ext) || "mpg".equalsIgnoreCase(ext) || "mpg4".equalsIgnoreCase(ext) || "rmvb".equalsIgnoreCase(ext)) {
            return "video/*";
        } else if ("java".equalsIgnoreCase(ext) || "conf".equalsIgnoreCase(ext) || "htm".equalsIgnoreCase(ext) || "html".equalsIgnoreCase(ext)
                || "shtml".equalsIgnoreCase(ext) || "log".equalsIgnoreCase(ext) || "prop".equalsIgnoreCase(ext) || "txt".equalsIgnoreCase(ext)
                || "xml".equalsIgnoreCase(ext) || "js".equalsIgnoreCase(ext) || "css".equalsIgnoreCase(ext) || "jsp".equalsIgnoreCase(ext)
                || "bak".equalsIgnoreCase(ext) || "properties".equalsIgnoreCase(ext)) {
            return "text/*";
        } else {
            return DEFAULT_TYPE;
        }
    }
}
