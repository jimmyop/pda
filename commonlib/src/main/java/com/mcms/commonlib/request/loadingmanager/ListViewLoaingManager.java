package com.mcms.commonlib.request.loadingmanager;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcms.commonlib.R;
import com.mcms.commonlib.utils.NetworkUtils;
import com.mcms.commonlib.utils.ToastUtils;


/***
 *  上下拉加载更多控件加载时的UI提示
 * 加载中：无提示
 * 加载失败：Toast形式.失败后，请求不会留在队列中
 * 数据为空：小雅做背景
 * 加载成功：移除所有的加载UI
 *  用途：listview上下拉刷新UI提示
 * @author fuxinrong
 *
 */
public class ListViewLoaingManager implements LoadingManager{

	private Context mContext;
	protected LinearLayout emptyDataView;
	/***
	 * 当请求失败了，是否需要移除移除请求
	 */
	private boolean needRemoveRequest = true;
	private TextView emptyDataTextView;
	protected ImageView emptyDataImageView;
	public ListViewLoaingManager(Context mContext) {
		this.mContext = mContext;
		View containerView = LayoutInflater.from(mContext).inflate(R.layout.loading_bg,null);
		emptyDataView = (LinearLayout) containerView.findViewById(R.id.loaing_emptydata_ll);
		emptyDataTextView = (TextView) containerView.findViewById(R.id.loaing_empty_tv);
		emptyDataImageView = (ImageView) containerView.findViewById(R.id.loaing_empty_iv);
	}
	
	@Override
	public View showBeginLoaingView(ViewGroup parentView, LayoutParams params) {
		emptyDataView.setVisibility(View.GONE);
		if (emptyDataView.getParent() != null) {

			ViewGroup viewGroup = (ViewGroup) emptyDataView.getParent();
			viewGroup.removeView(emptyDataView);
		
		}
		return null;
	}

	@Override
	public View showNetErrorView(ViewGroup parentView, LayoutParams params) {
		if(NetworkUtils.isNetworkConnected(mContext)){
			// 有网络，是服务器方面的问题导致连接不上
			ToastUtils.toast(mContext, "服务器忙，请稍候再试");
		} else {
			ToastUtils.toast(mContext, "网络不佳，请稍后再试");
		}
	
		return null;
	}

	@Override
	public View showEmptyDataView(ViewGroup parentView, String msg,
			LayoutParams params) {
		needRemoveRequest = true;
		emptyDataView.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(msg)) {
			emptyDataTextView.setText(msg);
		}
		if (emptyDataView.getParent() != null) {

			ViewGroup viewGroup = (ViewGroup) emptyDataView.getParent();
			viewGroup.removeView(emptyDataView);
		
		}
		parentView.addView(emptyDataView, params);
		return null;
	}

	@Override
	public View showEmptyDataView(ViewGroup parentView, int res, String msg, LayoutParams params) {
		needRemoveRequest = true;
		emptyDataView.setVisibility(View.VISIBLE);

		emptyDataImageView.setImageResource(res);

		if (!TextUtils.isEmpty(msg)) {
			emptyDataTextView.setText(msg);
		}
		if (emptyDataView.getParent() != null) {

			ViewGroup viewGroup = (ViewGroup) emptyDataView.getParent();
			viewGroup.removeView(emptyDataView);

		}
		parentView.addView(emptyDataView, params);
		return null;
	}

	@Override
	public View showResponseErrorView(ViewGroup parentView,int code,String msg, LayoutParams params) {
		needRemoveRequest = true;
		// 有网络，是服务器方面的问题导致连接不上
		ToastUtils.toast(mContext, msg);
		return null;
	}

	@Override
	public View showSuccessView(ViewGroup parentView) {
		needRemoveRequest = true;
		
		return null;
	}
	@Override
	public void setReloadDataWhenClick(OnClickListener onBgViewClickListener) {}
	@Override
	public boolean needRemoveRequest() {
		return needRemoveRequest;
	}
	@Override
	public void clearViews() {
		emptyDataView.setVisibility(View.GONE);
		if (emptyDataView.getParent() != null) {

			ViewGroup viewGroup = (ViewGroup) emptyDataView.getParent();
			viewGroup.removeView(emptyDataView);
		
		}
	}

	@Override
	public LoadingUiType uiType() {
		return LoadingUiType.PULLUPDOWN;
	}

	@Override
	public void setCancleClickListener(OnClickListener cancleClickListener) {
		// TODO Auto-generated method stub
		
	}



}
