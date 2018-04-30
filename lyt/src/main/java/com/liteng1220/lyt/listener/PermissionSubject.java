package com.liteng1220.lyt.listener;

import java.util.ArrayList;
import java.util.List;

public class PermissionSubject {

    private static volatile PermissionSubject instance;
    private List<PermissionObserver> observerList;

    private PermissionSubject() {
        observerList = new ArrayList<PermissionObserver>();
    }

    public static PermissionSubject getInstance() {
        if (instance == null) {
            synchronized (PermissionSubject.class) {
                if (instance == null) {
                    instance = new PermissionSubject();
                }
            }
        }
        return instance;
    }

    public void registerObserver(PermissionObserver observer) {
        unregisterObserver(observer);
        observerList.add(observer);
    }

    public void unregisterObserver(PermissionObserver observer) {
        if (observer == null) {
            return;
        }

        for (int k = 0; k < observerList.size(); k++) {
            PermissionObserver item = observerList.get(k);
            if (item != null && item.getClass().getName().equals(observer.getClass().getName())) {
                observerList.remove(item);
                break;
            }
        }
    }

    public void notifyPermissionGranted(String[] permissionNameArray) {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            PermissionObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onAppPermissionGranted(permissionNameArray);
            }
        }
    }

    public void notifyPermissionDeclined(String[] permissionNameArray) {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            PermissionObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onAppPermissionDeclined(permissionNameArray);
            }
        }
    }

    public void notifyPermissionReallyDeclined(String permissionName) {
        int size = observerList.size();
        for (int k = 0; k < size; k++) {
            PermissionObserver observer = observerList.get(k);
            if (observer != null) {
                observer.onAppPermissionReallyDeclined(permissionName);
            }
        }
    }

}
