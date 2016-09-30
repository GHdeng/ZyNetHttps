package com.deng.netlibrary.netLib.response;

import org.json.JSONObject;

/**
 * json回调
 * Created by 邓鉴恒 on 16/9/14.
 */
public abstract class JsonResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, JSONObject response);

//    public abstract void onFinish(int statusCode);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }

}
