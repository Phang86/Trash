package com.hnhy.trash.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hnhy.trash.R;
import com.hnhy.trash.model.TrashResponse;

import java.util.List;
/**
 * 搜索物品结果列表适配器
 */
public class SearchGoodsAdapter extends BaseQuickAdapter<TrashResponse.ResultBean.ListBean, BaseViewHolder> {

    private Context context;

    public SearchGoodsAdapter(Context context, int layoutResId, List<TrashResponse.ResultBean.ListBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TrashResponse.ResultBean.ListBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_explain, item.getExplain())
                .addOnClickListener(R.id.item_search_goods);
        TextView tvType = helper.getView(R.id.tv_type);
        switch (item.getType()) {
            case 0:
                //可回收垃圾
                tvType.setText(context.getString(R.string.recyclables));
                break;
            case 1:
                //有害垃圾
                tvType.setText(context.getString(R.string.hazardous_waste));
                break;
            case 2:
                //厨余垃圾
                tvType.setText(context.getString(R.string.kitchen_garbage));
                break;
            case 3:
                //干垃圾即其他垃圾
                tvType.setText(context.getString(R.string.dry_refuse));
                break;
            default:
                break;
        }
    }
}
