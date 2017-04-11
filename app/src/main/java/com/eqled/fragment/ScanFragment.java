package com.eqled.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.eqled.adapter.SpinnerAdapter;
import com.eqled.bese.BaseFragment;
import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.R;
import com.eqled.network.ConnectControlCard;
import com.eqled.network.SendPacket;
import com.eqled.utils.BasicOrder;
import com.eqled.utils.HttpUrlConn;
import com.eqled.utils.InterfaceConnect;
import com.eqled.utils.Utils;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ScanFragment extends BaseFragment implements OnCheckedChangeListener {

    private static final String TAG = "ScanUpdateFragment";
    // 常用扫描方式   扫描方式      频率 listview 数据
    private static String[] scanOftenArray = null;
    private static String[] scanWayArray = null;
    private static String[] frequencyArray = null;
    private static String[] scanListParameter = null;
    private Spinner scanOftenSpinner, scanWaySpinner, frequencySpinner;    // 常用扫描方式    扫描方式    频率 spinner
    private static String[][] scanWayArray_inif = null;
    private String scanOftenItem = "";
    private String scanWayItem = "";
    private String scanWayListItem = "";
    private Button send_scan;
    private ConnectControlCard ccc;

    private ListView scanListview;
    private TextView thisScanWayText;

    private CheckBox lineProtectCheckBox, constantChipCheckBox, nullCompileYardCheckBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_scan, container, false);
        initView(v);
        initData();
        return v;
    }

    @Override
    protected void initView(View v) {
        scanOftenSpinner = (Spinner) v.findViewById(R.id.scanOftenSpinner);
        scanWaySpinner = (Spinner) v.findViewById(R.id.scanWaySpinner);
        frequencySpinner = (Spinner) v.findViewById(R.id.frequencySpinner);
        scanListview = (ListView) v.findViewById(R.id.scanListView);
        thisScanWayText = (TextView) v.findViewById(R.id.thisScanWayText);
        lineProtectCheckBox = (CheckBox) v.findViewById(R.id.lineProtectCheckBox);
        constantChipCheckBox = (CheckBox) v.findViewById(R.id.constantChipCheckBox);
        nullCompileYardCheckBox = (CheckBox) v.findViewById(R.id.nullCompileYardCheckBox);
        lineProtectCheckBox.setOnCheckedChangeListener(this);
        constantChipCheckBox.setOnCheckedChangeListener(this);
        nullCompileYardCheckBox.setOnCheckedChangeListener(this);
        send_scan = (Button) v.findViewById(R.id.scan_send);
        send_scan.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        // 向 数组 添加数据
        scanOftenArray = getResources().getStringArray(R.array.scanOftenArray);
        scanWayArray = getResources().getStringArray(R.array.scanWayArray);
        frequencyArray = getResources().getStringArray(R.array.frequencyArray);

        scanListParameter = getResources().getStringArray(R.array.scan_16_Array);

        String[] scanWayArray1 = getResources().getStringArray(R.array.scanWayArray1);
        scanWayArray_inif = new String[][]{scanWayArray, scanWayArray1};

        bindingSpinnerData();

    }

    /**
     * 向 Spinner 绑定数据
     */
    private void bindingSpinnerData() {
        // scanOften_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, scanOftenArray);
        //  scanOften_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  	// 设置下拉列表的风格

        scanOftenSpinner.setAdapter(new SpinnerAdapter(getActivity(), scanOftenArray));                                                // 绑定adapter
        scanOftenSpinner.setOnItemSelectedListener(new scanOftenSelectedListener());                // 获取点击事件

        //  scanWay_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, scanWayArray);
        //  scanOften_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  	// 设置下拉列表的风格
        scanWaySpinner.setAdapter(new SpinnerAdapter(getActivity(), scanWayArray));                                                    // 绑定adapter
        scanWaySpinner.setOnItemSelectedListener(new scanWaySelectedListener());                    // 获取点击事件

        //  frequency_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, frequencyArray);
        //  scanOften_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  	// 设置下拉列表的风格
        frequencySpinner.setAdapter(new SpinnerAdapter(getActivity(), frequencyArray));                                                // 绑定adapter

        //  scanList_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, scanListParameter);
        //  scanList_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scanListview.setAdapter(new SpinnerAdapter(getActivity(), scanListParameter));
        scanListview.setOnItemClickListener(new scanWayListSelectedListener());
    }

    @Override
    public void onClick(View v) {
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
        if (v.getId() == R.id.scan_send) {
            if (scanOftenSpinner.getSelectedItemPosition() == 0) {
                byte[] scan = BasicOrder.getScanType1();
                ccc = new ConnectControlCard(0, scan, scan.length);
                new Thread(ccc).start();
            } else if (scanOftenSpinner.getSelectedItemPosition() == 1) {
                byte[] scan = BasicOrder.getScanType2();
                ccc = new ConnectControlCard(0, scan, scan.length);
                new Thread(ccc).start();
            }
        }

       /* Log.d("屏幕实际的分辨率",""+ Utils.getUiwidth(getActivity())+" : "
                +(Utils.getUiheight(getActivity())+Utils.getStatusHeight(getContext())));*/

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.lineProtectCheckBox:
                if (isChecked) {
                    System.out.println("选中  行保护.....");
                } else {
                    System.out.println("取消选中  行保护.....");
                }
                break;
            case R.id.constantChipCheckBox:
                if (isChecked) {
                    System.out.println("选中  恒流源芯片.....");
                } else {
                    System.out.println("取消选中 恒流源芯片.....");
                }
                break;
            case R.id.nullCompileYardCheckBox:
                if (isChecked) {
                    System.out.println("选中  无译码.....");
                } else {
                    System.out.println("取消选中  无译码.....");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 常用扫描方式
     *
     * @author Administrator
     */
    class scanOftenSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            scanOftenItem = scanOftenArray[position];
            int pos = scanOftenSpinner.getSelectedItemPosition();
            if (pos != 0) {
                pos = 1;
            } else {
                pos = 0;
            }
            // 重新绑定数据
            //scanWay_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, scanWayArray_inif[pos]);
            scanWaySpinner.setAdapter(new SpinnerAdapter(getActivity(), scanWayArray_inif[pos]));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    /**
     * 扫描方式
     *
     * @author Administrator
     */
    class scanWaySelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            scanWayItem = (String) scanWaySpinner.getItemAtPosition(position);
            //   scanList_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getScanListData(scanWayItem));
            //   scanList_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            scanListview.setAdapter(new SpinnerAdapter(getActivity(), getScanListData(scanWayItem)));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class scanWayListSelectedListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            scanWayListItem = scanListParameter[position];
            thisScanWayText.setText("");
            thisScanWayText.setTextColor(Color.RED);
            thisScanWayText.setTextSize(14);
            String str = "当前选择: " + scanOftenItem + "," + scanWayItem + "," + scanWayListItem;
            thisScanWayText.setText(str);
        }
    }

    /**
     * 根据扫描方式 加载扫描参数 方法
     *
     * @param str
     * @return
     */
    private String[] getScanListData(String str) {
        scanListParameter = null;
        if (str.trim().equals("16扫")) {
            scanListParameter = getResources().getStringArray(R.array.scan_16_Array);
        } else if (str.trim().equals("8扫")) {
            scanListParameter = getResources().getStringArray(R.array.scan_8_Array);
        } else if (str.trim().equals("4扫")) {
            scanListParameter = getResources().getStringArray(R.array.scan_4_Array);
        } else if (str.trim().equals("2扫")) {
            scanListParameter = getResources().getStringArray(R.array.scan_2_Array);
        } else if (str.trim().equals("静态")) {
            scanListParameter = getResources().getStringArray(R.array.scan_static_Array);
        } else {
            Toast_UI.toast(getActivity(), "未知错误....");
        }
        return scanListParameter;
    }
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Toast_UI.toast(getActivity(), "点击了返回键....");
            return true;
        }
        return this.onKeyDown(keyCode, event);
    }*/
}
