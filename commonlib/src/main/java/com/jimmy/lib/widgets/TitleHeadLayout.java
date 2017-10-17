package com.jimmy.lib.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jimmy.lib.R;
import com.jimmy.lib.utils.DisplayUtil;

import butterknife.ButterKnife;


/**
 * Created by chenjiaming1 on 2017/7/19.
 */

public class TitleHeadLayout extends RelativeLayout implements View.OnClickListener {
    private View viewParent;


    private LinearLayout rootMain;
    private RelativeLayout root;
    /**
     * 左边文字
     */
    private TextView tvLeft;
    /**
     * title
     */
    private TextView title;
    /**
     * 右边文字
     */
    private TextView tvRight;
    /**
     * 右边文字
     */
    private TextView tvRightSecondary;

    private TextView tvMsgNumber;

    private boolean statusInit = true;

    private Context mContext;
    OnTitleHeadItemClickListener mTitleClickListener;
    OnTitleHeadCenterItemClickListener mCenterTitleClickListerner;
    private boolean isReturnHome = false;
    int statusBarHeight = -1;

    public interface OnTitleHeadItemClickListener {

        public void onBackClickListener();

        public void onRightClickListener();

    }

    /***
     *  点击标题的回调接口
     * @author fuxinrong
     *
     */
    public interface OnTitleHeadCenterItemClickListener {

        public void onTileClickListener();


    }

    public TitleHeadLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TitleHeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void init() {
        viewParent = LayoutInflater.from(mContext).inflate(R.layout.title_head_layout, this, true);

        rootMain = ButterKnife.findById(viewParent, R.id.root_main);
        root = ButterKnife.findById(viewParent, R.id.root);
        title = ButterKnife.findById(viewParent, R.id.tv_title);
        tvLeft = ButterKnife.findById(viewParent, R.id.tv_left);
        tvRight = ButterKnife.findById(viewParent, R.id.tv_right);
        tvRightSecondary = ButterKnife.findById(viewParent, R.id.tv_right_secondary);
        tvMsgNumber = ButterKnife.findById(viewParent, R.id.tv_msg_number);

        ButterKnife.bind(this, viewParent);
//        rootMain = (LinearLayout) viewParent.findViewById(R.id.root_main);
//        root = (RelativeLayout) viewParent.findViewById(R.id.root);
//        title = (TextView) viewParent.findViewById(R.id.tv_title);
//        tvLeft = (TextView) viewParent.findViewById(R.id.tv_left);
//        tvRight = (TextView) viewParent.findViewById(R.id.tv_right);
//        tvRightSecondary = (TextView) viewParent.findViewById(R.id.tv_right_secondary);
//        tvMsgNumber = (TextView) viewParent.findViewById(R.id.tv_msg_number);
//        statusBar = viewParent.findViewById(R.id.status_bar);


        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
//        statusBar.setLayoutParams(lp);
        rootMain.setPadding(0, statusBarHeight, 0, 0);
        title.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRightSecondary.setOnClickListener(this);
        root.setOnClickListener(this);
    }

    /**
     * 设置右上角未读信息个数
     *
     * @param count
     */
    public void setMsgNumber(int count) {
        if (tvMsgNumber != null) {
            if (count > 0) {
                tvMsgNumber.setText(String.valueOf(count));
                setMsgNumberVisible(true);
            } else {
                setMsgNumberVisible(false);
            }
        }
    }

