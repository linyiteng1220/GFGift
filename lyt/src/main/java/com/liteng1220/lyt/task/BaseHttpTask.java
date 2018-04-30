package com.liteng1220.lyt.task;

public interface BaseHttpTask {

    void sendHttpRequest(String url, String json, HttpCallback callback);

    interface HttpCallback {
        void onResponse(String responseResult);

        void onNetworkException(Exception exception);

        void onFail(Exception exception);
    }
}