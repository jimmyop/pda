package com.mcms.pda.ui.activities;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.jimmy.lib.adapter.BaseArrayAdapter;
import com.jimmy.lib.avtivities.BaseRequestListViewActivity;
import com.jimmy.lib.request.YJLGsonRequest;
import com.jimmy.lib.request.data.BaseReslutRes;
import com.jimmy.lib.request.loadingmanager.LoadingUiType;
import com.jimmy.lib.utils.LogUtils;
import com.jimmy.lib.widgets.TitleHeadLayout;
import com.mcms.pda.R;
import com.mcms.pda.constants.ConstantsUrl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseRequestListViewActivity {

    MyAdapter mMyAdapter;

    @Override
    protected void initHeaderView(TitleHeadLayout headLayout) {
        super.initHeaderView(headLayout);
        headLayout.setTitleText("Main");
        headLayout.setRightText("grid");
        headLayout.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.this, GridActivity.class);
            }
        });
    }

    @Override
    protected void initViewEvents() {
        super.initViewEvents();


        mMyAdapter = new MyAdapter(this);
        getListView().setAdapter(mMyAdapter);
    }

    @Override
    public void firstLoadingData(String queueTag, LoadingUiType backGroundUI) {

        //dadasdad

        mMyAdapter.clear();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("item：" + i);
        }
        mMyAdapter.addAll(list);

//        addYJLGsonRequest2QueueByTag(getUserInfoRequest(), queueTag, backGroundUI);
//        startQueueRequests(true, queueTag);
    }

    @Override
    public void onRefreshData(String queueTag, LoadingUiType pullUpDownUI) {
        mMyAdapter.clear();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("item：" + i);
        }
        mMyAdapter.addAll(list);


        getPtrLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPtrLayout().finishRefresh();
            }
        }, 2000);
    }

    @Override
    public void getNextPageData(String queueTag, LoadingUiType pullUpDownUI) {

    }

    @Override
    public void onBindData(Object response, Object tag, Object ownerQueuetag, boolean isLoadMore) {
        LogUtils.e("-----onBindData------");
    }

    @Override
    public boolean showDataEmptyUI(Object response) {
        return false;
    }

    public class MyAdapter extends BaseArrayAdapter<String> {

        public MyAdapter(Context context) {
            super(context, null, R.layout.item_test);
        }

        @Override
        public void bindItemData(View view, String data, ViewHolder holder) {
            super.bindItemData(view, data, holder);
            holder.setText(R.id.tv, data);

            List<String> list = new ArrayList<>();
            list.add("http://img.poco.cn/mypoco/myphoto/20071129/21/3898355120071129211344578_007_640.jpg");
            list.add("http://heilongjiang.sinaimg.cn/2015/1026/U10115P1274DT20151026105445.jpg");
            list.add("http://img.clewm.net/richTextCover/2014/01/22/52dfa783110d0.jpg");
            list.add("http://imgsrc.baidu.com/imgad/pic/item/96dda144ad3459825e16a88606f431adcbef8402.jpg");
            list.add("http://heilongjiang.sinaimg.cn/2015/1026/U10115P1274DT20151026105445.jpg");


            int a = holder.getPosition() % 5;

            holder.loadImage(R.id.iv, list.get(a));
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
