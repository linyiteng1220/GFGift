package com.liteng1220.lyt.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.liteng1220.lyt.R;
import com.liteng1220.lyt.utility.BaseUtil;

/**
 * 右图标可点击的文本编辑框
 */
public class IconClickableEditText extends AppCompatEditText {

    private Drawable drawableRight;
    private Resources resources;
    private int padding;
    private Rect rect;
    private OnIconClickListener onIconClickListener;

    public IconClickableEditText(Context context) {
        super(context);
        init();
    }

    public IconClickableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconClickableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        resources = getResources();
        padding = resources.getDimensionPixelOffset(R.dimen.item_padding);
        rect = new Rect();

        setCompoundDrawablePadding(padding);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (rect != null && drawableRight != null) {
                        getGlobalVisibleRect(rect);
                        if (event.getRawX() >= (rect.right - drawableRight.getBounds().width() - padding)) {
                            if (onIconClickListener != null) {
                                onIconClickListener.onIconClicked();
                            }
                        }
                    }
                }
                return false;
            }
        });

        final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    drawableRight = getCompoundDrawables()[2];
                    setCompoundDrawables(null, null, BaseUtil.wrapColor4Icon(resources, drawableRight, R.color.color_accent), null);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public OnIconClickListener getOnIconClickListener() {
        return onIconClickListener;
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }

    public interface OnIconClickListener {
        void onIconClicked();
    }
}
