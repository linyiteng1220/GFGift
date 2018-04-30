package com.liteng1220.lyt.manager;

import android.app.Activity;
import android.content.res.Resources;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.utility.BaseUtil;
import com.liteng1220.lyt.widget.CustomViewOnClickListener;

public class ToastManager {

    public static void showInfo(Activity activity, int infoResId) {
        showInfo(activity, infoResId, 0, null, null);
    }

    public static void showInfo(Activity activity, String info) {
        showInfo(activity, info, 0, null, null);
    }

    public static void showInfo(Activity activity, String info, BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        showInfo(activity, info, 0, null, callback);
    }

    public static void showInfo(Activity activity, int infoResId, BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        showInfo(activity, infoResId, 0, null, callback);
    }

    private static void showInfo(Activity activity, int infoResId, int buttonResId, CustomViewOnClickListener listener, BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        showInfo(activity, activity.getString(infoResId), buttonResId, listener, callback);
    }

    private static void showInfo(Activity activity, String info, int buttonResId, CustomViewOnClickListener listener, BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        Resources res = activity.getResources();
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), info, Snackbar.LENGTH_LONG);
        if (buttonResId > 0 && listener != null) {
            snackbar.setAction(buttonResId, listener);
            snackbar.setActionTextColor(res.getColor(R.color.color_accent));
        }

        if (callback != null) {
            snackbar.addCallback(callback);
        }

        View view = snackbar.getView();
        view.setBackgroundColor(res.getColor(R.color.grey_light));
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(res.getColor(R.color.blue));

        snackbar.show();

        BaseUtil.hideKeyboard(activity); // 必须隐藏输入法，否则 snackbar 无法正常显示
    }
}
