package com.liteng1220.lyt.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.liteng1220.lyt.BuildConfig;
import com.liteng1220.lyt.listener.PermissionSubject;
import com.liteng1220.lyt.utility.ILog;

public abstract class BasePermissionActivity extends BaseToolbarActivity implements OnPermissionCallback {

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionHelper = PermissionHelper.getInstance(this);
    }

    public void requestPermission(Object permission) {
        permissionHelper.request(permission);
    }

    public void onPermissionGranted(@NonNull String[] permissionNameArray) {
        if (BuildConfig.DEBUG) {
            ILog.info("BasePermissionActivity.onAppPermissionGranted() >>> ");
        }
        PermissionSubject.getInstance().notifyPermissionGranted(permissionNameArray);
    }

    public void onPermissionDeclined(@NonNull String[] permissionNameArray) {
        if (BuildConfig.DEBUG) {
            ILog.info("BasePermissionActivity.onAppPermissionDeclined() >>> ");
        }
        PermissionSubject.getInstance().notifyPermissionDeclined(permissionNameArray);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        if (BuildConfig.DEBUG) {
            ILog.info("BasePermissionActivity.onPermissionPreGranted() >>> ");
        }
        PermissionSubject.getInstance().notifyPermissionGranted(new String[]{permissionsName});
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (BuildConfig.DEBUG) {
            ILog.info("BasePermissionActivity.onPermissionNeedExplanation() >>> ");
        }
        permissionHelper.requestAfterExplanation(permissionName);
    }

    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        if (BuildConfig.DEBUG) {
            ILog.info("BasePermissionActivity.onAppPermissionReallyDeclined() >>> ");
        }
        PermissionSubject.getInstance().notifyPermissionReallyDeclined(permissionName);
    }

    @Override
    public void onNoPermissionNeeded() {
        if (BuildConfig.DEBUG) {
            ILog.info("BasePermissionActivity.onNoPermissionNeeded() >>> ");
        }
        PermissionSubject.getInstance().notifyPermissionGranted(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
