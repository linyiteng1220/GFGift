package com.liteng1220.lyt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.widget.CustomMenuItemOnClickListener;
import com.liteng1220.lyt.widget.CustomViewOnClickListener;

public abstract class BaseToolbarActivity extends AppCompatActivity {

    protected static final int NAVIGATION_ICON_NULL = -1;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeSetContentView();
        setContentView(getContentViewResId());
        initAfterSetContentView();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);

        int navigationIconResId = getNavigationIconResId();
        if (navigationIconResId != NAVIGATION_ICON_NULL) {
            toolbar.setNavigationIcon(navigationIconResId);
        }

        // 设置点击事件的监听必须在 setSupportActionBar() 之后
        toolbar.setNavigationOnClickListener(new CustomViewOnClickListener() {
            @Override
            public void onSingleClick(View v) {
                onNavigationItemClicked(v);
            }
        });
    }

    protected abstract int getContentViewResId();

    protected abstract String getToolbarTitle();

    protected void initBeforeSetContentView() {
    }

    protected void initAfterSetContentView() {
    }

    protected void setMenu(int menuResId) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(menuResId);
    }

    protected Menu getMenu() {
        return toolbar.getMenu();
    }

    protected void setOnMenuItemClickListener(CustomMenuItemOnClickListener listener) {
        if (listener != null) {
            toolbar.setOnMenuItemClickListener(listener);
        }
    }

    protected int getNavigationIconResId() {
        return NAVIGATION_ICON_NULL;
    }

    protected void onNavigationItemClicked(View view) {
    }

    protected void changeTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    final public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}
