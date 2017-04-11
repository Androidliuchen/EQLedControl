package com.eqled.eqledcontrol;

import android.app.Application;
import android.util.Log;

import com.eqled.utils.LanguageUtil;
import com.eqled.utils.StatusBarCompat;
import com.eqled.utils.Utils;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("", LanguageUtil.loadData(this));
        LanguageUtil.setLanguage(this, LanguageUtil.loadData(this));

    }
}
