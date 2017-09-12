package com.mcms.commonlib.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mcms.commonlib.R;
import com.mcms.commonlib.utils.ToastUtils;
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

    Toolbar mToolbar;
    ViewGroup mContainerLayout;

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
    protected boolean hasToolbar() {
        return false;
    }

    @LayoutRes
    protected int getRootLayout() {
        if (hasToolbar()) {
            return R.layout.base_toolbar_container;
        } else {
            return R.layout.base_container;
        }
    }

    @LayoutRes
    protected abstract int getContentLayout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getRootLayout(), container, false);

        mContainerLayout = ButterKnife.findById(rootView, R.id.container_layout);
        mToolbar = ButterKnife.findById(rootView, R.id.toolbar);
        int layoutId = getContentLayout();
        View view = inflater.inflate(layoutId, mContainerLayout, false);
        mContainerLayout.addView(view);

        mUnbinder = ButterKnife.bind(this, rootView);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
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

    public void setTitle(CharSequence title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                activity.setTitle(title);
            }
        }

    }


    public void setTitle(int titleId) {
        if (mToolbar != null) {
            mToolbar.setTitle(titleId);
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                activity.setTitle(titleId);
            }
        }
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
