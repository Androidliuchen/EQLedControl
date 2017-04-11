package com.eqled.custom;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.eqled.eqledcontrol.R;
import com.eqled.utils.InterfaceClick;
import com.eqled.utils.Utils;

/**
 * Created by hyman on 2016/5/25.
 */
public class CustomPopWindow extends PopupWindow {
    private Activity at;
    private int id;   //控件的id。。位置xx控件之上下左右
    private InterfaceClick onClickListener;

    public CustomPopWindow(Activity at, int id) {
        super();
        this.at = at;
        this.id = id;
    }

    public CustomPopWindow(Activity at, int id, InterfaceClick onClickListener) {
        super();
        this.at = at;
        this.id = id;
        this.onClickListener = onClickListener;
    }

    /*
      设置自定义布局
         ui主体，自定义布局，显示的宽高度百分比，
     */
    public void setView(View contentView, float width, float height) {

        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        int w = (int) (Utils.getUiwidth(at) * width);
        int h = (int) (Utils.getUiheight(at) * height);
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                if (onClickListener != null) {
                    onClickListener.Click();
                }
            }
        });
        // this.update();
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 设置SelectPicPopupWindow弹出窗体动画效果
    }

    public void show() {
        //显示在main的底部
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(at.findViewById(id), Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.7f);
        } else {
            this.dismiss();
        }


    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = at.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        at.getWindow().setAttributes(lp);
    }


}
