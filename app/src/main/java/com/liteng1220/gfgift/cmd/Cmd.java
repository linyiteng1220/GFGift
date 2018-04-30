package com.liteng1220.gfgift.cmd;

import com.liteng1220.lyt.task.BaseHttpTask;

public interface Cmd {

    void sendHttpRequest(BaseHttpTask.HttpCallback callback, Object... params);

    Object parseResponseJson(String responseJson);

}
