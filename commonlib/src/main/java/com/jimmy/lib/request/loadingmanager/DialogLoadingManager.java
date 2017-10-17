package com.jimmy.lib.request.loadingmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jimmy.lib.utils.NetworkUtils;
import com.jimmy.lib.utils.ToastUtils;


/***
 *
 * 加载中：对话框形式
 * 加载失败：网络情况对话框，其他Toast形式。 失败后，网络情况请求依然还会留在队列中，其他情况不会留在队列中
 * 数据为空：对话框形式，没有实现实际逻辑
 * 加载成功：移除所有的加载UI
 *
 * 用途：主要用于提交某个请求到服务器使用的UI
 * @author fuxinrong
 *
 */
public class DialogLoadingManager implements LoadingManager {

    private OnClickListener clickListener;
    protected Context mContext;
    protected Dialog dialog;
    protected OnDismissListener dilogDismissListerner;
    /***
     *  按取消的回调
     */
    private OnClickListener cancleClickListener;
    protected boolean isEmptyData = false;
    /***
     * 当请求失败了，是否需要移除移除请求
     */
    protected boolean needRemoveRequest = true;

    protected boolean isCallDismissListener = true;

    public DialogLoadingManager(Context mContext) {
        this.mContext = mContext;
    }

    public DialogLoadingManager(Context mContext, OnClickListener reloadClickListener) {
        this.mContext = mContext;
        this.clickListener = reloadClickListener;
    }

    public DialogLoadingManager(Context mContext, OnClickListener reloadClickListener, OnDismissListener onDismissListener) {
        this.mContext = mContext;
        this.clickListener = reloadClickListener;
        this.dilogDismissListerner = onDismissListener;
    }

    @Override
    public View showBeginLoaingView(ViewGroup parentView, LayoutParams params) {
        if (dialog != null) {
            dialog.dismiss();
        }
        isEmptyData = false;
//		dialog = ResultDialog.createLoadingDialog(mContext, "正在加载...");
//		dialog.show();

        dialog = new MaterialDialog.Builder(mContext)
                .content("正在加载...")
                .progress(true, 0)
                .progressIndeterminateStyle(false).cancelable(false).build();
        try {
            dialog.show();
        } catch (Exception e) {

            e.printStackTrace();
        }

        needRemoveRequest = true;
        return null;
    }

    @Override
    public View showNetErrorView(ViewGroup parentView, LayoutParams params) {
        if (dialog != null) {
            dialog.dismiss();
        }
        isEmptyData = false;
        if (NetworkUtils.isNetworkConnected(mContext)) {
            needRemoveRequest = true;
            // 有网络，是服务器方面的问题导致连接不上
            ToastUtils.toast(mContext, "服务器忙，请稍候再试");
        } else {
            needRemoveRequest = false;
//			dialog =ResultDialog.createTitleMsgDialog(mContext, "网络提示", "网络不佳，请稍后再试", "重试", "取消", true, clickListener);

            isCallDismissListener = true;

            dialog = new MaterialDialog.Builder(mContext)
                    .title("网络提示")
                    .content("网络不佳，请稍后再试")
                    .positiveText("重试")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            isCallDismissListener = false;
                            if (clickListener != null) {
                                clickListener.onClick(dialog.getActionButton(DialogAction.POSITIVE));
                            }
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (cancleClickListener != null) {
                                cancleClickListener.onClick(dialog.getActionButton(DialogAction.NEGATIVE));
                            }
                        }
                    })
                    .dismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (isCallDismissListener && dilogDismissListerner != null) {
                                dilogDismissListerner.onDismiss(dialogInterface);
                            }
                        }
                    })
                    .build();
            dialog.show();

//			setReloadDataWhenClick(clickListener);
//			dialog.setOnDismissListener(dilogDismissListerner);

//			dialog.findViewById(R.id.tv_cancle).setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//					LogUtils.d("oncllick "+cancleClickListener);
//
//					if (cancleClickListener != null) {
//						cancleClickListener.onClick(v);
//					}
//				}
//			});
        }

        return null;
    }

    @Override
    public View showEmptyDataView(ViewGroup parentView, String msg,
                                  LayoutParams params) {
        if (dialog != null) {
            dialog.dismiss();
        }
        isEmptyData = true;
        needRemoveRequest = true;

        if (TextUtils.isEmpty(msg)) {
            ToastUtils.toast(mContext, "暂无数据，请稍后再试");
        } else {
            ToastUtils.toast(mContext, msg);
        }
        return null;
    }

    @Override
    public View showEmptyDataView(ViewGroup parentView, int res, String msg, LayoutParams params) {
        return showEmptyDataView(parentView, msg, params);
//		return null;
    }

    @Override
    public View showResponseErrorView(ViewGroup parentView, int code, String msg, LayoutParams params) {
        if (dialog != null) {
            dialog.dismiss();
        }
        isEmptyData = false;
        needRemoveRequest = true;
        /*dialog =ResultDialog.createTitleMsgDialog(mContext, "提示", msg, "重试", "取消", true, clickListener);
        setReloadDataWhenClick(clickListener);
		dialog.setOnDismissListener(dilogDismissListerner);
		dialog.show();*/
        // 有网络，是服务器方面的问题导致连接不上
        ToastUtils.toast(mContext, msg);
        return null;
    }

    @Override
    public View showSuccessView(ViewGroup parentView) {
        if (dialog != null && !isEmptyData) {
            dialog.setOnDismissListener(null);
            dialog.dismiss();
        }
        needRemoveRequest = true;
        return null;
    }

    @Override
    public void setReloadDataWhenClick(OnClickListener onBgViewClickListener) {
        if (dialog != null) {
//			dialog.findViewById(R.id.tv_ok).setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					dialog.setOnDismissListener(null);
//					return false;
//				}
//			});
//
//			if(onBgViewClickListener != null){
//				dialog.findViewById(R.id.tv_ok).setOnClickListener(onBgViewClickListener);
//			}
        }
    }

    @Override
    public boolean needRemoveRequest() {
        return needRemoveRequest;
    }

    @Override
    public void clearViews() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public LoadingUiType uiType() {
        return LoadingUiType.DIALOG;
    }

    @Override
    public void setCancleClickListener(OnClickListener cancleClickListener) {
        this.cancleClickListener = cancleClickListener;
    }

}
