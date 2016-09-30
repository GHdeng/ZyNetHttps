package com.deng.netlibrary.netLib.utils;


import com.deng.netlibrary.logLib.Logger;

/**
 * 输出日志
 *
 * Created by 邓鉴恒 on 16/9/13.
 */
public class LogHelper {
    public static boolean isAllowLog = true;// 是否允许开启log

    public static void init(String tag) {
        Logger.init(tag);
    }

    public static void e(String msg) {
        if (isAllowLog )
            Logger.e(msg);
    }

    public static void d(String msg) {
        if (isAllowLog )
            Logger.d(msg);
    }

    public static void i(String msg) {
        if (isAllowLog )
            Logger.i(msg);
    }

    public static void j(String msg) {
        if (isAllowLog )
            Logger.json(msg);
    }

    public static void e(String TAG, String msg) {
        if (isAllowLog )
            Logger.t(TAG).e(msg);
    }

    public static void d(String TAG, String msg) {
        if (isAllowLog )
            Logger.t(TAG).d(msg);
    }

    public static void i(String TAG, String msg) {
        if (isAllowLog )
            Logger.t(TAG).i(msg);
    }

    public static void j(String TAG, String msg) {
        if (isAllowLog )
            Logger.t(TAG).json(msg);
    }

    public static void e(Class<?> clazz, String msg) {
        if (isAllowLog )
            Logger.t(clazz.getSimpleName()).e(msg);
    }

    public static void d(Class<?> clazz, String msg) {
        if (isAllowLog )
            Logger.t(clazz.getSimpleName()).d(msg);
    }

    public static void i(Class<?> clazz, String msg) {
        if (isAllowLog )
            Logger.t(clazz.getSimpleName()).i(msg);
    }

    public static void j(Class<?> clazz, String msg) {
        if (isAllowLog )
            Logger.t(clazz.getSimpleName()).json(msg);
    }
}
