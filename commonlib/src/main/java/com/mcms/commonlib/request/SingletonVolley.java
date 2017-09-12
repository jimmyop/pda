package com.mcms.commonlib.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mcms.commonlib.BaseApplication;

/**
 * Volley请求队列
 */
public class SingletonVolley {
    private static SingletonVolley mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private SingletonVolley() {
        mContext = BaseApplication.getApplicationInstance();
        mRequestQueue = getRequestQueue();
    }

    public static SingletonVolley getInstance() {
        if (mInstance == null) {
            synchronized (SingletonVolley.class) {
                if (mInstance == null)
                    mInstance = new SingletonVolley();
            }
        }
        return mInstance;
    }

    /**
     * 获取RequestQueue
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 将Request加入RequestQueue
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
