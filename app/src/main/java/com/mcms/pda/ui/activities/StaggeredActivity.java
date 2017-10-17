package com.mcms.pda.ui.activities;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jimmy.lib.avtivities.BaseRequestRecyclerViewActivity;
import com.jimmy.lib.request.loadingmanager.LoadingUiType;
import com.jimmy.lib.widgets.TitleHeadLayout;
import com.mcms.pda.R;
import com.mcms.pda.constants.ImageUrl;
import com.mcms.pda.ui.adapter.Item;
import com.mcms.pda.ui.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiaming1 on 2017/9/13.
 */

public class StaggeredActivity extends BaseRequestRecyclerViewActivity {
    private MyAdapter mMutipleAdaper;

    @Override
    protected void initHeaderView(TitleHeadLayout headLayout) {
        super.initHeaderView(headLayout);
        headLayout.setTitleText("Staggered");
        headLayout.setRightText("test");
        headLayout.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StaggeredActivity.this, TestPullActivity.class);
            }
        });
    }

    @Override
    public void firstLoadingData(String queueTag, LoadingUiType backGroundUI) {


        // 错列网格布局
        getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));

        List<Item> Datas = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            int a = i % 2;
            if (i % 2 == 0) {
                Datas.add(new Item(R.mipmap.ic_launcher_round, "我 get 新技能 " + i, 0, ImageUrl.images[a]));//根据 Item 类 最后一个参数确定填充数据的不同
            } else {
                Datas.add(new Item(R.mipmap.ic_launcher_round, "你 get 新技能 " + i, 1, ImageUrl.images[a]));
            }
        }
        mMutipleAdaper = new MyAdapter(this, Datas);
        getRecyclerView().setAdapter(mMutipleAdaper);

//        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, getRecyclerView(), false);
//        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_footer, getRecyclerView(), false);
//        getRecyclerView().addHeaderView(headerView);
//        getRecyclerView().addFooterView(footerView);

        setLoadOver(false);
    }

    @Override
    public void onRefreshData(String queueTag, LoadingUiType pullUpDownUI) {
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

    }

    @Override
    public boolean showDataEmptyUI(Object response) {
        return false;
    }
}
