package com.liteng1220.gfgift.api;

import com.liteng1220.gfgift.action.Action;
import com.liteng1220.gfgift.cmd.Cmd;
import com.liteng1220.gfgift.cmd.CmdListVodSactions;
import com.liteng1220.gfgift.cmd.CmdSearch;
import com.liteng1220.gfgift.conf.Constant;

public class WebApi {

    public static void search(final String name, final ApiCallback callback) {
        checkCallback(callback);

        final Cmd cmd = new CmdSearch();
        cmd.sendHttpRequest(new WebHttpCallback(Constant.ActionType.ACTION_TYPE_SEARCH, callback) {
            @Override
            protected void onHandleData(String responseResult) {
                CmdSearch.Results results = (CmdSearch.Results) cmd.parseResponseJson(responseResult);
                if (results.status) {
                    if (results.data != null) {
                        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, ApiCallback.ON_RESPONSE_DATA);
                        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_DATA, results.data);
                        callback.onCallback(actionBuilder.build());
                    }
                } else {
                    actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, ApiCallback.ON_DATA_EXCEPTION);
                    actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_DATA, results.msg);
                    callback.onCallback(actionBuilder.build());
                }
            }
        }, name);
    }

    public static void parseVideoData(final String name, String uuid, final ApiCallback callback) {
        checkCallback(callback);

        final Cmd cmd = new CmdListVodSactions();
        cmd.sendHttpRequest(new WebHttpCallback(Constant.ActionType.ACTION_TYPE_START_DOWNLOAD, callback) {
            @Override
            protected void onHandleData(String responseResult) {
                CmdListVodSactions.Results results = (CmdListVodSactions.Results) cmd.parseResponseJson(responseResult);
                if (results.status) {
                    if (results.data != null) {
                        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, ApiCallback.ON_RESPONSE_DATA);
                        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_DATA, results.data);
                        actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_NAME, name);
                        callback.onCallback(actionBuilder.build());
                    }
                } else {
                    actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_STATUS, ApiCallback.ON_DATA_EXCEPTION);
                    actionBuilder.bundle(Constant.Bundle.BUNDLE_KEY_DATA, results.msg);
                    callback.onCallback(actionBuilder.build());
                }
            }
        }, uuid);
    }

    private static void checkCallback(ApiCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can not be null!");
        }
    }

    public interface ApiCallback {
        int ON_NETWORK_EXCEPTION = -3;
        int ON_FAIL = -2;
        int ON_DATA_EXCEPTION = -1;
        int ON_RESPONSE_NULL = 0;
        int ON_RESPONSE_DATA = 1;

        void onCallback(Action action);
    }
}
