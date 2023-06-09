package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hnhy.trash.R;
import com.hnhy.trash.adapter.DiscernResultAdapter;
import com.hnhy.trash.adapter.SearchGoodsAdapter;
import com.hnhy.trash.contract.ImageContract;
import com.hnhy.trash.model.GetDiscernResultResponse;
import com.hnhy.trash.model.GetTokenResponse;
import com.hnhy.trash.model.TrashResponse;
import com.hnhy.trash.utils.Base64Util;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.utils.FileUtil;
import com.hnhy.trash.utils.HistoryHelper;
import com.hnhy.trash.utils.SPUtils;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ImageInputActivity extends MvpActivity<ImageContract.ImagePresenter> implements ImageContract.ImageView, View.OnClickListener {

    private static final String TAG = "ImageInputActivity";
    /**
     * 打开相册
     */
    private static final int OPEN_ALBUM_CODE = 100;
    /**
     * 打开相机
     */
    private static final int TAKE_PHOTO_CODE = 101;
    /**
     * 鉴权Toeken
     */
    private String accessToken;

    private Toolbar toolbar;
    private ImageView ivPicture;
    private EditText etImageUrl;
    private LinearLayout layRecognitionResult,layClassificationResult;
    private RecyclerView rvRecognitionResult,rvClassificationResult;
    private RxPermissions rxPermissions;
    private NestedScrollView nestedScrollView;
    private String word;//输入的物品


    private File outputImage;


    @Override
    public int getLayoutId() {
        return R.layout.activity_image_input;
    }

    @Override
    protected ImageContract.ImagePresenter createPresenter() {
        return new ImageContract.ImagePresenter();
    }

    /**
     * 初始化
     */
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        ivPicture = findViewById(R.id.iv_picture);
        etImageUrl = findViewById(R.id.et_image_url);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        findViewById(R.id.btn_web_picture).setOnClickListener(this);
        findViewById(R.id.btn_open_album).setOnClickListener(this);
        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        layRecognitionResult = findViewById(R.id.lay_recognition_result);
        layClassificationResult = findViewById(R.id.lay_classification_result);
        rvRecognitionResult = findViewById(R.id.rv_recognition_result);
        rvClassificationResult = findViewById(R.id.rv_classification_result);
//        //设置页面状态栏
//        setStatubar(this, R.color.white, true);
        back(toolbar, true);
        rxPermissions = new RxPermissions(this);
    }

    /**
     * 滑动到屏幕底部
     */
    private void scrollToEnd() {
        nestedScrollView.post(() -> {
            nestedScrollView.fullScroll(View.FOCUS_DOWN);//滚到底部
            //nestedScrollView.fullScroll(ScrollView.FOCUS_UP);//滚到顶部
        });
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public void onClick(View v) {
        if (!hasNetwork()) {
            showMsg(getString(R.string.open_network));
            return;
        }
        switch (v.getId()) {
            case R.id.btn_web_picture://网络图片
                etImageUrl.setVisibility(View.VISIBLE);
                etImageUrl.setOnKeyListener((view, keyCode, keyEvent) -> {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        String webImageUrl = etImageUrl.getText().toString().trim();
                        //http://imgsrc.baidu.com/baike/pic/item/91ef76c6a7efce1b27893518a451f3deb58f6546.jpg
                        //https://api.dujin.org/bing/1920.php
                        //https://api.dujin.org/bing/m.php
                        //https://api.isoyu.com/mm_images.php
                        String defaultWebImageUrl = "http://power-api.cretinzp.com:8000/girls/581/bkgzkinjn1wpikfe.jpg";
                        String imageUrl = "".equals(webImageUrl) ? defaultWebImageUrl : webImageUrl;
                        //识别网络图片Url
                        showLoadingDialog();
                        Glide.with(context).load(imageUrl).into(ivPicture);
                        mPresenter.getDiscernResult(accessToken,null,imageUrl);
                        etImageUrl.setVisibility(View.GONE);
//                        Log.e(TAG, "搜索地址為: "+imageUrl);
                    }
                    return false;
                });
                break;
            case R.id.btn_open_album://相册图片
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rxPermissions.request(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(grant -> {
                                if (grant) {
                                    //获得权限
                                    openAlbum();
                                } else {
                                    showMsg(getString(R.string.not_camera));
                                }
                            });
                } else {
                    openAlbum();
                }
                break;
            case R.id.btn_take_photo://拍照图片
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rxPermissions.request(
                                    Manifest.permission.CAMERA)
                            .subscribe(grant -> {
                                if (grant) {
                                    //获得权限
                                    turnOnCamera();
                                } else {
                                    showMsg(getString(R.string.not_camera));
                                }
                            });
                } else {
                    turnOnCamera();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 打开相机
     */
    private void turnOnCamera() {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        //创建File对象
        outputImage = new File(getExternalCacheDir(), "takePhoto" + filename + ".jpg");
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this,
                    "com.hnhy.trash.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //打开相机
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }


    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showLoadingDialog();
            if (requestCode == OPEN_ALBUM_CODE) {
                //打开相册返回
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                final Uri imageUri = Objects.requireNonNull(data).getData();
                Cursor cursor = getContentResolver().query(imageUri, filePathColumns, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                //获取图片路径
                String imagePath = cursor.getString(columnIndex);
//                Log.e(TAG, " 圖片路徑："+imagePath);
                cursor.close();
                //识别
                localImageDiscern(imagePath);
            }else if (requestCode == TAKE_PHOTO_CODE) {
                //拍照返回
                String imagePath = outputImage.getAbsolutePath();
                //识别
                localImageDiscern(imagePath);
            }

        } else {
            showMsg(getString(R.string.nothing));
        }
    }

    /**
     * 本地图片识别
     */
    private void localImageDiscern(String imagePath) {
        try {
            String token = getAccessToken();
            //通过图片路径显示图片
            Glide.with(this).load(imagePath).into(ivPicture);
            //按字节读取文件
            byte[] imgData = FileUtil.readFileByBytes(imagePath);
            //字节转Base64
            String imageBase64 = Base64Util.encode(imgData);
            Log.e(TAG, "imageBase64: "+imageBase64);
            //本地图片识别
            mPresenter.getDiscernResult(token, imageBase64, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取鉴权Token
     */
    private String getAccessToken() {
        String token = SPUtils.getString(Constant.TOKEN, null, this);
        if (token == null) {
            //访问API获取接口
            mPresenter.getToken();
        } else {
            //则判断Token是否过期
            if (isTokenExpired()) {
                //过期
                mPresenter.getToken();
            } else {
                accessToken = token;
            }
        }
        return accessToken;
    }

    /**
     * Token是否过期
     *
     * @return
     */
    private boolean isTokenExpired() {
        //获取Token的时间
        long getTokenTime = SPUtils.getLong(Constant.GET_TOKEN_TIME, 0, this);
        //获取Token的有效时间
        long effectiveTime = SPUtils.getLong(Constant.TOKEN_VALID_PERIOD, 0, this);
        //获取当前系统时间
        long currentTime = System.currentTimeMillis() / 1000;

        return (currentTime - getTokenTime) >= effectiveTime;
    }



    /**
     * 获取鉴权Token成功返回
     * @param response GetTokenResponse
     */
    @Override
    public void getTokenResponse(GetTokenResponse response) {
        if (response != null){
            accessToken = response.getAccess_token();
            long expiresIn = response.getExpires_in();
            long currentTimeMillis = System.currentTimeMillis() / 1000;
            //放入缓存
            SPUtils.putString(Constant.TOKEN, accessToken, this);
            SPUtils.putLong(Constant.GET_TOKEN_TIME, currentTimeMillis, this);
            SPUtils.putLong(Constant.TOKEN_VALID_PERIOD, expiresIn, this);
            Log.e("TAG", "getTokenResponse: 獲取token成功"+response.toString());
        }else{
            showMsg(getString(R.string.token_isEmpty));
        }
    }

    /**
     * 获取Token失败返回
     * @param throwable 异常
     */
    @Override
    public void getTokenFailed(Throwable throwable) {
        Log.e(TAG, "Token獲取失敗: "+throwable.toString());
    }

    /**
     * 图片识别成功返回
     * @param response GetDiscernResultResponse
     */
    @Override
    public void getDiscernResultResponse(GetDiscernResultResponse response) {
        if(response == null){
            hideLoadingDialog();
            showMsg(getString(R.string.not_result));
            return;
        }
        ivPicture.setVisibility(View.VISIBLE);
        List<GetDiscernResultResponse.ResultBean> result = response.getResult();
        if (result != null && result.size() > 0) {
            //显示识别结果
            showDiscernResult(result);
        } else {
            hideLoadingDialog();
            showMsg(getString(R.string.not_result));
        }
    }

    /**
     * 显示识别的结果列表
     *
     * @param result
     */
    private void showDiscernResult(List<GetDiscernResultResponse.ResultBean> result) {
        DiscernResultAdapter adapter = new DiscernResultAdapter(R.layout.item_distinguish_result_rv, result);
        //添加列表Item点击
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            showLoadingDialog();
            word = result.get(position).getKeyword();
            //垃圾分类
            mPresenter.searchGoods(word);
        });
        rvRecognitionResult.setLayoutManager(new LinearLayoutManager(this));
        rvRecognitionResult.setAdapter(adapter);
        //隐藏加载
        hideLoadingDialog();
        //显示弹窗
        layRecognitionResult.setVisibility(View.VISIBLE);
        layClassificationResult.setVisibility(View.GONE);
        //滑动到屏幕底部
        scrollToEnd();

    }


    /**
     * 图片识别成功失败
     * @param throwable 异常
     */
    @Override
    public void getDiscernResultFailed(Throwable throwable) {
        Log.e(TAG, "圖片識別失敗: "+throwable.toString());
    }

    /**
     * 搜索物品进行垃圾分类成功返回
     *
     * @param response TrashResponse
     */
    @Override
    public void getSearchResponse(TrashResponse response) {
        //请求成功  进行数据的渲染
        if (response.getCode() == Constant.SUCCESS_CODE) {
            List<TrashResponse.ResultBean.ListBean> result = response.getResult().getList();
            if (result != null && result.size() > 0) {
                //显示分类结果
                showClassificationResult(result);
                //保存到历史记录里
                HistoryHelper.saveHistory(response.getResult().getList(), word);
            } else {
                showMsg(getString(R.string.knowledge_blind_spot));
            }
        } else {
            hideLoadingDialog();
            showMsg(response.getMsg());
        }
    }

    @Override
    public void getSearchResponseFailed(Throwable throwable) {
        Log.d(TAG, "搜索物品进行垃圾分类失败：" + throwable.toString());
    }

    /**
     * 显示物品分类结果
     * @param result
     */
    private void showClassificationResult(List<TrashResponse.ResultBean.ListBean> result) {
        SearchGoodsAdapter adapter = new SearchGoodsAdapter(ImageInputActivity.this,R.layout.item_search_rv,result);
        rvClassificationResult.setLayoutManager(new LinearLayoutManager(context));
        rvClassificationResult.setAdapter(adapter);
        //隐藏加载
        hideLoadingDialog();
        //显示弹窗
        layClassificationResult.setVisibility(View.VISIBLE);
        //滑动到屏幕底部
        scrollToEnd();
    }

}