    public void setMsgNumberVisible(boolean flag) {
        if (tvMsgNumber != null) {
            if (flag) {
                tvMsgNumber.setVisibility(View.VISIBLE);
            } else {
                tvMsgNumber.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置返回操作是否需要直接跳转到home页
     *
     * @param isReturnHome
     */
    public void setIsReturnHome(boolean isReturnHome) {
        this.isReturnHome = isReturnHome;
    }

    /***
     * 设置左边箭头图片
     * */
    public void setLeftImageResource(int resId) {
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        tvLeft.setText("");
    }

    /***
     * 设置左边文字
     * */
    public void setLeftText(int resId) {
        tvLeft.setText(getContext().getString(resId));
    }

    /***
     * 设置左边文字
     * */
    public void setLeftText(String str) {
        tvLeft.setText(str);
    }

    /***
     * 设置左边文字颜色
     * */
    public void setLeftTextColor(int color) {
        tvLeft.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /***
     * 设置右边箭头图片
     * */
    public void setRightImageResource(int resId) {
        tvRight.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        tvRight.setText("");
    }

    /***
     * 设置右边文字
     * */
    public void setRightText(int resId) {
        tvRight.setText(getContext().getString(resId));
    }

    /***
     * 设置右边文字
     * */
    public void setRightText(String str) {
        tvRight.setText(str);
    }

    /***
     * 设置右边文字颜色
     * */
    public void setRightTextColor(int color) {
        tvRight.setTextColor(ContextCompat.getColor(getContext(), color));
    }


    /**
     * 隐藏标题栏底部横线
     */
    public void hideTitleLine() {
        if (null != root) {
            root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    /**
     * 显示标题栏底部横线
     */
    public void showTitleLine() {
        if (null != root) {
            root.setBackgroundResource(R.drawable.shape_line_bottom);
        }
    }

    public void showTitle(boolean flag) {
        if (flag) {
            root.setVisibility(VISIBLE);
        } else {
            root.setVisibility(GONE);
        }
    }

    /***
     * 获取右边按钮
     * */
    public TextView getRightBtn() {
        return tvRight;
    }

    /***
     * 获取右边按钮
     * */
    public TextView getLeftBtn() {
        return tvLeft;
    }

    /***
     * 获取右边按钮
     * */
    public TextView getTitle() {
        return title;
    }


    public void setRightClickListener(OnClickListener listener) {
        if (tvRight != null) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setOnClickListener(listener);
        }
    }

    public void setLeftClickListener(OnClickListener listener) {
        if (tvLeft != null && tvLeft.getVisibility() == View.VISIBLE) {
            tvLeft.setOnClickListener(listener);
        }
    }

    public void hideTextRight() {
        tvRight.setVisibility(View.INVISIBLE);
    }

    public void showTextRight() {
        tvRight.setVisibility(View.VISIBLE);
    }

    public void hideTextLeft() {
        tvLeft.setVisibility(View.INVISIBLE);
    }

    public void showTextLeft() {
        tvLeft.setVisibility(View.VISIBLE);
    }


    /**
     * 设置title字体的颜色
     */
    public void setTitleTextColor(int res) {
        if (title != null) {
            title.setTextColor(ContextCompat.getColor(getContext(), res));
        }
    }


    public TextView getmTitle() {
        return title;
    }

    public void setTitleRightImageRes(int res) {
        if (title != null) {
            Drawable nav_up = getResources().getDrawable(res);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            title.setCompoundDrawables(null, null, nav_up, null);
        }
    }

    /**
     * 设置title的文字
     */
    public void setTitleText(int id) {
        if (title != null) {
            title.setText(id);
        }

    }

    /**
     * 设置title的文字
     */
    public void setTitleText(String text) {

        if (title != null) {
            title.setText(text);
        }
    }

    /**
     * 设置Title head的背景
     **/
    public void setHeadBackgroundResource(int resid) {
        if (root != null) {
            root.setBackgroundResource(resid);
        }
    }

    /**
     * 设置Title head的背景
     **/
    public void setHeadBackgroundColor(int color) {
        if (root != null) {
            root.setBackgroundColor(color);
        }
    }

    /**
     * 设置Title head的背景
     **/
    public void setMainBackgroundColor(int color) {
        if (rootMain != null) {
            rootMain.setBackgroundColor(color);
        }
    }

    public void showStatusBar(boolean flag) {
        if (flag) {
            statusInit = flag;
            rootMain.setPadding(0, statusBarHeight, 0, 0);

//            statusBar.setVisibility(VISIBLE);
        } else {
            statusInit = flag;
//            statusBar.setVisibility(GONE);
            rootMain.setPadding(0, 0, 0, 0);
        }
    }


    /***
     * Specify an alpha value for the drawable. 0 means fully transparent, and 255 means fully opaque
     * @param alpha
     */
    public void setTitleLayoutAlpha(int alpha) {
        rootMain.getBackground().setAlpha(alpha);
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.tv_left) {
            if (mTitleClickListener != null) {
                mTitleClickListener.onBackClickListener();
            } else if (mContext instanceof Activity) {
                gotoBack();
            }

        } else if (i == R.id.tv_right) {
            if (mTitleClickListener != null) {
                mTitleClickListener.onRightClickListener();
            }

        } else if (i == R.id.tv_title) {
            if (mCenterTitleClickListerner != null) {
                mCenterTitleClickListerner.onTileClickListener();
            }

        } else if (i == R.id.root) {//什么都不用做，为了处理穿透点击

        } else {
        }
    }

    public void setCenterTitleClickListerner(
            OnTitleHeadCenterItemClickListener mCenterTitleClickListerner) {
        this.mCenterTitleClickListerner = mCenterTitleClickListerner;
    }


    /**
     * 设置head中项的事件
     */
    public void setOnTitleHeadItemClickListener(OnTitleHeadItemClickListener listener) {
        this.mTitleClickListener = listener;
    }

    public int getTitleLayoutHeight() {
        int height = 0;

        if (statusInit) {
            height = height + statusBarHeight;
        }

        if (root.getVisibility() == VISIBLE) {
            height = height + DisplayUtil.dip2px(50);
        }

        return height;
    }

    /**
     * 统一的返回键处理
     */
    public void gotoBack() {
        ((Activity) mContext).finish();
    }

}