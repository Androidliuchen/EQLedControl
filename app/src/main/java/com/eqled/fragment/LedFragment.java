package com.eqled.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.eqled.adapter.SpinnerAdapter;
import com.eqled.bese.BaseFragment;
import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.NewWorkActivity;
import com.eqled.eqledcontrol.R;
import com.eqled.network.ConnectControlCard;
import com.eqled.network.SendPacket;
import com.eqled.utils.HttpUrlConn;
import com.eqled.utils.InterfaceConnect;
import com.eqled.utils.WindowSizeManager;

/**
 * Created by Administrator on 2016/5/26.
 */
public class LedFragment extends BaseFragment {

    private Spinner spinner1;
    private Spinner spinner2;
    private static String[] scWidth;
    private static String[] scHeight;
    private RadioGroup radioGroupData;                // Data Group
    private RadioGroup radioGroupColor;                // Color Group
    private RadioGroup radioGroupLine;                // Line Group
    private RadioGroup[] radioGroups;
    private Button send;
    //定义一个布局
    private static int dataCheckState = 1;            // 标记 data 选中状态
    private static int colorCheckState = 1;            // 标记 Color 选中状态
    private static int lineCheckState = 1;            // 标记 Line 选中状态
    private static int myWidth = 0;
    private static int myHight = 0;
    private ConnectControlCard ccc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_led, container, false);
        initView(v);
        initData();
        return v;
    }

    @Override
    protected void initView(View v) {
        spinner1 = (Spinner) v.findViewById(R.id.led_spinner1);

        spinner2 = (Spinner) v.findViewById(R.id.led_spinner2);
        send = (Button) v.findViewById(R.id.led_send);
        send.setOnClickListener(this);
        radioGroupColor = (RadioGroup) v.findViewById(R.id.rg3);
        radioGroupData = (RadioGroup) v.findViewById(R.id.rg2);
        radioGroupLine = (RadioGroup) v.findViewById(R.id.rg4);
        radioGroups = new RadioGroup[]{radioGroupColor, radioGroupData, radioGroupLine};
    }

    @Override
    protected void initData() {
        //读取宽度在数组的位置
        // 宽度设置
        WindowSizeManager windowSizeManager = WindowSizeManager.getSahrePreferencePosition(getContext());
        // Log.d("获取到的宽高",""+windowSizeManager.getWindowWidth()+" "+windowSizeManager.getWindowHeight() );
        scWidth = getResources().getStringArray(R.array.scrn_width);
        ArrayAdapter<String> width_Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, scWidth);
        spinner1.setAdapter(new SpinnerAdapter(getActivity(), scWidth));

        spinner1.setSelection(windowSizeManager.getWindowWidth());
        // 高度设置
        scHeight = getResources().getStringArray(R.array.scrn_hight);
        ArrayAdapter<String> hight_Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, scHeight);
        spinner2.setAdapter(new SpinnerAdapter(getActivity(), scHeight));
        spinner2.setSelection(windowSizeManager.getWindowHeight());
    }

    @Override
    public void onClick(View v) {
        sendParameter();
    }

    /**
     * 设置用户选中的值
     */
    private void setParameter() {
        if (spinner1.getSelectedItem() != null) {
            myWidth = Integer.parseInt(spinner1.getSelectedItem().toString());
        }
        if (spinner2.getSelectedItem() != null) {
            myHight = Integer.parseInt(spinner2.getSelectedItem().toString());
        }
        for (int i = 0; i < radioGroups.length; i++) {
            switch (radioGroups[i].getCheckedRadioButtonId()) {
                case R.id.colorDoubleBut:
                    colorCheckState = 0;
                    break;
                case R.id.colorOneBut:
                    colorCheckState = 1;
                    break;
                case R.id.datatrueBut:
                    dataCheckState = 0;
                    break;
                case R.id.datafalseBut:
                    dataCheckState = 1;
                    break;
                case R.id.line_rb1:
                    lineCheckState = 2;
                    break;
                case R.id.line_rb2:
                    lineCheckState = 0;
                    break;
                case R.id.line_rb3:
                    lineCheckState = 1;
                    break;
            }

        }


    }

    /*
       发送参数到LED屏幕
     */
    private void sendParameter() {
        setParameter();
        if (HttpUrlConn.wifiIP  == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.eqalert);//设置图标
            builder.setMessage("IP地址，网络连接失败！");//设置对话框的内容
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
            AlertDialog b = builder.create();
            b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
        }else {
            Toast_UI.toast(getActivity(), getActivity().getString(R.string.hardware_send));
        }
        WindowSizeManager.setSharedPreference(getContext(), spinner1.getSelectedItemPosition(), spinner2.getSelectedItemPosition());
        //      Log.d("bug检测",spinner1.getSelectedItemPosition()+" "+spinner2.getSelectedItemPosition());
        byte[] ScreenParameter =   //组织数据包
                SendPacket.setScreenParameterPkg(myWidth, myHight, colorCheckState, lineCheckState, dataCheckState);
        byte[] datapkg = SendPacket.dataPkg(ScreenParameter);  //添加固定包头包尾包校验
        ccc = new ConnectControlCard(0, datapkg, datapkg.length, new InterfaceConnect() {
            @Override
            public void success(byte[] result) {
                if (result[13] == 0) {   //返回的命令为00 表示加载成功
                    WindowSizeManager.setSharedPreference(getContext(), spinner1.getSelectedItemPosition(), spinner2.getSelectedItemPosition());

                }
            }

            @Override
            public void failure(int stateCode) {

            }
        });
        new Thread(ccc).start();
    }

}
