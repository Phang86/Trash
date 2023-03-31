package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.WallPaperAdapter;
import com.hnhy.trash.contract.WallPaperContract;
import com.hnhy.trash.model.PhotoResponse;
import com.hnhy.trash.model.WallPaper;
import com.hnhy.trash.model.WallPaperResponse;
import com.hnhy.trash.utils.Constant;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.llw.mvplibrary.network.utils.StatusBarUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class WallPaperActivity extends MvpActivity<WallPaperContract.WallPaperPresenter> implements WallPaperContract.WallPaperView {

    /**
     * 标题
     */
    private Toolbar toolbar;
    /**
     * 数据列表
     */
    private RecyclerView rv;
    /**
     * AppBarLayout布局
     */
    private AppBarLayout appbar;
    /**
     * 壁纸数据列表
     */
    private List<WallPaperResponse.ResBean.VerticalBean> mList = new ArrayList<>();
    /**
     * 壁纸数据适配器
     */
    private WallPaperAdapter mAdapter;
    /**
     * item高度列表
     */
    private List<Integer> heightList = new ArrayList<>();

    /**
     * 壁纸数量
     */
    private static final int WALLPAPER_NUM = 30;
    /**
     * 头部和底部的item数据
     */
    private WallPaperResponse.ResBean.VerticalBean topBean, bottomBean;



    @Override
    public int getLayoutId() {
        return R.layout.activity_wall_paper;
    }

    /**
     * 初始化列表数据
     */
    private void initWallPaperList() {
        StatusBarUtil.setStatusBarColor(this,R.color.white);
        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        appbar = findViewById(R.id.appbar);

        heightList.add(100);
        for (int i = 0; i < WALLPAPER_NUM; i++) {
            heightList.add(300);
        }
        heightList.add(100);

        mAdapter = new WallPaperAdapter(R.layout.item_wallpaper_list, mList, heightList);
        //瀑布流
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理
        rv.setLayoutManager(manager);
        //设置数据适配器
        rv.setAdapter(mAdapter);
        //请求数据
        mPresenter.getWallPaper();
        //获取必应壁纸
//        mPresent.biying();

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0 || position == mList.size()-1){
                    showMsg("这是广告");
                }else{
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtra("position",position-1);
                    startActivity(intent);
                }
            }
        });


    }


    @Override
    public void initData(Bundle savedInstanceState) {
        //加载弹窗
        showLoadingDialog();
        initWallPaperList();
        Back(toolbar);
    }

    @Override
    protected WallPaperContract.WallPaperPresenter createPresenter() {
        return new WallPaperContract.WallPaperPresenter();
    }

    @Override
    public void getWallPaperResult(WallPaperResponse response) {
        if (response.getRes() != null) {
            List<WallPaperResponse.ResBean.VerticalBean> data = response.getRes().getVertical();
            //创建头部和底部的两个广告item的假数据
            topBean = new WallPaperResponse.ResBean.VerticalBean();
            topBean.setDesc("top");
            topBean.setImg("");
            bottomBean = new WallPaperResponse.ResBean.VerticalBean();
            bottomBean.setDesc("bottom");
            bottomBean.setImg("");

            //数据填充
            if (data != null && data.size() > 0) {
                mList.clear();
                //添加头部
                mList.add(topBean);
                //添加主要数据
                for (int i = 0; i < data.size(); i++) {
                    mList.add(data.get(i));
                }
                //添加尾部
                mList.add(bottomBean);
                Log.d("list-->", new Gson().toJson(mList));

                //根据数据数量来刷新列表
                mAdapter.notifyItemInserted(mList.size());
                //删除数据库中的数据
                LitePal.deleteAll(WallPaper.class);
                for (int i = 0; i < mList.size(); i++) {
                    WallPaper wallPaper = new WallPaper();
                    wallPaper.setImgUrl(mList.get(i).getImg());
                    wallPaper.save();
                }
                hideLoadingDialog();
            } else {
                showMsg("壁纸数据为空");
                hideLoadingDialog();
            }
        } else {
            hideLoadingDialog();
            showMsg("未获取到壁纸数据");
        }
    }

    @Override
    public void getDataFailed(String e) {

    }

    @Override
    public void getPhotoResult(PhotoResponse response) {

    }

    @Override
    public void getPhotoFailed(String e) {

    }
}