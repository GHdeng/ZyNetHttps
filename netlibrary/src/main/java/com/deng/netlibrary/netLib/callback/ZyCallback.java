package com.deng.netlibrary.netLib.callback;

import android.os.Handler;

import com.deng.netlibrary.netLib.response.GsonResponseHandler;
import com.deng.netlibrary.netLib.response.IResponseHandler;
import com.deng.netlibrary.netLib.response.JsonResponseHandler;
import com.deng.netlibrary.netLib.response.StrResponseHandler;
import com.deng.netlibrary.netLib.utils.LogHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 一般定义回调
 * Created by 邓鉴恒 on 2016/9/26.
 */

public class ZyCallback implements Callback {

    private final String TAG = this.getClass().getSimpleName();

    private IResponseHandler mResponseHandler;
    private Handler mHandler;

    public ZyCallback(Handler handler, IResponseHandler mResponseHandler) {
        this.mHandler = handler;
        this.mResponseHandler = mResponseHandler;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailure(-2, e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFinish(response.code());
            }
        });

        if (response.isSuccessful()) {
            final String response_body = response.body().string();
            LogHelper.e(TAG, response_body);
            if (mResponseHandler instanceof JsonResponseHandler) {
                //Json回调
                try {
                    final JSONObject jsonObject = new JSONObject(response_body);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((JsonResponseHandler) mResponseHandler).onSuccess(response.code(), jsonObject);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogHelper.e(TAG, "JsonObject返回失败 -->" + response_body + "\n" + e.getMessage());
                    mResponseHandler.onFailure(response.code(), e.toString());
                }
            } else if (mResponseHandler instanceof GsonResponseHandler) {
                //Gson回调
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            ((GsonResponseHandler) mResponseHandler).onSuccess(response.code(), gson.fromJson(response_body, ((GsonResponseHandler) mResponseHandler).getType()));
                        } catch (Exception e) {
                            LogHelper.e(TAG, "Gson返回失败 -->" + response_body +"\n" + e.getMessage());
                        }
                    }
                });
            } else if (mResponseHandler instanceof StrResponseHandler) {
                //字符串回调
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((StrResponseHandler) mResponseHandler).onSuccess(response.code(), response_body);
                    }
                });
            }
        } else {
            LogHelper.e(TAG, "失败返回code:" + response.code());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(-2, "网络异常");
                }
            });
        }
    }
}
