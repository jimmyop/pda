package com.jimmy.lib.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by chenjiaming1 on 2017/9/1.
 */

public class LoadingBgView extends android.support.v7.widget.AppCompatImageView {
    public LoadingBgView(Context context) {
        this(context, null);
    }

    public LoadingBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        load();
    }

    private void load() {
        AnimationDrawable secondAnim = (AnimationDrawable) getDrawable();
        secondAnim.start();
    }
}
