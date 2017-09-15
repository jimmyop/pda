package com.mcms.commonlib.request;

import android.text.TextUtils;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mcms.commonlib.BaseApplication;
import com.mcms.commonlib.utils.LogUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chenjiaming1 on 2017/9/12.
 */

public class BuildUrl {

    int mehtod;
    String action;
    HashMap params;

    public BuildUrl(int mehtod, String action, HashMap params) {
        this.mehtod = mehtod;
        this.action = action;
        this.params = params;
    }


    public String buileUrl() {

        StringBuffer paramBuffer = new StringBuffer();
        paramBuffer.append("method=");
        paramBuffer.append(action);

        String queryJson = null;

        Gson gson = new Gson();
        if (params != null) {
            queryJson = gson.toJson(params);
            LogUtils.d("params " + queryJson);

            Iterator iter = params.entrySet().iterator();
            StringBuffer paramBuffer2 = new StringBuffer();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                paramBuffer2.append("&" + key);
                paramBuffer2.append("=");
                paramBuffer2.append(value);
            }

            queryJson = paramBuffer2.toString();
        }

        String signUrl = paramBuffer.toString();

//        if (!TextUtils.isEmpty(queryJson)) {
//            // 签名的数据是原始数据，不能对参数进行UrlEncode之后再签名；POST参数以同样的方式参与签名。
//            try {
//                queryJson = URLEncoder.encode(queryJson, "UTF-8");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            signUrl = signUrl + "&params=" + queryJson;
//        }
        if (!TextUtils.isEmpty(queryJson)) {
//            paramBuffer.append("&params=");
            paramBuffer.append(queryJson);
        }

        //TODO 签名
//        String secretKey = "";//秘钥
//        AppSignatureMd5 signUtil = new AppSignatureMd5(secretKey, signUrl);
//        String md5Sign = signUtil.sign();
//        paramBuffer.append("&sign=");
//        paramBuffer.append(md5Sign);

        String encodeUrl = paramBuffer.toString();

        String url = BaseApplication.APP_URL;
        url = url + "?";
        if (mehtod == Request.Method.GET) {
            encodeUrl = url + paramBuffer.toString();
        }

        return encodeUrl;
    }
}
