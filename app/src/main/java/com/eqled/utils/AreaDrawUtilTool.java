package com.eqled.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.eqled.bean.Areabean;
import com.eqled.bean.TextBean;
import com.eqled.bean.TimeDateBean;
import com.eqled.eqledcontrol.R;
import com.eqled.network.EQ_DateFile_Asc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.cos;

/**
 * Created by Administrator on 2016/7/13.
 *
 * 绘制区域窗口   Draw绘制展示图片     get绘制bitmap用于节目组织
 */
public abstract class AreaDrawUtilTool extends Context {


    public static void DrawArea(Context context, Canvas canvas, Areabean areabean, int times) {

        switch (areabean.getType()) {
            case Constant.AREA_TYPE_IMAGE:
                drawImage(canvas);
                break;
            case Constant.AREA_TYPE_TEXT:
                drawText(context, canvas, areabean, times);
                break;
            case Constant.AREA_TYPE_TIME:
                drawTime(context,canvas,areabean,times);
                break;
        }
    }

    private static void drawTime(Context context, Canvas canvas, Areabean areabean, int times) {
        TimeDateBean timeDateBean = (TimeDateBean) areabean;
        //设置画笔属性   timeDateBean.getM_rgbClockTextSize()
        Paint paint = Utils.getPaint(context, Utils.getPaintSize(context, Integer.parseInt(context.getResources().getStringArray(R.array.text_size)[timeDateBean.getM_rgbClockTextSize()])
                + Constant.FONT_SIZE_CORRECTION) * times);
        Utils.setTypeface(context, paint
                , (context.getResources().getStringArray(R.array.typeface_path))[timeDateBean.getNumber_typeface()]);
        int[] number_colors = new int[]{timeDateBean.getM_rgbClockTextColor(), timeDateBean.getM_rgbDayTextColor()
                , timeDateBean.getM_rgbWeekTextColor(), timeDateBean.getM_rgbTimeColor(),
                timeDateBean.getSecondcolor(), timeDateBean.getMinutecolor(),
                timeDateBean.getHourscolor(), timeDateBean.getFenbiaocolorposition(),
                timeDateBean.getShibiaocolorposition()};
        for (int i = 0; i < number_colors.length; i++) {
            switch (number_colors[i]) {
                case 0:
                    number_colors[i] = Color.YELLOW;
                    break;
                case 1:
                    number_colors[i] = Color.GREEN;
                    break;
                case 2:
                    number_colors[i] = Color.RED;
                    break;
                case 3:
                    number_colors[i] = Color.BLACK;
                    break;
            }
        }
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setColor(number_colors[0]);
        paint.setTextSize(Utils.getPaintSize(context, timeDateBean.getM_rgbClockTextSize() + Constant.FONT_SIZE_CORRECTION) * times); // 字体大小 进度条参数
        if (timeDateBean.getM_nClockType() == 3) { //模拟时钟
            drawTimeText(context,canvas,areabean,times);

        } else {  //数字时钟
            Calendar cal = Calendar.getInstance();
            //偏移
            int[] timelag = timeDateBean.splitStrTimeLag(timeDateBean.getM_strTimeLag());
            if (timeDateBean.getM_nOffset() == 0) {   //偏移+++
                cal.add(Calendar.DATE, timeDateBean.getM_nDayLag());
                cal.add(Calendar.HOUR, timelag[0]);
                cal.add(Calendar.MINUTE, timelag[1]);
            } else {
                cal.add(Calendar.DATE, -timeDateBean.getM_nDayLag());
                cal.add(Calendar.HOUR, -timelag[0]);
                cal.add(Calendar.MINUTE, -timelag[1]);
            }
            String year = String.valueOf(cal.get(Calendar.YEAR));//获取年份
            if (timeDateBean.getM_nYearType() == 1) { //2位年，把年份截取一半
                year = year.substring(2, 4);
            }
            String month = String.valueOf(cal.get(Calendar.MONTH) + 1);//获取月份
            if (month.length() == 1) {
                month = "0" + month;
            }
            String day = String.valueOf(cal.get(Calendar.DATE));//获取日
            if (day.length() == 1) {
                day = "0" + day;
            }
            String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));//小时
            if (hour.length() == 1) {
                hour = "0" + hour;
            }
            String minute = String.valueOf(cal.get(Calendar.MINUTE));//分
            if (minute.length() == 1) {
                minute = "0" + minute;
            }
            String second = String.valueOf(cal.get(Calendar.SECOND));//秒
            if (second.length() == 1) {
                second = "0" + second;
            }
            int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
            boolean[] str_ints = timeDateBean.getStrShowFormInt(timeDateBean.getM_strShowForm());
            //整合数据 ，年月日一组
            String date = "";
            //时钟风格  0,1,2
            int clockStyle = timeDateBean.getM_nClockType();
            if (LanguageUtil.loadData(context).equals("en") && clockStyle == 2) { //当语言环境为英语的时候，风格2 等于风格0
                clockStyle = 0;
            }
            String space = "";
            if (clockStyle == 0) {
                space = "-";
            }
            if (clockStyle == 1) {
                space = "/";
            }
            //组织文字
            if (str_ints[0] == true) {
                if (clockStyle == 2) {
                    date += year + "年";
                } else {
                    date += year;
                }
            }
            if (str_ints[1] == true) {
                if (clockStyle == 2) {
                    date += month + "月";
                } else {
                    if (!date.equals("")) {
                        date += space + month;
                    } else {
                        date += month;
                    }
                }
            }
            if (str_ints[2] == true) {
                if (clockStyle == 2) {
                    date += day + "日";
                } else {
                    if (!date.equals("")) {
                        date += space + day;
                    } else {
                        date += day;
                    }
                }
            }
            if ((str_ints[0] | str_ints[1] | str_ints[2]) & (str_ints[3] | str_ints[4]) & timeDateBean.getM_nRowType() == 0) {
                date += " ";
            }
            String week = "";
            if (str_ints[3] == true) {
                if (!LanguageUtil.loadData(context).equals("en")) {
                    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                    week += weekDays[WeekOfYear - 1];
                } else {
                    String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                    week += weekDays[WeekOfYear - 1];
                }
            }
            if (str_ints[3] == true & str_ints[4] == true & timeDateBean.getM_nRowType() == 0) {
                week += " ";
            }
            String time = "";
            if (str_ints[4] == true) {
                if (clockStyle == 2) {
                    time += hour + "时";
                } else {
                    time += hour;
                }
            }
            if (str_ints[5] == true) {
                if (clockStyle == 2) {
                    time += minute + "分";
                } else {
                    if (!time.equals("")) {
                        time += ":" + minute;
                    } else {
                        time += minute;
                    }
                }
            }
            if (str_ints[6] == true) {
                if (clockStyle == 2) {
                    time += second + "秒";
                } else {
                    if (!time.equals("")) {
                        time += ":" + second;
                    } else {
                        time += second;
                    }
                }
            }
            String fixed_text = timeDateBean.getM_strClockText();
            Paint.FontMetrics fm = paint.getFontMetrics();
            int time_x = 0, time_y = 0;
            if (timeDateBean.getM_nRowType() == 0) { //单行
                String show_str = fixed_text + date + week + time;
                time_x = (int) ((timeDateBean.getArea_X()) * times + (timeDateBean.getWindowWidth() * times - Utils.getTextWidth(paint, show_str)) / 2);
                time_y = (int) ((-fm.ascent + 1) + timeDateBean.getArea_y() * times
                        + (timeDateBean.getWindowHeight() * times - Utils.getFontHeight(paint)) / 2);

                //绘制固定文字
                if (!fixed_text.equals("")) {
                    paint.setColor(number_colors[0]);
                    canvas.drawText(fixed_text, time_x, time_y, paint);
                    time_x += paint.measureText(fixed_text);
                }
                //绘日期
                if (!date.equals("")) {
                    paint.setColor(number_colors[1]);
                    canvas.drawText(date, time_x, time_y, paint);
                    time_x += paint.measureText(date);
                }
                //绘制星期
                if (!week.equals("")) {
                    paint.setColor(number_colors[2]);
                    canvas.drawText(week, time_x, time_y, paint);
                    time_x += paint.measureText(week);
                }
                //绘制时间
                if (!time.equals("")) {
                    paint.setColor(number_colors[3]);
                    canvas.drawText(time, time_x, time_y, paint);
                }
            } else { //多行
                time_y = (int) (-fm.ascent + 1);
                int nRow = 0;
                if (!fixed_text.equals("")) {
                    nRow++;
                }
                if (!date.equals("")) {
                    nRow++;
                }
                if (!week.equals("")) {
                    nRow++;
                }
                if (!time.equals("")) {
                    nRow++;
                }

                if (nRow > 0) {
                    //计算每行高
                    int nRowHeight = (timeDateBean.getWindowHeight() * times) / (nRow);
                        //绘固定文字
                        if (!fixed_text.equals("")) {
                            paint.setColor(number_colors[0]);
                            time_x = (int) ((timeDateBean.getArea_X()) * times + (timeDateBean.getWindowWidth() * times - Utils.getTextWidth(paint, fixed_text)) / 2);
                            canvas.drawText(fixed_text, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                            time_y += nRowHeight;
                        }
                        //绘日期
                        if (!date.equals("")) {
                            paint.setColor(number_colors[1]);
                            time_x = (int) ((timeDateBean.getArea_X()) * times + (timeDateBean.getWindowWidth() * times - Utils.getTextWidth(paint, date)) / 2);
                            canvas.drawText(date, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                            time_y += nRowHeight;
                        }
                        //绘制星期
                        if (!week.equals("")) {
                            paint.setColor(number_colors[2]);
                            time_x = (int) ((timeDateBean.getArea_X()) * times + (timeDateBean.getWindowWidth() * times - Utils.getTextWidth(paint, week)) / 2);
                            canvas.drawText(week, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                            time_y += nRowHeight;
                        }
                        //绘制时间
                        if (!time.equals("")) {
                            paint.setColor(number_colors[3]);
                            time_x = (int) ((timeDateBean.getArea_X()) * times + (timeDateBean.getWindowWidth() * times - Utils.getTextWidth(paint, time)) / 2);
                            canvas.drawText(time, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                    }
                }
            }
        }

            drawLine(context, canvas, timeDateBean.getArea_X(), timeDateBean.getArea_y(), timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight(), times);

    }
    private static void drawTimeText(Context context, Canvas canvas, Areabean areabean, int times){
        TimeDateBean timeDateBean = (TimeDateBean) areabean;
        Paint paint = Utils.getPaint(context, Utils.getPaintSize(context, Integer.parseInt(context.getResources().getStringArray(R.array.text_size)[timeDateBean.getM_rgbClockTextSize()])
                + Constant.FONT_SIZE_CORRECTION) * times);
        Utils.setTypeface(context, paint
                , (context.getResources().getStringArray(R.array.typeface_path))[timeDateBean.getNumber_typeface()]);
        int[] number_colors = new int[]{timeDateBean.getM_rgbClockTextColor(), timeDateBean.getM_rgbDayTextColor()
                , timeDateBean.getM_rgbWeekTextColor(), timeDateBean.getM_rgbTimeColor(),
                timeDateBean.getSecondcolor(), timeDateBean.getMinutecolor(),
                timeDateBean.getHourscolor(), timeDateBean.getFenbiaocolorposition(),
                timeDateBean.getShibiaocolorposition()};
        for (int i = 0; i < number_colors.length; i++) {
            switch (number_colors[i]) {
                case 0:
                    number_colors[i] = Color.YELLOW;
                    break;
                case 1:
                    number_colors[i] = Color.GREEN;
                    break;
                case 2:
                    number_colors[i] = Color.RED;
                    break;
                case 3:
                    number_colors[i] = Color.BLACK;
                    break;
            }
        }
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setColor(number_colors[0]);
        paint.setTextSize(Utils.getPaintSize(context, timeDateBean.getM_rgbClockTextSize() + Constant.FONT_SIZE_CORRECTION) * times); // 字体大小 进度条参数
        float borderWidth = timeDateBean.getWidth_circle();
        //获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        timeDateBean.setMillSecond(calendar.get(Calendar.MILLISECOND));
        timeDateBean.setSecond(calendar.get(Calendar.SECOND));
        timeDateBean.setMinute(calendar.get(Calendar.MINUTE));
        timeDateBean.setHour(calendar.get(Calendar.HOUR));

        boolean[] str_ints = timeDateBean.getStrShowFormInt(timeDateBean.getM_strShowForm());
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        for (int i = 0; i < m_gStuAsc.length; i++) {
            m_gStuAsc[i] = new EQ_DateFile_Asc();
        }
        GetDateText(calendar,timeDateBean,str_ints);
        GetWeekText(calendar,timeDateBean,str_ints);
        GetTimeText(calendar,timeDateBean,str_ints);
        // 获取宽高参数
        Paint.FontMetrics fm = paint.getFontMetrics();
        if (timeDateBean.getWindowWidth() <= timeDateBean.getWindowHeight()) {
            int mWidth = Math.min(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            int mHeight = Math.max(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            //画刻度线
            Paint paintLine = new Paint();
            for (int i = 0; i < 60; i++) {
                if (i % 5 == 0) {
                    paintLine.setStrokeWidth(timeDateBean.getShibiaox());
                    paintLine.setColor(number_colors[8]);
                    paintLine.setStyle(Paint.Style.FILL);
                } else {
                    paintLine.setStrokeWidth(timeDateBean.getFenbiaox());
                    paintLine.setColor(number_colors[7]);
                    paintLine.setStyle(Paint.Style.FILL);
                }
                canvas.drawCircle(mWidth / 2 * times, mHeight / 2 * times - mWidth / 2 * times + borderWidth *2,timeDateBean.getRadius_center(), paintLine);
                //canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times - mWidth / 2 * times + borderWidth, mWidth / 2 * times, mHeight / 2 * times - mWidth / 2 * times + lineLength, paintLine);
                canvas.rotate(360 / 60, mWidth / 2 * times, mHeight / 2 * times);
            }

            //刻度数字
            String targetText[] = context.getResources().getStringArray(R.array.clock);

            //绘制时间文字
            float startX = mWidth / 2 * times - paint.measureText(targetText[1]) / 2;
            float startY = mHeight / 2 * times - mWidth / 2 * times + borderWidth * 2;
            float textR = (float) Math.sqrt(Math.pow(mWidth / 2 * times - startX, 2) + Math.pow(mHeight / 2 * times - startY, 2));

            for (int i = 0; i < 12; i++) {
                float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
                float y = (float) (startY + textR - cos(Math.PI / 6 * i) * textR);
                if (i != 11 && i != 10 && i != 0) {
                    y = y + paint.measureText(targetText[i]) / 2;
                } else {
                    x = x - paint.measureText(targetText[i]) / 4;
                    y = y + paint.measureText(targetText[i]) / 4;
                }
                canvas.drawText(targetText[i], x, y, paint);
            }
            //绘制秒针
            paint.setColor(number_colors[4]);
            paint.setStrokeWidth(timeDateBean.getWidth_second());
            float degree = timeDateBean.getRefresh_time() > 1000 ? (float) (timeDateBean.getSecond() * 360 / 60) : (float) (timeDateBean.getSecond() * 360 / 60 + timeDateBean.getMillSecond() / 1000 * 360 / 60);
            canvas.rotate(degree, mWidth / 2 * times, mHeight / 2 * times);
            canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times, mWidth / 2 * times, mHeight / 2 * times - (mWidth / 2 * times - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_second(), paint);
            canvas.rotate(-degree, mWidth / 2 * times, mHeight / 2 * times);

            //绘制分针
            paint.setColor(number_colors[5]);
            paint.setStrokeWidth(timeDateBean.getWidth_minutes());
            float degree2 = (float) (timeDateBean.getMinute() * 360 / 60);
            canvas.rotate(degree2, mWidth / 2 * times, mHeight / 2 * times);
            canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times, mWidth / 2 * times, mHeight / 3 * times - (mWidth /4 * times - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_minute(), paint);
            canvas.rotate(-degree2, mWidth / 2 * times, mHeight / 2 * times);

            //绘制时针
            paint.setColor(number_colors[6]);
            paint.setStrokeWidth(timeDateBean.getWidth_hour());
            float degreeHour = (float) timeDateBean.getHour() * 360 / 12;
            float degreeMinut = (float) timeDateBean.getMinute() / 60 * 360 / 12;
            float degree3 = degreeHour + degreeMinut;
            canvas.rotate(degree3, mWidth / 2 * times, mHeight / 2 * times);
            canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times, mWidth / 2 * times, mHeight / 2 * times + (mWidth / 2 * times - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_hour(), paint);
            canvas.rotate(-degree3, mWidth / 2 * times, mHeight / 2 * times);
            // 画圆心
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mWidth / 2 * times, mHeight / 2 * times, timeDateBean.getRadius_center(), paint);
           // 绘表标题
            if( !timeDateBean.getM_strClockText().isEmpty()){
                String fixed_text = timeDateBean.getM_strClockText();
                float textX = mWidth / 2 * times - paint.measureText(fixed_text) / 2;
                float textY = mHeight / 2 * times - mWidth / 4 * times;
                paint.setColor(number_colors[4]);
                canvas.drawText(fixed_text, textX, textY, paint);
            }
            //绘制日期
            if (timeDateBean.isDateshow() == true){
                long time=System.currentTimeMillis();
                Date date=new Date(time);
                SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                String textDay = format.format(date);
                float textDayX = mWidth / 2 * times - paint.measureText(textDay) / 2;
                float textDayY = mHeight / 2 * times + mWidth / 4 * times;
                paint.setColor(number_colors[5]);
                canvas.drawText(textDay,textDayX,textDayY,paint);
            }
            //绘制星期
            if (timeDateBean.isWeekshow() == true){
                int j = 0;
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x11);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x1);

                long time=System.currentTimeMillis();
                Date date=new Date(time);
                paint.setColor(number_colors[5]);
                SimpleDateFormat format=new SimpleDateFormat("EEEE");
                String weekDay = format.format(date);
                double height = Math.ceil(fm.descent - fm.ascent);
                float weekDayX = mWidth / 2 * times - paint.measureText(weekDay) / 2;
                double weekDayY = mHeight / 2 * times + mWidth / 4 * times + height;
                paint.setColor(number_colors[6]);
                canvas.drawText(weekDay,weekDayX, (float) weekDayY,paint);
            }
        } else {
            int mWidth = Math.max(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            int mHeight = Math.min(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            //画刻度线
            Paint paintLine = new Paint();
            for (int i = 0; i < 60; i++) {
                if (i % 5 == 0) {
                    paintLine.setStrokeWidth(timeDateBean.getShibiaox());
                    paintLine.setColor(number_colors[8]);
                    paintLine.setStyle(Paint.Style.FILL);
                } else {
                    paintLine.setStrokeWidth(timeDateBean.getFenbiaox());
                    paintLine.setColor(number_colors[7]);
                    paintLine.setStyle(Paint.Style.FILL);
                }
                canvas.drawCircle(mWidth / 2 * times, borderWidth,timeDateBean.getRadius_center(), paintLine);
                canvas.rotate(360 / 60, mWidth / 2 * times, mHeight / 2 * times);
            }
            //刻度数字
            String targetText[] = context.getResources().getStringArray(R.array.clock);
            //绘制时间文字
            float startX = mWidth / 2 * times - paint.measureText(targetText[1]) / 2;
            float startY = 0;
            float textR = (float) Math.sqrt(Math.pow(mWidth / 2 * times - startX, 2) + Math.pow(mHeight / 2 * times - startY, 2));

            for (int i = 0; i < 12; i++) {
                float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
                float y = (float) (startY + textR - cos(Math.PI / 6 * i) * textR);
                if (i != 11 && i != 10 && i != 0) {
                    y = y + paint.measureText(targetText[i]) / 2;
                } else {
                    x = x - paint.measureText(targetText[i]) / 4;
                    y = y + paint.measureText(targetText[i]) / 4;
                }
                canvas.drawText(targetText[i], x, y, paint);
            }
            //绘制秒针
            paint.setColor(number_colors[4]);
            paint.setStrokeWidth(timeDateBean.getWidth_second());
            float degree = timeDateBean.getRefresh_time() > 1000 ? (float) (timeDateBean.getSecond() * 360 / 60) : (float) (timeDateBean.getSecond() * 360 / 60 + timeDateBean.getMillSecond() / 1000 * 360 / 60);
            canvas.rotate(degree, mWidth / 2 * times, mHeight / 2 * times);
            canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times, mWidth / 2 * times, mWidth / 3 * times - (mHeight / 3 * times - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_second(), paint);
            canvas.rotate(-degree, mWidth / 2 * times, mHeight / 2 * times);

            //绘制分针
            paint.setColor(number_colors[5]);
            paint.setStrokeWidth(timeDateBean.getWidth_minutes());
            float degree2 = (float) (timeDateBean.getMinute() * 360 / 60);
            canvas.rotate(degree2, mWidth / 2 * times, mHeight / 2 * times);
            canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times, mWidth / 2 * times, mWidth / 3 * times - (mHeight / 4 * times - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_minute(), paint);
            canvas.rotate(-degree2, mWidth / 2 * times, mHeight / 2 * times);

            //绘制时针
            paint.setColor(number_colors[6]);
            paint.setStrokeWidth(timeDateBean.getWidth_hour());
            float degreeHour = (float) timeDateBean.getHour() * 360 / 12;
            float degreeMinut = (float) timeDateBean.getMinute() / 60 * 360 / 12;
            float degree3 = degreeHour + degreeMinut;
            canvas.rotate(degree3, mWidth / 2 * times, mHeight / 2 * times);
            canvas.drawLine(mWidth / 2 * times, mHeight / 2 * times, mWidth / 2 * times, mWidth / 3* times - (mHeight / 2 * times - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_hour(), paint);
            canvas.rotate(-degree3, mWidth / 2 * times, mHeight / 2 * times);
            // 画圆心
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mWidth / 2 * times, mHeight / 2 * times, timeDateBean.getRadius_center(), paint);

            //绘制固定文字
            String fixed_text = timeDateBean.getM_strClockText();
            float textX = mWidth / 2 * times - paint.measureText(fixed_text) / 2;
            float textY = mHeight / 4 * times;
            paint.setColor(number_colors[4]);
            canvas.drawText(fixed_text, textX, textY, paint);

            //绘制日期
            if (timeDateBean.isDateshow() == true){
                int j=0;
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x11);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x1);

                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                long time=System.currentTimeMillis();
                Date date=new Date(time);
                paint.setColor(number_colors[5]);
                SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                String textDay = format.format(date);
                float textDayX = mWidth / 2 * times - paint.measureText(textDay) / 2;
                float textDayY =  mHeight / 4 * times + mHeight / 2 * times;
                canvas.drawText(textDay,textDayX,textDayY,paint);
            }
            //绘制星期
            if (timeDateBean.isWeekshow() == true){
                int j=0;
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x11);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x1);

                long time=System.currentTimeMillis();
                Date date=new Date(time);
                paint.setColor(number_colors[6]);
                SimpleDateFormat format=new SimpleDateFormat("EEEE");
                String weekDay = format.format(date);
                switch( Calendar.DAY_OF_WEEK)
                {
                    case 1:	weekDay=("星期一"); break;
                    case 2:	weekDay=("星期二"); break;
                    case 3:	weekDay=("星期三"); break;
                    case 4:	weekDay=("星期四"); break;
                    case 5:	weekDay=("星期五"); break;
                    case 6:	weekDay=("星期六"); break;
                    case 7:	weekDay=("星期日"); break;
                }
                fm = paint.getFontMetrics();
                double height = Math.ceil(fm.descent - fm.ascent);
                float weekDayX = mWidth / 2 * times - paint.measureText(weekDay) / 2;
                double weekDayY = mHeight / 4 * times + mHeight / 2 * times + height;
                canvas.drawText(weekDay,weekDayX, (float) weekDayY,paint);
            }
        }
        int j=0;
        for(int i=0;i<100;i++){
            EQ_DateFile_Asc[] m_gStuAscB = timeDateBean.getAsc();
            m_gStuAscB[i].setM_byVaild(0);
            if(m_gStuAsc[i].getM_byVaild() == 0x80){
                m_gStuAscB[j] = new EQ_DateFile_Asc();
                m_gStuAscB[j].setM_wx(m_gStuAsc[i].getM_wx());
                m_gStuAscB[j].setM_wy(m_gStuAsc[i].getM_wy());
                m_gStuAscB[j].setM_byColor(m_gStuAsc[i].getM_byColor());
                m_gStuAscB[j].setM_byAsc(m_gStuAsc[i].getM_byAsc());
                m_gStuAscB[j++].setM_byVaild(1);
            }
        }
    }


    private static void drawText(Context context, Canvas canvas, Areabean areabean, int times) {
        TextBean textBean = (TextBean) areabean;
        Utils.SplitScreen(textBean.getTexts(), textBean.getText_content(), textBean.getWindowWidth(), textBean.getPaint());
        String show_str = "";
        if (textBean.getTexts().size() >= 1) {
            show_str = textBean.getTexts().get(0);
        }
        //设置画笔属性
        Paint paint1 = new Paint();
        paint1.setFakeBoldText(textBean.isText_size_bold()); // 字体是否加粗
        Utils.setTypeface(context, paint1
                , (context.getResources().getStringArray(R.array.typeface_path))[textBean.getText_typeface()]);
        paint1.setTextSize(Utils.getPaintSize(context, textBean.getText_size_position()) * times); // 字体大小 进度条参数
        paint1.setColor(Color.YELLOW); // 字体颜色 选择的 button **
        if (textBean.getText_typeface() == 1 && textBean.isText_size_bold() == true) {
            paint1.setTypeface(Typeface.DEFAULT_BOLD);
        }

        if (!show_str.equals("")) {
            Paint.FontMetrics fm1 = paint1.getFontMetrics();

            float text_x = 0;
            float text_y = 0;
            // 文本x坐标
            switch (textBean.getText_hcenter()) {
                case 0:
                    text_x = (textBean.getArea_X()) * times;
                    break;
                case 1:
                    text_x = ((textBean.getArea_X()) * times + (textBean.getWindowWidth() * times - Utils.getTextWidth(paint1, show_str)) / 2);
                    break;
                case 2:
                    text_x = ((textBean.getArea_X()) * times + (textBean.getWindowWidth() * times - paint1.measureText(show_str)));
                    break;
            }
            //文本y坐标
            if (textBean.isText_vcenter() == false) {
                text_y = ((-fm1.ascent + 1) + textBean.getArea_y() * times);
            } else {
                text_y = ((-fm1.ascent + 1) + textBean.getArea_y() * times
                        + (textBean.getWindowHeight() * times - Utils.getFontHeight(paint1)) / 2);
            }

            canvas.drawText(show_str, text_x, text_y, paint1);
            drawLine(context, canvas, textBean.getArea_X(), textBean.getArea_y(), textBean.getWindowWidth(), textBean.getWindowHeight(), times);
        }

    }

    private static void drawImage(Canvas canvas) {
    }

    /*
      绘制边框
     */
    private static void drawLine(Context context, Canvas canvas, int x, int y, int widht, int height, int times) {
        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        int line = Utils.dip2px(context, 1.0f);
        paint2.setStrokeWidth(line);
        //上
        canvas.drawLine(line + x * times, line + y * times,
                -line + x * times + widht * times, line + y * times, paint2);
        //左
        canvas.drawLine(line + x * times, line + y * times,
                line + x * times, y * times + height * times, paint2);
        //下
        canvas.drawLine(line + x * times,
                -line + y * times + height * times,
                x * times + widht * times - line,
                -line + y * times + height * times,
                paint2);
        //右
        canvas.drawLine(x * times + widht * times - line,
                line + y * times,
                x * times + widht * times - line,
                y * times + height * times,
                paint2);
        }

    /*
      获取bitmap
     */
    public static Bitmap getText(String content, TextBean textBean, ArrayList<Integer> bmpWidthList) {
        Bitmap bitmap = Bitmap.createBitmap(textBean.getWindowWidth(), textBean.getWindowHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = textBean.getPaint();
        if (textBean.getText_typeface() == 1 && textBean.isText_size_bold() == true) {
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            //paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        }
        Canvas canvas = new Canvas(bitmap); // 创建画布
        canvas.drawColor(Color.BLACK); // 颜色黑色
        Paint.FontMetrics fm = paint.getFontMetrics();
        float text_x = 0;
        float text_y = 0;
        // 文本x坐标
        switch (textBean.getText_hcenter()) {
            case 0:
                text_x = 0;
                break;
            case 1:
                text_x = (textBean.getWindowWidth() - Utils.getTextWidth(paint, content)) / 2;
                break;
            case 2:
                text_x = textBean.getWindowWidth() - paint.measureText(content);
                break;
        }
        //文本y坐标
        if (textBean.isText_vcenter() == false) {
            text_y = -fm.ascent + 1;
        } else {
            text_y = -fm.ascent + 1 + (textBean.getWindowHeight() - Utils.getFontHeight(paint)) / 2;
        }
        //  float  textBaselineY = -fm.ascent+1;
        bmpWidthList.add(Utils.getTextWidth(paint, content));
        canvas.drawText(content, text_x, text_y, paint);
        // canvas.drawText(content,0,-fm.ascent+1,paint);
        return bitmap;
    }

    //获取模拟时钟Bitmap
    public static Byte getTime(Context context, TimeDateBean timeDateBean) {

        Paint paint = Utils.getPaint(context, Utils.getPaintSize(context, Integer.parseInt(context.getResources().getStringArray(R.array.text_size)[timeDateBean.getM_rgbClockTextSize()])
                + Constant.FONT_SIZE_CORRECTION));//字体参数启动读取
        Utils.setTypeface(context, paint, (context.getResources().getStringArray(R.array.typeface_path))[timeDateBean.getNumber_typeface()]);
        int[] number_colors = new int[]{timeDateBean.getM_rgbClockTextColor(), timeDateBean.getM_rgbDayTextColor()
                , timeDateBean.getM_rgbWeekTextColor(), timeDateBean.getM_rgbTimeColor(),
                timeDateBean.getSecondcolor(), timeDateBean.getMinutecolor(),
                timeDateBean.getHourscolor(), timeDateBean.getFenbiaocolorposition(),
                timeDateBean.getShibiaocolorposition()};
        for (int i = 0; i < number_colors.length; i++) {
            switch (number_colors[i]) {
                case 0:
                    number_colors[i] = Color.YELLOW;
                    break;
                case 1:
                    number_colors[i] = Color.GREEN;
                    break;
                case 2:
                    number_colors[i] = Color.RED;
                    break;
                case 3:
                    number_colors[i] = Color.BLACK;
                    break;
            }
        }
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setColor(number_colors[0]);//重置画笔颜色
        Bitmap bitmap = Bitmap.createBitmap(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight(), Bitmap.Config.ARGB_8888);
        Canvas ca = new Canvas(bitmap); // 创建画布
        ca.drawColor(Color.BLACK); // 颜色黑色
        Paint.FontMetrics fm = paint.getFontMetrics();
        //获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        timeDateBean.setMillSecond(calendar.get(Calendar.MILLISECOND));
        timeDateBean.setSecond(calendar.get(Calendar.SECOND));
        timeDateBean.setMinute(calendar.get(Calendar.MINUTE));
        timeDateBean.setHour(calendar.get(Calendar.HOUR));

        //偏移
        int[] timelag = timeDateBean.splitStrTimeLag(timeDateBean.getM_strTimeLag());
        if (timeDateBean.getM_nOffset() == 0) {   //超前
            calendar.add(Calendar.DATE, timeDateBean.getM_nDayLag());
            calendar.add(Calendar.HOUR, timelag[0]);
            calendar.add(Calendar.MINUTE, timelag[1]);
        } else {//滞后
            calendar.add(Calendar.DATE, -timeDateBean.getM_nDayLag());
            calendar.add(Calendar.HOUR, -timelag[0]);
            calendar.add(Calendar.MINUTE, -timelag[1]);
        }

        boolean[] str_ints = timeDateBean.getStrShowFormInt(timeDateBean.getM_strShowForm());
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        for (int i = 0; i < m_gStuAsc.length; i++) {
            m_gStuAsc[i] = new EQ_DateFile_Asc();
        }
        GetDateText(calendar, timeDateBean, str_ints);
        GetWeekText(calendar, timeDateBean, str_ints);
        GetTimeText(calendar, timeDateBean, str_ints);
        for(int i=0;i<100;i++)
        {
            m_gStuAsc[i].setM_byVaild(0);
        }
        int m_iAscPosition = 0;
        if (timeDateBean.getM_nClockType() == 3) { //模拟时钟

        float borderWidth = timeDateBean.getWidth_circle();
            // 获取宽高参数
            if (timeDateBean.getWindowWidth() <= timeDateBean.getWindowHeight()) {
            int mWidth = Math.min(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            int mHeight = Math.max(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            //画刻度线
            Paint paintLine = new Paint();
            for (int i = 0; i < 60; i++) {
                if (i % 5 == 0) {
                    paintLine.setColor(number_colors[8]);
                } else {
                    paintLine.setColor(number_colors[7]);
                }
                ca.drawCircle(mWidth / 2, mHeight / 2 - mWidth / 2 + borderWidth, timeDateBean.getRadius_center(), paintLine);
                //ca.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2 + borderWidth, mWidth / 2, mHeight / 2 - mWidth / 2 + lineLength, paintLine);
                ca.rotate(360 / 60, mWidth / 2, mHeight / 2);
            }
            //刻度数字
            String targetText[] = context.getResources().getStringArray(R.array.clock);
            //绘制时间文字
            float startX = mWidth / 2 - paint.measureText(targetText[1]) / 2;
            float startY = mHeight / 2 - mWidth / 2 + borderWidth;
            float textR = (float) Math.sqrt(Math.pow(mWidth / 2 - startX, 2) + Math.pow(mHeight / 2 - startY, 2));

            for (int i = 0; i < 12; i++) {
                float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
                float y = (float) (startY + textR - cos(Math.PI / 6 * i) * textR);
                if (i != 11 && i != 10 && i != 0) {
                    y = y + paint.measureText(targetText[i]) / 2;
                } else {
                    x = x - paint.measureText(targetText[i]) / 4;
                    y = y + paint.measureText(targetText[i]) / 4;
                }
                ca.drawText(targetText[i], x, y, paint);

            }
            //绘制秒针
            paint.setColor(number_colors[4]);
            paint.setStrokeWidth(timeDateBean.getWidth_second());
            float degree = timeDateBean.getRefresh_time() > 1000 ? (float) (timeDateBean.getSecond() * 360 / 60) : (float) (timeDateBean.getSecond() * 360 / 60 + timeDateBean.getMillSecond() / 1000 * 360 / 60);
            ca.rotate(degree, mWidth / 2, mHeight / 2);
            ca.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 2 - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_second(), paint);
            ca.rotate(-degree, mWidth / 2, mHeight / 2);

            //绘制分针
            paint.setColor(number_colors[5]);
            paint.setStrokeWidth(timeDateBean.getWidth_minutes());
            float degree2 = (float) (timeDateBean.getMinute() * 360 / 60);
            ca.rotate(degree2, mWidth / 2, mHeight / 2);
            ca.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 2 - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_minute(), paint);
            ca.rotate(-degree2, mWidth / 2, mHeight / 2);

            //绘制时针
            paint.setColor(number_colors[6]);
            paint.setStrokeWidth(timeDateBean.getWidth_hour());
            float degreeHour = (float) timeDateBean.getHour() * 360 / 12;
            float degreeMinut = (float) timeDateBean.getMinute() / 60 * 360 / 12;
            float degree3 = degreeHour + degreeMinut;
            ca.rotate(degree3, mWidth / 2, mHeight / 2);
            ca.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 2 - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_hour(), paint);
            ca.rotate(-degree3, mWidth / 2, mHeight / 2);
            // 画圆心
            paint.setStyle(Paint.Style.FILL);
            ca.drawCircle(mWidth / 2, mHeight / 2, timeDateBean.getRadius_center(), paint);
            // 绘表标题
            if (!timeDateBean.getM_strClockText().isEmpty()) {
                String fixed_text = timeDateBean.getM_strClockText();
                float textX = mWidth / 2 - paint.measureText(fixed_text) / 2;
                float textY = mHeight / 2 - mWidth / 4;
                paint.setColor(number_colors[4]);
                ca.drawText(fixed_text, textX, textY, paint);
            }
                //绘制日期
                if (timeDateBean.isDateshow() == true){
                    int j=0;
                    m_gStuAsc[j].setM_byAsc('M');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('M');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x10);
                    m_gStuAsc[j++].setM_byVaild(0x2);

                    m_gStuAsc[j].setM_byAsc('D');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('D');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x10);
                    m_gStuAsc[j++].setM_byVaild(0x2);

                    long time=System.currentTimeMillis();
                    Date date=new Date(time);
                    paint.setColor(number_colors[5]);
                    SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                    String textDay = format.format(date);
                    float textDayX = mWidth / 2 - paint.measureText(textDay) / 2;
                    float textDayY =  mHeight / 4 + mHeight / 2;
                    ca.drawText(textDay,textDayX,textDayY,paint);
                }
            //绘制星期
            if (timeDateBean.isWeekshow() == true) {
                int j = 0;
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x11);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x1);

                long time = System.currentTimeMillis();
                Date date = new Date(time);
                paint.setColor(number_colors[6]);
                SimpleDateFormat format = new SimpleDateFormat("EEEE");
                String weekDay =format.format(date);
                double height = Math.ceil(fm.descent - fm.ascent);

                float weekDayX = mWidth / 2 - paint.measureText(weekDay) / 2;
                double weekDayY = mHeight / 4 + mHeight / 2 + height;
                ca.drawText(weekDay, weekDayX, (float) weekDayY, paint);
            }
        } else {
            int mWidth = Math.max(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            int mHeight = Math.min(timeDateBean.getWindowWidth(), timeDateBean.getWindowHeight());
            //画刻度线
            Paint paintLine = new Paint();
            for (int i = 0; i < 60; i++) {
                if (i % 5 == 0) {
                    paintLine.setColor(number_colors[8]);
                } else {
                    paintLine.setColor(number_colors[7]);
                }
                ca.drawCircle(mWidth / 2, borderWidth, timeDateBean.getRadius_center(), paintLine);
                ca.rotate(360 / 60, mWidth / 2, mHeight / 2);
            }
            //刻度数字
            String targetText[] = context.getResources().getStringArray(R.array.clock);
            //绘制时间文字
            float startX = mWidth / 2 - paint.measureText(targetText[1]) / 2;
            float startY = borderWidth;
            float textR = (float) Math.sqrt(Math.pow(mWidth / 2 - startX, 2) + Math.pow(mHeight / 2 - startY, 2));

            for (int i = 0; i < 12; i++) {
                float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
                float y = (float) (startY + textR - cos(Math.PI / 6 * i) * textR);
                if (i != 11 && i != 10 && i != 0) {
                    y = y + paint.measureText(targetText[i]) / 2;
                } else {
                    x = x - paint.measureText(targetText[i]) / 4;
                    y = y + paint.measureText(targetText[i]) / 4;
                }
                ca.drawText(targetText[i], x, y, paint);
            }
            //绘制秒针
            paint.setColor(number_colors[4]);
            paint.setStrokeWidth(timeDateBean.getWidth_second());
            float degree = timeDateBean.getRefresh_time() > 1000 ? (float) (timeDateBean.getSecond() * 360 / 60) : (float) (timeDateBean.getSecond() * 360 / 60 + timeDateBean.getMillSecond() / 1000 * 360 / 60);
            ca.rotate(degree, mWidth / 2, mHeight / 2);
            ca.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 3 - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_second(), paint);
            ca.rotate(-degree, mWidth / 2, mHeight / 2);

            //绘制分针
            paint.setColor(number_colors[5]);
            paint.setStrokeWidth(timeDateBean.getWidth_minutes());
            float degree2 = (float) (timeDateBean.getMinute() * 360 / 60);
            ca.rotate(degree2, mWidth / 2, mHeight / 2);
            ca.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 3 - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_minute(), paint);
            ca.rotate(-degree2, mWidth / 2, mHeight / 2);

            //绘制时针
            paint.setColor(number_colors[6]);
            paint.setStrokeWidth(timeDateBean.getWidth_hour());
            float degreeHour = (float) timeDateBean.getHour() * 360 / 12;
            float degreeMinut = (float) timeDateBean.getMinute() / 60 * 360 / 12;
            float degree3 = degreeHour + degreeMinut;
            ca.rotate(degree3, mWidth / 2, mHeight / 2);
            ca.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 3 - timeDateBean.getWidth_circle()) * timeDateBean.getDensity_hour(), paint);
            ca.rotate(-degree3, mWidth / 2, mHeight / 2);
            // 画圆心
            paint.setStyle(Paint.Style.FILL);
            ca.drawCircle(mWidth / 2, mHeight / 2, timeDateBean.getRadius_center(), paint);

            //绘制固定文字
            String fixed_text = timeDateBean.getM_strClockText();
            float textX = mWidth / 2 - paint.measureText(fixed_text) / 2;
            float textY = mHeight / 4;
            paint.setColor(number_colors[4]);
            ca.drawText(fixed_text, textX, textY, paint);

            //绘制日期
            if (timeDateBean.isDateshow() == true){
                int j=0;
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);

                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                long time = System.currentTimeMillis();
                Date date = new Date(time);
                paint.setColor(number_colors[5]);
                SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
                String textDay = format.format(date);
                float textDayX = mWidth / 2 - paint.measureText(textDay) / 2;
                float textDayY = mHeight / 4 + mHeight / 2;
                ca.drawText(textDay, textDayX, textDayY, paint);
            }
            //绘制星期
            if (timeDateBean.isWeekshow() == true){
                int j = 0;
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x11);
                m_gStuAsc[j].setM_byAsc('W');
                m_gStuAsc[j++].setM_byVaild(0x1);

                long time = System.currentTimeMillis();
                Date date = new Date(time);
                paint.setColor(number_colors[6]);
                SimpleDateFormat format = new SimpleDateFormat("EEEE");
                String weekDay =format.format(date);
                double height = Math.ceil(fm.descent - fm.ascent);
                float weekDayX = mWidth / 2 - paint.measureText(weekDay) / 2;
                double weekDayY = mHeight / 4 + mHeight / 2 + height;
                ca.drawText(weekDay, weekDayX, (float) weekDayY, paint);
            }
        }
        } else {  //数字时钟
            //偏移
//            int[] timelag = timeDateBean.splitStrTimeLag(timeDateBean.getM_strTimeLag());
            if (timeDateBean.getM_nOffset() == 0) {   //超前
                calendar.add(Calendar.DATE, timeDateBean.getM_nDayLag());
                calendar.add(Calendar.HOUR, timelag[0]);
                calendar.add(Calendar.MINUTE, timelag[1]);
            } else {//滞后
                calendar.add(Calendar.DATE, -timeDateBean.getM_nDayLag());
                calendar.add(Calendar.HOUR, -timelag[0]);
                calendar.add(Calendar.MINUTE, -timelag[1]);
            }
            String year = String.valueOf(calendar.get(Calendar.YEAR));//获取年份
            if (timeDateBean.getM_nYearType() == 1) { //2位年，把年份截取一半
                year = year.substring(2, 4);
            }
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);//获取月份
            if (month.length() == 1) {
                month = "0" + month;
            }
            String day = String.valueOf(calendar.get(Calendar.DATE));//获取日
            if (day.length() == 1) {
                day = "0" + day;
            }
            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));//小时
            if (hour.length() == 1) {
                hour = "0" + hour;
            }
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));//分
            if (minute.length() == 1) {
                minute = "0" + minute;
            }
            String second = String.valueOf(calendar.get(Calendar.SECOND));//秒
            if (second.length() == 1) {
                second = "0" + second;
            }
            int WeekOfYear = calendar.get(Calendar.DAY_OF_WEEK);//一周的第几天
            //整合数据 ，年月日一组
            String date = "";
            //时钟风格  0,1,2
            int clockStyle = timeDateBean.getM_nClockType();
            if (LanguageUtil.loadData(context).equals("en") && clockStyle == 2) { //当语言环境为英语的时候，风格2 等于风格0
                clockStyle = 0;
            }
            String space = "";
            if (clockStyle == 0) {
                space = "-";
            }
            if (clockStyle == 1) {
                space = "/";
            }
            //组织文字
            if (str_ints[0] == true) {
                if (clockStyle == 2) {
                    date += year + "年";
                } else {
                    date += year;
                }
            }
            if (str_ints[1] == true) {
                if (clockStyle == 2) {
                    date += month + "月";
                } else {
                    if (!date.equals("")) {
                        date += space + month;
                    } else {
                        date += month;
                    }
                }
            }
            if (str_ints[2] == true) {
                if (clockStyle == 2) {
                    date += day + "日";
                } else {
                    if (!date.equals("")) {
                        date += space + day;
                    } else {
                        date += day;
                    }
                }
            }
            if ((str_ints[0] | str_ints[1] | str_ints[2]) & (str_ints[3] | str_ints[4]) & timeDateBean.getM_nRowType() == 0) {
                date += " ";
            }
            String week = "";
            if (str_ints[3] == true) {
                if (!LanguageUtil.loadData(context).equals("en")) {
                    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                    week += weekDays[WeekOfYear - 1];
                } else {
                    String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                    week += weekDays[WeekOfYear - 1];
                }
            }
            if (str_ints[3] == true & str_ints[4] == true & timeDateBean.getM_nRowType() == 0) {
                week += " ";
            }
            String time = "";
            if (str_ints[4] == true) {
                if (clockStyle == 2) {
                    time += hour + "时";
                } else {
                    time += hour;
                }
            }
            if (str_ints[5] == true) {
                if (clockStyle == 2) {
                    time += minute + "分";
                } else {
                    if (!time.equals("")) {
                        time += ":" + minute;
                    } else {
                        time += minute;
                    }
                }
            }
            if (str_ints[6] == true) {
                if (clockStyle == 2) {
                    time += second + "秒";
                } else {
                    if (!time.equals("")) {
                        time += ":" + second;
                    } else {
                        time += second;
                    }
                }
            }
            String fixed_text = timeDateBean.getM_strClockText();
            int time_x = 0, time_y = 0;
            if (timeDateBean.getM_nRowType() == 0) { //单行
                String show_str = fixed_text + date + week + time;
                time_x = (int) (timeDateBean.getWindowWidth() - Utils.getTextWidth(paint, show_str)) / 2;
                time_y = (int) ((-fm.ascent + 1) + (timeDateBean.getWindowHeight() - Utils.getFontHeight(paint)) / 2);
                //绘制固定文字
                if (!fixed_text.equals("")) {
                    paint.setColor(number_colors[0]);
                    ca.drawText(fixed_text, time_x, time_y, paint);
                    time_x += paint.measureText(fixed_text);
                }
                //绘日期
                if (!date.equals("")) {
                    ClockTextOut(time_x, time_y, date, timeDateBean.getM_rgbDayTextColor(), timeDateBean, 0);
                    paint.setColor(number_colors[1]);
                    ca.drawText(date, time_x, time_y, paint);
                    time_x += paint.measureText(date);
                }
                //绘制星期
                if (!week.equals("")) {
                    ClockTextOut(time_x, time_y, week, timeDateBean.getM_rgbWeekTextColor(), timeDateBean, 1);
                    paint.setColor(number_colors[2]);
                    ca.drawText(week, time_x, time_y, paint);
                    time_x += paint.measureText(week);
                }
                //绘制时间
                if (!time.equals("")) {
                    ClockTextOut(time_x, time_y, time, timeDateBean.getM_rgbTimeColor(), timeDateBean, 0);
                    paint.setColor(number_colors[3]);
                    ca.drawText(time, time_x, time_y, paint);
                }
            } else { //多行
                time_y = (int) (-fm.ascent + 1);
                int nRow = 0;
                if (!fixed_text.equals("")) {
                    nRow++;
                }
                if (!date.equals("")) {
                    nRow++;
                }
                if (!week.equals("")) {
                    nRow++;
                }
                if (!time.equals("")) {
                    nRow++;
                }

                if (nRow > 0) {
                    //计算每行高
                    int nRowHeight = (timeDateBean.getWindowHeight()) / (nRow);
                    //绘固定文字
                    if (!fixed_text.equals("")) {
                        paint.setColor(number_colors[0]);
                        time_x = (int) (timeDateBean.getWindowWidth() - Utils.getTextWidth(paint, fixed_text)) / 2;
                        ca.drawText(fixed_text, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                        time_y += nRowHeight;
                    }
                    //绘日期
                    if (!date.equals("")) {
                        paint.setColor(number_colors[1]);
                        time_x = (int) ((timeDateBean.getArea_X()) + (timeDateBean.getWindowWidth() - Utils.getTextWidth(paint, date)) / 2);
                        ca.drawText(date, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                        ClockTextOut(time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), date, timeDateBean.getM_rgbDayTextColor(), timeDateBean, 0);
                        time_y += nRowHeight;
                    }
                    //绘制星期
                    if (!week.equals("")) {
                        paint.setColor(number_colors[2]);
                        time_x = (int) (timeDateBean.getWindowWidth() - Utils.getTextWidth(paint, week)) / 2;
                        ca.drawText(week, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                        ClockTextOut(time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), week, timeDateBean.getM_rgbWeekTextColor(), timeDateBean, 1);
                        time_y += nRowHeight;
                    }
                    //绘制时间
                    if (!time.equals("")) {
                        paint.setColor(number_colors[3]);
                        time_x = (int) ((timeDateBean.getArea_X()) + (timeDateBean.getWindowWidth() - Utils.getTextWidth(paint, time)) / 2);
                        ca.drawText(time, time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), paint);
                        ClockTextOut(time_x, time_y + (int) ((nRowHeight - Utils.getFontHeight(paint)) / 2), time, timeDateBean.getM_rgbTimeColor(), timeDateBean, 0);
                    }
                }
            }
        }
        return null;
    }

    /*
       获取日期字符串
     */
    private static String GetDateText(Calendar calendar, TimeDateBean timeDateBean, boolean[] showform) {
        int i, j = 0;
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        for (i = 0; i < 100; i++) {
            if (m_gStuAsc[i].getM_byVaild() == 0) {
                j = i;
                break;
            }
        }
        String strText = "";
        if (timeDateBean.getM_nClockType() == 0) {
            if (showform[0]) {
                strText += (timeDateBean.getM_nYearType() == 0 ? ("yyyy-") : ("yy-"));
                if (timeDateBean.getM_nYearType() == 0) {
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                } else {
                    m_gStuAsc[j].setM_byAsc('y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                }
            }

            if (showform[1]) {
                strText += "MM-";
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x2);
            }

            if (showform[2]) {
                strText += "dd-";
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x2);
            }
            strText = strText.substring(strText.lastIndexOf('-'));

            j=j-1;
            m_gStuAsc[j].setM_byVaild(0);
        }
        else if(timeDateBean.getM_nClockType() == 1){
            if (showform[0]){
                strText+=(timeDateBean.getM_nYearType() ==0?("yyyy/"):("yy/"));//年
                if(timeDateBean.getM_nYearType() == 0){
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                }else{
                    m_gStuAsc[j].setM_byAsc('y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                }
            }

            if (showform[1]){
                strText+=("MM/");	//月
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x2);
            }

            if (showform[2]){
                strText+=("dd/");	//日
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x2);
            }
            strText = strText.substring(strText.lastIndexOf('/'));
            j=j-1;
            m_gStuAsc[j].setM_byVaild(0);
        } else if(timeDateBean.getM_nClockType() == 2) {
            if (showform[0]) {
                strText+=(timeDateBean.getM_nYearType()==0?"yyyy年":"yy年");//年
                if(timeDateBean.getM_nYearType()==0) {
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('Y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                } else{
                    m_gStuAsc[j].setM_byAsc('y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j].setM_byAsc('y');
                    m_gStuAsc[j++].setM_byVaild(0x1);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                    m_gStuAsc[j++].setM_byVaild(0x2);
                }
            }

            if (showform[1]){
                strText+="MM月";//月
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('M');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x2);
            }
            if (showform[2]){
                strText+=("dd日");//日
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j].setM_byAsc('D');
                m_gStuAsc[j++].setM_byVaild(0x1);
                m_gStuAsc[j++].setM_byVaild(0x2);
                m_gStuAsc[j++].setM_byVaild(0x2);
            }
        }

        if( (showform[0]) || (showform[1]) || (showform[2]) ){
            if(timeDateBean.getM_nRowType()==0){
                if( (showform[3]) || (showform[4]) ){
                    strText += "";
                    m_gStuAsc[j++].setM_byVaild(0x2);
                }
            }
        }
        return strText;
    }


    /*
    获取星期字符串
    */
    private static String GetWeekText(Calendar calendar, TimeDateBean timeDateBean, boolean[] showform) {
        int i, j = 0;
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        for (i = 0; i < 100; i++) {
            if (m_gStuAsc[i].getM_byVaild() == 0) {
                j = i;
                break;
            }
        }

        if (showform[3] == false) {
            return "";
        }

        String strText ="";
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:	strText=("星期一"); break;
            case 2:	strText=("星期二"); break;
            case 3:	strText=("星期三"); break;
            case 4:	strText=("星期四"); break;
            case 5:	strText=("星期五"); break;
            case 6:	strText=("星期六"); break;
            case 7:	strText=("星期日"); break;
        }

        m_gStuAsc[j++].setM_byVaild(0x10);
        m_gStuAsc[j++].setM_byVaild(0x2);

        m_gStuAsc[j++].setM_byVaild(0x10);
        m_gStuAsc[j++].setM_byVaild(0x2);

        m_gStuAsc[j].setM_byAsc('W');
        m_gStuAsc[j++].setM_byVaild(0x11);
        m_gStuAsc[j].setM_byAsc('W');
        m_gStuAsc[j++].setM_byVaild(0x1);

        if( (showform[3])  ){
            if(timeDateBean.getM_nRowType()==0){
                if(showform[4]){
                    strText+="";
                    m_gStuAsc[j++].setM_byVaild(0x2);
                }
            }
        }
        return strText;
    }


    /*
       获取时间字符串
       */
    private static String GetTimeText(Calendar calendar, TimeDateBean timeDateBean, boolean[] showform) {
        int i, j = 0;
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        for (i = 0; i < 100; i++) {
            if (m_gStuAsc[i].getM_byVaild() == 0){
                j = i;
                break;
            }
        }
        String strText = "";
        if (showform[4]) {
            strText += (timeDateBean.getM_nClockType() == 2 ? ("HH点") : ("HH:"));
            m_gStuAsc[j].setM_byAsc('h');
            m_gStuAsc[j++].setM_byVaild(0x1);
            m_gStuAsc[j].setM_byAsc('h');
            m_gStuAsc[j++].setM_byVaild(0x1);


            if (timeDateBean.getM_nClockType() == 2) {
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x02);
            } else {
                m_gStuAsc[j++].setM_byVaild(02);
            }
        }

        if (showform[5]) {
            strText += (timeDateBean.getM_nClockType() == 2 ? ("MM分"):("MM:"));//分
            m_gStuAsc[j].setM_byAsc('m');
            m_gStuAsc[j++].setM_byVaild(0x1);
            m_gStuAsc[j].setM_byAsc('m');
            m_gStuAsc[j++].setM_byVaild(0x1);

            if (timeDateBean.getM_nClockType() == 2) {
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x02);
            } else {
                m_gStuAsc[j++].setM_byVaild(02);
            }
        }
        if (showform[6]) {
            strText += (timeDateBean.getM_nClockType()==2?("ss秒"):("ss:"));//秒
            m_gStuAsc[j].setM_byAsc('s');
            m_gStuAsc[j++].setM_byVaild(0x1);
            m_gStuAsc[j].setM_byAsc('s');
            m_gStuAsc[j++].setM_byVaild(0x1);

            if (timeDateBean.getM_nClockType() == 2) {
                m_gStuAsc[j++].setM_byVaild(0x10);
                m_gStuAsc[j++].setM_byVaild(0x02);
            } else {
                m_gStuAsc[j++].setM_byVaild(02);
            }
        }
        return timeDateBean.getM_nClockType()== 2 ? strText : strText.substring(strText.lastIndexOf(':'));
    }

    /*
       ASc类属性赋值,int  dex 属性， 星期str后不带时间str 时，ilen 必须+1 ，否则报错
     */
    private static void ClockTextOut(int x, int y, String strTextOut, int crColor, TimeDateBean timeDateBean, int dex) {
        int i;
        int byColor;
        if (crColor == 2) {
            byColor = 1;        //1 2 3  分别是红绿黄  在android数组里，我设定相反
        } else if (crColor == 1) {
            byColor = 2;
        } else if (crColor == 0) {
            byColor = 3;
        } else {
            byColor = 1;
        }

        int iLen = strTextOut.length();
        if (dex == 1 && strTextOut.indexOf(" ") == -1) {  ///这段逻辑 可能存在错误
            iLen++;
            strTextOut += " ";
        }
        Log.d("test", "循环次数" + iLen + " " + dex + " " + strTextOut.indexOf(" "));
        String str = "";
        int m_iAscPosition = 0;
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        for (i = 0; i < iLen; i++) {
            if (m_gStuAsc[m_iAscPosition].getM_byVaild() == 0x10) {
                str += strTextOut.charAt(i);
                m_iAscPosition++;
            } else if (m_gStuAsc[m_iAscPosition].getM_byVaild() == 0x11){

                str += strTextOut.charAt(i);
                m_gStuAsc[m_iAscPosition].setM_byColor(byColor);
                m_gStuAsc[m_iAscPosition].setM_wx(x);
                m_gStuAsc[m_iAscPosition].setM_wy(y);
                m_gStuAsc[m_iAscPosition].setM_byVaild(0x80);
                m_iAscPosition++;
                m_gStuAsc[m_iAscPosition].setM_byColor(byColor);
                m_gStuAsc[m_iAscPosition].setM_byVaild(0x80);
                m_iAscPosition++;
            } else if ((m_gStuAsc[m_iAscPosition].getM_byVaild() & 0x01) == 1){

                str += strTextOut.charAt(i);
                m_gStuAsc[m_iAscPosition].setM_byColor(byColor);
                m_gStuAsc[m_iAscPosition].setM_wx(x);
                m_gStuAsc[m_iAscPosition].setM_wy(y);
                m_gStuAsc[m_iAscPosition].setM_byVaild(0x80);
                m_iAscPosition++;
            } else {
                str += strTextOut.charAt(i);
                if (str == ""){
                    str = "a";
                }
                m_iAscPosition++;
            }
            x += timeDateBean.getPaint().measureText(str);
            str = "";
        }

    }
}
