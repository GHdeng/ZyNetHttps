# ZyNetHttps

# 对OkHttp3请求库简单封装适配Json-Rpc调用协议
###---------主要对Json-Rpc结构封装，同时保留post，get请求，加入文件图片download。回调形式Gson，Json，String等。

####技术简介

json-rpc是基于json的跨语言远程调用协议，比xml-rpc、webservice等基于文本的协议传输数据格小；相对hessian、java-rpc等二进制协议便于调试、实现、扩展，是非常优秀的一种远程调用协议。目前主流语言都已有json-rpc的实现框架，java语言中较好的json-rpc实现框架有jsonrpc4j、jpoxy、json-rpc。三者之中jsonrpc4j既可独立使用，又可与spring无缝集合，比较适合于基于spring的项目开发。

####JSON-RPC协议描述
	json-rpc协议非常简单，发起远程调用时向服务端传输数据格式如下：
```java
{ "method": "main", "params": ["JSON-RPC"], "id": 1}
```
	json-rpc返回数据
```java
{ "result":"Hello JSON-RPC","error": null,"id":1} 
```

###1.json-rpc请求
```java
private void postRpc() {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", 10);
        ZyNetHttps.getInstances()
                .newBuilder("http://zhanyun/shop?", "Main")//设置请求路径和请求方法名
                .tag(this)//当前请求标识
                .params(params)//请求参数
                .isInputDecryption(false)//输入加密
                .isOutputDecryption(false)//输出加密
                .ctime(30)//连接时间
                .wtime(10)//写入时间
                .rtime(10)//读取时间
                .callBack(new GsonResponseHandler<ModelMain>() {//请求回调
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
                .RPC();//请求类型
    }
```

###2.post请求
```java
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
```

###3.get请求
```java
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
```

###4.文件下载
```java
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
```

###5.取消当前网络请求
```java
private void cancel(Context mContex) {
        ZyNetHttps.getInstances().cancel(mContex);
}
```

github地址：https://github.com/GHdeng/ZyNetHttps
感觉封装得还是不够完善，欢迎大家吐槽Issues。


我们都是站在巨人的肩膀上：
参考
- https://github.com/hongyangAndroid/okhttputils
- https://github.com/tsy12321/BaseAndroidProject

# License
	Copyright (c) 2016 GHdeng
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
