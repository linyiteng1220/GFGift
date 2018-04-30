package com.liteng1220.lyt.listener;

import java.util.ArrayList;
import java.util.List;

public class FileDownloadSubject {

    private static volatile FileDownloadSubject instance;
    private List<FileDownloadObserver> observerList;

    private FileDownloadSubject() {
        observerList = new ArrayList<FileDownloadObserver>();
    }

    public static FileDownloadSubject getInstance() {
        if (instance == null) {
            synchronized (FileDownloadSubject.class) {
                if (instance == null) {
                    instance = new FileDownloadSubject();
                }
            }
        }
        return instance;
    }

    public void registerObserver(FileDownloadObserver observer) {
        unregisterObserver(observer);
        observerList.add(observer);
    }

    public void unregisterObserver(FileDownloadObserver observer) {
        if (observer == null) {
            return;
        }

        for (int k = 0; k < observerList.size(); k++) {
            FileDownloadObserver item = observerList.get(k);
            if (item != null && item.getClass().getName().equals(observer.getClass().getName())) {
                observerList.remove(item);
                break;
            }
        }
    }

    public void notifyFinished(String url, String filePath, String mimeType) {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            FileDownloadObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onFinished(url, filePath, mimeType);
            }
        }
    }

    public void notifyFail(String url) {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            FileDownloadObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onFailed(url);
            }
        }
    }

    public void notifyAllFinished() {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            FileDownloadObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onAllFinished();
            }
        }
    }

}
