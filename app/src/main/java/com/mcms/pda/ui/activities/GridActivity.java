package com.mcms.pda.ui.activities;

import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.jimmy.lib.avtivities.BaseRequestRecyclerViewActivity;
import com.jimmy.lib.request.loadingmanager.LoadingUiType;
import com.jimmy.lib.widgets.TitleHeadLayout;
import com.mcms.pda.R;
import com.mcms.pda.constants.ImageUrl;
import com.mcms.pda.ui.adapter.GridAdaper;
import com.mcms.pda.ui.adapter.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiaming1 on 2017/9/13.
 */

public class GridActivity extends BaseRequestRecyclerViewActivity {

    private GridAdaper mGridAdaper;

    @Override
    protected void initHeaderView(TitleHeadLayout headLayout) {
        super.initHeaderView(headLayout);
        headLayout.setTitleText("Grid");
        headLayout.setRightText("staggered");
        headLayout.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GridActivity.this, StaggeredActivity.class);
            }
        });
    }

    @Override
    public void firstLoadingData(String queueTag, LoadingUiType backGroundUI) {

//        getRecyclerView().addItemDecoration(new DividerGridItemDecoration(this));
        getRecyclerView().setLayoutManager(new GridLayoutManager(this, 3));

        List<Item> Datas = new ArrayList<>();
        for (int i = 0; i < ImageUrl.images.length; i++) {

            int a = i % ImageUrl.images.length;

            if (i % 2 == 0) {
                Datas.add(new Item(R.mipmap.ic_launcher_round, "我 get 新技能 " + i, 0, ImageUrl.images[a]));//根据 Item 类 最后一个参数确定填充数据的不同
            } else {
                Datas.add(new Item(R.mipmap.ic_launcher_round, "你 get 新技能 " + i, 1, ImageUrl.images[a]));
            }
        }
        mGridAdaper = new GridAdaper(this, Datas);
        getRecyclerView().setAdapter(mGridAdaper);

        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, getRecyclerView(), false);
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_footer, getRecyclerView(), false);
        getRecyclerView().addHeaderView(headerView);
        getRecyclerView().addFooterView(footerView);

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
