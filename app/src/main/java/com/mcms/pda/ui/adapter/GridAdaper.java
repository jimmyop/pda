package com.mcms.pda.ui.adapter;

import android.content.Context;

import com.jimmy.lib.adapter.BaseRecyclerAdapter;
import com.jimmy.lib.adapter.BaseRecyclerViewHolder;
import com.mcms.pda.R;

import java.util.List;

/**
 * Created by chenjiaming1 on 2017/9/27.
 */

public class GridAdaper extends BaseRecyclerAdapter<Item> {

    public GridAdaper(Context context, List<Item> datas) {
        super(context, R.layout.grid_item, datas);
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, final Item item, int position) {
        holder.setText(R.id.name, item.getTv1())
                .setImagePath(mContext, R.id.picture, item.getUrl());
    }
}