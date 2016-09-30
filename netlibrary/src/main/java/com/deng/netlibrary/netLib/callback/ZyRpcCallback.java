package com.deng.netlibrary.netLib.callback;

import android.os.Handler;

import com.deng.netlibrary.netLib.model.RPCBaseModel;
import com.deng.netlibrary.netLib.response.GsonResponseHandler;
import com.deng.netlibrary.netLib.response.IResponseHandler;
import com.deng.netlibrary.netLib.response.StrResponseHandler;
import com.deng.netlibrary.netLib.utils.AESHelper;
import com.deng.netlibrary.netLib.utils.LogHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * rpc结构回调
 * Created by 邓鉴恒 on 2016/9/26.
 */

public class ZyRpcCallback implements Callback {

    private final String TAG = this.getClass().getSimpleName();

    private IResponseHandler mResponseHandler;
    private Handler mHandler;
    private boolean isOutputDecryption;

    public ZyRpcCallback(boolean isOutputDecryption, Handler mHandler, IResponseHandler mResponseHandler) {
        this.mHandler = mHandler;
        this.mResponseHandler = mResponseHandler;
        this.isOutputDecryption = isOutputDecryption;
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
        final String response_body = response.body().string();
        if (response.isSuccessful()) {
            //Gson回调
            if (mResponseHandler instanceof GsonResponseHandler)
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String result = AESHelper.getInstance().decryptionByAES(isOutputDecryption, response_body);
                            final RPCBaseModel rpcBaseModel = new Gson().fromJson(result, RPCBaseModel.class);
                            if (rpcBaseModel.getError() != null) {//接口层错误
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mResponseHandler.onFailure(-2, rpcBaseModel.getError().getMessage());
                                    }
                                });
                            } else {//业务层
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Gson gson = new Gson();
                                        ((GsonResponseHandler) mResponseHandler).onSuccess(0,gson.fromJson(result,((GsonResponseHandler) mResponseHandler).getType()));
                                    }
                                });
                            }

                        } catch (Exception e) {
                            LogHelper.e(TAG, "onResponse fail parse gson , body = " + response_body + "{" + e + "}");
                        }
                    }
                });
            else if (mResponseHandler instanceof StrResponseHandler) {
                final String result = AESHelper.getInstance().decryptionByAES(isOutputDecryption, response_body);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((StrResponseHandler) mResponseHandler).onSuccess(0, result);
                    }
                });
            }
        } else {
            LogHelper.e(TAG, "onResponse fail status = " + response.code());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(-2, "网络异常" + response.code());
                }
            });
        }
    }
}
