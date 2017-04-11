package com.eqled.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.R;

/**
 * Created by Administrator on 2016/6/22.
 * 动画管理类
 */
public class AnimationManager {

    /*
    动画选择
     */
    public static Animation GetAnimation(Context context, int type) {

        switch (type) {
            case 1:
                return MoveUp(context);
            case 2:
                return MoveDown(context);
            case 3:
                return MoveLeft(context);
            case 4:
                return MoveRight(context);

            default:
                return null;

        }

    }


    /*
    上移动画
     */
    public static Animation MoveUp(Context context) {
        Animation translateAnimation = AnimationUtils.loadAnimation(context, R.anim.moveup_anim);

        // translateAnimation.setDuration(1000);//设置动画持续时间为3秒
        //translateAnimation.setRepeatCount(Integer.MAX_VALUE);

        //  translateAnimation.setInterpolator(context, android.R.anim.cycle_interpolator);//设置动画插入器

        translateAnimation.setFillAfter(true);//


        return translateAnimation;
    }

    /*
     下移动画
      */
    public static Animation MoveDown(Context context) {
        Animation translateAnimation = AnimationUtils.loadAnimation(context, R.anim.movedown_anim);

        // translateAnimation.setDuration(1000);//设置动画持续时间为3秒
        // translateAnimation.setRepeatCount(Integer.MAX_VALUE);

        //  translateAnimation.setInterpolator(context, android.R.anim.cycle_interpolator);//设置动画插入器

        //  translateAnimation.setFillAfter(true);//


        return translateAnimation;
    }

    /*
    左移动画
      */
    public static Animation MoveLeft(Context context) {
        Animation translateAnimation = AnimationUtils.loadAnimation(context, R.anim.moveleft_anim);

        // translateAnimation.setDuration(1000);//设置动画持续时间为3秒
        // translateAnimation.setRepeatCount(Integer.MAX_VALUE);

        //  translateAnimation.setInterpolator(context, android.R.anim.cycle_interpolator);//设置动画插入器

        //  translateAnimation.setFillAfter(true);//


        return translateAnimation;
    }

    /*
    右移动画
      */
    public static Animation MoveRight(Context context) {
        Animation translateAnimation = AnimationUtils.loadAnimation(context, R.anim.moveright_anim);

        // translateAnimation.setDuration(1000);//设置动画持续时间为3秒
        // translateAnimation.setRepeatCount(Integer.MAX_VALUE);

        //  translateAnimation.setInterpolator(context, android.R.anim.cycle_interpolator);//设置动画插入器

        //  translateAnimation.setFillAfter(true);//


        return translateAnimation;
    }


}