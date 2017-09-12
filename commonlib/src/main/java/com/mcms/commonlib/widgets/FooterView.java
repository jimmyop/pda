package com.mcms.commonlib.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mcms.commonlib.R;

/**
 * hyj 用于GridView分页的FooterView
 *
 * @author Administrator
 */
public class FooterView extends LinearLayout {
    private Context mContext;
    private View mFooter;
    public static final int HIDE = 0;
    public static final int MORE = 1;
    public static final int LOADING = 2;
    public static final int BADNETWORK = 3;
    private int curStatus;
    private OnClickListener ml;
    private ImageView mLoadingView;

    public ImageView getLoadingImageView() {
        return mLoadingView;
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public FooterView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        mFooter = LayoutInflater.from(mContext).inflate(R.layout.footer_progressbar_latest_layout, this, true);
//        mFooter.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 添加加载动画
        mLoadingView = (ImageView) mFooter.findViewById(R.id.loadingAnim);

        AnimationDrawable secondAnim = (AnimationDrawable) mLoadingView.getDrawable();
        secondAnim.start();

        setStatus(MORE);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        ml = l;
        super.setOnClickListener(ml);
    }

    public void setStatus(int status) {
        curStatus = status;
        switch (status) {
            case HIDE:
                mFooter.setVisibility(View.GONE);
                break;
            case MORE:
            case LOADING:
                mFooter.setVisibility(View.VISIBLE);
                break;
        }
    }

    public int getStatus() {
        return curStatus;
    }

    public boolean isLoading() {
        return curStatus == LOADING;
    }
}
