package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hnhy.trash.R;
import com.llw.mvplibrary.base.BaseActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class NewsDetailsActivity extends BaseActivity {
    private Toolbar toolbar;
    private WebView webView;

    @Override
    public void initData(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.web_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断webview是否处于当前Activity页面   是Activity页面直接finish   否则依次销毁当前web页面最后销毁当前Activity+
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
        //设置状态栏
//        setStatubar(this, R.color.white, true);
        if (!hasNetwork()) {
            showMsg(getString(R.string.open_network));
            return;
        }

        //加载WebView
        loadWebView();
    }

    /**
     * 加载webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView() {
        //开始加载Url
        showLoadingDialog();

        String url = getIntent().getStringExtra("url");
        if (url == null || url.isEmpty()) {
            showMsg(getString(R.string.no_news_info));
            return;
        }

        WebSettings webSet = webView.getSettings();
        //设置与JavaScript交互
        webSet.setJavaScriptEnabled(true);
        //设置JavaScript可以打开新的窗口
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置允许文件访问
        webSet.setAllowFileAccess(true);
        //设置布局算法
        webSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //支持可缩放
        webSet.setSupportZoom(true);
        //显示缩放控件
        webSet.setBuiltInZoomControls(true);
        //设置使用宽视图端口;
        webSet.setUseWideViewPort(true);
        //支持多个窗口设置;
        webSet.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        //设置应用程序缓存启用;
        webSet.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        //设置Dom Storage启用;
        webSet.setDomStorageEnabled(true);
        //	设置地理位置
        webSet.setGeolocationEnabled(true);
        //设置应用程序缓存最大尺寸;
        webSet.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        //插件设置状态;
        webSet.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //渲染设置优先级;
        webSet.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 设置缓存模式,
        webSet.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //https://www.baidu.com/
        webView.loadUrl(url);
        //一般情况下手机会使用默认浏览器打开网址，为了防止此操作，设置以下：
        //直接加载网址，省略默认浏览器.....
        webView.setWebViewClient(new WebViewClient());

        //重写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //加载完成
                if (newProgress == 100){
                    hideLoadingDialog();
                }else{//正在加载中
                    showLoadingDialog();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//
//            }
//        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void onDestroy() {
        //避免内存泄露
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}