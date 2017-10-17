package com.jimmy.lib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by chenjiaming1 on 2017/9/27.
 */

public class ImageLoaderUtil {

    private static volatile ImageLoaderUtil imageLoaderUtil;

    public static ImageLoaderUtil getInstance() {
        if (imageLoaderUtil == null) {
            synchronized (ImageLoaderUtil.class) {
                if (imageLoaderUtil == null) {
                    imageLoaderUtil = new ImageLoaderUtil();
                }
            }
        }
        return imageLoaderUtil;
    }

    public static void loadImage(Context context, ImageView imageView, String uri) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, String uri, RequestOptions m) {
        Glide.with(context)
                .load(uri)
                .apply(m)
                .into(imageView);
    }

}
