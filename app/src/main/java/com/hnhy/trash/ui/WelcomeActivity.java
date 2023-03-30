package com.hnhy.trash.ui;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hnhy.trash.R;
import com.hnhy.trash.contract.WallPaperContract;
import com.hnhy.trash.model.WallPaperResponse;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.utils.LanguageUtil;
import com.hnhy.trash.utils.SpUserUtils;
import com.llw.mvplibrary.base.BaseActivity;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.llw.mvplibrary.network.NetworkUtils;
import com.llw.mvplibrary.network.utils.StatusBarUtil;

import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginException;

public class WelcomeActivity extends MvpActivity<WallPaperContract.WallPaperPresenter> implements WallPaperContract.WallPaperView {

    private RelativeLayout img;
    private int time = 5;
    private CardView cv;
    private TextView tv;
    private String IMG_URL = "https://bing.biturl.top/?resolution=1366&format=image&index=0&mkt=zh-CN";
//    private String IMG_URL;
    private final String TAG = "WelcomeActivity";
//    private List<WallPaperResponse.ResBean.VerticalBean> list;
    //    private final String IMG_URL = "https://api.dujin.org/bing/1080.php";

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    private void initView() {
        img = findViewById(R.id.welcome_bg);
        tv = findViewById(R.id.tv);
        cv = findViewById(R.id.cardview);
        checkLanguage();
        Log.e(TAG, "initView: "+IMG_URL);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (!hasNetwork()){
            showMsg(getString(R.string.open_network));
        }
        //获取壁纸
        mPresenter.getWallPaper();
        StatusBarUtil.transparencyBar(this);
        initView();
    }

    //检查语言
    private void checkLanguage() {
        String language = SpUserUtils.getString(this, Constant.LANGUAGE_KEY);
        LanguageUtil.changeAppLanguage(this, language, WelcomeActivity.class,false);
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            cv.setVisibility(View.VISIBLE);
            time--;
            tv.setText(getString(R.string.skip)+" "+time);
            handler.postDelayed(this, 1000);
            if (time == 0){
                goToActivity();
            }
        }
    };

    private void goToActivity() {
        handler.removeCallbacksAndMessages(null);
        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected WallPaperContract.WallPaperPresenter createPresenter() {
        return new WallPaperContract.WallPaperPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void skit(View view) {
        goToActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            handler.removeCallbacksAndMessages(null);
        }
        return super.onKeyDown(keyCode, event);
    }

    //壁纸获取成功
    @Override
    public void getWallPaperResult(WallPaperResponse response) {
        if (response.getCode() == 0) {
            if (response.getRes().getVertical() != null){
                IMG_URL = response.getRes().getVertical().get((int) (Math.random() * response.getRes().getVertical().size())).getWp();
                loadingBg();
            }
        }else{
            loadingBg();
            Log.e(TAG, "getWallPaperResult: "+response.getMsg());
        }
    }

    //加载背景
    private void loadingBg() {
        try {
            Glide.with(this).load(IMG_URL).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        img.setBackground(resource);
                    }else{
                        //g.e("TAG", "onResourceReady: "+);
                        img.setBackground(resource);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //壁纸获取失败
    @Override
    public void getDataFailed(String e) {
        loadingBg();
    }
}