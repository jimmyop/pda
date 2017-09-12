package com.mcms.commonlib.avtivities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.mcms.commonlib.R;
import com.mcms.commonlib.request.RequestUiManager;
import com.mcms.commonlib.request.loadingmanager.ListViewLoaingManager;
import com.mcms.commonlib.request.loadingmanager.LoadingManager;
import com.mcms.commonlib.request.loadingmanager.LoadingUiType;
import com.mcms.commonlib.utils.CollectionUtils;
import com.mcms.commonlib.utils.LogUtils;
import com.mcms.commonlib.utils.StringUtils;
import com.mcms.commonlib.widgets.FooterView;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIDefaultRefreshOffsetCalculator;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.Collection;
import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by jimmy on 2017/7/27.
 */

public abstract class BaseRequestListViewActivity extends BaseRequestActivity implements AdapterView.OnItemClickListener, QMUIPullRefreshLayout.OnPullListener, ListView.OnScrollListener {

    protected static final int PAGE_SIZE = 10;
    protected static final String PARAM_PAGE_INDEX = "pageIndex";
    protected static final String PARAM_PAGE_SIZE = "pageSize";

    QMUIPullRefreshLayout mPtrLayout;
    ListView mListView;
    protected FooterView mFooterView;

    private boolean isLoadOver = true;// 是否已加载完成的开关

    @Override
    protected int getContentLayout() {
        return R.layout.base_ptr_listview_layout;
    }

    protected ListView getListView() {
        return mListView;
    }

    protected QMUIPullRefreshLayout getPtrLayout() {
        return mPtrLayout;
    }

    @CallSuper
    @Override
    protected void initViewEvents() {
        mPtrLayout = ButterKnife.findById(this, R.id.meituan_ptr_layout);
        mListView = ButterKnife.findById(this, R.id.list_view);
        mPtrLayout.setRefreshOffsetCalculator(new QMUIDefaultRefreshOffsetCalculator());
        mFooterView = new FooterView(this);
        mListView.setOnItemClickListener(this);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setDividerHeight(0);
        if (mPtrLayout != null) {
            mPtrLayout.setOnPullListener(this);
        }
    }

    @CallSuper
    @Override
    protected void initData(Bundle savedInstanceState) {
        firstLoaindData(DEFAULT_REQUEST_TAG, LoadingUiType.BACKGROUND);
    }

