package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.SearchGoodsAdapter;
import com.hnhy.trash.contract.TextContract;
import com.hnhy.trash.model.TrashResponse;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.utils.HistoryHelper;
import com.hnhy.trash.utils.SpeechUtil;
import com.llw.mvplibrary.base.BaseActivity;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class VoiceInputActivity extends MvpActivity<TextContract.TextPresenter> implements TextContract.TextView {

    private static final String TAG = "VoiceInputActivity";
    private MaterialToolbar toolbar;//工具栏
    private RxPermissions rxPermissions;//异步权限请求框架
    private List<TrashResponse.ResultBean.ListBean> newslistBeanList = new ArrayList<>();//数据列表
    private SearchGoodsAdapter searchGoodsAdapter;//结果列表适配器
    private RecyclerView rvResult;//结果列表
    private RelativeLayout relativeLayout;
    private String word;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_input;
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        back(toolbar,true);
        rxPermissions = new RxPermissions(this);
        rvResult = findViewById(R.id.rv_result);
        relativeLayout = findViewById(R.id.nodata_layout);
        //初始化语音
        SpeechUtil.init(this);
        //配置适配器  设置布局和数据源
        searchGoodsAdapter = new SearchGoodsAdapter(this,R.layout.item_search_rv, newslistBeanList);
        //设置列表的布局管理器
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        //列表item点击事件
        searchGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            showMsg("点击了" + newslistBeanList.get(position).getName());
        });
        //设置列表适配器
        rvResult.setAdapter(searchGoodsAdapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    /**
     * 请求权限
     */
    @SuppressLint("CheckResult")
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Android6.0及以上版本
            rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(granted -> {
                if (granted) {//权限通过
                    //开始语音监听
                    startVoiceListener();
                } else {//权限未通过
                    showMsg(getString(R.string.permission_no_pass));
                }
            });
            return;
        }
        //Android6.0以下无须动态请求
        //开始语音监听
        startVoiceListener();
    }

    /**
     * 开始语音监听
     */
    private void startVoiceListener() {
        SpeechUtil.startDictation(goodsName -> {
            if (goodsName.isEmpty()) {
                return;
            }
            //判断字符串是否包含句号
            if (goodsName.contains("。")) {
                return;
            }
            //请求接口搜索物品的垃圾分类
            showMsg(getString(R.string.item_being_searched) + goodsName);
            //语音识别结果赋值
            word = goodsName;
            mPresenter.searchGoods(goodsName);
        });
    }


    public void voiceInput(View view) {
        //请求权限
        requestPermission();
    }

    /**
     * 搜索物品返回数据
     *
     * @param response
     */
    @Override
    public void getSearchResponse(TrashResponse response) {
        //隐藏加载弹窗
        hideLoadingDialog();
        if (response.getCode() == Constant.SUCCESS_CODE) {
            //请求成功  进行数据的渲染
            if (response.getResult().getList() != null && response.getResult().getList().size() > 0) {
                relativeLayout.setVisibility(View.GONE);
                newslistBeanList.clear();
                newslistBeanList.addAll(response.getResult().getList());
                //刷新适配器
                searchGoodsAdapter.notifyDataSetChanged();
                //保存到历史记录里面
                HistoryHelper.saveHistory(response.getResult().getList(), word);
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
                showMsg(getString(R.string.knowledge_blind_spot));
                newslistBeanList.clear();
                searchGoodsAdapter.notifyDataSetChanged();
            }
        } else {
            //显示请求接口失败的原因
            showMsg(response.getMsg());
            relativeLayout.setVisibility(View.VISIBLE);
            newslistBeanList.clear();
            searchGoodsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 搜索物品失败返回
     *
     * @param throwable 异常信息
     */
    @Override
    public void getSearchResponseFailed(Throwable throwable) {
        hideLoadingDialog();
        Log.e(TAG, throwable.toString());
    }

    @Override
    protected TextContract.TextPresenter createPresenter() {
        return new TextContract.TextPresenter();
    }
}