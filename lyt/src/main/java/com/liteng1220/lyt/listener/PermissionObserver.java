package com.liteng1220.lyt.listener;

public interface PermissionObserver {

    void onAppPermissionGranted(String[] permissionNameArray);

    void onAppPermissionDeclined(String[] permissionNameArray);

    void onAppPermissionReallyDeclined(String permissionName);

}
