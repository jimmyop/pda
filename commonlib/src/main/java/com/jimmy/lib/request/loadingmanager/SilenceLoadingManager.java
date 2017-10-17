package com.jimmy.lib.request.loadingmanager;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/***
 * 加载中：无提示
 * 加载失败：无提示.失败后，请求不会留在队列中
 * 数据为空：无提示
 * 加载成功：无提示
 * 用途：主要用于静默请求的UI提示
 * @author fuxinrong
 *
 */
public class SilenceLoadingManager implements LoadingManager {

    @Override
    public View showBeginLoaingView(ViewGroup parentView, LayoutParams params) {
        return null;
    }

    @Override
    public View showNetErrorView(ViewGroup parentView, LayoutParams params) {
        return null;
    }


    @Override
    public View showResponseErrorView(ViewGroup parentView, int code, String msg,
                                      LayoutParams params) {
        return null;
    }

    @Override
    public View showSuccessView(ViewGroup parentView) {
        return null;
    }

    @Override
    public void setReloadDataWhenClick(OnClickListener onBgViewClickListener) {

    }

    @Override
    public boolean needRemoveRequest() {
        return true;
    }

    @Override
    public void clearViews() {
        // TODO Auto-generated method stub

    }

    @Override
    public LoadingUiType uiType() {
        return LoadingUiType.SILENCE;
    }

    @Override
    public void setCancleClickListener(OnClickListener cancleClickListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public View showEmptyDataView(ViewGroup parentView, String msg,
                                  LayoutParams params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View showEmptyDataView(ViewGroup parentView, int res, String msg, LayoutParams params) {
        return null;
    }
}