    @Override
    public final void onRefresh() {
        if (!mFooterView.isLoading()) {
            // 底部正在加载更多，不能进行顶部刷新
            onRefreshData(getQueueStringTag(), LoadingUiType.PULLUPDOWN);
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, int position, final long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == (view.getCount() - 1) && mFooterView.getStatus() != FooterView.LOADING && !isLoadOver) {
                if (!isTopRefreshing()) {
                    // 顶部正在下拉刷新，不能进行底部加载更多
                    mFooterView.setStatus(FooterView.LOADING);
                    loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 上拉加载更多数据
     */
    private void loadMore() {
//        mFooterView.setStatus(FooterView.LOADING);
//        mFooterView.setLayoutParams(pl_show);
        getNextPageData(getQueueStringTag(), LoadingUiType.PULLUPDOWN);

    }

    /**
     * 设置是否已加载完
     * true for over that means no more footview will show up again
     *
     * @param flag
     */
    public void setLoadOver(boolean flag) {
        isLoadOver = flag;

        if (isLoadOver) {

            if (mListView.getFooterViewsCount() > 0) {
                mFooterView.setStatus(FooterView.HIDE);
                mListView.removeFooterView(mFooterView);
            }

        } else {
            if (mListView.getFooterViewsCount() == 0) {
                mListView.addFooterView(mFooterView);

            }
        }
    }

    /**
     * 获取是否已load完
     *
     * @return
     */
    public boolean isLoadOver() {
        return isLoadOver;
    }

    /***
     * 请求数据成功，code = 0 时的回调
     */
    @Override
    public void onResponseSuccess(Object response, boolean cache, Object tag,
                                  Object ownerQueuetag) {
        super.onResponseSuccess(response, cache, tag, ownerQueuetag);
        onBindData(response, tag, ownerQueuetag, mFooterView.isLoading());
        if (!mFooterView.isLoading() && showDataEmptyUI(response)) {
            // 不是加载更多且数据为空，显示emptyUI
            RequestUiManager requestUiManager = getRequestUiManager(String.valueOf(ownerQueuetag));
            if (requestUiManager != null) {
                LoadingManager loadingManager = requestUiManager.getLoadingManager();
                if (loadingManager != null) {


                    String text = getSpecialEmpetyData(response);

                    if (!StringUtils.isEmpty(setDataEmptyUIText())) {
                        text = setDataEmptyUIText();
                    }

                    if (setDataEmptyUIImage() != 0) {
                        loadingManager.showEmptyDataView(getContentContainer(), setDataEmptyUIImage(), text, getParms());
                    } else {
                        loadingManager.showEmptyDataView(getContentContainer(), text, getParms());
                    }

                } else {
                    LogUtils.d("loadingManager is null cann't show emptyUI");
                }
            } else {
                LogUtils.d("requestUiManager is null cann't show emptyUI");
            }
        }
        if (String.valueOf(ownerQueuetag).equals(getQueueStringTag())) { // 是listview请求用到的组
//            mFooterView.setStatus(FooterView.HIDE);
//            mFooterView.setLayoutParams(pl_hide);
            LogUtils.e(getQueueStringTag() + " map size " + getRequestMap(getQueueStringTag()).size());
            if (getRequestMap(getQueueStringTag()).size() == 0 && mPtrLayout != null) {
                mPtrLayout.finishRefresh();
            }
        }
    }

    /**
     * 隐藏加载更多
     */
    protected void hideFooterView() {
//        mFooterView.setStatus(FooterView.HIDE);
//        mFooterView.setLayoutParams(pl_hide);
    }

    /***
     * 请求数据成功，服务器返回失败信息 code ！= 0 时的回调
     */
    @Override
    public void onResponseCodeFailure(int code, String msg_cn, String msg_en,
                                      Object tag, Object ownerQueuetag) {
        super.onResponseCodeFailure(code, msg_cn, msg_en, tag, ownerQueuetag);
        if (String.valueOf(ownerQueuetag).equals(getQueueStringTag())) { // 是listview请求用到的组失败了

            if (mListView.getFooterViewsCount() > 0) {
                mFooterView.setStatus(FooterView.HIDE);
                mListView.removeFooterView(mFooterView);
            }


            if (mPtrLayout != null) {
                mPtrLayout.finishRefresh();
            }
        }
    }

    /***
     * 请求数据失败的回调
     */
    @Override
    public void onResponseFailure(VolleyError error, Object tag, Object ownerQueuetag) {
        super.onResponseFailure(error, tag, ownerQueuetag);
        if (String.valueOf(ownerQueuetag).equals(getQueueStringTag())) { // 是listview请求用到的组失败了

            if (mListView.getFooterViewsCount() > 0) {
                mFooterView.setStatus(FooterView.HIDE);
                mListView.removeFooterView(mFooterView);
            }

            if (mPtrLayout != null) {
                mPtrLayout.finishRefresh();
            }
        }
    }

    protected boolean isTopRefreshing() {
        if (mPtrLayout != null) {
//            return mPtrLayout.isRefreshing();
        }
        return false;
    }


    /***
     *
     *
     *  首次加载服务器数据
     *   @param queueTag 采用的请求组标识
     *   @param backGroundUI 加载的UI类型,此处是背景的加载模式
     *
     *
     */
    public abstract void firstLoaindData(String queueTag, LoadingUiType backGroundUI);

    /***
     *  listiew 下拉刷新数据
     *   @param queueTag  采用的请求组标识,无特殊情况，必须使用该标识，否则功能会受限
     *    @param pullUpDownUI 加载的UI类型,此处是上下拉的加载模式
     *
     */
    public abstract void onRefreshData(String queueTag, LoadingUiType pullUpDownUI);

    /**
     * pull up to load more will call this function
     *
     * @param queueTag     采用的请求组标识
     * @param pullUpDownUI 加载的UI类型,此处是上下拉的加载模式
     */
    public abstract void getNextPageData(String queueTag, LoadingUiType pullUpDownUI);

    /***
     *
     * @param response
     * @param tag
     * @param isLoadMore 是否是加载更多
     */
    public abstract void onBindData(Object response, Object tag, Object ownerQueuetag, boolean isLoadMore);

    /***
     *  返回listview 上拉加载更多用到的请求组标识
     *  可覆盖
     * @return
     */
    public String getQueueStringTag() {
        return "listview";
    }

    /***
     *  list 集合是否为空，用于判断是否需要base显示空数据的UI.true 需要显示,false不需要
     * @param response 保证是需要有数组的对象
     * @return true for empty,than show emptyUI
     */
    public abstract boolean showDataEmptyUI(Object response);

    /**
     * 设置空数据的时候的图片
     *
     * @return
     */
    protected int setDataEmptyUIImage() {
        return 0;
    }

    /**
     * 设置空数据的时候的文字
     *
     * @return
     */
    protected String setDataEmptyUIText() {
        return "";
    }


    protected void setDefaultQueuePullLoadingManager() {
        setRequestQueueLoadingManagerByTag(DEFAULT_REQUEST_TAG, LoadingUiType.PULLUPDOWN);
    }

    @Override
    protected LoadingManager loadingManagerFactory(LoadingUiType type) {

        if (type == LoadingUiType.PULLUPDOWN) {
            return new ListViewLoaingManager(this);
        } else {
            return super.loadingManagerFactory(type);
        }
    }

    /***
     * 设置listview的分割线和高度
     * @param drawable
     * @param heightPx  px单位
     */
    protected void setListViewDivider(Drawable drawable, int heightPx) {
        mListView.setDivider(drawable);
        mListView.setDividerHeight(heightPx);
    }

    /**
     * 获取分页加载数据的索引
     *
     * @param count
     * @return
     */
    protected int getPageIndex(int count) {
        return getPageIndex(count, PAGE_SIZE);
    }

    /**
     * 获取分页加载数据的索引
     *
     * @param count
     * @param pageSize
     * @return
     */
    protected int getPageIndex(int count, int pageSize) {
        int pageIndex = count / pageSize + 1;
        return pageIndex;
    }

    /**
     * 获取分页加载数据的索引
     *
     * @param collection
     * @return
     */
    protected int getPageIndex(Collection<?> collection) {
        return getPageIndex(CollectionUtils.getSize(collection), PAGE_SIZE);
    }

    /**
     * 获取分页加载数据的索引
     *
     * @param collection
     * @param pageSize
     * @return
     */
    protected int getPageIndex(Collection<?> collection, int pageSize) {
        return getPageIndex(CollectionUtils.getSize(collection), pageSize);
    }

    /**
     * 设置加载更多的状态
     *
     * @param collection
     */
    protected void setLoadMoreState(Collection<?> collection) {
        setLoadOver(CollectionUtils.isSmallThan(collection, PAGE_SIZE));
    }

    /**
     * 设置加载更多的状态
     *
     * @param collection
     * @param pageSize
     */
    protected void setLoadMoreState(Collection<?> collection, int pageSize) {
        setLoadOver(CollectionUtils.isSmallThan(collection, pageSize));
    }

    protected HashMap<String, String> getPageSizeParam(int pageIndex, int pageSize) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE_INDEX, "" + pageIndex);
        params.put(PARAM_PAGE_SIZE, "" + pageSize);
        return params;
    }

    @Override
    public void onMoveTarget(int offset) {

    }

    @Override
    public void onMoveRefreshView(int offset) {

    }
}
