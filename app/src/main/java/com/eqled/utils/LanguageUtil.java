package com.eqled.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.eqled.eqledcontrol.MainActivity;

import java.util.Locale;

/**
 * Created by Administrator on 2016/6/23.
 *
 *  语言切换管理类   如果系统语言发现改变，可能导致bug
 */
public class LanguageUtil {
   /*
	  app内部切换语言
	 */

    public  static  void swithLanguage(Activity at, String language) {
       /* Resources resources = at.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, displayMetrics);*/
        if (language.equals("en")) {
            saveData(at,"en");
          //  setLanguage(at.getApplication(),language);

        } else {
            saveData(at,"zh-rCN");
         //   setLanguage(at.getApplication(),language);
        }
        at.finish();
        Intent intent = new Intent(at,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        at.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    /*
	  启动设置语言
	 */
    public  static  void setLanguage(Application at, String language) {
        Resources resources = at.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (language.equals("en")) {
            configuration.locale = Locale.ENGLISH;
        } else {
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(configuration, displayMetrics);


    }

    /*
      保存用户选择的语言
     */

    public   static  void  saveData(Context context,String language){
        SharedPreferences sharedPreferences =context.getSharedPreferences("language_setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.commit();// 提交修改
    }

     /*
        读取语言设置
     */

    public   static  String  loadData(Context context){
        SharedPreferences sharedPreferences =context.getSharedPreferences("language_setting", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "");

        return  language;
    }


    /**
     * 是否是设置值
     *
     * @return 是否是设置值  比对设置的语言值和 系统的值是否一致
     */
    public static boolean isSetValue(Activity context,String language) {
        Locale currentLocale = context.getResources().getConfiguration().locale;
        return currentLocale.equals(language);
    }
}


