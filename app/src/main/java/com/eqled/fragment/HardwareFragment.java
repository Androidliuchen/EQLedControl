package com.eqled.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.eqled.bese.BaseFragment;
import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.R;
import com.eqled.network.ConnectControlCard;
import com.eqled.network.SendPacket;
import com.eqled.utils.HttpUrlConn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/26.
 */
public class HardwareFragment extends BaseFragment {

    private int getBaudStr = 1; // 存储选中的spinner的值
    private static final String TAG = "HardwareUpdateFragment";
    private RadioGroup baudRadioGroup; // 串口波特率
    private EditText cardAddressSet, ipSet, portSet, subnetSet, gatewaySet; // card地址,ip地址,端口号,子网掩码,默认网关
    private Button send;
    private ConnectControlCard ccc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_hardware, container, false);
        initView(v);
        initData();
        return v;
    }

    @Override
    protected void initView(View v) {
        baudRadioGroup = (RadioGroup) v.findViewById(R.id.hardware_bt);
        cardAddressSet = (EditText) v.findViewById(R.id.cardAddressSet);
        ipSet = (EditText) v.findViewById(R.id.ipSet);
        portSet = (EditText) v.findViewById(R.id.portSet);
        subnetSet = (EditText) v.findViewById(R.id.subnetSet);
        gatewaySet = (EditText) v.findViewById(R.id.gatewaySet);
        send = (Button) v.findViewById(R.id.hardware_send);
        send.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        sendParameter();
    }

    private void sendParameter() {   //写的不好，需要优化
        Log.e(TAG, sendHardwarePkg());
        if (ParameterCheck()) {
            // Toast_UI.toast(getActivity(), "参数合法");
            //
            byte[] ip;
            int ipport;
            byte[] IPSubMask;
            byte[] IPGateWay;
            int CardAddress;
            int SerialBaud;
            if (ipSet.getText().toString().equals("")) {
                ip = splitIp(ipSet.getHint().toString());
            } else {
                ip = splitIp(ipSet.getText().toString());
            }
            if (portSet.getText().toString().equals("")) {
                ipport = Integer.valueOf(portSet.getHint().toString());
            } else {
                ipport = Integer.valueOf(portSet.getText().toString());
            }
            if (subnetSet.getText().toString().equals("")) {
                IPSubMask = splitIp(subnetSet.getHint().toString());
            } else {
                IPSubMask = splitIp(subnetSet.getText().toString());
            }
            if (gatewaySet.getText().toString().equals("")) {
                IPGateWay = splitIp(gatewaySet.getHint().toString());
            } else {
                IPGateWay = splitIp(gatewaySet.getText().toString());
            }
            if (cardAddressSet.getText().toString().equals("")) {
                CardAddress = Integer.valueOf(cardAddressSet.getHint()
                        .toString());
            } else {
                CardAddress = Integer.valueOf(cardAddressSet.getText()
                        .toString());
            }
            isSerialBaud();
            SerialBaud = getBaudStr;
            byte[] by = SendPacket.setHardwareParameterPkg(ip, ipport,
                    IPSubMask, IPGateWay, CardAddress, SerialBaud);
            byte[] bys = SendPacket.dataPkg(by);
            // Log.d("硬件数据包", SendPacket.bytes2HexString(bys, bys.length));
            ccc = new ConnectControlCard(0, bys, bys.length);
            new Thread(ccc).start();
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
        }

    }

    // 获取发送的内容
    private String sendHardwarePkg() {
        StringBuffer sb = new StringBuffer();
        sb.append((cardAddressSet.getText().toString().equals("") ? cardAddressSet
                .getHint().toString() : cardAddressSet.getText().toString())
                + ",  ");
        isSerialBaud();
        sb.append(getBaudStr + ",  ");
        sb.append((ipSet.getText().toString().equals("") ? ipSet.getHint()
                .toString() : ipSet.getText().toString()) + ",  ");
        sb.append((portSet.getText().toString().equals("") ? portSet.getHint()
                .toString() : portSet.getText().toString()) + ",  ");
        sb.append((subnetSet.getText().toString().equals("") ? subnetSet
                .getHint().toString() : subnetSet.getText().toString()) + ",  ");
        sb.append((gatewaySet.getText().toString().equals("") ? gatewaySet
                .getHint().toString() : gatewaySet.getText().toString())
                + ",  ");
        String str = sb.toString();
        return str;
    }

    /**
     * 参数合法性检查 2016/05/03 hyman
     */
    private boolean ParameterCheck() {
        if (!cardAddressSet.getText().toString().equals("")
                && isNumeric(cardAddressSet.getText().toString()) == false) {
            Toast_UI.toast(getActivity(), getActivity().getString(R.string.hardware_cardAddress_hint));
            return false;
        }
        if (!portSet.getText().toString().equals("")
                && isNumeric(portSet.getText().toString()) == false) {
            Toast_UI.toast(getActivity(), getActivity().getString(R.string.hardware_port_hint));
            return false;
        }
        if (!ipSet.getText().toString().equals("")
                && isIp(ipSet.getText().toString()) == false) {
            Toast_UI.toast(getActivity(), getActivity().getString(R.string.hardware_ip_hint));
            return false;
        }
        if (!subnetSet.getText().toString().equals("")
                && isIp(subnetSet.getText().toString()) == false) {
            Toast_UI.toast(getActivity(), getActivity().getString(R.string.hardware_subnet_hint));
            return false;
        }
        if (!gatewaySet.getText().toString().equals("")
                && isIp(gatewaySet.getText().toString()) == false) {
            Toast_UI.toast(getActivity(), getActivity().getString(R.string.hardware_gateway_hint));
            return false;
        }
        return true;

    }

    /**
     * 字符是否为一个正确的的ip
     */
    public boolean isIp(String str) {
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        // 判断ip地址是否与正则表达式匹配
        if (str.matches(regex)) {

            return true;
        } else {

            return false;
        }

    }

    /*
     * 字符是否是否为数字
	 */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 分割ip，必须通过ip合法性检查，如果未通过，可能导致程序崩溃
     */
    public byte[] splitIp(String ipstr) {
        String[] arr = ipstr.replaceAll(" ", "").split("\\.");
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) Integer.parseInt(arr[i]);
        }
        return b;
    }

    /**
     * 判断串口波特率
     */
    private void isSerialBaud() {
        if (baudRadioGroup.getCheckedRadioButtonId() == R.id.hardware_bt1) {
            getBaudStr = 1;
        } else {
            getBaudStr = 0;
        }

    }

}
