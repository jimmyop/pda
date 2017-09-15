package com.mcms.pda.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mcms.commonlib.adapter.BaseArrayAdapter;
import com.mcms.commonlib.avtivities.BaseRequestActivity;
import com.mcms.commonlib.utils.LogUtils;
import com.mcms.commonlib.widgets.TitleHeadLayout;
import com.mcms.pda.R;
import com.mcms.pda.widget.TestLayout;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUICenterGravityRefreshOffsetCalculator;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chenjiaming1 on 2017/9/13.
 */

public class TestActivity extends BaseRequestActivity {
    @BindView(R.id.meituan_ptr_layout)
    TestLayout mQMUIPullRefreshLayout;

    @BindView(R.id.list_view)
    ListView mListView;

    MyAdapter mMyAdapter;


    @Override
    protected void initHeaderView(TitleHeadLayout headLayout) {
        super.initHeaderView(headLayout);
        headLayout.setTitleText("AAAA");
        headLayout.setRightText("test");
        headLayout.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TestActivity.this, TestPullActivity.class);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViewEvents() {
        mMyAdapter = new MyAdapter(this);
        mListView.setAdapter(mMyAdapter);

        mQMUIPullRefreshLayout.setRefreshOffsetCalculator(new QMUICenterGravityRefreshOffsetCalculator());

        mQMUIPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                mQMUIPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mQMUIPullRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mMyAdapter.clear();
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            list.add("item：" + i);
        }
        mMyAdapter.addAll(list);
        LogUtils.e("-----onRefreshData------");
    }

    public class MyAdapter extends BaseArrayAdapter<String> {

        public MyAdapter(Context context) {
            super(context, null, R.layout.item_test);
        }

        @Override
        public void bindItemData(View view, String data, ViewHolder holder) {
            super.bindItemData(view, data, holder);
            holder.setText(R.id.tv, data);
        }
    }
}
