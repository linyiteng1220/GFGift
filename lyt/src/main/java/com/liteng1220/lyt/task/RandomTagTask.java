package com.liteng1220.lyt.task;

import com.liteng1220.lyt.utility.LytEncryption;

import okhttp3.OkHttpClient;

public class RandomTagTask extends OkHttpTask {

    public RandomTagTask(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    public RandomTagTask() {
        super();
    }

    @Override
    protected String encryptJson(String json) {
        return LytEncryption.encryptJsonData(json);
    }

    @Override
    protected String decryptJson(String responseData) {
        return LytEncryption.decryptJsonData(responseData);
    }

}
