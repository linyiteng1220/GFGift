package com.liteng1220.lyt.task;

import com.liteng1220.lyt.BuildConfig;
import com.liteng1220.lyt.manager.HandlerManager;
import com.liteng1220.lyt.manager.OkHttpManager;
import com.liteng1220.lyt.utility.ILog;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class OkHttpTask implements BaseHttpTask {

    private String url;
    private static OkHttpClient client;

    public OkHttpTask() {
        if (client == null) {
            client = OkHttpManager.getNormalClient();
        }
    }

    public OkHttpTask(OkHttpClient okHttpClient) {
        client = okHttpClient;
    }

    public final void sendHttpRequest(String url, String jsonData, final HttpCallback callback) {
        if (url == null) {
            return;
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (jsonData != null) {
            if (BuildConfig.DEBUG) {
                ILog.warn("BaseHttpTask.sendHttpRequest()  #  " + url + " original data >>> " + jsonData);
            }
            String requestJsonEncrypted = encryptJson(jsonData);
            if (BuildConfig.DEBUG) {
                ILog.warn("BaseHttpTask.sendHttpRequest()  #  " + url + " encrypted data >>> " + requestJsonEncrypted);
            }

            builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestJsonEncrypted));
        }

        this.url = url;

        Request request = builder.build();
        client.newCall(request).enqueue(new BaseHttpCallback(callback, this));
    }

    protected abstract String encryptJson(String json);

    protected abstract String decryptJson(String responseData);

    public static class BaseHttpCallback implements Callback {
        HttpCallback callback;
        OkHttpTask baseCmdHttpTask;

        public BaseHttpCallback(HttpCallback callback, OkHttpTask baseCmdHttpTask) {
            this.callback = callback;
            this.baseCmdHttpTask = baseCmdHttpTask;
        }

        @Override
        public void onFailure(Call call, final IOException ioe) {
            if (callback != null) {
                if (BuildConfig.DEBUG) {
                    ioe.printStackTrace();
                    ILog.error("BaseHttpCallback.onFail()", ioe);
                }

                if (ioe instanceof ConnectException || ioe instanceof SocketException || ioe instanceof UnknownHostException
                        || ioe instanceof ConnectTimeoutException || ioe instanceof SocketTimeoutException) {
                    HandlerManager.getInstance().postUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onNetworkException(ioe);
                        }
                    });
                } else {
                    HandlerManager.getInstance().postUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(ioe);
                        }
                    });
                }
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (callback != null && baseCmdHttpTask != null) {
                if (response != null) {
                    ResponseBody responseBody = response.body();
                    try {
                        if (responseBody != null) {
                            String result = responseBody.string();

                            if (BuildConfig.DEBUG) {
                                ILog.warn("ResponseListener.onResponse()  #  " + baseCmdHttpTask.url + " original data <<< " + result);
                            }
                            final String resultDecrypted = baseCmdHttpTask.decryptJson(result);
                            if (BuildConfig.DEBUG) {
                                ILog.warn("ResponseListener.onResponse()  #  " + baseCmdHttpTask.url + " decrypted data <<< " + resultDecrypted);
                            }

                            HandlerManager.getInstance().postUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResponse(resultDecrypted);
                                }
                            });
                        }
                    } catch (final Exception e) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                            ILog.error("BaseHttpCallback.onResponse()", e);
                        }
                        HandlerManager.getInstance().postUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail(e);
                            }
                        });
                    } finally {
                        if (responseBody != null) {
                            responseBody.close();
                        }
                    }
                } else {
                    final Exception exception = new Exception("Server Error!!!");
                    if (BuildConfig.DEBUG) {
                        exception.printStackTrace();
                        ILog.error("BaseHttpCallback.onResponse()", exception);
                    }
                    HandlerManager.getInstance().postUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(exception);
                        }
                    });
                }
            }
        }
    }
}