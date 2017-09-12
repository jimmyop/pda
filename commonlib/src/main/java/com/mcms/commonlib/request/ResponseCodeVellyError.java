package com.mcms.commonlib.request;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.mcms.commonlib.request.data.BaseReslutRes;

public class ResponseCodeVellyError extends VolleyError {
    /**
     *
     */
    private static final long serialVersionUID = -2821936380214670576L;
    private BaseReslutRes commonResponse;

    public ResponseCodeVellyError() {
    }

    public ResponseCodeVellyError(BaseReslutRes commonResponse) {
        this.commonResponse = commonResponse;
    }

    public ResponseCodeVellyError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public ResponseCodeVellyError(Throwable cause) {
        super(cause);
    }

    public BaseReslutRes getCommonResponse() {
        return commonResponse;
    }

    public void setCommonResponse(BaseReslutRes commonResponse) {
        this.commonResponse = commonResponse;
    }

}
