package com.hnhy.trash;

import com.llw.mvplibrary.BaseApplication;
import com.llw.mvplibrary.network.NetworkApi;

/**
 * 自定义Application
 */
public class TrashApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化网络框架
        NetworkApi.init(new NetworkRequiredInfo(this));
    }
}
