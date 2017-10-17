package com.jimmy.lib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DisplayUtil {

    public static int mScreenWidth;
    public static int mScreenHeight;


    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, double d) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (d * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(double d) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (d * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spVal   需转换的sp值
     * @return 转后的px值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽
     *
     * @return
     */
    public static int getScreenWidth() {
        if (mScreenWidth != 0) {
            return mScreenWidth;
        }

        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return mScreenWidth;
    }

    /**
     * 获取屏幕搞
     *
     * @return
     */
    public static int getScreenHeight() {
        if (mScreenHeight != 0) {
            return mScreenHeight;
        }
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        return mScreenHeight;
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = Resources.getSystem().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取手机的分辨率 格式为1280x720
     *
     * @return
     */
    public static String getPhoneResolution(Context context) {
        String ret = getScreenWidth() + "x" + getScreenHeight();
        return ret;
    }
}
