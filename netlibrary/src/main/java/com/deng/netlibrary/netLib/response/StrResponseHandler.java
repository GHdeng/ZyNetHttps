package com.deng.netlibrary.netLib.response;

/**
 * 获取到字符串回调
 * Created by 邓鉴恒 on 16/9/14.
 */

public abstract class StrResponseHandler implements IResponseHandler {

    public abstract void onSuccess(int statusCode, String response);

//    public abstract void onFinish(int statusCode);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
