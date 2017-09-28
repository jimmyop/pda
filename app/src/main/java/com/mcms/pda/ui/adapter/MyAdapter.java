package com.mcms.pda.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mcms.commonlib.adapter.BaseRecyclerAdapter;
import com.mcms.commonlib.adapter.BaseRecyclerViewHolder;
import com.mcms.pda.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chenjiaming1 on 2017/9/28.
 */

public class MyAdapter extends BaseRecyclerAdapter<Item> {
    private List<Integer> mHeights;
    private Integer[] colors = {R.color.bg_blue, R.color.bg_secondary, R.color.text_warning, R.color.btn_red_normal, R.color.text_yellow};

    public MyAdapter(Context context, List<Item> datas) {
        super(context, R.layout.list_item, datas);
        mHeights = new ArrayList<>();
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, final Item item, int position) {

        // 随机高度, 模拟瀑布效果.
        if (mHeights.size() <= position) {
            mHeights.add((int) (100 + Math.random() * 300));
        }

        ViewGroup.LayoutParams lp = holder.getView(R.id.llt).getLayoutParams();
        lp.height = mHeights.get(position);

        holder.getView(R.id.llt).setLayoutParams(lp);


        int b = position % 5;
        int c = colors[b];
        holder.setText(R.id.tv1, item.getTv1())
                .setImageResource(R.id.img, item.getRes())
                .setBackgroundResourse(R.id.llt, c)
                .setOnClickListener(R.id.tv1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, item.getTv1(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
