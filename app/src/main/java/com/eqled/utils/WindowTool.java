package com.eqled.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eqled.adapter.SpinnerAdapter;
import com.eqled.adapter.SpinnerImageAdapter;
import com.eqled.bean.TextBean;
import com.eqled.bean.TimeDateBean;
import com.eqled.custom.Toast_UI;
import com.eqled.databasemanagement.ProgramBeanDao;
import com.eqled.databasemanagement.TextBeanDao;
import com.eqled.databasemanagement.TimeDateBeanDao;
import com.eqled.eqledcontrol.ProgramActivity;
import com.eqled.eqledcontrol.R;

/**
 * Created by Administrator on 2016/7/13.
 * <p>
 * <p>
 * 管理各类  对话框  弹出框  整合
 */
public class WindowTool {


    /*
     获取文本参数窗口，
   */
    public static View getTextWindow(final Context context, final TextBean textBean,
                                     final int windowWidth, final int windowHeight,
                                     final InterfaceDismiss interfaceDismiss) {
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.window_text, null);
        final EditText editText = (EditText) v.findViewById(R.id.window_text_content);
        final Spinner text_size = (Spinner) v.findViewById(R.id.window_text_size);
        final Spinner text_inmode = (Spinner) v.findViewById(R.id.window_text_inmode);
        final Spinner text_outmode = (Spinner) v.findViewById(R.id.window_text_outmode);
        final Spinner text_runtime = (Spinner) v.findViewById(R.id.window_text_runtime);
        final EditText text_speed = (EditText) v.findViewById(R.id.window_text_speed);
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.window_text_rg);
        final RadioButton radioButton1 = (RadioButton) v.findViewById(R.id.window_text_off);
        final RadioButton radioButton2 = (RadioButton) v.findViewById(R.id.window_text_on);
        final EditText text_w = (EditText) v.findViewById(R.id.window_text_w);
        final EditText text_h = (EditText) v.findViewById(R.id.window_text_h);
        final EditText text_x = (EditText) v.findViewById(R.id.window_text_x);
        final EditText text_y = (EditText) v.findViewById(R.id.window_text_y);
        final Spinner text_hcenter = (Spinner) v.findViewById(R.id.window_text_hcenter);
        final CheckBox text_vcenter = (CheckBox) v.findViewById(R.id.window_text_vcenter);
        final Spinner text_typeface = (Spinner) v.findViewById(R.id.window_text_typeface);
        //字体粗细
        if (textBean.isText_size_bold() == true) {
            radioButton2.setChecked(true);
        } else {
            radioButton1.setChecked(true);
        }
        //文本窗宽高
        if (textBean.getWindowWidth() != 0) {
            text_w.setText(textBean.getWindowWidth() + "");
            text_h.setText(textBean.getWindowHeight() + "");
        } else {
            text_w.setText(windowWidth + "");
            text_h.setText(windowHeight + "");
        }
        text_x.setText(textBean.getArea_X() + "");
        text_y.setText(textBean.getArea_y() + "");
        //字体选择
        text_typeface.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.typeface)));
        text_typeface.setSelection(textBean.getText_typeface());
        //字体大小
        text_size.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.text_size)));
        text_size.setSelection(textBean.getText_size_position());
        editText.setText(textBean.getText_content());
        editText.requestFocus();
        //文字水平方向
        text_hcenter.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.text_vcenter)));
        text_hcenter.setSelection(textBean.getText_hcenter());
        //文字垂直方向
        text_vcenter.setChecked(textBean.isText_vcenter());
        //进场方式
        text_inmode.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.text_inmode_string)));
        text_inmode.setSelection(textBean.getText_inmode_position());
        //出场方式
        text_outmode.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.text_outmode_string)));
        text_outmode.setSelection(textBean.getText_outmode_position());
        //运行时间
        text_runtime.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.text_runtime_string)));
        text_runtime.setSelection(textBean.getText_runtime_position());
        Dialog builder;
        builder = new Dialog(context, R.style.dialog);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //实时参数写入数据库
                boolean text_bold = false;
                if (radioGroup.getCheckedRadioButtonId() == R.id.window_text_on) {
                    text_bold = true;
                } else {
                    text_bold = false;
                }
                try {
                    if (Integer.parseInt(text_w.getText().toString()) + Integer.parseInt(text_x.getText().toString()) <= windowWidth
                            && Integer.parseInt(text_h.getText().toString()) + Integer.parseInt(text_y.getText().toString()) <= windowHeight) {
                        textBean.setWindowWidth(Integer.parseInt(text_w.getText().toString()));
                        textBean.setWindowHeight(Integer.parseInt(text_h.getText().toString()));
                        textBean.setArea_X(Integer.parseInt(text_x.getText().toString()));
                        textBean.setArea_y(Integer.parseInt(text_y.getText().toString()));
                    } else {
                        Toast_UI.toast(context, "参数超出边界，请重新设置");
                    }
                } catch (NumberFormatException exception) {
                }
                textBean.setText_typeface(text_typeface.getSelectedItemPosition());
                textBean.setText_content(editText.getText().toString());
                textBean.setText_size_bold(text_bold);
                textBean.setText_hcenter(text_hcenter.getSelectedItemPosition());
                textBean.setText_vcenter(text_vcenter.isChecked());
                textBean.setText_size_position(text_size.getSelectedItemPosition());
                textBean.setText_inmode_position(text_inmode.getSelectedItemPosition());
                textBean.setText_outmode_position(text_outmode.getSelectedItemPosition());
                textBean.setText_runtime_position(text_runtime.getSelectedItemPosition());
                if (!text_speed.getText().toString().equals("")) {
                    textBean.setText_duration_position(Integer.parseInt(text_speed.getText().toString()));
                }
                new TextBeanDao(context).update(textBean);
                textBean.getPaint().setTextSize(Integer.parseInt(text_size.getSelectedItem().toString()) + Constant.FONT_SIZE_CORRECTION); //实时改变画笔的字体大小
                textBean.getPaint().setFakeBoldText(text_bold);
                Utils.setTypeface(context, textBean.getPaint()
                        , (context.getResources().getStringArray(R.array.typeface_path))[textBean.getText_typeface()]);
                interfaceDismiss.dismiss(editText.getText().toString());

            }
        });
        builder.show();
        builder.getWindow().setContentView(v);
        Utils.setDialogW(context, builder);
        return v;
    }


    /**
     * 获取时间窗
     */
    public static View getTimeWindow(final Context context, final TimeDateBean timeDateBean, final int windowWidth, final int windowHeight,
                                     final InterfaceDismiss interfaceDismiss) {
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.window_time, null);
        final EditText wt_width = (EditText) v.findViewById(R.id.window_time_w);
        final EditText wt_height = (EditText) v.findViewById(R.id.window_time_h);
        final EditText wt_x = (EditText) v.findViewById(R.id.window_time_x);
        final EditText wt_y = (EditText) v.findViewById(R.id.window_time_y);
        final Spinner wt_timestyle = (Spinner) v.findViewById(R.id.window_time_style);
        final EditText wt_time_fixedtext = (EditText) v.findViewById(R.id.window_time_fixedtext);
        final Spinner wt_fixe_typeface = (Spinner) v.findViewById(R.id.window_time_typeface);
        final Spinner wt_fixe_color = (Spinner) v.findViewById(R.id.window_time_textcolor);
        final Spinner wt_fixe_size = (Spinner) v.findViewById(R.id.window_time_textsize);
        final CheckBox wt_year = (CheckBox) v.findViewById(R.id.window_time_year);
        final CheckBox wt_month = (CheckBox) v.findViewById(R.id.window_time_month);
        final CheckBox wt_day = (CheckBox) v.findViewById(R.id.window_time_day);
        final Spinner wt_datecolor = (Spinner) v.findViewById(R.id.window_time_datecolor);
        final CheckBox wt_week = (CheckBox) v.findViewById(R.id.window_time_week);
        final Spinner wt_weekcolor = (Spinner) v.findViewById(R.id.window_time_weekcolor);
        final CheckBox wt_hour = (CheckBox) v.findViewById(R.id.window_time_hour);
        final CheckBox wt_minute = (CheckBox) v.findViewById(R.id.window_time_minute);
        final CheckBox wt_second = (CheckBox) v.findViewById(R.id.window_time_second);
        final Spinner wt_timecolor = (Spinner) v.findViewById(R.id.window_time_timecolor);
        final RadioGroup yearformat = (RadioGroup) v.findViewById(R.id.window_time_yearformat);
        final RadioButton yearButton1 = (RadioButton) v.findViewById(R.id.window_time_fouryear);
        final RadioButton yearButton2 = (RadioButton) v.findViewById(R.id.window_time_twoyear);
        final RadioGroup lineformat = (RadioGroup) v.findViewById(R.id.window_time_lineformat);
        final RadioButton line1 = (RadioButton) v.findViewById(R.id.window_time_oneline);
        final RadioButton line2 = (RadioButton) v.findViewById(R.id.window_time_nline);
        final Spinner wt_offset = (Spinner) v.findViewById(R.id.window_time_offset);
        final EditText wt_daylag = (EditText) v.findViewById(R.id.window_time_daylag);
        final TextView wt_time = (TextView) v.findViewById(R.id.window_time_timelag);
        final LinearLayout numbercolck = (LinearLayout) v.findViewById(R.id.window_time_numberclock);
        final LinearLayout analogcolck = (LinearLayout) v.findViewById(R.id.window_time_analogclock);
        wt_timestyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    analogcolck.setVisibility(View.VISIBLE);
                    numbercolck.setVisibility(View.GONE);
                } else {
                    numbercolck.setVisibility(View.VISIBLE);
                    analogcolck.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //find 模拟时钟新增的参数
        final Spinner clockshape = (Spinner) v.findViewById(R.id.window_time_shape);
        final CheckBox weekshow = (CheckBox) v.findViewById(R.id.window_time_wkshow);
        final CheckBox datashow = (CheckBox) v.findViewById(R.id.window_time_dashow);
        final Spinner shibiaocolor = (Spinner) v.findViewById(R.id.window_time_shibiaocolor);
        final EditText shibiaox = (EditText) v.findViewById(R.id.window_time_shibiaox);
        final EditText shibiaoy = (EditText) v.findViewById(R.id.window_time_shibiaoy);
        final Spinner shibiaoshape = (Spinner) v.findViewById(R.id.window_time_shibiaoshape);
        final Spinner fenbiaocolor = (Spinner) v.findViewById(R.id.window_time_fenbiao);
        final EditText fenbiaox = (EditText) v.findViewById(R.id.window_time_fenbiaox);
        final EditText fenbiaoy = (EditText) v.findViewById(R.id.window_time_fenbiaoy);
        final Spinner fenbiaoshape = (Spinner) v.findViewById(R.id.window_time_fenbiaoshape);
        final Spinner hourcolor = (Spinner) v.findViewById(R.id.window_time_shizhen);
        final Spinner minutecolor = (Spinner) v.findViewById(R.id.window_time_fenzhen);
        final Spinner secondcolor = (Spinner) v.findViewById(R.id.window_time_miaozhen);

        //区域宽高 ，坐标
        if (timeDateBean.getWindowWidth() != 0) {
            wt_width.setText(timeDateBean.getWindowWidth() + "");
            wt_height.setText(timeDateBean.getWindowHeight() + "");
        } else {
            wt_width.setText(windowWidth + "");
            wt_height.setText(windowHeight + "");
        }
        wt_x.setText(timeDateBean.getArea_X() + "");
        wt_y.setText(timeDateBean.getArea_y() + "");
        //时钟样式
        wt_timestyle.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.colck_style)));
        wt_timestyle.setSelection(timeDateBean.getM_nClockType());
        //固定文字  固定文字字体，颜色，大小
        wt_time_fixedtext.setText(timeDateBean.getM_strClockText());
        wt_fixe_typeface.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.typeface)));
        wt_fixe_typeface.setSelection(timeDateBean.getNumber_typeface());
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.text_color);
        int[] color_id = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            color_id[i] = typedArray.getResourceId(i, 0);
        }
        wt_fixe_color.setAdapter(new SpinnerImageAdapter(context, color_id));
        wt_fixe_color.setSelection(timeDateBean.getM_rgbClockTextColor());
        wt_fixe_size.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.text_size)));
        wt_fixe_size.setSelection(timeDateBean.getM_rgbClockTextSize());
        //年月日，是否显示，颜色读取
        final CheckBox[] checkBoxes = {wt_year, wt_month, wt_day, wt_week, wt_hour, wt_minute, wt_second};
        final String m_strShowForm = timeDateBean.getM_strShowForm();
        boolean[] str_ints = timeDateBean.getStrShowFormInt(m_strShowForm);
        for (int i = 0; i < m_strShowForm.length(); i++) {
            checkBoxes[i].setChecked(str_ints[i]);
        }
        wt_datecolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        wt_datecolor.setSelection(timeDateBean.getM_rgbDayTextColor());
        wt_weekcolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        wt_weekcolor.setSelection(timeDateBean.getM_rgbWeekTextColor());
        wt_timecolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        wt_timecolor.setSelection(timeDateBean.getM_rgbTimeColor());
        //年格式，行格式  0 代表4位，单行  1代表2位
        if (timeDateBean.getM_nYearType() == 0) {
            yearButton1.setChecked(true);
        } else {
            yearButton2.setChecked(true);
        }
        if (timeDateBean.getM_nRowType() == 0) {
            line1.setChecked(true);
        } else {
            line2.setChecked(true);
        }
        //时差   日期  ，  天数  ， 时间
        wt_offset.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.colck_offset)));
        wt_offset.setSelection(timeDateBean.getM_nOffset());
        wt_daylag.setText(timeDateBean.getM_nDayLag() + "");
        wt_time.setText("" + timeDateBean.getM_strTimeLag());
        wt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View a = ((Activity) context).getLayoutInflater().inflate(R.layout.timeselector, null);
                final TimePicker timePicker = (TimePicker) a.findViewById(R.id.timePic1);
                timePicker.setIs24HourView(true);
                int[] times = timeDateBean.splitStrTimeLag(wt_time.getText().toString());
                timePicker.setCurrentHour(Integer.valueOf(times[0]));
                timePicker.setCurrentMinute(Integer.valueOf(times[1]));
                Dialog builder;
                builder = new Dialog(context, R.style.dialog);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        String hour = String.valueOf(timePicker.getCurrentHour());
                        String minute = String.valueOf(timePicker.getCurrentMinute());
                        if (hour.length() == 1) {
                            hour = "0" + hour;
                        }
                        if (minute.length() == 1) {
                            minute = "0" + minute;
                        }
                        String ss = hour + ":" + minute;
                        wt_time.setText(ss);
                    }
                });
                builder.show();
                builder.getWindow().setContentView(a);

            }
        });
        //模拟时钟参数填写
        //时钟形状，是否显示星期，日期等文字
        clockshape.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.colck_shape)));
        clockshape.setSelection(timeDateBean.getColckShape());
        weekshow.setChecked(timeDateBean.isWeekshow());
        datashow.setChecked(timeDateBean.isDateshow());
        //时标，颜色，宽高，形状
        shibiaocolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        shibiaocolor.setSelection(timeDateBean.getHourscolor());
        shibiaox.setText(timeDateBean.getShibiaox() + "");
        shibiaoy.setText(timeDateBean.getShibiaoy() + "");
        shibiaoshape.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.colck_zhizhenshape)));
        shibiaoshape.setSelection(timeDateBean.getShibiaoshape());
        //分标
        fenbiaocolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        fenbiaocolor.setSelection(timeDateBean.getFenbiaocolorposition());
        fenbiaox.setText(timeDateBean.getFenbiaox() + "");
        fenbiaoy.setText(timeDateBean.getFenbiaoy() + "");
        fenbiaoshape.setAdapter(new SpinnerAdapter(context, context.getResources().getStringArray(R.array.colck_zhizhenshape)));
        fenbiaoshape.setSelection(timeDateBean.getFenbiaoshape());
        //时针，分针，秒针的颜色
        hourcolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        hourcolor.setSelection(timeDateBean.getHourscolor());
        minutecolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        minutecolor.setSelection(timeDateBean.getMinutecolor());
        secondcolor.setAdapter(new SpinnerImageAdapter(context, color_id));
        secondcolor.setSelection(timeDateBean.getSecondcolor());

        //启动对话框
        Dialog builder;
        builder = new Dialog(context, R.style.dialog);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    if (Integer.parseInt(wt_width.getText().toString()) + Integer.parseInt(wt_x.getText().toString()) <= windowWidth
                            && Integer.parseInt(wt_height.getText().toString()) + Integer.parseInt(wt_y.getText().toString()) <= windowHeight) {
                        timeDateBean.setWindowWidth(Integer.parseInt(wt_width.getText().toString()));
                        timeDateBean.setWindowHeight(Integer.parseInt(wt_height.getText().toString()));
                        timeDateBean.setArea_X(Integer.parseInt(wt_x.getText().toString()));
                        timeDateBean.setArea_y(Integer.parseInt(wt_y.getText().toString()));
                    } else {
                        Toast_UI.toast(context, "参数超出边界，请重新设置");
                    }

                    //时标分表的xy值
                    timeDateBean.setShibiaox(Integer.parseInt(shibiaox.getText().toString()));
                    timeDateBean.setShibiaoy(Integer.parseInt(shibiaoy.getText().toString()));
                    timeDateBean.setFenbiaox(Integer.parseInt(fenbiaox.getText().toString()));
                    timeDateBean.setFenbiaoy(Integer.parseInt(fenbiaoy.getText().toString()));

                } catch (NumberFormatException exception) {

                }
                //模拟时钟参数存储
                timeDateBean.setColckShape(clockshape.getSelectedItemPosition());
                timeDateBean.setWeekshow(weekshow.isChecked());
                timeDateBean.setDateshow(datashow.isChecked());
                timeDateBean.setShibiaocolorposition(shibiaocolor.getSelectedItemPosition());
                timeDateBean.setShibiaoshape(shibiaoshape.getSelectedItemPosition());
                timeDateBean.setFenbiaocolorposition(fenbiaocolor.getSelectedItemPosition());
                timeDateBean.setFenbiaoshape(fenbiaoshape.getSelectedItemPosition());
                timeDateBean.setHourscolor(hourcolor.getSelectedItemPosition());
                timeDateBean.setMinutecolor(minutecolor.getSelectedItemPosition());
                timeDateBean.setSecondcolor(secondcolor.getSelectedItemPosition());

                //数值时钟参数存储
                timeDateBean.setM_nClockType(wt_timestyle.getSelectedItemPosition());
                timeDateBean.setM_strClockText(wt_time_fixedtext.getText().toString());
                timeDateBean.setNumber_typeface(wt_fixe_typeface.getSelectedItemPosition());
                timeDateBean.setM_rgbClockTextColor(wt_fixe_color.getSelectedItemPosition());
                timeDateBean.setM_rgbClockTextSize(wt_fixe_size.getSelectedItemPosition());
                //
                StringBuffer strShowForm = new StringBuffer();
                for (CheckBox cb : checkBoxes
                        ) {
                    if (cb.isChecked() == true) {
                        strShowForm.append("1");
                    } else {
                        strShowForm.append("0");
                    }
                }
                if(timeDateBean.getM_nClockType() == 3){
                    if(weekshow.isChecked()){
                        strShowForm.replace(3, 4, "1");
                    }else{
                        strShowForm.replace(3, 4, "0");
                    }
                    if(datashow.isChecked()){
                        strShowForm.replace(0, 3, "111");
                    }else{
                        strShowForm.replace(0, 3, "000");
                    }
                }

                timeDateBean.setM_strShowForm(strShowForm.toString());
                timeDateBean.setM_rgbDayTextColor(wt_datecolor.getSelectedItemPosition());
                timeDateBean.setM_rgbWeekTextColor(wt_weekcolor.getSelectedItemPosition());
                timeDateBean.setM_rgbTimeColor(wt_timecolor.getSelectedItemPosition());
                //
                if (yearformat.getCheckedRadioButtonId() == R.id.window_time_fouryear) {
                    timeDateBean.setM_nYearType(0);
                } else {
                    timeDateBean.setM_nYearType(1);
                }
                if (lineformat.getCheckedRadioButtonId() == R.id.window_time_oneline) {
                    timeDateBean.setM_nRowType(0);
                } else {
                    timeDateBean.setM_nRowType(1);
                }
                timeDateBean.setM_nOffset(wt_offset.getSelectedItemPosition());
                timeDateBean.setM_nDayLag(Integer.parseInt(wt_daylag.getText().toString()));
                timeDateBean.setM_strTimeLag(wt_time.getText().toString());
                new TimeDateBeanDao(context).update(timeDateBean);
                //获取画笔属性
                Paint paint = Utils.getPaint(context, Utils.getPaintSize(context, Integer.parseInt(context.getResources().getStringArray(R.array.text_size)[timeDateBean.getM_rgbClockTextSize()])
                        + Constant.FONT_SIZE_CORRECTION));
                Utils.setTypeface(context, paint
                        , (context.getResources().getStringArray(R.array.typeface_path))[timeDateBean.getNumber_typeface()]);
                paint.setTextSize(Utils.getPaintSize(context, timeDateBean.getM_rgbClockTextSize() + Constant.FONT_SIZE_CORRECTION)); // 字体大小 进度条参数
                timeDateBean.setPaint(paint);
                interfaceDismiss.dismiss("");


            }
        });
        builder.show();
        builder.getWindow().setContentView(v);
        Utils.setDialogW(context, builder);
        return v;
    }

    public interface InterfaceDismiss {

        void dismiss(String content);

    }


}
