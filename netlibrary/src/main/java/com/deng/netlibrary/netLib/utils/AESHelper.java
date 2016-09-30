package com.deng.netlibrary.netLib.utils;

import android.util.Base64;

import com.deng.netlibrary.netLib.model.RPCRequestModel;
import com.google.gson.Gson;

import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/**
 * AES 是一种可逆加密算法
 */
public class AESHelper {

    private final String TAG = this.getClass().getSimpleName();

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "your key";//key，可自行修改
    private String ivParameter = "your ivkey";//偏移量,可自行修改
    private static AESHelper instance = null;

    public static AESHelper getInstance() {
        if (instance == null)
            synchronized (AESHelper.class) {
                if (instance == null) {
                    instance = new AESHelper();
                }
            }
        return instance;
    }

    // 加密
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);// 此处使用BASE64做转码。
    }

    // 解密
    public String decrypt(String sSrc) throws Exception {
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original, "utf-8");
        return originalString;
    }

    /**
     * 通过AES加密输入数据
     *
     * @param currentTime 时间戳
     * @param method      方法名
     * @param params      参数
     * @return sb 加密后的数据
     */
    public String paramsByAES(boolean isEncrypt, long currentTime, String method, Map<String, Object> params) {
        RPCRequestModel requestModel = new RPCRequestModel();

        try {
            requestModel.setId(currentTime);
            requestModel.setJsonrpc("2.0");
            requestModel.setMethod(method);
            if (isEncrypt) {
                String tempParams = JSONUtil.toJson(params);
                tempParams = AESHelper.getInstance().encrypt(tempParams);
                requestModel.setParams(tempParams);
            } else {
                requestModel.setParams(params);
            }
        } catch (Exception e) {
            new RuntimeException("加密出错:"+e.getMessage());
        }
        Gson gson = new Gson();
        return  gson.toJson(requestModel);
    }

    /**
     * 通过AES解密
     *
     * @param result 解密数据
     * @return 解密结果
     */
    /**
     * 通过ADE进行解密
     */
    public String decryptionByAES(boolean isDecryption, String result) {
        if (isDecryption) {
            try {
                Map<String, Object> obj = JSONUtil.fromJson(result, Map.class);//将最开始的对象序列化成Map
                Object result1Obj = obj.get("result");//得到result
                String result1Str = "";//定义一个存放result的变量
                if (result1Obj != null) {//判断是否有result
                    result1Str = result1Obj.toString();//对存放result的变量进行赋值
                }
                if (result1Str != "") {//判断存放result变量是否有值
                    //开始进行字符串进行切割
                    String result1 = result.substring(0, result.indexOf("\"" + result1Str + "\""));
                    String result2 = AESHelper.getInstance().decrypt(result1Str);//解密字符串
                    String result3 = result.substring(result.indexOf("\"" + result1Str + "\"") + result1Str.length() + 2);
                    result = result1 + result2 + result3;//最后拼接字符串并赋值到全局的result变量中
                }

            }catch (Exception e){
                new RuntimeException("解密出错:"+e.getMessage());
            }

        }

        LogHelper.j(TAG, result);
        return result;
    }

}