package com.ss.ftpserver.gui;

import android.app.Application;
import android.content.Context;

/**
 * 当Android程序启动时系统会创建一个Application对象，用来存储系统的一些信息
 * 自定义application，在任何地方都可以得到application context
 */
public class MyApplication extends Application {
    private static Context mContext;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
