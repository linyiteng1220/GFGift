package com.liteng1220.lyt.listener;

public interface FileDownloadObserver {

    void onFinished(String url, String filePath, String mimeType);

    void onFailed(String url);

    void onAllFinished();

}
