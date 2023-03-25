package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.SearchGoodsAdapter;
import com.hnhy.trash.contract.TextContract;
import com.hnhy.trash.model.TrashResponse;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.view.PhEditText;
import com.llw.mvplibrary.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

public class TextInputActivity extends MvpActivity<TextContract.TextPresenter> implements TextContract.TextView {

    //private final String URL = "https://apis.tianapi.com/lajifenlei/index?key=13dbd95e28f9db3d84286971ca8c9e4f&word=";
    private static final String TAG = "TextInputActivity";
    private PhEditText etGoods;//输入框
    private MaterialToolbar toolbar;
    //    private ImageView ivClear;//清空输入框
    private RecyclerView rvResult;//结果显示列表
    private List<TrashResponse.ResultBean.ListBean> newslistBeanList = new ArrayList<>();//数据列表
    private SearchGoodsAdapter searchGoodsAdapter;//结果列表适配器
    private RelativeLayout nodatalayout;

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    /**
     * 页面初始化
     */
    private void initView() {
        etGoods = findViewById(R.id.et_goods);
//        ivClear = findViewById(R.id.iv_clear);
        rvResult = findViewById(R.id.rv_result);
        toolbar = findViewById(R.id.toolbar);
        nodatalayout = findViewById(R.id.nodata_layout);
        //返回
        back(toolbar,true);
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

        //设置动作监听
        etGoods.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String word = etGoods.getText().toString().trim();
                if (word.isEmpty()) {
                    showMsg(getString(R.string.enter_item_name));
                } else {
                    //显示加载弹窗
                    showLoadingDialog();
                    //控制输入法
                    controlInputMethod();
                    //请求接口
                    mPresenter.searchGoods(word);
                }
                return true;
            }
            return false;
        });

    }

    /**
     * 控制输入法
     * 当输入法打开时关闭，关闭时弹出
     */
    private void controlInputMethod() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_text_input;
    }

    @Override
    protected TextContract.TextPresenter createPresenter() {
        return new TextContract.TextPresenter();
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
            Log.e(TAG, "getSearchResponse: "+response.toString());
            //请求成功  进行数据的渲染
            if (response.getResult().getList() != null && response.getResult().getList().size() > 0) {
                nodatalayout.setVisibility(View.GONE);
                newslistBeanList.clear();
                newslistBeanList.addAll(response.getResult().getList());
                //刷新适配器
                searchGoodsAdapter.notifyDataSetChanged();
            } else {
                newslistBeanList.clear();
                searchGoodsAdapter.notifyDataSetChanged();
                nodatalayout.setVisibility(View.VISIBLE);
                showMsg(getString(R.string.knowledge_blind_spot));
            }
        } else {
            //显示请求接口失败的原因
            showMsg(response.getMsg());
            if (!newslistBeanList.isEmpty()){
                newslistBeanList.clear();
                searchGoodsAdapter.notifyDataSetChanged();
            }
            nodatalayout.setVisibility(View.VISIBLE);
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

}