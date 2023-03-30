package com.hnhy.trash;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.hnhy.trash.api.LanguageListener;
import com.hnhy.trash.utils.Constant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.llw.mvplibrary.BaseApplication;
import com.llw.mvplibrary.network.NetworkApi;
import com.tencent.smtt.sdk.QbSdk;

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

        //初始化X5内核
        initX5();

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();//这里处理所有的Rxjava异常
            }
        });
    }

    private void initX5() {
        QbSdk.setDownloadWithoutWifi(true);
        //x5内核初始化接口//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.initX5Environment(getApplicationContext(),  new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
            }
        });
    }
}
