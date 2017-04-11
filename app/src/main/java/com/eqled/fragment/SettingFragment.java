package com.eqled.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eqled.bese.BaseFragment;
import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.AboutActivity;
import com.eqled.eqledcontrol.NewWorkActivity;
import com.eqled.eqledcontrol.R;
import com.eqled.eqledcontrol.ScreenparametersActivity;
import com.eqled.eqledcontrol.WifiActivity;
import com.eqled.utils.HttpUrlConn;
import com.eqled.utils.LanguageUtil;

public class SettingFragment extends BaseFragment {
    private View v;
    private LinearLayout screenparameters;
    private LinearLayout init;  //硬件复位
    private LinearLayout language;
    private LinearLayout about;
    private LinearLayout exit;//
    private LinearLayout wifi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(v);
        initData();
        return v;
    }

    @Override
    protected void initView(View v) {
        screenparameters = (LinearLayout) v.findViewById(R.id.setting_screenparameters);
        screenparameters.setOnClickListener(this);
        wifi = (LinearLayout) v.findViewById(R.id.setting_wifi);
        wifi.setOnClickListener(this);
        init = (LinearLayout) v.findViewById(R.id.setting_init);
        init.setOnClickListener(this);
        language = (LinearLayout) v.findViewById(R.id.setting_language);
        language.setOnClickListener(this);
        about = (LinearLayout) v.findViewById(R.id.setting_about);
        about.setOnClickListener(this);
        exit = (LinearLayout) v.findViewById(R.id.setting_exit);
        exit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_screenparameters:
                Intent intent = new Intent(getActivity(), ScreenparametersActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.setting_wifi:
                if(HttpUrlConn.wifiIP == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.eqalert);//设置图标
                    builder.setTitle("请先连接控制卡WiFi！");//设置对话框的标题
                    builder.setMessage("是否要连接WiFi？");//设置对话框的内容
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), NewWorkActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                    AlertDialog b = builder.create();
                    b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
                }else {
                    Intent intent2 = new Intent(getActivity(), WifiActivity.class);
                    getActivity().startActivity(intent2);
                }

            case R.id.setting_init:
                //暂时屏蔽
                //Toast_UI.toast(getActivity(),"复位成功");
                break;
            case R.id.setting_language:
                final String[] arrayFruit = new String[]{getString(R.string.setting_language_cn),
                        getString(R.string.setting_language_en)};
                AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
                dia.setTitle(getString(R.string.setting_language_select))
                        .setIcon(R.drawable.eq_xuanzhe).setItems(arrayFruit,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                Toast.makeText(getActivity(),
                                        arrayFruit[which],
                                        Toast.LENGTH_SHORT).show();
                                if (which == 1) {
                                    LanguageUtil.swithLanguage(
                                            getActivity(), "en");
                                } else if (which == 0) {
                                    LanguageUtil.swithLanguage(
                                            getActivity(), "zh-rCN");
                                }
                            }
                        }).setNegativeButton(getString(R.string.setting_cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        });

                dia.show();

                break;
            case R.id.setting_about:
                Intent intent1 = new Intent();
                Context context = getActivity();
                intent1.setClass(context, AboutActivity.class);
                context.startActivity(intent1);
                break;
            case R.id.setting_exit:
                getActivity().finish();
                System.exit(0);
                break;
        }

    }
}
