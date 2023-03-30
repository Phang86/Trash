package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.HistoryAdapter;
import com.hnhy.trash.model.History;
import com.hnhy.trash.utils.HistoryHelper;
import com.llw.mvplibrary.base.BaseActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity {

    //工具栏
    private Toolbar toolbar;
    //空数据布局
    private RelativeLayout layEmptyData;
    //历史列表
    private RecyclerView rvHistory;
    //适配器
    private HistoryAdapter mAdapter;
    //历史数据列表
    private List<History> mList;
    //全刪按鈕
    private TextView tvDelAll;

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    /**
     * 页面初始化
     */
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        //设置页面状态栏
//        setStatubar(this, R.color.white, true);
        back(toolbar,false);
        layEmptyData = findViewById(R.id.nodata_layout);
        rvHistory = findViewById(R.id.rv_history);
        tvDelAll = findViewById(R.id.tv_all_delete);

        mList = new ArrayList<>();
        //设置列表的数据
        mAdapter = new HistoryAdapter(R.layout.item_history_rv, mList);
        rvHistory.setLayoutManager(new LinearLayoutManager(context));
        rvHistory.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //删除数据
                HistoryHelper.deleteHistoryById(mList.get(position).getId());
                showListData();
            }
        });
    }

    /**
     * 显示列表
     */
    private void showListData() {
        List<History> historyList = HistoryHelper.queryAllHistory();
        if (historyList.size() > 0) {
            //设置列表的数据
            mList.clear();
            mList.addAll(historyList);
            mAdapter.notifyDataSetChanged();
            layEmptyData.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            tvDelAll.setVisibility(View.VISIBLE);
        } else {
            //隐藏列表
            layEmptyData.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
            tvDelAll.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        showListData();
    }

    //全删按钮
    public void deleteAll(View view) {
        HistoryHelper.deleteAllHistory();
        showListData();
    }
}