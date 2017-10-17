package com.jimmy.lib.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimmy.lib.utils.ImageLoaderUtil;


/**
 * Created by chenjiaming1 on 2017/9/27.
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }

    /**
     * 从ItemView获取View
     *
     * @param id  ItemView里包含的ViewId
     * @param <V> 返回View
     * @return
     */
    public <V extends View> V getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (V) view;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseRecyclerViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置ImageView的值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public BaseRecyclerViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 设置ImageView的值
     * 第三方  ImageLoder Glide Picasso
     * 不能直接写死第三方图片加载
     * 使用自己的一套规范  ImageLoder
     *
     * @param viewId
     * @return
     */
    public BaseRecyclerViewHolder setImagePath(int viewId, ImageLoder imageLoder) {
        ImageView view = getView(viewId);
        imageLoder.loadImage(view, imageLoder.getPath());
        return this;
    }

    public BaseRecyclerViewHolder setImagePath(Context context, int viewId, String url) {
        ImageView view = getView(viewId);
        ImageLoaderUtil.loadImage(context, view, url);
        return this;
    }


    //图片加载 （对第三方库加载图片等封装）
    public abstract static class ImageLoder {
        private String path;

        public ImageLoder(String path) {
            this.path = path;
        }

        //需要复写该方法加载图片
        public abstract void loadImage(ImageView imageView, String path);

        public String getPath() {
            return path;
        }
    }

    /**
     * 设置是否可见
     *
     * @param viewId
     * @param visible
     * @return
     */
    public BaseRecyclerViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置tag
     *
     * @param viewId
     * @param tag
     * @return
     */
    public BaseRecyclerViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseRecyclerViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BaseRecyclerViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseRecyclerViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View view = getView(viewId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        return this;
    }


    public BaseRecyclerViewHolder setBackgroundResourse(int viewId, int res) {
        View view = getView(viewId);
        view.setBackgroundResource(res);

        return this;
    }

    /**
     * 设置Checkable
     *
     * @param viewId
     * @param checked
     * @return
     */
    public BaseRecyclerViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    //点击事件
    public BaseRecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    //触摸事件
    public BaseRecyclerViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    //长按事件
    public BaseRecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
