package com.liteng1220.lyt.manager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.liteng1220.lyt.BuildConfig;

import java.util.Random;

public class HandlerManager {

    private volatile static HandlerManager instance;
    private HandlerThread handlerThread;
    private Handler uiHandler; // UI主线程
    private Handler handler; // 子线程

    private HandlerManager() {
        handlerThread = new HandlerThread(BuildConfig.APPLICATION_ID);
        handlerThread.start();

        uiHandler = new Handler(Looper.getMainLooper());
        handler = new Handler(handlerThread.getLooper());
    }

    public static HandlerManager getInstance() {
        if (instance == null) {
            synchronized (HandlerManager.class) {
                if (instance == null) {
                    instance = new HandlerManager();
                }
            }
        }
        return instance;
    }

    public void recycle() {
        uiHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        handlerThread.quitSafely();
        instance = null;
    }

    public void postUiThread(Runnable runnable) {
        long delay = new Random().nextInt(100); // 必须添加随机延时，防止同一时间多个调用导致异常：android.os.DeadObjectException
        postUiThread(runnable, delay);
    }

    public void postUiThread(Runnable runnable, long delay) {
        uiHandler.postDelayed(runnable, delay);
    }

    public void postThread(Runnable runnable) {
        long delay = new Random().nextInt(100);
        postThread(runnable, delay);
    }

    public void postThread(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }
}
