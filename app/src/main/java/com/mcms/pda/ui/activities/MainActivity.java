package com.mcms.pda.ui.activities;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.android.volley.Request;
import com.mcms.commonlib.adapter.YJLArrayAdapter;
import com.mcms.commonlib.avtivities.BaseRequestListViewActivity;
import com.mcms.commonlib.constants.ConstantsUrl;
import com.mcms.commonlib.request.YJLGsonRequest;
import com.mcms.commonlib.request.data.BaseReslutRes;
import com.mcms.commonlib.request.loadingmanager.LoadingUiType;
import com.mcms.commonlib.utils.LogUtils;
import com.mcms.commonlib.widgets.TitleHeadLayout;
import com.mcms.pda.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseRequestListViewActivity {

    MyAdapter mMyAdapter;

    @Override
    protected void initHeaderView(TitleHeadLayout headLayout) {
        super.initHeaderView(headLayout);
        headLayout.setTitleText("Test");
    }

    @Override
    protected void initViewEvents() {
        super.initViewEvents();
        mMyAdapter = new MyAdapter(this);
        getListView().setAdapter(mMyAdapter);
    }

    @Override
    public void firstLoaindData(String queueTag, LoadingUiType backGroundUI) {

        LogUtils.e("-----firstLoaindData------");
        //dadasdad

        mMyAdapter.clear();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("item：" + i);
        }
        mMyAdapter.addAll(list);

        addYJLGsonRequest2QueueByTag(getUserInfoRequest(), queueTag, backGroundUI);
        startQueueRequests(true, queueTag);
    }

    @Override
    public void onRefreshData(String queueTag, LoadingUiType pullUpDownUI) {
        mMyAdapter.clear();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("item：" + i);
        }
        mMyAdapter.addAll(list);
        LogUtils.e("-----onRefreshData------");


        getPtrLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPtrLayout().finishRefresh();
            }
        }, 2000);
    }

    @Override
    public void getNextPageData(String queueTag, LoadingUiType pullUpDownUI) {
        LogUtils.e("-----getNextPageData------");
    }

    @Override
    public void onBindData(Object response, Object tag, Object ownerQueuetag, boolean isLoadMore) {
        LogUtils.e("-----onBindData------");
    }

    @Override
    public boolean showDataEmptyUI(Object response) {
        return false;
    }

    public class MyAdapter extends YJLArrayAdapter<String> {

        public MyAdapter(Context context) {
            super(context, null, R.layout.item_test);
        }

        @Override
        public void bindItemData(View view, String data, ViewHolder holder) {
            super.bindItemData(view, data, holder);
            holder.setText(R.id.tv, data);
        }
    }

    private YJLGsonRequest<BaseReslutRes> getUserInfoRequest() {
//        HashMap<String, String> parm = new HashMap<>();
//        parm.put("usercode", "7126");
//        parm.put("password", "7071");
        YJLGsonRequest request = new YJLGsonRequest<>(
                ConstantsUrl.LOGIN,
                null,
                BaseReslutRes.class,
                this
        );
        request.setMethod(Request.Method.GET);
        return request;
    }

}
