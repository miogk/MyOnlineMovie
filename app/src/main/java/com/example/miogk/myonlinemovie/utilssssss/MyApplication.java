package com.example.miogk.myonlinemovie.utilssssss;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, "appid=" + "5c5d7387");
        MobSDK.init(this);
        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
