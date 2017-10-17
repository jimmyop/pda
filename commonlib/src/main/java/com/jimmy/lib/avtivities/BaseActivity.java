package com.jimmy.lib.avtivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.jimmy.lib.R;
import com.jimmy.lib.utils.ToastUtils;
import com.jimmy.lib.widgets.TitleHeadLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 1、hasToolbar()方法决定是否有标题
 * 2、getContentLayout()方法决定内容布局
 * 5、swipebackable()方法决定当前页面是否可以滑动返回
 * Created by jimmy on 2017/7/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private TitleHeadLayout mTitleHeadLayout;
    private ViewGroup mContainerLayout;
    private InputMethodManager mInputMethodManager;


    @LayoutRes
    protected int getRootLayout() {
        return R.layout.base_toolbar_container;
    }

    @LayoutRes
    protected abstract int getContentLayout();

    /**
     * 标题是否有返回按钮
     *
     * @return
     */
    protected boolean navigationUp() {
        return true;
    }

    /**
     * 是否有标题栏
     *
     * @return true 默认有标题栏
     */
    protected boolean hasTitleLayout() {
        return true;
    }

    /**
     * 容器是否在标题栏下面
     *
     * @return true 默认是在下面
     */
    protected boolean underTitleLayout() {
        return true;
    }

    /**
     * statusbar 字体颜色默认是黑色
     *
     * @return true
     */
    protected boolean isStatusDark() {
        return true;
    }

    /**
     * 是否可以滑动返回，默认为true
     *
     * @return
     */
    protected boolean swipebackable() {
        return false;
    }

    protected boolean hideSoftInputFromWindow(IBinder windowToken) {
        if (mInputMethodManager != null) {
            return mInputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setContentView(getRootLayout());

        QMUIStatusBarHelper.translucent(this);

        if (isStatusDark()) {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }

        mTitleHeadLayout = ButterKnife.findById(this, R.id.title_head_layout);
        mContainerLayout = ButterKnife.findById(this, R.id.container_layout);


        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(getContentLayout(), mContainerLayout, false);
        mContainerLayout.addView(contentView);

        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        // 判断有没有设置TitleLayout
        if (hasTitleLayout()) {
            //有TitleLayout ，初始化
            initHeaderView(mTitleHeadLayout);

            //小于6.0系统，隐藏title 上面的 statusbar间隔
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mTitleHeadLayout.showStatusBar(false);
            } else {

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

        initViewEvents();
        initData(savedInstanceState);

    }

    /**
     * 初始化状态栏相关，
     * PS: 设置全屏需要在调用super.onCreate(arg0);之前设置setIsFullScreen(true);否则在Android 6.0下非全屏的activity会出错;
     * SDK19：可以设置状态栏透明，但是半透明的SYSTEM_BAR_BACKGROUNDS会不好看；
     * SDK21：可以设置状态栏颜色，并且可以清除SYSTEM_BAR_BACKGROUNDS，但是不能设置状态栏字体颜色（默认的白色字体在浅色背景下看不清楚）；
     * SDK23：可以设置状态栏为浅色（SYSTEM_UI_FLAG_LIGHT_STATUS_BAR），字体就回反转为黑色。
     * 为兼容目前效果，仅在SDK23才显示沉浸式。
     */

    boolean mIsFullScreen = false;
    boolean isStatusBarTranslate = false;

    private void initStatusBar(boolean isDark) {
        Window win = getWindow();
        if (mIsFullScreen) {
            win.requestFeature(Window.FEATURE_NO_TITLE);
            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕高亮
        } else {
            //KITKAT也能满足，只是SYSTEM_UI_FLAG_LIGHT_STATUS_BAR（状态栏字体颜色反转）只有在6.0才有效
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
                // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
                if (isDark) {
                    win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }

                // 部分机型的statusbar会有半透明的黑色背景
                win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                win.setStatusBarColor(Color.TRANSPARENT);// SDK21

                isStatusBarTranslate = true;
            }
        }
    }

    /**
     * 初始化Title布局
     *
     * @param headLayout
     */
    protected void initHeaderView(TitleHeadLayout headLayout) {
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(Object event) {

    }

    protected abstract void initViewEvents();

    protected abstract void initData(Bundle savedInstanceState);


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    /**
     * 预览选择的图片
     *
     * @param selectedImageList
     * @param position
     */
    public void previewSelectedImageList(ArrayList<String> selectedImageList, int position) {
//        MultiImagePreviewActivity.startForResult(this, selectedImageList, position, REQUEST_CODE_PREVIEW_IMAGE);
    }

    protected void onCropImageError(String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            toast(errorMsg);
        }
    }

    /**
     * 裁剪图片回调
     *
     * @param path
     */
    protected void onCropImageCallback(String path) {

    }

    protected TitleHeadLayout getTitleHeadLayout() {
        return mTitleHeadLayout;
    }

    protected ViewGroup getContentContainer() {
        return mContainerLayout;
    }

    protected void toast(int resId) {
        ToastUtils.toast(this, resId);
    }

    protected void toast(String text) {
        ToastUtils.toast(this, text);
    }

    public static void startActivity(Activity context, Class clazz) {
        startActivity(context, clazz, null, 0);
    }

    public static void startActivity(Activity context, Class clazz, Bundle bundle) {
        startActivity(context, clazz, bundle, 0);
    }

    public static void startActivity(Activity context, Class clazz, int requestCode) {
        startActivity(context, clazz, null, requestCode);
    }

    public static void startActivity(Activity context, Class clazz, Bundle bundle, int requestCode) {
        Intent starter = new Intent(context, clazz);

        if (bundle != null) {
            starter.putExtras(bundle);
        }

        if (requestCode > 0) {
            context.startActivityForResult(starter, requestCode);
        } else {
            context.startActivity(starter);
        }

    }


}
