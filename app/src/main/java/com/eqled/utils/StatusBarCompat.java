package com.eqled.utils;

import android.os.Build;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.eqled.eqledcontrol.R;

/**
 * Created by Administrator on 2016/5/23.
 */
public class StatusBarCompat {


    public static void SetColor(Activity mActivity, int color) {
        final int sdk = Build.VERSION.SDK_INT;
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        if (sdk >= Build.VERSION_CODES.KITKAT) {
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            // 设置透明状态栏
            if ((params.flags & bits) == 0) {
                params.flags |= bits;
                window.setAttributes(params);
            }

            // 设置状态栏颜色
            ViewGroup contentLayout = (ViewGroup) mActivity.findViewById(android.R.id.content);
            setupStatusBarView(mActivity, contentLayout, color);

            // 设置Activity layout的fitsSystemWindows
            View contentChild = contentLayout.getChildAt(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                contentChild.setFitsSystemWindows(true);
            }
        }

    }

    public static void setStatusBarColor(Activity at, int color, int id) {


    }


    public static View mStatusBarView;

    private static void setupStatusBarView(Activity mActivity, ViewGroup contentLayout, int color) {
        if (mStatusBarView == null) {
            View statusBarView = new View(mActivity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(mActivity));
            contentLayout.addView(statusBarView, lp);
            mStatusBarView = statusBarView;
        }

        mStatusBarView.setBackgroundColor(color);
    }

    /**
     * 获得状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);

    }

}