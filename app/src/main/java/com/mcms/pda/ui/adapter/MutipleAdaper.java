package com.mcms.pda.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.mcms.commonlib.adapter.BaseRecyclerAdapter;
import com.mcms.commonlib.adapter.BaseRecyclerViewHolder;
import com.mcms.pda.R;

import java.util.List;

/**
 * Created by chenjiaming1 on 2017/9/27.
 */

public class MutipleAdaper extends BaseRecyclerAdapter<Item> {

    public MutipleAdaper(Context context, List<Item> datas) {
        super(context, datas, new MutipleTypeSupport<Item>() {
            @Override
            public int getLayoutId(Item item) {
                if (item.getType() == 1){//该处1是通过 item 传过来的
                    return R.layout.list_item;
                }else {
                    return R.layout.list_item1;
                }
            }
        });
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, final Item item, int position) {
        holder.setText(R.id.tv1,item.getTv1())
                .setImageResource(R.id.img,item.getRes())
                .setOnClickListener(R.id.tv1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,item.getTv1(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}