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

import com.hnhy.trash.R;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.utils.LanguageUtil;
import com.hnhy.trash.utils.SpUserUtils;
import com.llw.mvplibrary.base.BaseActivity;


public class MainActivity extends BaseActivity implements View.OnTouchListener{

    private long exitTime = 0;
    private int position;
    private Switch swCn,swTw,swEn;
    private LinearLayout switchLayout;

    @Override
    public void initData(Bundle savedInstanceState) {
        swCn = findViewById(R.id.sw_cn);
        swTw = findViewById(R.id.sw_tw);
        swEn = findViewById(R.id.sw_en);
        switchLayout = findViewById(R.id.switch_layout);

        swCn.setOnTouchListener(this);
        swTw.setOnTouchListener(this);
        swEn.setOnTouchListener(this);

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
}