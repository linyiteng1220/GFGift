package com.liteng1220.lyt.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

    private volatile static ThreadManager instance;
    private ExecutorService executorService;

    private ThreadManager() {
        executorService = Executors.newCachedThreadPool();
    }

    public static ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

}
