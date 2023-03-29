package com.hnhy.trash.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.TrashNewsAdapter;
import com.hnhy.trash.contract.MainContract;
import com.hnhy.trash.model.TrashNewsResponse;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.utils.LanguageUtil;
import com.hnhy.trash.utils.SpUserUtils;
import com.llw.mvplibrary.base.BaseActivity;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends MvpActivity<MainContract.MainPresenter> implements MainContract.MainView,View.OnTouchListener{

    private long exitTime = 0;
    private int position;
    private Switch swCn,swTw,swEn;
    private LinearLayout switchLayout;
    private final String TAG = "MainActivity";
    //轮播
    private Banner banner;
    private RecyclerView rvNews;
    private List<TrashNewsResponse.ResultBean.NewslistBean> mList = new ArrayList<>();
    private TrashNewsAdapter mAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;

    /**
     * 页面初始化
     */
    private void initView() {
        banner = findViewById(R.id.banner);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.appbar_layout);
        rvNews = findViewById(R.id.rv_news);
        nestedScrollView = findViewById(R.id.main_nestedScrollView);

        swCn = findViewById(R.id.sw_cn);
        swTw = findViewById(R.id.sw_tw);
        swEn = findViewById(R.id.sw_en);

        switchLayout = findViewById(R.id.switch_layout);

        swCn.setOnTouchListener(this);
        swTw.setOnTouchListener(this);
        swEn.setOnTouchListener(this);

        //伸缩偏移量监听
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//收缩时
                    collapsingToolbarLayout.setTitle(getString(R.string.garbage_sorting));
                    isShow = true;
                } else if (isShow) {//展开时
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
        //设置列表
        mAdapter = new TrashNewsAdapter(R.layout.item_trash_new_rv, mList);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //跳转到新闻详情页面
            Intent intent = new Intent(context, NewsDetailsActivity.class);
            intent.putExtra("url", mList.get(position).getUrl());
            startActivity(intent);
        });
        rvNews.setLayoutManager(new LinearLayoutManager(context));
        rvNews.setAdapter(mAdapter);
        //请求垃圾分类新闻数据
        mPresenter.getTrashNews(10);
    }

    /**
     * 显示新闻列表
     *
     * @param list
     */
    private void showList(List<TrashNewsResponse.ResultBean.NewslistBean> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initView();

        String strLanguage = SpUserUtils.getString(this, Constant.LANGUAGE_KEY);

        if (strLanguage.equals("zh")){
            swCn.setChecked(true);
            swTw.setChecked(false);
            swEn.setChecked(false);
            position = 0;
        }else if (strLanguage.equals("zh_rTW")){
            swCn.setChecked(false);
            swTw.setChecked(true);
            swEn.setChecked(false);
            position = 1;
        }else if (strLanguage.equals("en")){
            swCn.setChecked(false);
            swTw.setChecked(false);
            swEn.setChecked(true);
            position = 2;
        }

        mPresenter.getTrashNews(10);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    //文字输入
    public void jumpTextInput(View view) {
        gotoActivity(TextInputActivity.class);
    }

    //语音输入
    public void jumpVoiceInput(View view) {
        gotoActivity(VoiceInputActivity.class);
    }

    /**
     * 进入Activity
     * @param clazz 目标Activity
     */
    private void gotoActivity(Class<?> clazz) {
        startActivity(new Intent(MainActivity.this, clazz));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showMsg(getString(R.string.confirm_exit));
            exitTime = System.currentTimeMillis();
        } else {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            System.exit(0);
            finish();
        }
    }

    //中英切换
    public void changeLanguage(View view) {
        switchLayout.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String items[] = {getString(R.string.bounced_a),getString(R.string.switch_on)};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    alertChangeLanguageDialog();
                }else{
                    switchLayout.setVisibility(View.VISIBLE);
                    //自动滚动到底部
                    nestedScrollView.post(() ->{
                       nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    });
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void alertChangeLanguageDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String items[] = {getString(R.string.cn),getString(R.string.tw),getString(R.string.en)};
        String language = SpUserUtils.getString(this, Constant.LANGUAGE_KEY);
        if (language.equals("zh")){
            position = 0;
        }else if (language.equals("zh_rTW")){
            position = 1;
        }else if (language.equals("en")){
            position = 2;
        }
        alert.setTitle(getString(R.string.language_select));
        alert.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (position == which){
                    showMsg(getString(R.string.is_language));
                }else{
                    switch (which) {
                        case 0:
                            //简体中文
                            showSaveLanguage("zh");
                            syncSwitch(0);
                            break;
                        case 1:
                            //繁体中文
                            showSaveLanguage("zh_rTW");
                            syncSwitch(1);
                            break;
                        case 2:
                            //英文
                            showSaveLanguage("en");
                            syncSwitch(2);
                            break;
                        default:
                            break;
                    }
                }
                dialog.dismiss();
            }
        });
        alert.create().show();
    }

    private void syncSwitch(int position) {
        if (position == 0){
            swCn.setChecked(true);
            swTw.setChecked(false);
            swEn.setChecked(false);
        }else if (position == 1){
            swCn.setChecked(false);
            swTw.setChecked(true);
            swEn.setChecked(false);
        }else if (position == 2){
            swCn.setChecked(false);
            swTw.setChecked(false);
            swEn.setChecked(true);
        }
    }

    /**
     * 保存设置的语言
     */
    private void showSaveLanguage(String language){
        //设置的语言、重启的类一般为应用主入口（微信也是到首页）
        LanguageUtil.changeAppLanguage(this, language, MainActivity.class,true);
        //保存设置的语言
        SpUserUtils.putString(this, Constant.LANGUAGE_KEY, language);
    }


    public void switchOnclick(View view) {
        switch (view.getId()) {
            case R.id.sw_cn:
                if (position != 0) {
                    swCn.setChecked(true);
                    swTw.setChecked(false);
                    swEn.setChecked(false);
                    showSaveLanguage("zh");
                    position = 0;
                }else{
                    showMsg(getString(R.string.is_language));
                    swCn.setChecked(true);
                }
                break;
            case R.id.sw_tw:
                if (position != 1) {
                    swCn.setChecked(false);
                    swTw.setChecked(true);
                    swEn.setChecked(false);
                    showSaveLanguage("zh_rTW");
                    position = 1;
                }else{
                    showMsg(getString(R.string.is_language));
                    swTw.setChecked(true);
                }
                break;
            case R.id.sw_en:
                if (position != 2) {
                    swCn.setChecked(false);
                    swTw.setChecked(false);
                    swEn.setChecked(true);
                    showSaveLanguage("en");
                    position = 2;
                }else{
                    showMsg(getString(R.string.is_language));
                    swEn.setChecked(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    //只支持点击事件
    public boolean onTouch(View v, MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_MOVE;
    }

    public void jumpImageInput(View view) {
        gotoActivity(ImageInputActivity.class);
    }

    /**
     * 显示轮播图
     *
     * @param list
     */
    public void showBanner(List<TrashNewsResponse.ResultBean.NewslistBean> list) {
        banner.setAdapter(new BannerImageAdapter<TrashNewsResponse.ResultBean.NewslistBean>(list) {
                    @Override
                    public void onBindView(BannerImageHolder holder, TrashNewsResponse.ResultBean.NewslistBean data, int position, int size) {
                        //显示轮播图片
                        Glide.with(holder.itemView)
                                .load(data.getPicUrl())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                .into(holder.imageView);
                    }
                })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(this));
    }


    /**
     * 获取垃圾分类新闻成功返回
     *
     * @param response
     */
    @Override
    public void getTrashNewsResponse(TrashNewsResponse response) {
        if (response.getCode() == Constant.SUCCESS_CODE) {
            List<TrashNewsResponse.ResultBean.NewslistBean> list = response.getResult().getNewslist();
            if (list.size() > 0) {
                //数据显示
                showBanner(list); //轮播显示
                showList(list); //显示新闻数据
            } else {
                showMsg("垃圾分类新闻为空");
            }
        } else {
            showMsg(response.getMsg());
        }
    }

    /**
     * 获取垃圾分类新闻失败返回
     *
     * @param throwable 异常
     */
    @Override
    public void getTrashNewsFailed(Throwable throwable) {
        Log.d(TAG, "获取垃圾分类新闻失败：" + throwable.toString());
    }

    @Override
    protected MainContract.MainPresenter createPresenter() {
        return new MainContract.MainPresenter();
    }
}