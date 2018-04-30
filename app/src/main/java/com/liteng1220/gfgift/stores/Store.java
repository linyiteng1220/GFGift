package com.liteng1220.gfgift.stores;

import com.liteng1220.gfgift.action.Action;
import com.liteng1220.gfgift.api.WebApi;
import com.liteng1220.gfgift.dispatcher.Dispatcher;

public abstract class Store {

    public static final int STATUS_UNKNOWN = 0x01;
    public static final int STATUS_NETWORK_ERROR = 0x02;
    public static final int STATUS_REQUEST_FAIL = 0x04;
    public static final int STATUS_RESPONSE_DATA_EXCEPTION = 0x08;
    public static final int STATUS_RESPONSE_NULL = 0x16;
    public static final int STATUS_RESPONSE_OK = 0x32;

    protected void emitStoreChange() {
        Dispatcher.getInstance().emitChange(changeEvent());
    }

    protected int convert2StoreStatus(int webApiStatus) {
        switch (webApiStatus) {
            case WebApi.ApiCallback.ON_NETWORK_EXCEPTION:
                return STATUS_NETWORK_ERROR;
            case WebApi.ApiCallback.ON_FAIL:
                return STATUS_REQUEST_FAIL;
            case WebApi.ApiCallback.ON_DATA_EXCEPTION:
                return STATUS_RESPONSE_DATA_EXCEPTION;
            case WebApi.ApiCallback.ON_RESPONSE_NULL:
                return STATUS_RESPONSE_NULL;
            case WebApi.ApiCallback.ON_RESPONSE_DATA:
                return STATUS_RESPONSE_OK;
        }
        return STATUS_UNKNOWN;
    }

    protected abstract StoreChangeEvent changeEvent();

    protected abstract void onAction(Action action);

    public interface StoreChangeEvent {
    }
}
