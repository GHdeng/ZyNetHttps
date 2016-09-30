package com.deng.netlibrary.netLib;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.deng.netlibrary.BuildConfig;
import com.deng.netlibrary.netLib.body.ResponseProgressBody;
import com.deng.netlibrary.netLib.callback.ZyCallback;
import com.deng.netlibrary.netLib.callback.ZyDownloadCallback;
import com.deng.netlibrary.netLib.callback.ZyRpcCallback;
import com.deng.netlibrary.netLib.response.DownloadResponseHandler;
import com.deng.netlibrary.netLib.response.IResponseHandler;
import com.deng.netlibrary.netLib.utils.AESHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 邓鉴恒 on 2016/9/26.
 */

public class ZyNetHttps {

    /* rpc请求 , post请求 , get请求  , download下载 ,upload上传*/
    private final int POSTRPC = 0, POST = 1, GET = 2, DOWNLOAD = 3, UPLOAD = 4;
    private long currentTime;//时间戳
    private final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private static ZyNetHttps instances;

    private OkHttpClient client;

    public static ZyNetHttps getInstances() {
        if (instances == null) {
            synchronized (ZyNetHttps.class) {
                if (instances == null)
                    instances = new ZyNetHttps(null, -1);
            }
        }
        return instances;
    }

    private ZyNetHttps(Builder builder, int type) {
        instances = this;
        client = new OkHttpClient();

        if (builder != null)
            switch (type) {
                case POSTRPC:
                    postRpc(builder);
                    break;
                case POST:
                case GET:
                    postOrGet(builder, type);
                    break;
                case DOWNLOAD:
                    download(builder);
                    break;
                case UPLOAD:

                    break;
                default:

                    break;
            }
    }

    /**
     * 构建一个builder
     *
     * @param url
     * @param method
     * @return
     */
    public Builder newBuilder(String url, String method) {
        return new Builder(url, method);
    }

    public Builder newBuilder(String url){
        return new Builder(url);
    }

    /**
     * rpc post 请求
     *
     * @param builder
     */
    private void postRpc(Builder builder) {

        final Context context = builder.tag;
        final String url = builder.mUrl;
        final String method = builder.mMethod;
        final Map<String, Object> params = builder.mParams;
        final int rTime = builder.rTime;
        final int wTime = builder.wTime;
        final int connTime = builder.cTime;
        final boolean isInputDecryption = builder.isInputDecryption;
        final boolean isOutputDecryption = builder.isOutputDecryption;
        final IResponseHandler responseHandler = builder.mResponseHandler;
        //获取到唯一标识
        final String deviceId = "you device id";
        currentTime = System.currentTimeMillis();

        client = client
                .newBuilder().connectTimeout(connTime, TimeUnit.SECONDS)
                .writeTimeout(wTime, TimeUnit.SECONDS)
                .readTimeout(rTime, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();

        String requestIdStr = String.valueOf(currentTime);
        String version = String.valueOf(BuildConfig.VERSION_CODE);

        if (TextUtils.isEmpty(method)) {
            new RuntimeException("rpc请求 method参数不许为空!");
        }

        String paramStr = AESHelper.getInstance().paramsByAES(isInputDecryption, currentTime, method, params);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, paramStr))
                .addHeader("deviceid", deviceId + "")
                .addHeader("X-JSON-RPC", method)
                .addHeader("er", String.valueOf(isOutputDecryption))
                .addHeader("dr", String.valueOf(isInputDecryption));

        if (context != null) {
            requestBuilder.tag(context);
        }

