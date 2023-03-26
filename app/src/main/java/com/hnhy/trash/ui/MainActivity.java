package com.hnhy.trash.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import com.hnhy.trash.R;
import com.hnhy.trash.utils.Constant;
import com.hnhy.trash.utils.LanguageUtil;
import com.hnhy.trash.utils.SpUserUtils;
import com.llw.mvplibrary.base.BaseActivity;

import java.util.Locale;

import butterknife.internal.Utils;


public class MainActivity extends BaseActivity {

    private long exitTime = 0;
    private String lan;
    private int position;

    @Override
    public void initData(Bundle savedInstanceState) {

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
        alertChangeLanguageDialog();
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
                            break;
                        case 1:
                            //繁体中文
                            showSaveLanguage("zh_rTW");
                            break;
                        case 2:
                            //英文
                            showSaveLanguage("en");
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

    /**
     * 保存设置的语言
     */
    private void showSaveLanguage(String language){
        //设置的语言、重启的类一般为应用主入口（微信也是到首页）
        LanguageUtil.changeAppLanguage(this, language, MainActivity.class,true);
        //保存设置的语言
        SpUserUtils.putString(this, Constant.LANGUAGE_KEY, language);
    }


}