package com.hnhy.trash.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.hnhy.trash.R;
import com.llw.mvplibrary.base.BaseActivity;

public class VoiceInputActivity extends BaseActivity {

    private MaterialToolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_input;
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        back(toolbar,true);

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }


    public void voiceInput(View view) {

    }
}