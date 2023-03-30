package com.hnhy.trash.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hnhy.trash.R;
import com.hnhy.trash.model.History;

import java.util.List;

public class HistoryAdapter extends BaseQuickAdapter<History, BaseViewHolder> {

    public HistoryAdapter(int layoutResId, @Nullable List<History> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, History item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_datetime,item.getDateTime())
                .setText(R.id.tv_explain, item.getExplain())
                .addOnClickListener(R.id.btn_delete);
        TextView tvType = helper.getView(R.id.tv_type);
        switch (item.getType()) {
            case 0:
                tvType.setText(mContext.getString(R.string.recyclables));
                break;
            case 1:
                tvType.setText(mContext.getString(R.string.hazardous_waste));
                break;
            case 2:
                tvType.setText(mContext.getString(R.string.kitchen_garbage));
                break;
            case 3:
                //干垃圾即其他垃圾
                tvType.setText(mContext.getString(R.string.dry_refuse));
                break;
            default:
                tvType.setText(mContext.getString(R.string.recyclables));
                break;
        }
    }
}
