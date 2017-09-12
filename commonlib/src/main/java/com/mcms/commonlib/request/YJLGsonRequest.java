package com.mcms.commonlib.request;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mcms.commonlib.BaseApplication;
import com.mcms.commonlib.cache.DataCache;
import com.mcms.commonlib.request.data.BaseReslutRes;
import com.mcms.commonlib.utils.LogUtils;
import com.mcms.commonlib.utils.M9Secure;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 *
 * @param <T> for response bean
 * @author jimmy
 */
public class YJLGsonRequest<T> extends Request<T> {
    private static String TAG = "YJLGsonRequest";

    private static final String UTF_8 = "UTF-8";

    private Gson mGson;

    private Class<T> mClass;

    private ResponseCallbackListener<T> mResponseCallbackListener;

    private Listener<T> mListener;

    /**
     * Post请求内容的字符串
     */
    private String mStrBody;
    /**
     * Post请求内容是否需要M9加密
     */
    private boolean mNeedEncrypt;

    /**
     * 是否缓存泛型T的序列化对象到文件
     */
    private boolean mNeedDataCache = false;

    /**
     * 缓存泛型T的序列化对象到文件的KEY
     */
    private String mDataCacheKey;

    private SingletonVolley mSingletonVolley;

    /***
     * 30S
     */
    private final int DEFAULT_TIMEOUT = 30 * 1000;
    /**
     * 从得到的json的哪一层key开始解析（只取key对应的value值进行解析）
     * 例子：data：{json} 只对{json}进行转化成T
     * 为null则全部json数据都进行转化为T
     * 默认从data开始
     * 多层用“.”连接 "data.infos"
     */
    private String parserKey = "data";

    private String methodUrl;
    private HashMap paramObject;

    /***
     * 属于哪个请求组的标识
     */
    private Object owerQueueTag;


    /***
     * 更新请求地址的请求参数
     *
     * @param paramObject
     */
    private void setParamObject(HashMap paramObject) {
        this.paramObject = paramObject;
        this.updateUrl();
    }

    /**
     * 用来替代Response.Listener和Response.ErrorListener接口
     *
     * @param <T>
     */
    public interface ResponseCallbackListener<T> {
        /***
         * 请求成功的回调
         *
         * @param response
         * @param cache        是否缓存response到文件
         * @param tag          标识此次请求的tag,是method方法
         * @param owerQueueTag 标识该请求属于哪个组
         */
        void onResponseSuccess(T response, boolean cache, Object tag, Object owerQueueTag);

        /***
         * 请求得到的数据非期望得到的数据 code！=0
         *
         * @param code         错误码
         * @param msg_cn       错误提示
         * @param msg_en
         * @param tag          标识此次请求的tag 是method方法
         * @param owerQueueTag 标识该请求属于哪个组
         */
        void onResponseCodeFailure(int code, String msg_cn, String msg_en, Object tag, Object owerQueueTag);

        /**
         * 请求失败的回调,网络超时，服务器500等类型
         * 非code ！=0 的错误
         *
         * @param error
         * @param tag          标识此次请求的tag 是method方法
         * @param owerQueueTag 标识该请求属于哪个组
         */
        void onResponseFailure(VolleyError error, Object tag, Object owerQueueTag);
    }

    /***
     * (默认会自动添加小区id)
     *
     * @param methodUrl               method 方法地址
     * @param requestParam            请求的业务参数对象 nullable
     * @param responseClazz           期望得到的响应对象  parserKey: {json} 这一层对应的对象 (parserKey默认为data，多层用“.”连接 "data.infos"可设置)
     * @param requestCallbackListener
     */

