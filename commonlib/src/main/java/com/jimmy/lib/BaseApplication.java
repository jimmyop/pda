package com.jimmy.lib;

import android.app.Application;

import com.jimmy.lib.utils.PreferencesUtils;
import com.jimmy.lib.utils.Utils;

import java.io.InputStream;

/**
 * Created by chenjiaming1 on 2017/9/11.
 */

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    /***
     * app 统一请求地址
     */
    public static String APP_URL;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initConfig();

        APP_URL = PreferencesUtils.getString(getApplicationInstance(), "app_url", getResources().getString(R.string.app_url));
        Utils.init(this);
        // refWatcher = LeakCanary.install(this);
    }

    private void initConfig() {
        // config配置文件, 基线没有config文件，只有具体项目有
        int id = getResources().getIdentifier("config", "raw", getPackageName());

        if (id == 0) {
            return;
        }

        InputStream config = getResources().openRawResource(id);
//        Config.init(config);
    }

    public static BaseApplication getApplicationInstance() {
        return mInstance;
    }
}
