package com.liteng1220.lyt.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.adapter.DialogItemSelectAdapter;
import com.liteng1220.lyt.vo.ItemVo;
import com.liteng1220.lyt.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DialogManager {

    private volatile static DialogManager instance;
    private Map<String, List<Dialog>> dialogMap;

    private DialogManager() {
        dialogMap = new HashMap<String, List<Dialog>>();
    }

    public static DialogManager getInstance() {
        if (instance == null) {
            synchronized (DialogManager.class) {
                if (instance == null) {
                    instance = new DialogManager();
                }
            }
        }
        return instance;
    }

    public void hideDialog(String content) {
        if (content == null) {
            return;
        }

        if (dialogMap != null) {
            List<Dialog> dialogList = dialogMap.get(content);
            if (dialogList != null) {
                Iterator<Dialog> it = dialogList.iterator();
                while (it.hasNext()) {
                    Dialog dialog = it.next();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }

        removeDialog(content);
    }

    public void showInfoDialog(Activity activity, String content) {
        showAlternativeDialog(activity, activity.getString(R.string.dialog_title_notice), content, activity.getString(R.string.button_confirm), null, null);
    }

    public void showInfoDialog(Activity activity, String title, String content, DialogButtonClickListener listener) {
        showAlternativeDialog(activity, title, content, activity.getString(R.string.button_confirm), null, listener);
    }

    public void showAlternativeDialog(Activity activity, final String content, String positiveButtonName, final DialogButtonClickListener listener) {
        showAlternativeDialog(activity, activity.getString(R.string.dialog_title_notice), content, positiveButtonName, activity.getString(R.string.button_cancel), listener);
    }

    public void showAlternativeDialog(Activity activity, String title, final String content, String positiveButtonName, final DialogButtonClickListener listener) {
        showAlternativeDialog(activity, title, content, positiveButtonName, activity.getString(R.string.button_cancel), listener);
    }

    public void showAlternativeDialog(Activity activity, String title, final String content, String positiveButtonName, String negativeButtonName, final DialogButtonClickListener listener) {
        if (activity == null) {
            return;
        }

        if (activity.isFinishing()) {
            return;
        }

        /* 关闭相同内容的对话框 */
        hideDialog(title);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();

        if (positiveButtonName != null) {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    removeDialog(content, alertDialog);
                    if (listener != null) {
                        listener.onPositiveButtonClicked();
                    }
                }
            });
        }

        if (negativeButtonName != null) {
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    removeDialog(content, alertDialog);
                    if (listener != null) {
                        listener.onNegativeButtonClicked();
                    }
                }
            });
        }

        alertDialog.show();
        saveDialog(content, alertDialog);
    }

    public void showInputDialog(Activity activity, final String title, String positiveButtonName, final DialogInputListener listener) {
        if (activity == null) {
            return;
        }

        if (activity.isFinishing()) {
            return;
        }

        /* 关闭相同内容的对话框 */
        hideDialog(title);

        View contentView = activity.getLayoutInflater().inflate(R.layout.dialog_input, null);
        final AppCompatEditText editText = (AppCompatEditText) contentView.findViewById(R.id.et_input);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();

        if (positiveButtonName != null) {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    removeDialog(title, alertDialog);
                    if (listener != null) {
                        listener.onInputConfirm(editText.getText().toString());
                    }
                }
            });
        }

        alertDialog.show();
        saveDialog(title, alertDialog);

        HandlerManager.getInstance().postUiThread(new Thread() {
            @Override
            public void run() {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 200);
    }

    public void showItemSelectDialog(Activity activity, final String title, final List<ItemVo> itemVoList, final DialogItemSelectListener listener) {
        if (activity == null) {
            return;
        }

        if (activity.isFinishing()) {
            return;
        }

        /* 关闭相同内容的对话框 */
        hideDialog(title);

        View contentView = activity.getLayoutInflater().inflate(R.layout.dialog_item_selection_list, null);
        RecyclerView rvItemList = (RecyclerView) contentView.findViewById(R.id.rv_item_list);
        rvItemList.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        rvItemList.setLayoutManager(new LinearLayoutManager(activity));

        DialogItemSelectAdapter adapter = new DialogItemSelectAdapter(activity, itemVoList, listener);
        rvItemList.setAdapter(adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setView(contentView);
        builder.setCancelable(true);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                removeDialog(title);
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        saveDialog(title, alertDialog);
    }

    public void showItemMultiSelectDialog(final Activity activity, final String title, final String[] itemArray, final boolean[] valueArray, final DialogItemMultiSelectListener listener) {
        if (activity == null) {
            return;
        }

        if (activity.isFinishing()) {
            return;
        }

        /* 关闭相同内容的对话框 */
        hideDialog(title);

        final boolean[] selectValueArray = valueArray == null ? new boolean[itemArray.length] : Arrays.copyOf(valueArray, itemArray.length);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMultiChoiceItems(itemArray, selectValueArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectValueArray[which] = isChecked;
            }
        });
        builder.setCancelable(true);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                removeDialog(title);
            }
        });
        builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    List<String> itemList = new ArrayList<String>();
                    for (int k = 0; k < selectValueArray.length; k++) {
                        if (selectValueArray[k]) {
                            itemList.add(itemArray[k]);
                        }
                    }
                    listener.onItemsSelected(selectValueArray, itemList.toArray(new String[itemList.size()]));
                }
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        saveDialog(title, alertDialog);
    }

    private void saveDialog(String content, Dialog dialog) {
        if (content == null || dialog == null) {
            return;
        }

        if (dialogMap != null) {
            List<Dialog> dialogList = dialogMap.get(content);
            if (dialogList == null) {
                dialogList = new ArrayList<Dialog>();
            }

            dialogList.add(dialog);
            dialogMap.put(content, dialogList);
        }
    }

    private void removeDialog(String content) {
        if (content == null) {
            return;
        }

        if (dialogMap != null) {
            dialogMap.remove(content);
        }
    }

    private void removeDialog(String content, Dialog dialog) {
        if (content == null || dialog == null) {
            return;
        }

        if (dialogMap != null) {
            List<Dialog> dialogList = dialogMap.get(content);
            if (dialogList != null) {
                dialogList.remove(dialog);
            }
        }
    }

    public void clearDialog() {
        if (dialogMap != null) {
            dialogMap.clear();
        }
    }

    public interface DialogButtonClickListener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }

    public interface DialogItemSelectListener {
        void onItemSelected(int id);
    }

    public interface DialogItemMultiSelectListener {
        void onItemsSelected(boolean[] indexArray, String[] valueArray);
    }

    public interface DialogInputListener {
        void onInputConfirm(String text);
    }
}
