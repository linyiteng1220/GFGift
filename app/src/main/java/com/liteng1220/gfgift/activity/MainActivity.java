package com.liteng1220.gfgift.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liteng1220.gfgift.R;
import com.liteng1220.gfgift.action.ActionsCreator;
import com.liteng1220.gfgift.adapter.BaseAdapter;
import com.liteng1220.gfgift.adapter.SearchListAdapter;
import com.liteng1220.gfgift.dispatcher.Dispatcher;
import com.liteng1220.gfgift.service.VideoDownloadService;
import com.liteng1220.gfgift.stores.Store;
import com.liteng1220.gfgift.stores.VideoDownloadStore;
import com.liteng1220.gfgift.stores.VideoSearchStore;
import com.liteng1220.gfgift.util.YddUtil;
import com.liteng1220.lyt.activity.BasePermissionActivity;
import com.liteng1220.lyt.manager.DialogManager;
import com.liteng1220.lyt.manager.LoadingDialogManager;
import com.liteng1220.lyt.manager.ToastManager;
import com.liteng1220.lyt.widget.CustomMenuItemOnClickListener;
import com.squareup.otto.Subscribe;

public class MainActivity extends BasePermissionActivity {

    private LinearLayout llGuide;
    private LinearLayout llSearchResult;
    private RecyclerView rvResultList;
    private TextView tvKeyword;
    private TextView tvFileDownloadPath;
    private SearchListAdapter adapter;

    private VideoSearchStore videoSearchStore;
    private VideoDownloadStore videoDownloadStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        llGuide = findViewById(R.id.ll_guide);
        llSearchResult = findViewById(R.id.ll_search_result);
        rvResultList = findViewById(R.id.rv_result_list);
        tvKeyword = findViewById(R.id.tv_keyword);
        tvFileDownloadPath = findViewById(R.id.tv_file_download_path);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvResultList.setLayoutManager(linearLayoutManager);

        adapter = new SearchListAdapter(this);
        adapter.setHeader(LayoutInflater.from(this).inflate(R.layout.header_empty, rvResultList, false));
        rvResultList.setAdapter(adapter);
        adapter.setOnItemClickListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseAdapter.Data data) {
                if (data instanceof SearchListAdapter.SearchResultData) {
                    LoadingDialogManager.getInstance().showLoadingDialog(MainActivity.this, getString(R.string.dialog_info_parsing));
                    SearchListAdapter.SearchResultData searchResultData = (SearchListAdapter.SearchResultData) data;
                    ActionsCreator.getInstance().startDownload(searchResultData.name, searchResultData.uuid);
                }
            }
        });

        setOnMenuItemClickListener(new CustomMenuItemOnClickListener() {
            @Override
            public void onSingleClick(MenuItem menuItem) {
                handleMenuItemClicked(menuItem.getItemId());
            }
        });

        tvFileDownloadPath.setText(getString(R.string.index_guide_download, YddUtil.getFileDownloadPath()));

        videoSearchStore = new VideoSearchStore();
        videoDownloadStore = new VideoDownloadStore();

        startService(new Intent(getApplicationContext(), VideoDownloadService.class));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dispatcher.getInstance().register(this);
        Dispatcher.getInstance().register(videoSearchStore);
        Dispatcher.getInstance().register(videoDownloadStore);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dispatcher.getInstance().unregister(this);
        Dispatcher.getInstance().unregister(videoSearchStore);
        Dispatcher.getInstance().unregister(videoDownloadStore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_menu, menu);
        return true;
    }

    public boolean handleMenuItemClicked(int id) {
        if (id == R.id.menu_search) {
            DialogManager.getInstance().showInputDialog(this, getString(R.string.dialog_title_input_tv_name), getString(R.string.button_confirm), new DialogManager.DialogInputListener() {
                @Override
                public void onInputConfirm(String text) {
                    if (TextUtils.isEmpty(text)) {
                        ToastManager.showInfo(MainActivity.this, R.string.toast_tv_name_empty);
                        return;
                    }

                    llGuide.setVisibility(View.GONE);
                    llSearchResult.setVisibility(View.VISIBLE);

                    tvKeyword.setText(text);
                    LoadingDialogManager.getInstance().showLoadingDialog(MainActivity.this, getString(R.string.dialog_info_searching));
                    ActionsCreator.getInstance().search(text);
                }
            });
        } else if (id == R.id.menu_stop_download) {
            DialogManager.getInstance().showAlternativeDialog(this, getString(R.string.dialog_title_stop_download), getString(R.string.dialog_info_stop_download_desc), getString(R.string.button_confirm), new DialogManager.DialogButtonClickListener() {
                @Override
                public void onPositiveButtonClicked() {
                    ActionsCreator.getInstance().stopDownload();
                }

                @Override
                public void onNegativeButtonClicked() {
                }
            });
        }
        return false;
    }

    @Subscribe
    public void onVideoSearchStoreChange(VideoSearchStore.VideoSearchStoreChangeEvent event) {
        switch (event.status) {
            case Store.STATUS_RESPONSE_OK:
                adapter.clearData();
                adapter.updateData(event.dataList);
                adapter.notifyDataSetChanged();
                break;
            case Store.STATUS_RESPONSE_NULL:
                ToastManager.showInfo(MainActivity.this, R.string.toast_parse_error);
                break;
            case Store.STATUS_RESPONSE_DATA_EXCEPTION:
                ToastManager.showInfo(MainActivity.this, event.msg);
                break;
            case Store.STATUS_REQUEST_FAIL:
                ToastManager.showInfo(MainActivity.this, R.string.toast_server_error);
                break;
            case Store.STATUS_NETWORK_ERROR:
                ToastManager.showInfo(MainActivity.this, R.string.toast_network_error);
                break;
        }

        LoadingDialogManager.getInstance().hideDialog();
    }

    @Subscribe
    public void onVideoDownloadStoreChange(VideoDownloadStore.VideoDownloadChangeEvent event) {
        switch (event.status) {
            case Store.STATUS_RESPONSE_OK:
                startService(new Intent(getApplicationContext(), VideoDownloadService.class));
                ToastManager.showInfo(MainActivity.this, getString(R.string.toast_start_to_download, event.name));
                break;
            case Store.STATUS_RESPONSE_NULL:
                ToastManager.showInfo(MainActivity.this, R.string.toast_parse_error);
                break;
            case Store.STATUS_RESPONSE_DATA_EXCEPTION:
                ToastManager.showInfo(MainActivity.this, event.msg);
                break;
            case Store.STATUS_REQUEST_FAIL:
                ToastManager.showInfo(MainActivity.this, R.string.toast_server_error);
                break;
            case Store.STATUS_NETWORK_ERROR:
                ToastManager.showInfo(MainActivity.this, R.string.toast_network_error);
                break;
        }

        LoadingDialogManager.getInstance().hideDialog();
    }
}
