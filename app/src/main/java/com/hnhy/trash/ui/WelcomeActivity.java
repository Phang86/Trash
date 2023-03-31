package com.hnhy.trash.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hnhy.trash.R;
import com.hnhy.trash.contract.WallPaperContract;
import com.hnhy.trash.model.PhotoResponse;
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

    private ImageView img,img1,img2;
    private int time = 5;
    private CardView cv;
    private TextView tv;
    private String IMG_URL = "http://power-api.cretinzp.com:8000/girls/422/pusxk0lsmfzq8uor.jpg";
//    private String IMG_URL;
    private final String TAG = "WelcomeActivity";
//    private List<WallPaperResponse.ResBean.VerticalBean> list;
    //    private final String IMG_URL = "https://api.dujin.org/bing/1080.php";

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    private void initView() {
        //设置透明状态栏
        StatusBarUtil.transparencyBar(context);
        img = findViewById(R.id.welcome_bg);
        img1 = findViewById(R.id.welcome_bg1);
        img2 = findViewById(R.id.welcome_bg2);
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
        //mPresenter.getWallPaper();
        //获取美女壁纸
        mPresenter.getPhoto();
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
//                IMG_URL = response.getRes().getVertical().get((int) (Math.random() * response.getRes().getVertical().size())).getWp();
//                loadingBg();
            }
        }else{
//            loadingBg();
            Log.e(TAG, "getWallPaperResult: "+response.getMsg());
        }
    }

    //加载背景
    private void loadingBg(String url,ImageView img) {
        try {
            Glide.with(this).load(url).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                    int width = resource.getIntrinsicHeight();
//                    int height = resource.getIntrinsicHeight();
//                    //获取imageView的宽
//                    int imageViewWidth= img.getWidth();
//                    //计算缩放比例
//                    float sy= (float) (imageViewWidth* 0.1)/(float) (width * 0.1);
//                    //计算图片等比例放大后的高
//                    int imageViewHeight= (int) (height * sy);
//                    ViewGroup.LayoutParams params = img.getLayoutParams();
//                    params.height = imageViewHeight;
//                    img.setLayoutParams(params);

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

//        try {
//            Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    int width = resource.getWidth();
//                    int height = resource.getHeight();
//                    //获取imageView的宽
//                    int imageViewWidth= img.getWidth();
//                    //计算缩放比例
//                    float sy= (float) (imageViewWidth* 0.1)/(float) (width * 0.1);
//                    //计算图片等比例放大后的高
//                    int imageViewHeight= (int) (height * sy);
//                    ViewGroup.LayoutParams params = img.getLayoutParams();
//                    params.height = imageViewHeight;
//                    img.setLayoutParams(params);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        Drawable drawable =new BitmapDrawable(resource);
//                        img.setBackground(drawable);
//                    }else{
//                        //g.e("TAG", "onResourceReady: "+);
//                        //img.setBackground(drawable);
//                    }
//                }
//
//                @Override
//                public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                }
//
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

    //壁纸获取失败
    @Override
    public void getDataFailed(String e) {
        loadingBg(IMG_URL,img);
    }

    @Override
    public void getPhotoResult(PhotoResponse response) {
        if (response.getCode() == 1){
            if (response.getData() != null){
                Log.e(TAG, "getPhotoResult: "+response.getData().toString());
                String url1 = response.getData().get(0).getImageUrl();
                String url2 = response.getData().get(1).getImageUrl();
                String url3 = response.getData().get(2).getImageUrl();
                loadingBg(url1,img);
                loadingBg(url2,img1);
                loadingBg(url3,img2);
            }else{
                Log.e(TAG, "返回数据为空");
                loadingBg(IMG_URL,img);
            }
        }else{
            Log.e(TAG, "结果码："+response.getCode());
            loadingBg(IMG_URL,img);
        }
    }

    @Override
    public void getPhotoFailed(String e) {
        Log.e(TAG, "返回失败："+e);
        loadingBg(IMG_URL,img);
    }


}