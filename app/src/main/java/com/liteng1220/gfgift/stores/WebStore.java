package com.liteng1220.gfgift.stores;

import com.liteng1220.gfgift.adapter.BaseAdapter;
import com.liteng1220.gfgift.conf.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class WebStore extends Store {

    protected int status;
    protected String msg;
    protected List<BaseAdapter.Data> dataList;

    public WebStore() {
        super();
        dataList = new ArrayList<>();
    }

    protected void handleActionData(HashMap actionData) {
        status = convert2StoreStatus((int) actionData.get(Constant.Bundle.BUNDLE_KEY_STATUS));
        Object data = actionData.get(Constant.Bundle.BUNDLE_KEY_DATA);

        if (data != null) {
            if (data instanceof String) {
                msg = (String) data;
            } else {
                handleResponseResult(data, actionData);
            }
        }
    }

    protected abstract void handleResponseResult(Object data, HashMap actionData);

}
