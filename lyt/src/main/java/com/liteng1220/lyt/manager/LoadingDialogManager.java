package com.liteng1220.lyt.manager;

import android.app.Activity;
import android.app.ProgressDialog;

public class LoadingDialogManager {

    private volatile static LoadingDialogManager instance;
    private ProgressDialog progressDialog;

    private LoadingDialogManager() {
    }

    public static LoadingDialogManager getInstance() {
        if (instance == null) {
            synchronized (LoadingDialogManager.class) {
                if (instance == null) {
                    instance = new LoadingDialogManager();
                }
            }
        }
        return instance;
    }

    public void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showLoadingDialog(Activity activity, String title) {
        hideDialog();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(title);
        progressDialog.show();
    }

}
