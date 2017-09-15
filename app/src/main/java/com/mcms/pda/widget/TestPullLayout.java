package com.mcms.pda.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mcms.commonlib.utils.LogUtils;
import com.mcms.commonlib.widgets.GranzortViewTest;
import com.qmuiteam.qmui.drawable.QMUIMaterialProgressDrawable;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

/**
 * Created by chenjiaming1 on 2017/9/13.
 */

public class TestPullLayout extends QMUIPullRefreshLayout {

    public TestPullLayout(Context context) {
        super(context);
    }

    public TestPullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestPullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createRefreshView() {
        LogUtils.e("jimmy TestPullLayout createRefreshView");
        return new GranzortViewTest(getContext());
    }


    public static class TestRefreshView extends android.support.v7.widget.AppCompatImageView implements IRefreshView {
        private static final int MAX_ALPHA = 255;
        private static final float TRIM_RATE = 0.85f;
        private static final float TRIM_OFFSET = 0.4f;

        private QMUIMaterialProgressDrawable mProgress;

        public TestRefreshView(Context context) {
            super(context);
            mProgress = new QMUIMaterialProgressDrawable(getContext(), this);
            mProgress.setColorSchemeColors(QMUIResHelper.getAttrColor(context, com.qmuiteam.qmui.R.attr.qmui_config_color_blue));
            mProgress.updateSizes(QMUIMaterialProgressDrawable.LARGE);
            mProgress.setAlpha(MAX_ALPHA);
            mProgress.setArrowScale(1.1f);
            setImageDrawable(mProgress);
        }

        @Override
        public void onPull(int offset, int total, int overPull) {
            float end = TRIM_RATE * offset / total;
            float rotate = TRIM_OFFSET * offset / total;
            if (overPull > 0) {
                rotate += TRIM_OFFSET * overPull / total;
            }
            mProgress.showArrow(true);
            mProgress.setStartEndTrim(0, end);
            mProgress.setProgressRotation(rotate);
        }

        public void setSize(int size) {
            if (size != QMUIMaterialProgressDrawable.LARGE && size != QMUIMaterialProgressDrawable.DEFAULT) {
                return;
            }
            setImageDrawable(null);
            mProgress.updateSizes(size);
            setImageDrawable(mProgress);
        }

        public void stop() {
            mProgress.stop();
        }

        public void doRefresh() {
            mProgress.start();
        }

        public void setColorSchemeResources(@ColorRes int... colorResIds) {
            final Context context = getContext();
            int[] colorRes = new int[colorResIds.length];
            for (int i = 0; i < colorResIds.length; i++) {
                colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
            }
            setColorSchemeColors(colorRes);
        }

        public void setColorSchemeColors(@ColorInt int... colors) {
            mProgress.setColorSchemeColors(colors);
        }
    }
}
