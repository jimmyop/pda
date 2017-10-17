package com.jimmy.lib.request;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.jimmy.lib.request.loadingmanager.LoadingManager;
import com.jimmy.lib.request.loadingmanager.LoadingUiType;

import java.util.HashMap;
import java.util.LinkedHashMap;


/***
 *
 * @author jimmy
 *  对请求中的请求类和对应的UI提示的封装
 */
public class RequestUiManager {
    /***
     *  用于标识该请求组合的标志
     */
    private String queueTag;
    /***
     * 存放请求的对象  key -- request getTag
     * LinkedHashMap 保证了插入和取出的顺序是一致的
     */
    protected LinkedHashMap<Object, YJLGsonRequest> requestsMap = new LinkedHashMap<>();
    /**
     * 存放请求的回调对象  key -- request getTag
     */
    protected HashMap<Object, YJLGsonRequest.ResponseCallbackListener> callbackListenersMap = new HashMap<>();

    /***
     * 加载方式的策略，设置不同的加载管理类，有不同的加载显示方式
     * 默认为背景加载的方式,null为不需要加载显示方式
     */
    protected LoadingManager loadingManager;


    /****
     * 标志队列的请求是否已经开始了
     */
    private boolean isRequesting = false;
    /***
     *  是否显示开始加载中的提示
     */
    private boolean isDisplayBeginLoding = true;

    public RequestUiManager(LoadingManager loadingManager) {
        this.loadingManager = loadingManager;
    }

    public LinkedHashMap<Object, YJLGsonRequest> getRequestsMap() {
        return requestsMap;
    }

    public void setRequestsMap(LinkedHashMap<Object, YJLGsonRequest> requestsMap) {
        this.requestsMap = requestsMap;
    }

    public HashMap<Object, YJLGsonRequest.ResponseCallbackListener> getCallbackListenersMap() {
        return callbackListenersMap;
    }

    public void setCallbackListenersMap(
            HashMap<Object, YJLGsonRequest.ResponseCallbackListener> callbackListenersMap) {
        this.callbackListenersMap = callbackListenersMap;
    }

    public LoadingManager getLoadingManager() {
        return loadingManager;
    }

    public void setLoadingManager(LoadingManager loadingManager) {
        this.loadingManager = loadingManager;
    }

    public int requestMapSize() {
        return requestsMap.size();
    }

    /***
     *  加入request 请求和回调 到对应的队列中
     * @param request
     * @param callbackListener
     */
    public void addRequestAndCallbackListener(YJLGsonRequest request, YJLGsonRequest.ResponseCallbackListener callbackListener) {
        callbackListenersMap.put(request.getRequestTag(), callbackListener);
        requestsMap.put(request.getRequestTag(), request);
    }

    protected void removeRequestAndCallbackListener(YJLGsonRequest request) {
        callbackListenersMap.remove(request.getTag());
        requestsMap.remove(request.getTag());
    }

    public YJLGsonRequest.ResponseCallbackListener getRequestCallBackListener(YJLGsonRequest request) {
        YJLGsonRequest.ResponseCallbackListener responseCallbackListener = callbackListenersMap.get(request.getTag());
        return responseCallbackListener;
    }

    /***
     *  从对应的队列中移除request 请求和回调
     * @param requestTag 某个request请求的标识tag
     */
    public void removeRequestAndCallbackListener(Object requestTag) {
        callbackListenersMap.remove(requestTag);
        requestsMap.remove(requestTag);
    }

    /***
     *  当请求失败时，是否需要把该请求移除
     *  不同的加载方式会影响到要不要移除失败的请求
     * @return
     */
    public boolean needRemoveRequest() {
        if (loadingManager != null) {
            return loadingManager.needRemoveRequest();
        }
        return true;
    }

    /***
     * 显示初始加载显示的View
     * @return
     */
    public View showBeginLoaingView(ViewGroup parentView, ViewGroup.LayoutParams params) {
        if (loadingManager != null && isDisplayBeginLoding) {
            return loadingManager.showBeginLoaingView(parentView, params);
        }
        return null;
    }

    /***
     * 显示网络错误的提示View
     * @return
     */
    public View showNetErrorView(ViewGroup parentView, ViewGroup.LayoutParams params) {
        if (loadingManager != null) {
            return loadingManager.showNetErrorView(parentView, params);
        }
        return null;
    }

    /***
     * 显示数据为空
     * @param parentView
     * @return
     */
    public View showEmptyDataView(ViewGroup parentView, String msg, ViewGroup.LayoutParams params) {
        if (loadingManager != null) {
            return loadingManager.showEmptyDataView(parentView, msg, params);
        }
        return null;
    }

    /***
     * 显示服务器返回数据错误的提示view
     * @param parentView
     * @return
     */
    public View showResponseErrorView(ViewGroup parentView, int code, String msg, ViewGroup.LayoutParams params) {
        if (loadingManager != null) {
            return loadingManager.showResponseErrorView(parentView, code, msg, params);
        }
        return null;
    }

    /***
     * 请求成功了
     * @param parentView
     * @return
     */
    public View showSuccessView(ViewGroup parentView) {
        if (loadingManager != null) {
            return loadingManager.showSuccessView(parentView);
        }
        return null;
    }

    /***
     * 消失加载的view
     * @param parentView
     * @return
     */
    public View dismissLoadingView(ViewGroup parentView) {
        if (loadingManager != null) {
            return loadingManager.showSuccessView(parentView);
        }
        return null;
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    /***
     * 设置重新加载数据的点击回调
     * @param onBgViewClickListener
     */
    public void setReloadDataWhenClick(OnClickListener onBgViewClickListener) {
        if (loadingManager != null) {
            loadingManager.setReloadDataWhenClick(onBgViewClickListener);
        }
    }

    public void clearData() {
        requestsMap.clear();
        callbackListenersMap.clear();
    }

    public boolean isDisplayBeginLoding() {
        return isDisplayBeginLoding;

    }

    public void setDisplayBeginLoding(boolean isDisplayBeginLoding) {
        this.isDisplayBeginLoding = isDisplayBeginLoding;
    }

    public String getQueueTag() {
        return queueTag;
    }

    public void setQueueTag(String queueTag) {
        this.queueTag = queueTag;
    }

    public boolean isSameLoadingManagerUi(LoadingUiType uiType) {
        if (loadingManager != null) {
            return loadingManager.uiType() == uiType;
        }
        return false;
    }
}
