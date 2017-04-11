package com.eqled.bean;

import android.graphics.Paint;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Administrator on 2016/7/13.
 *
 *  区域基类
 */
public class Areabean implements Comparable<Areabean> {
    @DatabaseField(columnName = "area_type")
    private int type;
    private Paint paint; //每个区域自己的画笔
    @DatabaseField(columnName = "windowWidth")
    private int windowWidth;//窗口宽度
    @DatabaseField(columnName = "windowHeight")
    private int windowHeight; //窗口高度
    @DatabaseField(columnName = "area_X")
    private int area_X;//区域在窗体中的坐标x
    @DatabaseField(columnName = "area_y")
    private int area_y; //坐标
    @DatabaseField(columnName = "area_position")
    private int area_position;  //在区域数组中所处的位置
    private int index;  //区域转data以后在区域数据集合里，所在的位置的起点坐标

    public int getIndex() {
        return index;
    }

    public int getArea_position() {
        return area_position;
    }

    public void setArea_position(int area_position) {
        this.area_position = area_position;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public int getArea_X() {
        return area_X;
    }

    public void setArea_X(int area_X) {
        this.area_X = area_X;
    }

    public int getArea_y() {
        return area_y;
    }

    public void setArea_y(int area_y) {
        this.area_y = area_y;
    }


    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Areabean another) {

        return this.getArea_position() - another.getArea_position();
    }
}
