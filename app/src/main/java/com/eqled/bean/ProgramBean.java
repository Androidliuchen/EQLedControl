package com.eqled.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/31.
 */

/*
  节目参数
 */

@DatabaseTable(tableName = "tb_program")
public class ProgramBean extends Areabean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "text_content")
    private String text_content; //当节目有文本窗，并且有文本内容时
    /***************************
     * 11.14 新添加测试代码
     ******************************************/
    private String time_content;//当节目时间有窗，并且有文本内容时
    private ArrayList<String> arrayData;
    public ProgramBean() {

    }

    public String getTime_content() {
        return time_content;
    }

    public void setTime_content(String time_content) {
        this.time_content = time_content;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}
