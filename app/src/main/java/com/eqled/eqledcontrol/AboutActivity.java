package com.eqled.eqledcontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 关于功能模块的功能实现
 **/
public class AboutActivity extends Activity implements View.OnClickListener {
    private TextView mTextView1, mTextView2, mTextView3;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mTextView1 = (TextView) findViewById(R.id.text);
        mTextView2 = (TextView) findViewById(R.id.text_banben);
        mTextView3 = (TextView) findViewById(R.id.text_web);
        mImageView = (ImageView) findViewById(R.id.ima_btn);


        mImageView.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        //获取版本信息
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //version为当前版本号
        String version = info.versionName;
        mTextView1.setText(version);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ima_btn:
                finish();
                break;
            case R.id.text_web:
                Intent intent = new Intent(AboutActivity.this, WebActivity.class);
                startActivity(intent);
        }
    }
}
