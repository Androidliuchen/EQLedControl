package com.eqled.utils;

/**
 * Created by Administrator on 2016/6/20.
 * 一些常量保存
 */
public class Constant {

    public static final String PROGRAM_ID = "program_id";  //传递节目id 的键
    public static final int UDP_WAIT = 5000;  //UDP接收数据包等待时长
    public static final int DATA_SPLIT = 1000;  //数据分包大小
    //区域种类 参数 枚举值
    public static final int AREA_TYPE_TEXT = 2;  //文本窗
    public static final int AREA_TYPE_PROGRAM = 0;  //节目参数本身
    public static final int AREA_TYPE_TIME = 3;    // 时间窗
    public static final int AREA_TYPE_IMAGE = 1;    // 图文窗
    //字体大小校正 ，字体默认+3 ，勉强跟PC端大小近似
    public static final int FONT_SIZE_CORRECTION = 3;
    public static final String SEND_STR = "send";  //Intet  键值对 ，用于判断是否立即执行节目发送操作
    public static final int SEND = 1;
}
