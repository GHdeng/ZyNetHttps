package com.deng.zynethttps;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deng.netlibrary.netLib.ZyNetHttps;
import com.deng.netlibrary.netLib.response.DownloadResponseHandler;
import com.deng.netlibrary.netLib.response.GsonResponseHandler;
import com.deng.netlibrary.netLib.response.StrResponseHandler;
import com.deng.netlibrary.netLib.utils.LogHelper;
import com.deng.zynethttps.model.ModelMain;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogHelper.init(TAG);
    }

    /**
     * 符合json rpc 的请求
     */
    private void postRpc() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", 10);
        ZyNetHttps.getInstances()
                .newBuilder("http://zhanyun/shop?", "Main")
                .tag(this)
                .params(params)
                .isInputDecryption(false)
                .isOutputDecryption(false)
                .ctime(30)
                .wtime(10)
                .rtime(10)
                .callBack(new GsonResponseHandler<ModelMain>() {
                    @Override
                    public void onFinish(int statusCode) {
                        LogHelper.e(statusCode + "");
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        LogHelper.e(error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, ModelMain response) {

                    }
                })
                .RPC();
    }

    private void post() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", 10);
        ZyNetHttps.getInstances()
                .newBuilder("http://zhanyun/shop?")
                .tag(this)
                .params(params)
                .callBack(new GsonResponseHandler<ModelMain>() {
                    @Override
                    public void onFinish(int statusCode) {
                        LogHelper.e(statusCode + "");
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        LogHelper.e(error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, ModelMain response) {

                    }
                })
                .POST();
    }

    private void get() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", 10);
        ZyNetHttps.getInstances()
                .newBuilder("http://zhanyun/shop?")
                .tag(this)
                .params(params)
                .callBack(new StrResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {

                    }

                    @Override
                    public void onFinish(int statusCode) {

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                })
                .GET();
    }

    private void download() {
        ZyNetHttps.getInstances()
                .newBuilder("http://zhanyun/shop?")
                .filedir("/android/")
                .filedname("android.png")
                .callBack(new DownloadResponseHandler() {

                    @Override
                    public void onFinish(int statusCode) {

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {

                    }


                })
                .DOWNLOAD();
    }

    /**
     * 取消请求
     *
     * @param mContex
     */
    private void cancel(Context mContex) {
        ZyNetHttps.getInstances().cancel(mContex);
    }

}
