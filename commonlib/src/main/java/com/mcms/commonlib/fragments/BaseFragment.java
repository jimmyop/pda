package com.mcms.commonlib.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mcms.commonlib.R;
import com.mcms.commonlib.utils.ToastUtils;
import com.mcms.commonlib.widgets.TitleHeadLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jimmy on 2017/7/27.
 */
public abstract class BaseFragment extends Fragment {

    private TitleHeadLayout mTitleHeadLayout;
    private ViewGroup mContainerLayout;
    private Unbinder mUnbinder;
    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    /**
     * 是否有标题拦
     *
     * @return
     */
    protected boolean hasTitleLayout() {
        return false;
    }

    /**
     * 容器是否在标题栏下面
     *
     * @return true 默认是在下面
     */
    protected boolean underTitleLayout() {
        return true;
    }

    @LayoutRes
    protected int getRootLayout() {
        return R.layout.base_toolbar_container;
    }

    @LayoutRes
    protected abstract int getContentLayout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getRootLayout(), container, false);

        mTitleHeadLayout = ButterKnife.findById(rootView, R.id.title_head_layout);
        mContainerLayout = ButterKnife.findById(rootView, R.id.container_layout);

        View view = inflater.inflate(getContentLayout(), mContainerLayout, false);
        mContainerLayout.addView(view);

        mUnbinder = ButterKnife.bind(this, rootView);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 判断有没有设置TitleLayout
        if (hasTitleLayout()) {
            //有TitleLayout ，初始化
            initHeaderView(mTitleHeadLayout);

            //小于6.0系统，隐藏title 上面的 statusbar间隔
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mTitleHeadLayout.showStatusBar(false);
            }

            // 判断容器是否在titleLayout下面
            if (underTitleLayout()) {
                //设置容器距离parent top 的高度
                int height = mTitleHeadLayout.getTitleLayoutHeight();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, height, 0, 0);
                mContainerLayout.setLayoutParams(lp);
            }

        } else {
            // 隐藏titleLayout
            mTitleHeadLayout.showTitle(false);

            //小于6.0系统，隐藏title 上面的 statusbar间隔
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mTitleHeadLayout.showStatusBar(false);
            }

            if (underTitleLayout()) {

                int height = mTitleHeadLayout.getTitleLayoutHeight();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, height, 0, 0);
                mContainerLayout.setLayoutParams(lp);
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewEvents();

        initData(savedInstanceState);
    }

    protected abstract void initViewEvents();

    protected abstract void initData(Bundle savedInstanceState);

    protected ViewGroup getContentContainer() {
        return mContainerLayout;
    }

    @Override
    public void onDestroy() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(Object event) {

    }


    /**
     * 初始化Title布局
     *
     * @param headLayout
     */
    protected void initHeaderView(TitleHeadLayout headLayout) {
    }

    protected String getFragmentSimpleName() {
        return getClass().getSimpleName();
    }

    protected void toast(int resId) {
        ToastUtils.toast(getActivity(), resId);
    }

    protected void toast(String text) {
        ToastUtils.toast(getActivity(), text);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDrawalEvent(Object event) {
    }

}
