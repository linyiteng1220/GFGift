package com.liteng1220.gfgift.api;

import com.liteng1220.gfgift.action.Action;
import com.liteng1220.gfgift.conf.Constant;
import com.liteng1220.lyt.task.BaseHttpTask;

public abstract class WebHttpCallback implements BaseHttpTask.HttpCallback {

    protected WebApi.ApiCallback callback;
    protected Action.Builder actionBuilder;

    public WebHttpCallback(String actionType, WebApi.ApiCallback callback) {
        actionBuilder = Action.type(actionType);
        this.callback = callback;
    }

    @Override
    public void onResponse(String responseResult) {
        if (responseResult != null) {
            onHandleData(responseResult);
        } else {
            actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, WebApi.ApiCallback.ON_RESPONSE_NULL);
            callback.onCallback(actionBuilder.build());
        }
    }

    protected abstract void onHandleData(String responseResult);

    @Override
    public void onNetworkException(Exception exception) {
        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, WebApi.ApiCallback.ON_NETWORK_EXCEPTION);
        callback.onCallback(actionBuilder.build());
    }

    @Override
    public void onFail(Exception exception) {
        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, WebApi.ApiCallback.ON_FAIL);
        callback.onCallback(actionBuilder.build());
    }
}
