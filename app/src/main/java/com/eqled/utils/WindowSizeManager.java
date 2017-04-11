package com.eqled.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eqled.eqledcontrol.R;

/**
 * Created by Administrator on 2016/6/29.
 * 窗体大小管理器
 */
public class WindowSizeManager {
    public static final String KEY = "window_size_manager";
    private int windowWidth;//窗口宽度
    private int windowHeight; //窗口高度

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    // 存储sharedpreferences
    public static void setSharedPreference(Context context, int windowWidth, int windowHeight) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("window_width", windowWidth);
        editor.putInt("window_height", windowHeight);

        editor.commit();// 提交修改
    }

    // 清除sharedpreferences的数据
    public static void removeSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY);
        editor.commit();// 提交修改
    }

    // 获得sharedpreferences的数据
    public static WindowSizeManager getSahrePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        //  Log.d("屏幕宽度列表总长度",""+sharedPreferences.getInt("window_height",3));
        WindowSizeManager windowSizeManager = new WindowSizeManager();
        windowSizeManager.setWindowWidth(
                Integer.parseInt(
                        context.getResources().getStringArray(R.array.scrn_width)
                                [sharedPreferences.getInt("window_width", 3)].
                                toString()))
        ;
        windowSizeManager.setWindowHeight(
                Integer.parseInt(
                        context.getResources().getStringArray(R.array.scrn_hight)
                                [sharedPreferences.getInt("window_height", 3)].
                                toString()))
        ;

        return windowSizeManager;
    }

    // 获得窗口宽高在数组里的位置
    public static WindowSizeManager getSahrePreferencePosition(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        WindowSizeManager windowSizeManager = new WindowSizeManager();
        windowSizeManager.setWindowWidth(sharedPreferences.getInt("window_width", 3));
        windowSizeManager.setWindowHeight(sharedPreferences.getInt("window_height", 3));
        return windowSizeManager;
    }


}