    public YJLGsonRequest(String methodUrl, HashMap requestParam,
                          Class<T> responseClazz,
                          ResponseCallbackListener<T> requestCallbackListener) {
        // url 请求先置为空,在发送请求的时候再进行url的赋值，保证请求的时间戳不过期
        // 默认是post方式
        super(Method.POST, null);

        mSingletonVolley = SingletonVolley.getInstance();
        setmResponseCallbackListener(requestCallbackListener);
        mGson = new Gson();
        mClass = responseClazz;
        this.methodUrl = methodUrl;
        this.paramObject = requestParam;
        setTag(getRequestTag());
        this.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 1.0f));
        //this.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    /***
     * 设置为post或者get，在sendRequest()之前生效
     *
     * @param method
     */
    public void setMethod(int method) {
        super.setmMethod(method);
    }

    /***
     * 设置请求为post方式
     */
    public void setRequestToPost() {
        setMethod(Method.POST);
    }

    /***
     * 设置仅请求一次，设置超时时间，单位毫秒
     * initialTimeoutMs The initial timeout for the policy.
     * maxNumRetries The maximum number of retries.
     * backoffMultiplier Backoff multiplier for the policy.
     */
    public void setOneShotPolicy(int initialTimeoutMs) {
        this.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs, 0, 1.0f));
    }

    /***
     * 设置仅请求一次，设置超时时间为默认设置25s
     * initialTimeoutMs The initial timeout for the policy.
     * maxNumRetries The maximum number of retries.
     * backoffMultiplier Backoff multiplier for the policy.
     */
    public void setOneShotPolicyByDefaultTimeOut() {
        this.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT, 0, 1.0f));
    }

    public ResponseCallbackListener<T> getmResponseCallbackListener() {
        return mResponseCallbackListener;
    }

    public void setmResponseCallbackListener(ResponseCallbackListener<T> mResponseCallbackListener) {
        this.mResponseCallbackListener = mResponseCallbackListener;
        mListener = getResponseListener();
        setErrorListener(getResponseErrorListener());
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogUtils.d(TAG, " json from server " + jsonString);

            BaseReslutRes commResponse = mGson.fromJson(jsonString, BaseReslutRes.class);

            if (0 != commResponse.getCode()) { // 不是正常响应的code,回调onResponseCodeFailure
                return Response.error(new ResponseCodeVellyError(commResponse));
            }

            String parserValue = jsonString;
            if (!TextUtils.isEmpty(parserKey)) {
                if (parserKey.contains(".")) {
                    String[] keys = parserKey.split("\\.");
                    for (int i = 0; i < keys.length; i++) {
                        JSONObject jsonObject = new JSONObject(parserValue);
                        parserValue = jsonObject.getString(keys[i]);
                    }
                } else {
                    JSONObject jsonObject = new JSONObject(parserValue);
                    parserValue = jsonObject.getString(parserKey);
                }
            }
            LogUtils.d(TAG, " parserJson " + parserValue);

            return Response.success(mGson.fromJson(parserValue, mClass), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * post的body内容  volley 会进行调用，参看jsonRequest
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mNeedEncrypt == true) {
            byte[] content = {};
            if (mStrBody != null) {
                try {
                    content = mStrBody.getBytes(UTF_8);
                    byte[] steam = M9Secure.m9Encode(content);
                    return steam;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (mStrBody != null) {
                try {
                    byte[] content = mStrBody.getBytes(UTF_8);
                    return content;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 设置Response对象缓存文件的key，注意不要有“/”字符
     *
     * @return
     */
    public void setDataCacheKey(String key) {
        mDataCacheKey = key;
    }

    /**
     * 返回Response对象缓存文件的key
     *
     * @return
     */
    public String getDataCacheKey() {
        return mDataCacheKey;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    /**
     * 添加请求到队列
     * 进行url编码
     */
    public void sendRequest() {
        updateUrl();
        mSingletonVolley.addToRequestQueue(this);
    }

    /**
     * 添加请求到队列
     *
     * @param dataCache 是否读取缓存对象文件，并将请求到的对象覆盖缓存
     * @deprecated 统一使用HTTP缓存，不建议使用缓存对象到文件
     */
    public void sendRequest(boolean dataCache) {
        if (dataCache == true) {
            mNeedDataCache = true;

            Object object = DataCache.getInstance().getDataFromDiskCache(
                    getDataCacheKey());
            if (object != null && mResponseCallbackListener != null) {
                mResponseCallbackListener.onResponseSuccess((T) object,
                        true, getTag(), getOwerQueueTag());
            }
        }
        sendRequest();
    }

    public boolean ismNeedDataCache() {
        return mNeedDataCache;
    }

    public void setmNeedDataCache(boolean mNeedDataCache) {
        this.mNeedDataCache = mNeedDataCache;
    }

    /**
     * @return
     */
    protected Response.Listener<T> getResponseListener() {
        return new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (mResponseCallbackListener != null) {
                    if (mNeedDataCache == true && response != null) {
                        DataCache.getInstance().addDataToDiskCache(getDataCacheKey(), response);
                    }

                    mResponseCallbackListener.onResponseSuccess(response, false, getRequestTag(), getOwerQueueTag());
                }
            }
        };
    }

    /**
     * @return
     */
    protected Response.ErrorListener getResponseErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mResponseCallbackListener != null) {
                    if (error instanceof ResponseCodeVellyError) {
                        ResponseCodeVellyError codeVellyError = (ResponseCodeVellyError) error;
                        BaseReslutRes commonResponse = codeVellyError.getCommonResponse();
                        mResponseCallbackListener.onResponseCodeFailure(commonResponse.getCode(), commonResponse.getMsg_cn(), commonResponse.getMsg_en(), getRequestTag(), getOwerQueueTag());

                    } else {
                        mResponseCallbackListener.onResponseFailure(error, getRequestTag(), getOwerQueueTag());
                    }
                }
            }
        };
    }

    /***
     * 更新请求的url地址(去除自动添加的小区id可以在这之前调用setNeedCommunityId = false)
     */
    public void updateUrl() {

        BuildUrl mBuildUrl = new BuildUrl(getMethod(), methodUrl, paramObject);

        if (getMethod() == Method.GET) {
            setUrl(mBuildUrl.buileUrl());
        } else if (getMethod() == Method.POST) {

            String url = BaseApplication.APP_URL;
            setUrl(url);
            mStrBody = mBuildUrl.buileUrl();
        }
    }

//    /***
//     * 拼接请求链接地址和系统参数(ver,timestamp,session_key,sign，[accessToken:有了则加入无则忽略])
//     *
//     * @param mehtod          get or post
//     * @param methodUrl
//     * @param params          业务参数bean
//     * @return get with the params,post just return url
//     */
//    public static String buildParamsUrl(int mehtod, String methodUrl, HashMap params) {
//        StringBuffer paramBuffer = new StringBuffer();
//        paramBuffer.append("method=");
//        paramBuffer.append(methodUrl);
//
//        String queryJson = null;
//
//        Gson gson = new Gson();
//        if (params != null) {
//            queryJson = gson.toJson(params);
//            LogUtils.d("params " + queryJson);
//
//            Iterator iter = params.entrySet().iterator();
//            StringBuffer paramBuffer2 = new StringBuffer();
//
//            while (iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                Object key = entry.getKey();
//                Object value = entry.getValue();
//                paramBuffer2.append("&" + key);
//                paramBuffer2.append("=");
//                paramBuffer2.append(value);
//            }
//
//            queryJson = paramBuffer2.toString();
//        }
//
//
////        try {
////            // 插入accessToken的值
////            final String token = LoginTokenPrefs.get(BaseApplication.getApplicationInstance()).getToken();
////            if (!TextUtils.isEmpty(token)) {
////                JSONObject queryJsonObject = null;
////                if (params != null) {
////                    queryJsonObject = new JSONObject(queryJson);
////                } else {
////                    queryJsonObject = new JSONObject();
////                }
////                if (!queryJsonObject.has("accessToken")) {
////                    queryJsonObject.put("accessToken", token);
////
////                }
////
////                queryJson = queryJsonObject.toString();
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        String signUrl = paramBuffer.toString();
//
////        if (!TextUtils.isEmpty(queryJson)) {
////            // 签名的数据是原始数据，不能对参数进行UrlEncode之后再签名；POST参数以同样的方式参与签名。
////            try {
////                queryJson = URLEncoder.encode(queryJson, "UTF-8");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            signUrl = signUrl + "&params=" + queryJson;
////        }
//        if (!TextUtils.isEmpty(queryJson)) {
////            paramBuffer.append("&params=");
//            paramBuffer.append(queryJson);
//        }
//
//        //TODO 签名
////        String secretKey = "";//秘钥
////        AppSignatureMd5 signUtil = new AppSignatureMd5(secretKey, signUrl);
////        String md5Sign = signUtil.sign();
////        paramBuffer.append("&sign=");
////        paramBuffer.append(md5Sign);
//
//        String encodeUrl = paramBuffer.toString();
//
//        String url = BaseApplication.APP_URL;
//        url = url + "?";
//        if (mehtod == Method.GET) {
//            encodeUrl = url + paramBuffer.toString();
//        }
//
//        return encodeUrl;
//    }


    public String getParserKey() {
        return parserKey;
    }

    /**
     * 从得到的json的哪一层key开始解析（只取key对应的value值进行解析）
     * 例子：data：{json} 只对{json}进行转化成T
     * 为null则全部json数据都进行转化为T
     * 多层用“.”连接 "data.infos"
     */
    public YJLGsonRequest<T> setParserKey(String parserKey) {
        this.parserKey = parserKey;
        return this;
    }

    public YJLGsonRequest<T> setDataParserKey() {
        this.parserKey = "data";
        return this;
    }

    public YJLGsonRequest<T> setDataInfosParserKey() {
        this.parserKey = "data.infos";
        return this;
    }

    /**
     * 返回标识请求的tag
     *
     * @return
     */
    public Object getRequestTag() {

        return getTag();

    }

    /***
     * 标识，用于代表该请求
     */
    @Override
    public Object getTag() {
        return this.getUrlTag();
    }

    /***
     * 获得url带参数的字符串，作为一个标识，标识不同的请求对象。
     *
     * @return
     */
    public String getUrlTag() {
        if (paramObject == null) {
            return this.methodUrl;
        } else {
            return this.methodUrl + mGson.toJson(paramObject);
        }
    }

    public Object getOwerQueueTag() {
        return owerQueueTag;
    }

    public void setOwerQueueTag(Object owerQueueTag) {
        this.owerQueueTag = owerQueueTag;
    }


}