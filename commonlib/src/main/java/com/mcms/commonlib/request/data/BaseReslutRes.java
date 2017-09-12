package com.mcms.commonlib.request.data;

/***
 *
 * 用于接收服务响应某个请求是否成功的bean封装
 * gsonrequest 的parserkey 为null
 * @author jimmy
 *
 */
public class BaseReslutRes {
    protected int code;
    private String msg_cn;
    private String msg_en;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg_cn() {
        return msg_cn;
    }

    public void setMsg_cn(String msg_cn) {
        this.msg_cn = msg_cn;
    }

    public String getMsg_en() {
        return msg_en;
    }

    public void setMsg_en(String msg_en) {
        this.msg_en = msg_en;
    }
}
