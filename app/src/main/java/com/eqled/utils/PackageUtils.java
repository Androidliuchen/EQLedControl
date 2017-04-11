package com.eqled.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/10/20.
 * PackageUtils 操作PackageManager，得到清单的一些信息
 */

public class PackageUtils {
    private Context context;
    private PackageManager manager = null;
    private PackageInfo Info = null;

    public PackageUtils(Context context) {
        this.context = context;
        init();// 初始化
    }

    public void init() {
        manager = context.getPackageManager();
        try {
            Info = manager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获得版本号
    public int getVersionCode() {
        return Info.versionCode;
    }

    // 获取版本名称
    public String getVersionName() {
        return Info.versionName;
    }
    //是否更新
    public boolean isUpdate(int oldCode,int newCode){
        boolean flag = oldCode < newCode ? true :false;
        return flag;
    }

}
