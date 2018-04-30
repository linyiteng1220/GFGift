package com.liteng1220.gfgift.stores;

import com.liteng1220.gfgift.GfApplication;
import com.liteng1220.gfgift.R;
import com.liteng1220.gfgift.action.Action;
import com.liteng1220.gfgift.adapter.BaseAdapter;
import com.liteng1220.gfgift.adapter.SearchListAdapter;
import com.liteng1220.gfgift.cmd.CmdSearch;
import com.liteng1220.gfgift.conf.Constant;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoSearchStore extends WebStore {

    public VideoSearchStore() {
        super();
        dataList = new ArrayList<>();
    }

    @Override
    @Subscribe
    public void onAction(Action action) {
        switch (action.getType()) {
            case Constant.ActionType.ACTION_TYPE_SEARCH:
                handleActionData(action.getData());
                emitStoreChange();
                break;
        }
    }

    @Override
    protected void handleResponseResult(Object data, HashMap actionData) {
        if (data instanceof CmdSearch.SearchData) {
            CmdSearch.SearchData searchData = (CmdSearch.SearchData) data;
            List<CmdSearch.DataItem> dataItemList = searchData.dataItemList;
            if (dataItemList != null) {
                dataList.clear();
                dataList.addAll(convert2AdapterData(dataItemList));
            }
        }
    }

    private List<BaseAdapter.Data> convert2AdapterData(List<CmdSearch.DataItem> dataItemList) {
        List<BaseAdapter.Data> adapterDataList = new ArrayList<>();

        for (CmdSearch.DataItem dataItem : dataItemList) {
            SearchListAdapter.SearchResultData data = new SearchListAdapter.SearchResultData();
            data.name = dataItem.name;
            data.totalNum = GfApplication.getContext().getString(R.string.tv_total_num, dataItem.totalNum);
            data.starring = GfApplication.getContext().getString(R.string.tv_starring, dataItem.starring);
            data.uuid = dataItem.uuid;
            adapterDataList.add(data);
        }

        return adapterDataList;
    }

    @Override
    protected StoreChangeEvent changeEvent() {
        return new VideoSearchStoreChangeEvent(status, msg, dataList);
    }

    public class VideoSearchStoreChangeEvent implements StoreChangeEvent {
        public int status;
        public String msg;
        public List<BaseAdapter.Data> dataList;

        public VideoSearchStoreChangeEvent(int searchResultStatus, String searchResultMsg, List<BaseAdapter.Data> searchResultList) {
            this.status = searchResultStatus;
            this.msg = searchResultMsg;
            this.dataList = searchResultList;
        }
    }
}
