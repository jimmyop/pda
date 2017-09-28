package com.mcms.commonlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chenjiaming1 on 2017/9/19.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected int mLayoutId;//布局id
    protected List<T> mDatas;//数据源
    protected Context mContext;//上下文
    private LayoutInflater mInflater;

    public BaseRecyclerAdapter(Context context, List<T> datas, MutipleTypeSupport typeSupport) {
        this(context, -1, datas);
        this.mMutipleTypeSupport = typeSupport;
    }

    public BaseRecyclerAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    //调用 onCreateViewHolder() 方法之前调用 getItemViewType()
    @Override
    public int getItemViewType(int position) {
        if (mMutipleTypeSupport != null) {
            return mMutipleTypeSupport.getLayoutId(mDatas.get(position));
        }
        return super.getItemViewType(position);
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //判断是否需要多布局
        if (mMutipleTypeSupport != null) {
            mLayoutId = viewType;
        }

        View itemView = mInflater.inflate(mLayoutId, parent, false);
        return new BaseRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        bindData(holder, mDatas.get(position), position);
    }

    /**
     * 把必要参数传进去，让每个 Adapter 去设置具体值
     *
     * @param holder   RecyclerViewHolder
     * @param t        数据
     * @param position 当前位置
     */
    protected abstract void bindData(BaseRecyclerViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface MutipleTypeSupport<T> {
        //根据当前条目获取布局
        int getLayoutId(T t);
    }

    private MutipleTypeSupport<T> mMutipleTypeSupport;
}
