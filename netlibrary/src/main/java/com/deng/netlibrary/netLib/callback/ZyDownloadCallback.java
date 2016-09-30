package com.deng.netlibrary.netLib.callback;

import android.os.Handler;

import com.deng.netlibrary.netLib.response.DownloadResponseHandler;
import com.deng.netlibrary.netLib.utils.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 文件下载回调
 * Created by 邓鉴恒 on 2016/9/26.
 */

public class ZyDownloadCallback implements Callback {

    private Handler mHandler;
    private DownloadResponseHandler mDownloadResponseHandler;
    private String mFileDir;
    private String mFilename;

    public ZyDownloadCallback(Handler handler, DownloadResponseHandler downloadResponseHandler,
                              String filedir, String filename) {
        mHandler = handler;
        mDownloadResponseHandler = downloadResponseHandler;
        mFileDir = filedir;
        mFilename = filename;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDownloadResponseHandler.onFailure(e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if(response.isSuccessful()) {
            File file = null;
            try {
                file = saveFile(response, mFileDir, mFilename);
            } catch (final IOException e) {
                LogHelper.e("下载文件失败"+ e);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFailure("保存文件失败" + e.toString());
                    }
                });
            }

            final File newFile = file;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadResponseHandler.onFinish(-2,newFile);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadResponseHandler.onFailure("失败Code:" + response.code());
                }
            });
        }
    }

    //保存文件
    private File saveFile(Response response, String filedir, String filename) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filedir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

}
