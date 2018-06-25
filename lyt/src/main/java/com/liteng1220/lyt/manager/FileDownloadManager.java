package com.liteng1220.lyt.manager;

import android.content.Context;

import com.liteng1220.lyt.BuildConfig;
import com.liteng1220.lyt.utility.FileUtil;
import com.liteng1220.lyt.utility.ILog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FileDownloadManager {

    private Context context;
    private FileDownloadListener fileDownloadListener;
    private Map<String, Call> downloadCallMap;
    private OkHttpClient okHttpClient;
    private DecimalFormat decimalFormat = new DecimalFormat("##0.0");

    public FileDownloadManager(Context context, FileDownloadListener fileDownloadListener) {
        this.context = context;
        this.fileDownloadListener = fileDownloadListener;
        downloadCallMap = new HashMap<>();
        okHttpClient = OkHttpManager.getNormalClient();
    }

    public void download(final String url, String folderPath, String filename) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File file = new File(folderPath, filename);
        long downloadLength = file.length();

        Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + downloadLength + "-")
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        downloadCallMap.put(url, call);

        InputStream is = null;
        FileOutputStream fos = null;

        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                if (fileDownloadListener != null) {
                    HandlerManager.getInstance().postUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fileDownloadListener.onFail(url);
                        }
                    }, 500);
                }
                return;
            }

            long contentLength = responseBody.contentLength() + downloadLength;
            is = responseBody.byteStream();
            fos = new FileOutputStream(file, true);

            byte[] buffer = new byte[1024 * 8];
            int len;
            while ((len = is.read(buffer)) != -1) {
                if (contentLength > 0 && fileDownloadListener != null) {
                    downloadLength += len;
                    final float percent = downloadLength * 100f / contentLength;
                    HandlerManager.getInstance().postThread(new Runnable() {
                        @Override
                        public void run() {
                            fileDownloadListener.onProgressChange(url, decimalFormat.format(percent));
                        }
                    }, 0);
                }

                fos.write(buffer, 0, len);
            }

            fos.flush();

            downloadCallMap.remove(url);

            if (fileDownloadListener != null) {
                final String mimeType = FileUtil.getMimeType(filename);
                HandlerManager.getInstance().postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fileDownloadListener.onSucceed(url, file.getAbsolutePath(), mimeType);
                    }
                }, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG) {
                ILog.error("FileDownloadManager.download()", e);
            }
            if (fileDownloadListener != null) {
                HandlerManager.getInstance().postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fileDownloadListener.onFail(url);
                    }
                }, 500);
            }
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (downloadCallMap != null) {
            Collection<Call> values = downloadCallMap.values();
            for (Call call : values) {
                call.cancel();
            }
            downloadCallMap.clear();
        }
    }

    public interface FileDownloadListener {
        /**
         * 下载进度通知，回调于子线程
         * @param url
         * @param percent
         */
        void onProgressChange(String url, String percent);

        /**
         * 下载完成，回调于主线程
         * @param url
         * @param filePath
         * @param mimeType
         */
        void onSucceed(String url, String filePath, String mimeType);

        /**
         * 下载失败，回调于主线程
         * @param url
         */
        void onFail(String url);
    }
}
