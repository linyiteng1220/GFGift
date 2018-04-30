package com.liteng1220.gfgift.action;

import com.liteng1220.gfgift.api.WebApi;
import com.liteng1220.gfgift.conf.Constant;
import com.liteng1220.gfgift.dispatcher.Dispatcher;

public class ActionsCreator {

    private static ActionsCreator instance;

    private ActionsCreator() {
    }

    public static ActionsCreator getInstance() {
        if (instance == null) {
            synchronized (ActionsCreator.class) {
                if (instance == null) {
                    instance = new ActionsCreator();
                }
            }
        }
        return instance;
    }

    public void search(String name) {
        WebApi.search(name, new WebApi.ApiCallback() {
            @Override
            public void onCallback(Action action) {
                Dispatcher.getInstance().dispatch(action);
            }
        });
    }

    public void startDownload(String name, String uuid) {
        WebApi.parseVideoData(name, uuid, new WebApi.ApiCallback() {
            @Override
            public void onCallback(Action action) {
                Dispatcher.getInstance().dispatch(action);
            }
        });
    }

    public void stopDownload() {
        Action action = new Action.Builder().with(Constant.ActionType.ACTION_TYPE_STOP_DOWNLOAD).build();
        Dispatcher.getInstance().dispatch(action);
    }

}
