package com.liteng1220.gfgift.cmd;

import com.liteng1220.gfgift.util.Sign;
import com.liteng1220.lyt.manager.HttpManager;
import com.liteng1220.lyt.task.BaseHttpTask;
import com.liteng1220.lyt.utility.TextConverter;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseCmd implements Cmd {

    private static final String SERVER_URL = "eJzSLKCkpKLbS108syNTLTUSlNJzskvTdFLzs/VS84DAItuNCew="; // https://api.mddcloud.com.cn

    private static final String PARAMS_APP_TOKEN = "eJxELLCgIycE9VOzQMADpkDVQw=="; // appToken
    private static final String PARAMS_CHANNEL = "eJxELzkjMEyI0vNAQALISQLa"; // channel
    private static final String PARAMS_OS = "eJzELLwEYVAAVMAV4w=="; // os
    private static final String PARAMS_SIGN = "eJwPrzkPzDPAwAEDRwGy"; // sign
    private static final String PARAMS_TIME = "eJwXrycXxTNBQAETTgGw"; // time
    private static final String PARAMS_VERSION = "eJwSrSy0qSzKszPAwAMKLAMH"; // version
    private static final String PARAMS_DATA = "eJxWLSSWxNJBAAENAAGb"; // data

    private static final String RESULTS_STATUS = "eJwBrLkksBKLS0GAAk5LAqU="; // status
    private static final String RESULTS_MSG = "eJzHLLUH4YHAAKXYAUg="; // msg
    private static final String RESULTS_DATA = "eJxWLSSWxNJBAAENAAGb"; // data

    private static final String VALUE_APP_TOKEN = ""; // 替换成 埋堆堆 APP 里的个人 TOKEN ，需要先登录
    private static final String VALUE_CHANNEL = "1000";
    private static final String VALUE_OS = "Android";
    private static final String VALUE_VERSION = "1.18.4";
    private static final String VALUE_PRIVATE_KEY = "e1be6b4cf4021b3d181170d1879a530a9e4130b69032144d5568abfd6cd6c1c2";

    private static final String SIGN_KEY_ACTION = "eJxDLTC7JDzIM8DAAiSIAn8="; // action
    private static final String SIGN_KEY_PRIVATE_KEY = "eJwArKMosSyAxXJ9U6tBAAXXMAQl"; // privateKey

    @Override
    public void sendHttpRequest(BaseHttpTask.HttpCallback callback, Object... params) {
        if (params != null) {
            String json = createRequestJson(params);
            HttpManager.sendNoEncryptRequest(getActionUrl(), json, callback);
        }
    }

    private String getActionUrl() {
        return TextConverter.decode(SERVER_URL) + getAction();
    }

    private String createRequestJson(Object... params) {
        JSONObject jsonObject = new JSONObject();

        try {
            long curTime = System.currentTimeMillis();

            jsonObject.put(TextConverter.decode(PARAMS_APP_TOKEN), VALUE_APP_TOKEN);
            jsonObject.put(TextConverter.decode(PARAMS_CHANNEL), VALUE_CHANNEL);
            jsonObject.put(TextConverter.decode(PARAMS_OS), VALUE_OS);
            jsonObject.put(TextConverter.decode(PARAMS_VERSION), VALUE_VERSION);
            jsonObject.put(TextConverter.decode(PARAMS_TIME), curTime);
            jsonObject.put(TextConverter.decode(PARAMS_DATA), createJsonData(params));
            jsonObject.put(TextConverter.decode(PARAMS_SIGN), computeSign(curTime, createSignData(params)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    private String computeSign(long curTime, String data) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(TextConverter.decode(PARAMS_OS));
        stringBuilder.append(":");
        stringBuilder.append(VALUE_OS);
        stringBuilder.append("|");

        stringBuilder.append(TextConverter.decode(PARAMS_VERSION));
        stringBuilder.append(":");
        stringBuilder.append(VALUE_VERSION);
        stringBuilder.append("|");

        stringBuilder.append(TextConverter.decode(SIGN_KEY_ACTION));
        stringBuilder.append(":");
        stringBuilder.append(getAction());
        stringBuilder.append("|");

        stringBuilder.append(TextConverter.decode(PARAMS_TIME));
        stringBuilder.append(":");
        stringBuilder.append(curTime);
        stringBuilder.append("|");

        stringBuilder.append(TextConverter.decode(PARAMS_APP_TOKEN));
        stringBuilder.append(":");
        stringBuilder.append(VALUE_APP_TOKEN);
        stringBuilder.append("|");

        stringBuilder.append(TextConverter.decode(SIGN_KEY_PRIVATE_KEY));
        stringBuilder.append(":");
        stringBuilder.append(VALUE_PRIVATE_KEY);
        stringBuilder.append("|");

        stringBuilder.append(TextConverter.decode(PARAMS_DATA));
        stringBuilder.append(":");
        stringBuilder.append(data);

        return Sign.md5(stringBuilder.toString());
    }

    protected abstract String getAction();

    protected abstract String createSignData(Object... params);

    protected abstract JSONObject createJsonData(Object... params);

    protected abstract Data parseJsonData(Object jsonData);

    @Override
    public Results parseResponseJson(String responseJson) {
        if (responseJson == null) {
            return null;
        }

        Results results = new Results();
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            results.status = jsonObject.optBoolean(TextConverter.decode(RESULTS_STATUS));
            results.msg = jsonObject.optString(TextConverter.decode(RESULTS_MSG));
            Object dataObject = jsonObject.opt(TextConverter.decode(RESULTS_DATA));
            if (dataObject != null) {
                results.data = parseJsonData(dataObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static class Results {
        public boolean status;
        public String msg;
        public Data data;
    }

    public static class Data {
    }
}
