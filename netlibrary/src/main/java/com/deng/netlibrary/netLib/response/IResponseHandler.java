package com.deng.netlibrary.netLib.response;

/**
 * 网络请求Handler回调接口
 * Created by 邓鉴恒 on 16/9/14.
 */

public interface IResponseHandler {
    void onFinish(int statusCode);
    void onFailure(int statusCode, String error_msg);
    void onProgress(long currentBytes, long totalBytes);
}
