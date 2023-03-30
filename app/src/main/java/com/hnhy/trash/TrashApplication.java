package com.hnhy.trash;

import android.content.Context;
import android.content.res.Configuration;

import com.hnhy.trash.api.LanguageListener;
import com.hnhy.trash.utils.Constant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.llw.mvplibrary.BaseApplication;
import com.llw.mvplibrary.network.NetworkApi;

import org.litepal.LitePal;

import java.util.Locale;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

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

        //数据库初始化
        LitePal.initialize(this);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();//这里处理所有的Rxjava异常
            }
        });

    }
}
