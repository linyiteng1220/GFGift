package com.liteng1220.gfgift;

import android.app.Application;
import android.content.Context;

import com.liteng1220.lyt.AppContext;

public class GfApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        AppContext.setContext(context);

        if (BuildConfig.DEBUG) {
            com.facebook.stetho.Stetho.initializeWithDefaults(this);
        }
    }

    public static Context getContext() {
        return context;
    }
}