package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.WallPaperAdapter;
import com.hnhy.trash.model.WallPaper;
import com.llw.mvplibrary.base.BaseActivity;
import com.llw.mvplibrary.network.utils.DateUtil;
import com.llw.mvplibrary.network.utils.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;

    private MaterialButton btnSettingWallpaper;

    private MaterialButton btnDownload;

    private ViewPager2 vp;

    private RxPermissions rxPermissions;

    List<WallPaper> mList = new ArrayList<>();
    WallPaperAdapter mAdapter;
    String wallpaperUrl = null;

    private int position;
    private Bitmap bitmap;


    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    private void initView(){
        //透明状态栏
        StatusBarUtil.transparencyBar(context);
        ivBack = findViewById(R.id.iv_back);
        btnSettingWallpaper = findViewById(R.id.btn_setting_wallpaper);
        btnDownload = findViewById(R.id.btn_download);
        vp = findViewById(R.id.vp);
        ivBack.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnSettingWallpaper.setOnClickListener(this);
        rxPermissions = new RxPermissions(this);

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();

        //获取位置
        position = getIntent().getIntExtra("position", 0);
        //获取数据
        mList = LitePal.findAll(WallPaper.class);
        Log.d("list-->", "" + mList.size());
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getImgUrl().equals("")) {
                    mList.remove(i);
                }
            }
        }
        Log.d("list-->", "" + mList.size());

        //RecyclerView实现方式
        mAdapter = new WallPaperAdapter(R.layout.item_image_list, mList);

        Log.d("wallPaper", new Gson().toJson(mList));

        //ViewPager2实现方式
        vp.setAdapter(mAdapter);

        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d("position-->", "" + position);
                wallpaperUrl = mList.get(position).getImgUrl();
                bitmap = getBitMap(wallpaperUrl);
            }
        });

        mAdapter.notifyDataSetChanged();
        vp.setCurrentItem(position, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_download:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rxPermissions.request(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(grant -> {
                                if (grant) {
                                    //获得权限
                                    saveImageToGallery(this,bitmap);
                                } else {
                                    showMsg("请前往设置打开存储权限");
                                }
                            });
                }else{
                    saveImageToGallery(this,bitmap);
                }

                break;
            case R.id.btn_setting_wallpaper:
                showMsg("你个吊毛，点我也没用，哈哈哈哈哈哈哈");
                break;
            default:
                break;
        }
    }

    /**
     * 壁纸适配器
     */
    public class WallPaperAdapter extends BaseQuickAdapter<WallPaper, BaseViewHolder> {

        public WallPaperAdapter(int layoutResId,  List<WallPaper> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WallPaper item) {
            ImageView imageView = helper.getView(R.id.wallpaper);
            Glide.with(mContext).load(item.getImgUrl()).into(imageView);
        }
    }

    /**
     * 保存图片到本地相册
     *
     * @param context 上下文
     * @param bitmap  bitmap
     * @return
     */
//    public void saveImage(Context context, Bitmap bitmap) {
//        boolean isSaveSuccess = saveImageToGallery(context,bitmap);
//        if (isSaveSuccess) {
//            String str = getString(R.string.save_wallpaper);
//            String content = String.format(str, filePath);
//            showMsg(content);
//        } else {
//            showMsg(getString(R.string.save_wallpaper_failure));
//        }
//    }

    /**
     * 保存图片到本地相册
     *
     * @param context 上下文
     * @param bitmap  bitmap
     * @return
     */
    public boolean saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "trash_wallpaper";
        File appDir = new File(filePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String nowTime = DateUtil.getDateTime();
        String fileName = "wallpaper_" +nowTime+"_"+ 1024 + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                String str = getString(R.string.save_wallpaper);
                String content = String.format(str, file);
                showMsg(content);
                return true;
            } else {
                showMsg("图片保存失败");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        showMsg("图片保存失败");
        return false;
    }

    /**
     * Url转Bitmap
     *
     * @param url
     * @return
     */
    public Bitmap getBitMap(final String url) {
        //新启一个线程进行转换
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;

    }

}