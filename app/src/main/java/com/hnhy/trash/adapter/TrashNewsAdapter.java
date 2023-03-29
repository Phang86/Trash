package com.hnhy.trash.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hnhy.trash.R;
import com.hnhy.trash.model.TrashNewsResponse;

import java.util.List;

public class TrashNewsAdapter extends BaseQuickAdapter<TrashNewsResponse.ResultBean.NewslistBean, BaseViewHolder> {
    public TrashNewsAdapter(int layoutResId, @Nullable List<TrashNewsResponse.ResultBean.NewslistBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrashNewsResponse.ResultBean.NewslistBean item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_description, item.getDescription())
                .addOnClickListener(R.id.item_trash_news);
    }
}
