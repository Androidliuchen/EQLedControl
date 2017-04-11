package com.eqled.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/6/20.
 * <p>
 * <p>
 * 使用简单存储来保存节目自动命名的时的 item 值
 */
public class ProgramNameItemManager {

    // 存储sharedpreferences
    public static void setSharedPreference(Context context, int program_name_count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("program_name_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("program_name_count", program_name_count);


        editor.commit();// 提交修改
    }

    // 清除sharedpreferences的数据
    public static void removeSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("program_name_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("program_name_count");
        editor.commit();// 提交修改
    }

    // 获得sharedpreferences的数据
    public static int getSahrePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("program_name_count", Context.MODE_PRIVATE);
        int password = sharedPreferences.getInt("program_name_count", 1);

        return password;
    }


}
