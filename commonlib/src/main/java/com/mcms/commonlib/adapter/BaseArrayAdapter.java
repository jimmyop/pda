package com.mcms.commonlib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcms.commonlib.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author zengxiangbin
 *
 * @param <T>
 */
public class BaseArrayAdapter<T> extends BaseAdapter {

    public interface AdapterItemBinder<T>{
        void onBindItemData(View view, T data, int position, BaseArrayAdapter<T> adapter);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private int mResource;

    private List<T> mData;

    private final Object mLock = new Object();

    private boolean mNotifyOnChange = true;

    public BaseArrayAdapter(Context context) {
        this(context, null);
    }

    public BaseArrayAdapter(Context context, List<T> data){
        this(context, data, 0);
    }

    public BaseArrayAdapter(Context context, List<T> data, int resource) {
        this(context, data, resource, null, null);
    }

    public BaseArrayAdapter(Context context, List<T> data, int resource, ListView listView, AbsListView.OnScrollListener l) {
        mContext = context;
        mData = data == null ? new ArrayList<T>():data;
        mResource = resource;
        mInflater = LayoutInflater.from(context);
        if (listView != null && l != null){
//            listView.setOnScrollListener(
//                    new PauseOnScrollListener(YjlImageLoader.getInstance(), true, true, l)
//            );
        }
    }

    public void change(@NonNull List<T> data){
        if (data == null){
            data = new ArrayList<T>();
        }
        mData = new ArrayList<T>(data);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    public void replaceAll(@NonNull List<T> data){
    	if(data == null){
    		data = new ArrayList<T>();
    	}
    	mData = new ArrayList<T>(data);
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
       
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        int size = mData.size();
        if (0 <= position && position < size){
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData(){
        return new ArrayList<T>(mData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = newView(mInflater, position, parent);
            convertView.setTag(R.id.tag_view_holder, new ViewHolder(parent, convertView).setPosition(position));
        }
        if (convertView instanceof AdapterItemBinder){
            AdapterItemBinder<T> itemBinder = (AdapterItemBinder<T>) convertView;
            itemBinder.onBindItemData(convertView, getItem(position), position, this);
            afterAutoBindData(convertView, position);
        }else {
            ViewHolder holder = (ViewHolder)convertView.getTag(R.id.tag_view_holder);
            if (holder != null) {
                holder.setPosition(position);
            }
            bindItemData(convertView, getItem(position), holder);
        }
        return convertView;
    }
    
    protected void afterAutoBindData(View convertView, int position){
    	
    }

    /**
     * new a item view
     * @param inflater LayoutInflater
     * @param position current position
     * @param parent parent layout
     * @return item view
     */
    protected View newView(LayoutInflater inflater, int position, ViewGroup parent){
        if (mResource > 0){
            return inflater.inflate(mResource, parent, false);
        }
        throw new IllegalArgumentException("cannot new item view");
    }

    /**
     * bing view with data
     * @param view item view
     * @param data item data
     * @param holder item child view holder
     */
    public void bindItemData(View view, T data, ViewHolder holder) {
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            textView.setText(data.toString());
        }
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(@NonNull T object) {
        synchronized (mLock) {
            mData.add(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(@NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            mData.addAll(collection);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(@NonNull T ... items) {
        synchronized (mLock) {
            Collections.addAll(mData, items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(@NonNull T object, int index) {
        synchronized (mLock) {
            mData.add(index, object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(@NonNull T object) {
        synchronized (mLock) {
            mData.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    
    public void remove(int position){
    	synchronized (mLock) {
            mData.remove(position);
        }
    	if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
           mData.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(@NonNull Comparator<? super T> comparator) {
        synchronized (mLock) {
           Collections.sort(mData, comparator);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public static class ViewHolder {
        private SparseArray<View> views = new SparseArray<View>();
        public final ViewGroup parent;
        public final View itemView;
        private int position;

        public ViewHolder(@NonNull ViewGroup parent, @NonNull View convertView) {
            this.parent = parent;
            this.itemView = convertView;
        }

        @SuppressWarnings("unchecked")
        public <E extends View> E getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = itemView.findViewById(resId);
                views.put(resId, v);
            }
            return (E) v;
        }

        public int getPosition() {
            return position;
        }

        public ViewHolder setPosition(int position) {
            this.position = position;
            return this;
        }
        
        public void setText(int id, CharSequence text){
        	TextView textView = getView(id);
        	textView.setText(text);
        }
        
        public void setBackgroundResource(int id, int resid){
        	View view = getView(id);
        	view.setBackgroundResource(resid);
        }

        public void setImageResource(int id, int resid){
            ImageView imageView = getView(id);
            imageView.setImageResource(resid);
        }
        
        public void setOnClickListener(int id, OnClickListener l){
        	getView(id).setOnClickListener(l);
        }
        
//        public void loadImage(int id, String uri, DisplayImageOptions options){
//        	ImageView imageView = getView(id);
//        	YjlImageLoader.getInstance().displayImage(uri, imageView, options);
//        }
        
        public void setVisibility(int id, int visibility){
        	View view = getView(id);
        	view.setVisibility(visibility);
        }
        
        public void setVisibility(int id, boolean isVisibility){
        	View view = getView(id);
        	view.setVisibility(isVisibility?View.VISIBLE:View.GONE);
        }
    }

    public Context getContext() {
        return mContext;
    }
}
