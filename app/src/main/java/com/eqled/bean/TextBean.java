package com.eqled.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 * <p>
 * <p>
 * 文本参数bean
 */
@DatabaseTable(tableName = "tb_text")
public class TextBean extends Areabean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = true, foreign = true, columnName = "program_id")
    private ProgramBean programBean;
    /* @DatabaseField(columnName = "windowWidth")
     private  int   windowWidth  ;//窗口宽度 跟文本宽高不一定一致，暂时默认一致
     @DatabaseField(columnName = "windowHeight")
     private  int   windowHeight ; //窗口高度
     @DatabaseField(columnName = "area_X")
     private  int   area_X  ;//区域在窗体中的坐标x
     @DatabaseField(columnName = "area_y")
     private  int   area_y ; //坐标*/
    @DatabaseField(columnName = "text_hcenter")
    private int text_hcenter = 0; //文字水平位置 , 0 ,1 ,2  左对齐， 水平居中，右对齐 ，默认0
    @DatabaseField(columnName = "text_typeface")
    private int text_typeface = 1;  //字体默认
    @DatabaseField(columnName = "text_vcenter")
    private boolean text_vcenter = false;  //文字垂直是否居中
    @DatabaseField(columnName = "text_content")
    private String text_content = ""; //当节目有文本窗，并且用文本内容时
    @DatabaseField(columnName = "text_size")
    private int text_size_position = 6;  //记录用户点击字体大小的item,数据库里不记录实际大小，只记录item
    @DatabaseField(columnName = "text_bold")
    private boolean text_size_bold = false;  //记录字体是否加粗
    @DatabaseField(columnName = "text_inmode")
    private int text_inmode_position = 0; //记录 用户选择的入场特效
    @DatabaseField(columnName = "text_outmode")
    private int text_outmode_position = 0; //记录 用户选择的出场特效
    @DatabaseField(columnName = "text_runtime")
    private int text_runtime_position = 1; //记录 文本运行速度，也就是进场时间和出场时间（2者相等）
    @DatabaseField(columnName = "text_duration")
    private int text_duration_position = 20; //文字停留时间
    private List<String> texts = new ArrayList<String>();  //文本窗分屏，实时分屏，不存入数据库

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", text_content =" + text_content
                ;
    }

    public int getText_typeface() {
        return text_typeface;
    }

    public void setText_typeface(int text_typeface) {
        this.text_typeface = text_typeface;
    }

    public int getText_hcenter() {
        return text_hcenter;
    }

    public void setText_hcenter(int text_hcenter) {
        this.text_hcenter = text_hcenter;
    }

    public boolean isText_vcenter() {
        return text_vcenter;
    }

    public void setText_vcenter(boolean text_vcenter) {
        this.text_vcenter = text_vcenter;
    }

    public int getId() {
        return id;
    }

    public ProgramBean getProgramBean() {
        return programBean;
    }

    /*public int getWindowWidth() {
        return windowWidth;
    }*/

    public String getText_content() {
        return text_content;
    }

    /*public int getWindowHeight() {
        return windowHeight;
    }
*/
    public int getText_size_position() {
        return text_size_position;
    }

    public int getText_inmode_position() {
        return text_inmode_position;
    }

    public int getText_outmode_position() {
        return text_outmode_position;
    }

    public int getText_runtime_position() {
        return text_runtime_position;
    }

    public int getText_duration_position() {
        return text_duration_position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProgramBean(ProgramBean programBean) {
        this.programBean = programBean;
    }

    /*public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }*/

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public void setText_duration_position(int text_duration_position) {
        this.text_duration_position = text_duration_position;
    }

    public void setText_runtime_position(int text_runtime_position) {
        this.text_runtime_position = text_runtime_position;
    }

    public void setText_outmode_position(int text_outmode_position) {
        this.text_outmode_position = text_outmode_position;
    }

    public void setText_inmode_position(int text_inmode_position) {
        this.text_inmode_position = text_inmode_position;
    }

    public void setText_size_position(int text_size_position) {
        this.text_size_position = text_size_position;
    }

    /*public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }*/

    public boolean isText_size_bold() {
        return text_size_bold;
    }

    public void setText_size_bold(boolean text_size_bold) {
        this.text_size_bold = text_size_bold;
    }

    /*public int getArea_X() {
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
    }*/
}
