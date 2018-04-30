package com.liteng1220.lyt.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.utility.BaseUtil;

/**
 * 带清除按钮的文本编辑框
 */
public class ClearableEditText extends AppCompatEditText {

    private Resources resources;
    private Drawable clearDrawable;
    private int padding;
    private Rect rect;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        resources = getResources();
        padding = resources.getDimensionPixelOffset(R.dimen.item_padding);
        clearDrawable = BaseUtil.wrapColor4Icon(resources, R.drawable.ic_clear_black, R.color.color_accent);
        rect = new Rect();

        setCompoundDrawablePadding(padding);
        hideClearButton();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawable = getCompoundDrawables()[DRAWABLE_RIGHT];
                    if (rect != null && drawable != null) {
                        getGlobalVisibleRect(rect);
                        if (event.getRawX() >= (rect.right - drawable.getBounds().width() - padding)) {
                            getText().clear();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (getText().length() > 0) {
                        showClearButton();
                    } else {
                        hideClearButton();
                    }
                } else {
                    hideClearButton();
                }
            }
        });
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0) {
            showClearButton();
        } else {
            hideClearButton();
        }
    }

    private void showClearButton() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, clearDrawable, null);
    }

    private void hideClearButton() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }
}
