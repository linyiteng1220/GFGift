package com.liteng1220.lyt.widget;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

public abstract class CustomViewOnClickListener implements View.OnClickListener {

    private Handler handler;
    private int countClick;

    public abstract void onSingleClick(View v);

    public void onDoubleClick(View v) {
    }

    public CustomViewOnClickListener() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onClick(final View clickedView) {
        countClick++;

        if (countClick == 1) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (countClick == 2) {
                        onDoubleClick(clickedView);
                    } else {
                        onSingleClick(clickedView);
                    }
                    countClick = 0;
                }
            }, 100);
        }
    }
}