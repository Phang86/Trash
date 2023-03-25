package com.hnhy.trash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.hnhy.trash.R;
import com.llw.mvplibrary.base.BaseActivity;


public class MainActivity extends BaseActivity {

    private long exitTime = 0;

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

}