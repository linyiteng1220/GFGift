package com.liteng1220.lyt.widget;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public abstract class CustomMenuItemOnClickListener implements Toolbar.OnMenuItemClickListener {

    private Handler handler;
    private int countClick;

    public abstract void onSingleClick(MenuItem menuItem);

    public void onDoubleClick(MenuItem menuItem) {
    }

    public CustomMenuItemOnClickListener() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        countClick++;

        if (countClick == 1) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (countClick >= 2) {
                        onDoubleClick(item);
                    } else {
                        onSingleClick(item);
                    }
                    countClick = 0;
                }
            }, 200);
        }

        return true;
    }
}
