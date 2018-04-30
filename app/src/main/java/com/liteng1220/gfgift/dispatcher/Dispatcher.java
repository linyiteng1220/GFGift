package com.liteng1220.gfgift.dispatcher;

import com.liteng1220.gfgift.action.Action;
import com.liteng1220.gfgift.stores.Store;
import com.squareup.otto.Bus;

public class Dispatcher {
    private final Bus bus;
    private static Dispatcher instance;

    private Dispatcher() {
        this.bus = new Bus();
    }

    public static Dispatcher getInstance() {
        if (instance == null) {
            synchronized (Dispatcher.class) {
                if (instance == null) {
                    instance = new Dispatcher();
                }
            }
        }
        return instance;
    }

    public void register(final Object klass) {
        bus.register(klass);
    }

    public void unregister(final Object klass) {
        bus.unregister(klass);
    }

    public void emitChange(Store.StoreChangeEvent storeChangeEvent) {
        post(storeChangeEvent);
    }

    public void dispatch(Action action) {
        post(action);
    }

    private boolean isEmpty(String type) {
        return type == null || type.isEmpty();
    }

    private void post(final Object event) {
        bus.post(event);
    }
}
