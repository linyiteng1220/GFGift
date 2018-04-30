package com.liteng1220.lyt.task;

import okhttp3.OkHttpClient;

public class NonEncryptTask extends OkHttpTask {

    public NonEncryptTask(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    public NonEncryptTask() {
        super();
    }

    @Override
    protected String encryptJson(String json) {
        return json;
    }

    @Override
    protected String decryptJson(String responseData) {
        return responseData;
    }

}
