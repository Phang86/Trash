package com.hnhy.trash.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hnhy.trash.R;
import com.llw.mvplibrary.base.BaseActivity;
import com.llw.mvplibrary.network.utils.StatusBarUtil;

public class WelcomeActivity extends BaseActivity {

    private RelativeLayout relativeLayout;
    private int time = 5;
    private CardView cv;
    private TextView tv;
    private final String IMG_URL = "https://api.dujin.org/bing/m.php";

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    private void initView() {
        relativeLayout = findViewById(R.id.welcome_bg);
        tv = findViewById(R.id.tv);
        cv = findViewById(R.id.cardview);
        try {
            Glide.with(this).load(IMG_URL).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        relativeLayout.setBackground(resource);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(this);
        initView();
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            cv.setVisibility(View.VISIBLE);
            time--;
            tv.setText(getString(R.string.skip)+"\t"+time);
            handler.postDelayed(this, 1000);
            if (time == 0){
                goToActivity();
            }
        }
    };

    private void goToActivity() {
        //handler.removeCallbacksAndMessages(null);
        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        finish();
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
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            handler.removeCallbacksAndMessages(null);
        }
        return super.onKeyDown(keyCode, event);
    }
}