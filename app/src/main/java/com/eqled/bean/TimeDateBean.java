package com.eqled.bean;

import com.eqled.network.EQ_DateFile_Asc;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 * <p>
 * 时钟日期类
 */
@DatabaseTable(tableName = "tb_time")
public class TimeDateBean extends Areabean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = true, foreign = true, columnName = "program_id")
    private ProgramBean programBean;
    /*@DatabaseField(columnName = "windowWidth")
    private  int   windowWidth  ;//窗口宽度 跟文本宽高不一定一致，暂时默认一致
    @DatabaseField(columnName = "windowHeight")
    private  int   windowHeight ; //窗口高度
    @DatabaseField(columnName = "area_X")
    private  int   area_X  ;//区域在窗体中的坐标x
    @DatabaseField(columnName = "area_y")
    private  int   area_y ; //坐标*/
    @DatabaseField(columnName = "m_nClockType")
    private int m_nClockType;      // 时钟样式，一共4种
    //数字时钟的参数
    @DatabaseField(columnName = "number_typeface")
    private int number_typeface = 1;  //数字时钟字体
    @DatabaseField(columnName = "m_strClockText")
    private String m_strClockText = "";  //固定文字
    @DatabaseField(columnName = " m_rgbClockTextColor")
    private int m_rgbClockTextColor; //固定文字颜色
    @DatabaseField(columnName = "m_rgbClockTextSize")
    private int m_rgbClockTextSize = 0;  //固定文字大小 。默认第6项  12点。。
    @DatabaseField(columnName = "m_nYearType")
    private int m_nYearType;         //年格式
    @DatabaseField(columnName = "m_nRowType")
    private int m_nRowType;         //行格式
    @DatabaseField(columnName = "m_strShowForm")
    private String m_strShowForm = "1111111";   //年月日星期时分秒是否显示，默认为“1111111” ,0不显示， 1显示
    @DatabaseField(columnName = "m_rgbDayTextColor")
    private int m_rgbDayTextColor;            //日期颜色
    @DatabaseField(columnName = "m_rgbWeekTextColor")
    private int m_rgbWeekTextColor;            //星期颜色
    @DatabaseField(columnName = "m_rgbTimeColor")
    private int m_rgbTimeColor;                //时间颜色
    @DatabaseField(columnName = "m_nDayLag")
    private int m_nDayLag;                //时差  ： 天
    @DatabaseField(columnName = "m_strTimeLag")
    private String m_strTimeLag = "00:00";     // 时差 ：  时间
    @DatabaseField(columnName = "m_nOffset")
    private  int m_nOffset;              //时差 ： 0超前 ，1 滞后   +    -

    //模拟时钟参数
    @DatabaseField(columnName = "shibiaox")
    private int shibiaox = 4; //小时标识宽
    @DatabaseField(columnName = "shibiaoy")
    private int shibiaoy = 2; //标识长度
    @DatabaseField(columnName = "fenbiaox")
    private int fenbiaox = 4; //分针标识宽
    @DatabaseField(columnName = "fenbiaoy")
    private int fenbiaoy = 2; //分针标识长度
    @DatabaseField(columnName = "refresh_time")
    private float refresh_time = 1000;//秒针刷新的时间
    @DatabaseField(columnName = "density_second")
    private float density_second = 0.85f;//秒针长度比例
    @DatabaseField(columnName = "density_minute")
    private float density_minute = 0.70f;//分针长度比例
    @DatabaseField(columnName = "density_hour")
    private float density_hour = 0.45f;//时针长度比例
    @DatabaseField(columnName = "width_circle")
    private float width_circle = 5;//表盘最外圆圈的宽度
    @DatabaseField(columnName = "width_hour")
    private float width_hour = 10;//时针宽度
    @DatabaseField(columnName = "width_minutes")
    private float width_minutes = 8;//分针刻度宽度
    @DatabaseField(columnName = "width_second")
    private float width_second = 6;//秒针刻度宽度
    @DatabaseField(columnName = "radius_center")
    private float radius_center = 3;//表盘正中心的半径长度 radius_center
    @DatabaseField(columnName = "millSecond")
    private double millSecond;//获取当前的时间参数（毫秒，秒，分钟，小时）
    @DatabaseField(columnName = "second")
    private double second;
    @DatabaseField(columnName = "minute")
    private double minute;
    @DatabaseField(columnName = "hour")
    private double hour;
    @DatabaseField(columnName = "hourscolor")
    private int hourscolor;
    @DatabaseField(columnName = "minutecolor")
    private int minutecolor;
    @DatabaseField(columnName = "secondcolor")
    private int secondcolor;
    @DatabaseField(columnName = "colckShape")
    private int colckShape;  //时钟形状
    @DatabaseField(columnName = "weekshow")
    private boolean weekshow = true;  //模拟时钟星期是否显示
    @DatabaseField(columnName = "dateshow")
    private boolean dateshow = true;  //日期是否显示
    @DatabaseField(columnName = "shibiaocolorposition")
    private int shibiaocolorposition; //hour时间标识颜色

    @DatabaseField(columnName = "shibiaoshape")
    private int shibiaoshape;  //标识形状position
    @DatabaseField(columnName = "fenbiaocolorposition")
    private int fenbiaocolorposition; //分钟

    @DatabaseField(columnName = "fenbiaoshape")
    private int fenbiaoshape;
    private EQ_DateFile_Asc[] asc;

    public EQ_DateFile_Asc[] getAsc() {
        if (asc == null) {
            asc = new EQ_DateFile_Asc[100];
        }
        return asc;
    }

    public void setAsc(EQ_DateFile_Asc[] asc) {
        this.asc = asc;
    }

    public float getRefresh_time() {
        return refresh_time;
    }

    public void setRefresh_time(float refresh_time) {
        this.refresh_time = refresh_time;
    }

    public float getDensity_second() {
        return density_second;
    }

    public void setDensity_second(float density_second) {
        this.density_second = density_second;
    }

    public float getDensity_minute() {
        return density_minute;
    }

    public void setDensity_minute(float density_minute) {
        this.density_minute = density_minute;
    }

    public float getDensity_hour() {
        return density_hour;
    }

    public void setDensity_hour(float density_hour) {
        this.density_hour = density_hour;
    }

    public float getWidth_circle() {
        return width_circle;
    }

    public void setWidth_circle(float width_circle) {
        this.width_circle = width_circle;
    }

    public float getWidth_hour() {
        return width_hour;
    }

    public void setWidth_hour(float width_hour) {
        this.width_hour = width_hour;
    }

    public float getWidth_minutes() {
        return width_minutes;
    }

    public void setWidth_minutes(float width_minutes) {
        this.width_minutes = width_minutes;
    }

    public float getWidth_second() {
        return width_second;
    }

    public void setWidth_second(float width_second) {
        this.width_second = width_second;
    }

    public float getRadius_center() {
        return radius_center;
    }

    public void setRadius_center(float radius_center) {
        this.radius_center = radius_center;
    }

    public double getMillSecond() {
        return millSecond;
    }

    public void setMillSecond(double millSecond) {
        this.millSecond = millSecond;
    }

    public double getSecond() {
        return second;
    }

    public void setSecond(double second) {
        this.second = second;
    }

    public double getMinute() {
        return minute;
    }

    public void setMinute(double minute) {
        this.minute = minute;
    }

    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }

    public int getColckShape() {
        return colckShape;
    }

    public void setColckShape(int colckShape) {
        this.colckShape = colckShape;
    }

    public boolean isWeekshow() {
        return weekshow;
    }

    public void setWeekshow(boolean weekshow) {
        this.weekshow = weekshow;
    }

    public boolean isDateshow() {
        return dateshow;
    }

    public void setDateshow(boolean dateshow) {
        this.dateshow = dateshow;
    }

    public int getShibiaocolorposition() {
        return shibiaocolorposition;
    }

    public void setShibiaocolorposition(int shibiaocolorposition) {
        this.shibiaocolorposition = shibiaocolorposition;
    }

    public int getShibiaox() {
        return shibiaox;
    }

    public void setShibiaox(int shibiaox) {
        this.shibiaox = shibiaox;
    }

    public int getShibiaoy() {
        return shibiaoy;
    }

    public void setShibiaoy(int shibiaoy) {
        this.shibiaoy = shibiaoy;
    }

    public int getShibiaoshape() {
        return shibiaoshape;
    }

    public void setShibiaoshape(int shibiaoshape) {
        this.shibiaoshape = shibiaoshape;
    }

    public int getFenbiaocolorposition() {
        return fenbiaocolorposition;
    }

    public void setFenbiaocolorposition(int fenbiaocolorposition) {
        this.fenbiaocolorposition = fenbiaocolorposition;
    }

    public int getFenbiaox() {
        return fenbiaox;
    }

    public void setFenbiaox(int fenbiaox) {
        this.fenbiaox = fenbiaox;
    }

    public int getFenbiaoy() {
        return fenbiaoy;
    }

    public void setFenbiaoy(int fenbiaoy) {
        this.fenbiaoy = fenbiaoy;
    }

    public int getFenbiaoshape() {
        return fenbiaoshape;
    }

    public void setFenbiaoshape(int fenbiaoshape) {
        this.fenbiaoshape = fenbiaoshape;
    }

    public int getHourscolor() {
        return hourscolor;
    }

    public void setHourscolor(int hourscolor) {
        this.hourscolor = hourscolor;
    }

    public int getMinutecolor() {
        return minutecolor;
    }

    public void setMinutecolor(int minutecolor) {
        this.minutecolor = minutecolor;
    }

    public int getSecondcolor() {
        return secondcolor;
    }

    public void setSecondcolor(int secondcolor) {
        this.secondcolor = secondcolor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProgramBean getProgramBean() {
        return programBean;
    }

    public void setProgramBean(ProgramBean programBean) {
        this.programBean = programBean;
    }

   /* @Override
    public int getWindowWidth() {
        return windowWidth;
    }

    @Override
    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    @Override
    public int getWindowHeight() {
        return windowHeight;
    }

    @Override
    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    @Override
    public int getArea_X() {
        return area_X;
    }

    @Override
    public void setArea_X(int area_X) {
        this.area_X = area_X;
    }

    @Override
    public int getArea_y() {
        return area_y;
    }

    @Override
    public void setArea_y(int area_y) {
        this.area_y = area_y;
    }*/

    public int getM_nClockType() {
        return m_nClockType;
    }

    public void setM_nClockType(int m_nClockType) {
        this.m_nClockType = m_nClockType;
    }

    public String getM_strClockText() {
        return m_strClockText;
    }

    public void setM_strClockText(String m_strClockText) {
        this.m_strClockText = m_strClockText;
    }

    public int getM_rgbClockTextColor() {
        return m_rgbClockTextColor;
    }

    public void setM_rgbClockTextColor(int m_rgbClockTextColor) {
        this.m_rgbClockTextColor = m_rgbClockTextColor;
    }

    public int getM_rgbClockTextSize() {
        return m_rgbClockTextSize;
    }

    public void setM_rgbClockTextSize(int m_rgbClockTextSize) {
        this.m_rgbClockTextSize = m_rgbClockTextSize;
    }

    public int getM_nYearType() {
        return m_nYearType;
    }

    public void setM_nYearType(int m_nYearType) {
        this.m_nYearType = m_nYearType;
    }

    public int getM_nRowType() {
        return m_nRowType;
    }

    public void setM_nRowType(int m_nRowType) {
        this.m_nRowType = m_nRowType;
    }

    public String getM_strShowForm() {
        return m_strShowForm;
    }

    public void setM_strShowForm(String m_strShowForm) {
        this.m_strShowForm = m_strShowForm;
    }

    public int getM_rgbDayTextColor() {
        return m_rgbDayTextColor;
    }

    public void setM_rgbDayTextColor(int m_rgbDayTextColor) {
        this.m_rgbDayTextColor = m_rgbDayTextColor;
    }

    public int getM_rgbWeekTextColor() {
        return m_rgbWeekTextColor;
    }

    public void setM_rgbWeekTextColor(int m_rgbWeekTextColor) {
        this.m_rgbWeekTextColor = m_rgbWeekTextColor;
    }

    public int getM_rgbTimeColor() {
        return m_rgbTimeColor;
    }

    public void setM_rgbTimeColor(int m_rgbTimeColor) {
        this.m_rgbTimeColor = m_rgbTimeColor;
    }

    public int getM_nDayLag() {
        return m_nDayLag;
    }

    public void setM_nDayLag(int m_nDayLag) {
        this.m_nDayLag = m_nDayLag;
    }

    public String getM_strTimeLag() {
        return m_strTimeLag;
    }

    public void setM_strTimeLag(String m_strTimeLag) {
        this.m_strTimeLag = m_strTimeLag;
    }

    public int getM_nOffset() {
        return m_nOffset;
    }

    public void setM_nOffset(int m_nOffset) {
        this.m_nOffset = m_nOffset;
    }

    public int getNumber_typeface() {
        return number_typeface;
    }

    public void setNumber_typeface(int number_typeface) {
        this.number_typeface = number_typeface;
    }

    /**
     * 把  年月日是否显示的 string 切割成int[]  0不显示，1显示
     */
    public boolean[] getStrShowFormInt(String m_strShowForm) {
        boolean[] ints = new boolean[m_strShowForm.length()];
        for (int i = 0; i < m_strShowForm.length(); i++) {
            int y = Integer.parseInt(String.valueOf(m_strShowForm.charAt(i)));
            if (y == 0) {
                ints[i] = false;
            } else {
                ints[i] = true;
            }
        }
        return ints;
    }

    /**
     * 拆分时差 时间
     */
    public int[] splitStrTimeLag(String m_strTimeLag) {
        String[] strs = m_strTimeLag.split(":");
        int[] times = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            times[i] = Integer.parseInt(strs[i]);
        }
        return times;
    }
}
