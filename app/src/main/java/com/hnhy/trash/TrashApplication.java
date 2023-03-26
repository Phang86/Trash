package com.hnhy.trash;

import android.content.Context;
import android.content.res.Configuration;

import com.hnhy.trash.api.LanguageListener;
import com.hnhy.trash.utils.Constant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.llw.mvplibrary.BaseApplication;
import com.llw.mvplibrary.network.NetworkApi;

import java.util.Locale;

/**
 * 自定义Application
 */
public class TrashApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化网络框架
        NetworkApi.init(new NetworkRequiredInfo(this));
        //初始化讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + Constant.XF_APPID);

    }
}
