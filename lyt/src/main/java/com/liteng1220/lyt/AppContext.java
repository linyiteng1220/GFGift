package com.liteng1220.lyt;

import android.content.Context;

public class AppContext {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppContext.context = context;
    }

    // TODO: 2018/4/13 为资源添加特定前缀
}