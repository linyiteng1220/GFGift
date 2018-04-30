package com.liteng1220.gfgift.listener;

import com.liteng1220.gfgift.db.VideoDownload;

import java.util.ArrayList;
import java.util.List;

public class VideoSqliteDataSubject {

    private static volatile VideoSqliteDataSubject instance;
    private List<VideoSqliteDataObserver> observerList;

    private VideoSqliteDataSubject() {
        observerList = new ArrayList<VideoSqliteDataObserver>();
    }

    public static VideoSqliteDataSubject getInstance() {
        if (instance == null) {
            synchronized (VideoSqliteDataSubject.class) {
                if (instance == null) {
                    instance = new VideoSqliteDataSubject();
                }
            }
        }
        return instance;
    }

    public void registerObserver(VideoSqliteDataObserver observer) {
        unregisterObserver(observer);
        observerList.add(observer);
    }

    public void unregisterObserver(VideoSqliteDataObserver observer) {
        if (observer == null) {
            return;
        }

        for (int k = 0; k < observerList.size(); k++) {
            VideoSqliteDataObserver item = observerList.get(k);
            if (item != null && item.getClass().getName().equals(observer.getClass().getName())) {
                observerList.remove(item);
                break;
            }
        }
    }

    public void notifyDataAdded(VideoDownload videoDownload) {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            VideoSqliteDataObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onDataAdded(videoDownload);
            }
        }
    }

}
