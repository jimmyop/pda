package com.mcms.commonlib.request.loadingmanager;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcms.commonlib.R;


/***
 * 背景的加载方式管理类
 * 加载中：背景形式
 * 加载失败：背景形式，失败后，请求依然还会留在队列中不管code为何值
 * 网络失败：背景形式，
 * 数据为空：小雅背景形式
 * 加载成功：移除所有的加载UI
 * 用途：用于页面的初次加载中，最常用
 * @author fuxinrong
 *
 */
public class BgLoadingManager extends LinearLayout implements LoadingManager {


    protected LinearLayout beginLoaingView;
    protected LinearLayout netErrorView;
    protected LinearLayout emptyDataView;
    protected TextView emptyDataTextView;
    protected ImageView emptyDataImageView;
    protected Context mContext;


    public BgLoadingManager(Context context) {
        this(context, null);
    }

    public BgLoadingManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View containerView = LayoutInflater.from(mContext).inflate(
                R.layout.loading_bg, this, true);
        beginLoaingView = (LinearLayout) containerView.findViewById(R.id.loaing_begin_ll);
        netErrorView = (LinearLayout) containerView.findViewById(R.id.loaing_neterror_ll);
        emptyDataView = (LinearLayout) containerView.findViewById(R.id.loaing_emptydata_ll);
        emptyDataTextView = (TextView) containerView.findViewById(R.id.loaing_empty_tv);
        emptyDataImageView = (ImageView) containerView.findViewById(R.id.loaing_empty_iv);
    }

    @Override
    public View showBeginLoaingView(ViewGroup parentView,
                                    ViewGroup.LayoutParams params) {
        beginLoaingView.setVisibility(View.VISIBLE);
        netErrorView.setVisibility(View.GONE);
        emptyDataView.setVisibility(View.GONE);
        if (this.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) this.getParent();
            viewGroup.removeView(this);
        }
        parentView.addView(this, params);
        return this;
    }

    @Override
    public View showNetErrorView(ViewGroup parentView,
                                 ViewGroup.LayoutParams params) {
        beginLoaingView.setVisibility(View.GONE);
        netErrorView.setVisibility(View.VISIBLE);
        emptyDataView.setVisibility(View.GONE);
        if (this.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) this.getParent();
            viewGroup.removeView(this);
        }
        parentView.addView(this, params);

        return this;
    }

    @Override
    public View showEmptyDataView(ViewGroup parentView, String msg,
                                  ViewGroup.LayoutParams params) {
        beginLoaingView.setVisibility(View.GONE);
        netErrorView.setVisibility(View.GONE);
        emptyDataView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(msg)) {
            emptyDataTextView.setText(msg);
        }
        if (this.getParent() != null) {

            ViewGroup viewGroup = (ViewGroup) this.getParent();
            viewGroup.removeView(this);

        }
        parentView.addView(this, params);
        return this;
    }

    @Override
    public View showEmptyDataView(ViewGroup parentView, int res, String msg, ViewGroup.LayoutParams params) {

        beginLoaingView.setVisibility(View.GONE);
        netErrorView.setVisibility(View.GONE);
        emptyDataView.setVisibility(View.VISIBLE);

        emptyDataImageView.setImageResource(res);

        if (!TextUtils.isEmpty(msg)) {
            emptyDataTextView.setText(msg);
        }
        if (this.getParent() != null) {

            ViewGroup viewGroup = (ViewGroup) this.getParent();
            viewGroup.removeView(this);

        }
        parentView.addView(this, params);
        return this;
    }


    @Override
    public View showResponseErrorView(ViewGroup parentView, int code, String msg,
                                      ViewGroup.LayoutParams params) {
        beginLoaingView.setVisibility(View.GONE);
        netErrorView.setVisibility(View.VISIBLE);
        TextView netTextView = (TextView) netErrorView.findViewById(R.id.loaing_neterror_tv);
        netTextView.setText(msg);
        emptyDataView.setVisibility(View.GONE);
        if (this.getParent() != null) {

            ViewGroup viewGroup = (ViewGroup) this.getParent();
            viewGroup.removeView(this);

        }
        parentView.addView(this, params);
        return this;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        netErrorView.setOnClickListener(onClickListener);
        emptyDataView.setOnClickListener(onClickListener);
    }

    @Override
    public View showSuccessView(ViewGroup parentView) {
        if (!emptyDataView.isShown()) {
            // 数据为空的UI没有进行显示
            beginLoaingView.setVisibility(View.GONE);
            netErrorView.setVisibility(View.GONE);
            emptyDataView.setVisibility(View.GONE);
            parentView.removeView(this);
        }
        return this;
    }

    @Override
    public void setReloadDataWhenClick(OnClickListener onBgViewClickListener) {
        netErrorView.setOnClickListener(onBgViewClickListener);
        emptyDataView.setOnClickListener(onBgViewClickListener);
    }

    @Override
    public boolean needRemoveRequest() {
        return false;
    }

    @Override
    public void clearViews() {
        beginLoaingView.setVisibility(View.GONE);
        netErrorView.setVisibility(View.GONE);
        emptyDataView.setVisibility(View.GONE);
        if (this.getParent() != null) {

            ViewGroup viewGroup = (ViewGroup) this.getParent();
            viewGroup.removeView(this);

        }
    }

    @Override
    public LoadingUiType uiType() {
        return LoadingUiType.BACKGROUND;
    }

    @Override
    public void setCancleClickListener(OnClickListener cancleClickListener) {

    }


}
