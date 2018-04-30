package com.liteng1220.lyt.manager;

import com.liteng1220.lyt.task.BaseHttpTask;
import com.liteng1220.lyt.task.NonEncryptTask;
import com.liteng1220.lyt.task.RandomTagTask;

import okhttp3.OkHttpClient;

public class HttpManager {

    public static void sendRandomTagRequest(String url, String json, BaseHttpTask.HttpCallback callback) {
        new RandomTagTask().sendHttpRequest(url, json, callback);
    }

    public static void sendRandomTagRequest(OkHttpClient okHttpClient, String url, String json, BaseHttpTask.HttpCallback callback) {
        new RandomTagTask(okHttpClient).sendHttpRequest(url, json, callback);
    }

    public static void sendNoEncryptRequest(String url, String json, BaseHttpTask.HttpCallback callback) {
        new NonEncryptTask().sendHttpRequest(url, json, callback);
    }

    public static void sendNoEncryptRequest(OkHttpClient okHttpClient, String url, String json, BaseHttpTask.HttpCallback callback) {
        new NonEncryptTask(okHttpClient).sendHttpRequest(url, json, callback);
    }
}
