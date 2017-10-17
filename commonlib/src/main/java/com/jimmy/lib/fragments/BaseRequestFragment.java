package com.jimmy.lib.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.jimmy.lib.request.RequestUiManager;
import com.jimmy.lib.request.YJLGsonRequest;
import com.jimmy.lib.request.loadingmanager.BgLoadingManager;
import com.jimmy.lib.request.loadingmanager.DialogLoadingManager;
import com.jimmy.lib.request.loadingmanager.ListViewLoaingManager;
import com.jimmy.lib.request.loadingmanager.LoadingManager;
import com.jimmy.lib.request.loadingmanager.LoadingUiType;
import com.jimmy.lib.request.loadingmanager.SilenceLoadingManager;
import com.jimmy.lib.utils.LogUtils;
import com.jimmy.lib.utils.UIUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jimmy on 2017/7/27.
 */
public abstract class BaseRequestFragment extends BaseFragment implements
        YJLGsonRequest.ResponseCallbackListener, InvocationHandler {
    public final static int RELOGING_SUCCESS_REQ_CODE = 789;

    /**
     * 默认请求组的标识
     */
    protected final static String DEFAULT_REQUEST_TAG = "default";
    /****
     * 存放请求管理的数组，默认第一个RequestUiManager。
     */
    private ConcurrentLinkedQueue<RequestUiManager> requestUiManagerList;

    /***
     * 存放请求的对象 key -- request getTag LinkedHashMap 保证了插入和取出的顺序是一致的
     */
    private Map<Object, YJLGsonRequest> requestsMap;
    /***
     * 当前在运行中的请求组 标识
     */
    protected String runningQueueTag = DEFAULT_REQUEST_TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRequestManager();
    }

    private void initRequestManager() {
        requestUiManagerList = new ConcurrentLinkedQueue<RequestUiManager>();
        RequestUiManager defaultRequestUiManager = requestUiManagerFactory(
                DEFAULT_REQUEST_TAG, LoadingUiType.BACKGROUND);
        requestsMap = defaultRequestUiManager.getRequestsMap();
        requestUiManagerList.add(defaultRequestUiManager);
    }

    /***
     * 开始启动第一条请求,成功后会继续执行队列的请求，直到结束或者是请求失败
     * 子类onResponseSuccess()方法中新增请求到队列后，不能重新startRequests()，队列会自动执行
     */
    protected void startDefaultQueueRequests(boolean isDisplayLoding) {
        startQueueRequests(isDisplayLoding, DEFAULT_REQUEST_TAG);
    }

    protected void startQueueRequests(boolean isDisplayLoding, String tag) {
        for (RequestUiManager requestUiManager : requestUiManagerList) {
            if (tag.equals(requestUiManager.getQueueTag())) {

                Set<Map.Entry<Object, YJLGsonRequest>> entrySet = requestUiManager
                        .getRequestsMap().entrySet();
                Iterator iter = entrySet.iterator();
                if (iter.hasNext()) {
                    requestUiManager.setDisplayBeginLoding(isDisplayLoding);
                    requestUiManager.showBeginLoaingView(getContentContainer(),
                            getParms());
                    Map.Entry entry = (Map.Entry) iter.next();
                    YJLGsonRequest request = (YJLGsonRequest) entry.getValue();
                    request.sendRequest();
                }
                // break;
            }

        }

    }

    /***
     *  获得除了account.authority.getkey外的第一条请求
     * @param requestUiManager
     * @return
     */
    public YJLGsonRequest getNextRequestEceptKeyReq(RequestUiManager requestUiManager) {
        Set<Map.Entry<Object, YJLGsonRequest>> entrySet = requestUiManager.getRequestsMap().entrySet();
        Iterator iter = entrySet.iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            YJLGsonRequest request = (YJLGsonRequest) entry.getValue();
            if (request.getTag().toString().contains("account.authority.getkey")) {
                continue;
            }
            return request;
        }
        return null;
    }

    protected ViewGroup.LayoutParams getParms() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /****
     * RequestUiManager 工厂方法
     *
     * @param queueTag
     *            RequestUiManager的标识
     * @param type
     *            UI加载的类型
     * @return RequestUiManager
     */
    private RequestUiManager requestUiManagerFactory(String queueTag,
                                                     LoadingUiType type) {
        RequestUiManager requestUiManager = null;
        requestUiManager = new RequestUiManager(loadingManagerFactory(type));
        requestUiManager.setQueueTag(queueTag);
        return requestUiManager;
    }

    /***
     * 产生不同加载UI管理器
     *
     * @param type
     * @return
     */
    protected LoadingManager loadingManagerFactory(LoadingUiType type) {
        LoadingManager localLoadingManager = null;
        if (type == LoadingUiType.BACKGROUND) {
            localLoadingManager = new BgLoadingManager(getActivity());
            localLoadingManager.setReloadDataWhenClick(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startDefaultQueueRequests(true);
                }
            });
        } else if (LoadingUiType.DIALOG == type) {
            localLoadingManager = new DialogLoadingManager(getActivity(),
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startQueueRequests(true, runningQueueTag);
                        }
                    }, new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    clearQueueHistoryRequestsByTag(runningQueueTag);
                }
            });
        } else if (type == LoadingUiType.PULLUPDOWN) {
            localLoadingManager = new ListViewLoaingManager(getActivity());
        } else if (LoadingUiType.SILENCE == type) {
            localLoadingManager = new SilenceLoadingManager();
        }
        return localLoadingManager;
    }

    protected void clearQueueHistoryRequestsByTag(String tag) {
        for (RequestUiManager manager : requestUiManagerList) {
            if (tag.equals(manager.getQueueTag())) {
                manager.clearData();
            }
        }

    }

    /***
     * 请求数据成功，code = 0 时的回调
     */
    @Override
    public void onResponseSuccess(Object response, boolean cache, Object tag,
                                  Object owerQueueTag) {

    }

    /***
     * 请求数据成功，code ！= 0 时的回调 （默认情况下loadingManager ！= null）提示的UI已处理，子类不需要重复显示处理了
     */
    @Override
    public void onResponseCodeFailure(int code, String msg_cn, String msg_en,
                                      Object tag, Object owerQueueTag) {

    }

    /***
     * 请求数据失败的回调 （默认情况下loadingManager ！= null）提示的UI已处理，子类不需要重复显示处理了
     */
    @Override
    public void onResponseFailure(VolleyError error, Object tag,
                                  Object owerQueueTag) {

    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        for (RequestUiManager requestUiManager : requestUiManagerList) {

            processSigleRequestManage(requestUiManager, method, objects);
        }
        return null;
    }

    /***
     * 处理单组（请求队列和UI）的请求
     *
     * @param requestUiManager
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object processSigleRequestManage(RequestUiManager requestUiManager,
                                            Method method, Object[] args) throws Throwable {
        runningQueueTag = requestUiManager.getQueueTag();
        int length = args.length;
        Object requestTag = args[length - 2];

        YJLGsonRequest request = requestUiManager.getRequestsMap().get(requestTag);
        if (null == request) {
            return null;
        }

        String url = request.getUrl();
        YJLGsonRequest.ResponseCallbackListener responseCallbackListener = requestUiManager.getCallbackListenersMap().get(requestTag);
        if ("onResponseSuccess".equals(method.getName())) {
            // 请求数据成功，且code = 0
            requestUiManager.removeRequestAndCallbackListener(requestTag);

        } else if ("onResponseCodeFailure".equals(method.getName())) {
            // 请求数据成功，但code != 0
            LogUtils.e("onResponseCodeFailure " + url);
            // 有的部分是局部loading
            UIUtils.getInstance().hideProgressDialog(getActivity());

            Object obj = args[1];
            if (obj == null) {
                obj = "服务器忙,请稍候再试";
            }

            int code = (Integer) args[0];
            boolean needLogin = false;
            if (1000 == code || 1104 == code) {

            } else if (1103 == code) { // session_key 会话失效了。


            } else if (1101 == code) { // 时间戳过期

            } else if (26010001 == code) {

            }
            boolean removeRequest = true;
            // 显示UI
            if (requestUiManager != null) {
                removeRequest = requestUiManager.needRemoveRequest();
                /*
                 * if ( !needLogin) {
				 * requestUiManager.showResponseErrorView(mRootViewRelative
				 * ,obj.toString(),getParms()); } else {
				 * requestUiManager.showSuccessView(mRootViewRelative); }
				 */
                requestUiManager.showResponseErrorView(getContentContainer(), code,
                        obj.toString(), getParms());
            }
            // 根据UI里的方法判断视情况决定是否要把请求移除
            if (removeRequest) {
                requestUiManager.removeRequestAndCallbackListener(requestTag);
            }

        } else if ("onResponseFailure".equals(method.getName())) {

            if (requestUiManager != null) {
                requestUiManager.showNetErrorView(getContentContainer(), getParms());
            }
            // 有的部分是局部loading
            UIUtils.getInstance().hideProgressDialog(getActivity());

            boolean removeRequest = true;
            // 显示UI
            if (requestUiManager != null) {
                removeRequest = requestUiManager.needRemoveRequest();
            }
            // 根据UI里的方法判断视情况决定是否要把请求移除
            if (removeRequest) {
                requestUiManager.removeRequestAndCallbackListener(requestTag);
            }

            VolleyError error = (VolleyError) args[0];
            // 请求数据失败
            LogUtils.e("onResponseFailure " + url + " error "
                    + error.toString());

        }
        // 方法调用
        try {
            method.invoke(responseCallbackListener, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 方法调用之后

        onResultLogic(method.getName(), requestUiManager);
        return null;
    }

    /**
     * 方法调用之后
     *
     * @param methodName 方法名
     */
    private void onResultLogic(String methodName,
                               RequestUiManager requestUiManager) {
        if ("onResponseSuccess".equals(methodName)) {

            boolean nextRequests = startQueueNextRequests(requestUiManager);
            if (!nextRequests) {
                // 所有请求已发送完毕
                LogUtils.d(requestUiManager.getQueueTag()
                        + " all request sended success");
                requestUiManager.showSuccessView(getContentContainer());

            }
        }
    }

    /***
     * 启动下一跳请求
     *
     * @return boolean 是否有下一条进行启动
     */
    private boolean startQueueNextRequests(RequestUiManager requestUiManager) {
        boolean hasNext = false;
        Set<Map.Entry<Object, YJLGsonRequest>> entrySet = requestUiManager.getRequestsMap().entrySet();
        Iterator iter = entrySet.iterator();
        if (iter.hasNext()) {
            hasNext = true;
            Map.Entry entry = (Map.Entry) iter.next();
            YJLGsonRequest request = (YJLGsonRequest) entry.getValue();
            request.sendRequest();
        }
        return hasNext;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RELOGING_SUCCESS_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                LogUtils.d("relogin success");
            } else {
                LogUtils.d("no relogin ");
            }
        }
    }

    /***
     * 把请求加到默认的请求队列中
     *
     * @param request
     */
    protected void addYJLGsonRequest2DefaultQueue(YJLGsonRequest request) {
        RequestUiManager requestUiManager = getRequestUiManager(DEFAULT_REQUEST_TAG);
        LoadingManager runLoadingManager = requestUiManager.getLoadingManager();
        if (runLoadingManager != null) {
            addYJLGsonRequest2QueueByTag(request, DEFAULT_REQUEST_TAG,
                    runLoadingManager.uiType());
        } else {
            addYJLGsonRequest2QueueByTag(request, DEFAULT_REQUEST_TAG,
                    LoadingUiType.SILENCE);
        }
    }

    /***
     *
     * 把请求加到特定的请求组中
     *
     * @param request
     * @param queueTag
     *            请求队列的标识
     * @param uiType
     *            加载的ui类型,以请求组的最后一个request为主
     */
    @SuppressWarnings("unchecked")
    protected void addYJLGsonRequest2QueueByTag(YJLGsonRequest request,
                                                String queueTag, LoadingUiType uiType) {
        RequestUiManager myRequestUiManager = null;
        YJLGsonRequest.ResponseCallbackListener responseCallbackListener = request
                .getmResponseCallbackListener();
        YJLGsonRequest.ResponseCallbackListener responseCallbackListenerProxy = (YJLGsonRequest.ResponseCallbackListener) Proxy
                .newProxyInstance(responseCallbackListener.getClass().getClassLoader(),
                        new Class[]{YJLGsonRequest.ResponseCallbackListener.class}, this);
        request.setmResponseCallbackListener(responseCallbackListenerProxy);
        request.setOwerQueueTag(queueTag);
        for (RequestUiManager requestUiManager : requestUiManagerList) {
            if (queueTag.equals(requestUiManager.getQueueTag())) {
                myRequestUiManager = requestUiManager;
            }
        }
        if (null == myRequestUiManager) {
            myRequestUiManager = requestUiManagerFactory(queueTag, uiType);
            requestUiManagerList.add(myRequestUiManager);
        }
        if (!myRequestUiManager.isSameLoadingManagerUi(uiType)) {
            LoadingManager loadingManager = myRequestUiManager.getLoadingManager();
            if (loadingManager != null) {
                loadingManager.clearViews();
            }
            myRequestUiManager.setLoadingManager(loadingManagerFactory(uiType));
        }
        myRequestUiManager.addRequestAndCallbackListener(request,
                responseCallbackListener);
    }

    /***
     * 加入请求到正在跑的请求组里,无需手动启动
     *
     * @param request
     */
    public void addRequst2RunningQueue(YJLGsonRequest request) {
        RequestUiManager requestUiManager = getRequestUiManager(runningQueueTag);
        LoadingManager runLoadingManager = requestUiManager.getLoadingManager();
        if (runLoadingManager != null) {
            addYJLGsonRequest2QueueByTag(request, runningQueueTag,
                    runLoadingManager.uiType());
        } else {
            addYJLGsonRequest2QueueByTag(request, runningQueueTag,
                    LoadingUiType.SILENCE);
        }

    }

    /***
     * 返回请求管理组的管理器
     *
     * @param queueTag
     * @return
     */
    protected RequestUiManager getRequestUiManager(String queueTag) {
        for (RequestUiManager requestUiManager : requestUiManagerList) {
            if (requestUiManager.getQueueTag().equals(queueTag)) {
                return requestUiManager;
            }
        }
        return null;
    }

    /***
     * 设置默认的加载方式为对话框的形式
     */
    protected void setDefaultQueueDialogLoadingManager() {
        setRequestQueueLoadingManagerByTag(DEFAULT_REQUEST_TAG,
                LoadingUiType.DIALOG);
    }

    /***
     * 设置默认的加载方式为背景的形式
     */
    protected void setDefaultQueueBgLoadingManager() {
        setRequestQueueLoadingManagerByTag(DEFAULT_REQUEST_TAG,
                LoadingUiType.BACKGROUND);

    }

    /***
     * 设置指定请求组的加载方式 会调用LoadingManagerFactory 产生加载ui管理类
     */
    protected void setRequestQueueLoadingManagerByTag(String tag,
                                                      LoadingUiType uiType) {
        for (RequestUiManager requestUiManager : requestUiManagerList) {
            if (tag.equals(requestUiManager.getQueueTag())) {

                LoadingManager loadingManager = loadingManagerFactory(uiType);
                requestUiManager.setLoadingManager(loadingManager);
            }
        }

    }

    public Map<Object, YJLGsonRequest> getRequestMap(String queueTag) {
        for (RequestUiManager requestUiManager : requestUiManagerList) {
            if (requestUiManager.getQueueTag().equals(queueTag)) {
                return requestUiManager.getRequestsMap();
            }
        }
        return new HashMap<Object, YJLGsonRequest>();
    }

    /***
     *  取得特殊的数据为空的提示,只会在需要显示空页面提示的时候调用该方法
     * @param response
     */
    protected String getSpecialEmpetyData(Object response) {
        return null;
    }
}