        if (responseHandler == null)
            client.newCall(requestBuilder.build()).enqueue(new ZyRpcCallback(isOutputDecryption, new Handler(), null));
        else
            client.newCall(requestBuilder.build()).enqueue(new ZyRpcCallback(isOutputDecryption, new Handler(), responseHandler));
    }

    /**
     * post 请求
     *
     * @param builder
     */
    private void postOrGet(Builder builder, int type) {
        final Context context = builder.tag;
        final String url = builder.mUrl;
        final Map<String, Object> params = builder.mParams;
        final int rTime = builder.rTime;
        final int wTime = builder.wTime;
        final int connTime = builder.cTime;
        final IResponseHandler responseHandler = builder.mResponseHandler;

        //post builder 参数
        FormBody.Builder okBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                okBuilder.add(entry.getKey(), (String) entry.getValue());
            }
        }

        Request request = null;

        //发起request
        switch (type) {
            case POST:
                if (context == null) {
                    request = new Request.Builder()
                            .url(url)
                            .post(okBuilder.build())
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .post(okBuilder.build())
                            .tag(context)
                            .build();
                }
                break;
            case GET:
                //拼接url
                String get_url = url;
                if (params != null && params.size() > 0) {
                    int i = 0;
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        if (i++ == 0) {
                            get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                        } else {
                            get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                        }
                    }
                }
                if (context == null) {
                    request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .get()
                            .tag(context)
                            .build();
                }
                break;
        }

        client.newCall(request).enqueue(new ZyCallback(new Handler(), responseHandler));
    }

    /**
     * 下载
     *
     * @param builder
     */
    private void download(Builder builder) {
        final String url = builder.mUrl;
        final String filedir = builder.fileDir;
        final String filename = builder.fileName;
        final Context context = builder.tag;
        final IResponseHandler downloadResponseHandler = builder.mResponseHandler;

        Request request;
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .tag(context)
                    .build();
        }

        if (downloadResponseHandler instanceof DownloadResponseHandler)
            client.newBuilder()
                    .addNetworkInterceptor(new Interceptor() {      //设置拦截器
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    .body(new ResponseProgressBody(originalResponse.body(), (DownloadResponseHandler) downloadResponseHandler))
                                    .build();
                        }
                    })
                    .build()
                    .newCall(request)
                    .enqueue(new ZyDownloadCallback(new Handler(),(DownloadResponseHandler) downloadResponseHandler, filedir, filename));
    }


    /**
     * 通过builder的模式来构建参数
     */
    public class Builder {
        //设置tag下标,停止请求
        private Context tag;
        //请求路径
        private String mUrl;
        //方法名
        private String mMethod;
        //参数
        private Map<String, Object> mParams;
        private int rTime = 30;//读
        private int wTime = 30;//写
        private int cTime = 30;//连接
        private String fileDir;//文件路径
        private String fileName = currentTime+"";//文件名称
        private boolean isInputDecryption = false;//输入加密
        private boolean isOutputDecryption = false;//输出加密
        private IResponseHandler mResponseHandler;

        public Builder(String url, String method) {
            this.mUrl = url;
            this.mMethod = method;
        }

        public Builder(String url) {
            this.mUrl = url;
        }

        public Builder(String url, String fileDir, String fileName) {
            this.mUrl = url;
            this.fileDir = fileDir;
            this.fileName = fileName;
        }

        public Builder tag(Context tag) {
            this.tag = tag;
            return this;
        }

        public Builder method(String method) {
            this.mMethod = method;
            return this;
        }

        public Builder params(Map<String, Object> mParams) {
            this.mParams = mParams;
            return this;
        }

        public Builder rtime(int rTime) {
            this.rTime = rTime;
            return this;
        }

        public Builder wtime(int wTime) {
            this.wTime = wTime;
            return this;
        }

        public Builder ctime(int cTime) {
            this.cTime = cTime;
            return this;
        }

        public Builder isInputDecryption(boolean isInputDecryption) {
            this.isInputDecryption = isInputDecryption;
            return this;
        }

        public Builder isOutputDecryption(boolean isOutputDecryption) {
            this.isOutputDecryption = isOutputDecryption;
            return this;
        }

        public Builder filedir(String fileDir) {
            this.fileDir = fileDir;
            return this;
        }

        public Builder filedname(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder callBack(IResponseHandler mResponseHandler) {
            this.mResponseHandler = mResponseHandler;
            return this;
        }

        public ZyNetHttps RPC() {
            //post rpc 请求
            return new ZyNetHttps(this, POSTRPC);
        }

        public ZyNetHttps POST() {
            //post 请求
            return new ZyNetHttps(this, POST);
        }

        public ZyNetHttps GET() {
            //get 请求
            return new ZyNetHttps(this, GET);
        }

        public ZyNetHttps DOWNLOAD() {
            //下载文件
            return new ZyNetHttps(this, DOWNLOAD);
        }
    }

    /**
     * 取消当前的context标签请求
     *
     * @param context
     */
    public void cancel(Context context) {
        if (client != null) {
            //取消队列中得请求
            for (Call call : client.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(context)) {
                    call.cancel();
                }
            }
            //取消正在运行的请求
            for (Call call : client.dispatcher().runningCalls()) {
                if (call.request().tag().equals(context)) {
                    call.cancel();
                }
            }
        }
    }
}
