package com.eqled.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.eqled.bese.BaseFragment;
import com.eqled.custom.CustomPopWindow;
import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.NewWorkActivity;
import com.eqled.eqledcontrol.R;
import com.eqled.network.ConnectControlCard;
import com.eqled.network.SendPacket;
import com.eqled.utils.BasicOrder;
import com.eqled.utils.InterfaceConnect;
import com.eqled.utils.Utils;

public class ControlFragment extends BaseFragment {
	private LinearLayout wifi;
	private LinearLayout isopen;
	private CustomPopWindow customPopWindow;
	private LinearLayout open;  // 打开屏幕
	private LinearLayout close;
	private Dialog builder;
	private LinearLayout brightness;   //亮度调节
	private TextView bright_text;   //选择的亮度值
	private SeekBar seekBar;
	private LinearLayout bright_setting;//发送亮度设定
	private int brightness_int = 5;    // 亮度 默认值为15
	private LinearLayout bright_time;  //定时亮度设置
	private LinearLayout bright_load;  //读取连接的led屏当前的亮度
	private LinearLayout timecollatin;  //时间校正
	private ConnectControlCard ccc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_control, container, false);
		initView(v);
		initData();
		return v;
	}

	@Override
	protected void initView(View v) {
		wifi = (LinearLayout) v.findViewById(R.id.setting_wifi);
		wifi.setOnClickListener(this);
		isopen = (LinearLayout) v.findViewById(R.id.control_isopen);
		isopen.setOnClickListener(this);
		brightness = (LinearLayout) v.findViewById(R.id.contrll_brightness);
		brightness.setOnClickListener(this);
		timecollatin = (LinearLayout) v.findViewById(R.id.control_timecollatin);
		timecollatin.setOnClickListener(this);

	}

	@Override
	protected void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.setting_wifi:

				Intent intent = new Intent(getActivity(), NewWorkActivity.class);
				getActivity().startActivity(intent);
				break;

			case R.id.control_isopen:
				if (customPopWindow == null) {   //可以选择不复用，减少开销
					customPopWindow = new CustomPopWindow(getActivity(), R.id.main);
					View view = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_isopen, null);
					open = (LinearLayout) view.findViewById(R.id.popu_open);
					open.setOnClickListener(this);
					close = (LinearLayout) view.findViewById(R.id.popu_close);
					close.setOnClickListener(this);
					customPopWindow.setView(view, 1.0f, 0.3f);
				}
				customPopWindow.show();
				break;

			case R.id.popu_open:
				Toast_UI.toast(getActivity(), getActivity().getString(R.string.control_open_screen));
				customPopWindow.dismiss();
				byte[] openScreen = BasicOrder.openScreen();
				ccc = new ConnectControlCard(0, openScreen, openScreen.length);
				new Thread(ccc).start();

				break;
			case R.id.popu_close:
				customPopWindow.dismiss();
				Toast_UI.toast(getActivity(), getActivity().getString(R.string.control_close_screen));
				byte[] closeScreen = BasicOrder.closeScreen();
				ccc = new ConnectControlCard(0, closeScreen, closeScreen.length);
				new Thread(ccc).start();
				break;

			case R.id.contrll_brightness:
				brightness_int = 5; //初始化
				builder = new AlertDialog.Builder(getActivity(), R.style.dialog).create();
				builder.show();
				builder.getWindow().setContentView(getBrightDialogView());
				break;
			case R.id.bright_setting:
				builder.dismiss();
				Toast_UI.toast(getActivity(), getActivity().getString(R.string.control_send_brightness) + brightness_int);
				byte[] setbrightness = BasicOrder.setbrightness(brightness_int);
				ccc = new ConnectControlCard(0, setbrightness, setbrightness.length);
				new Thread(ccc).start();
				break;
			case R.id.bright_time:
				builder.dismiss();  //暂时屏蔽
				Toast_UI.toast(getActivity(), "跳转到定时亮度设置");
				break;
			case R.id.bright_load:
				Toast_UI.toast(getActivity(), getActivity().getString(R.string.control_load_data));
				byte[] getbrightness = BasicOrder.getbrightness();
				ccc = new ConnectControlCard(0, getbrightness, getbrightness.length, new InterfaceConnect() {
					@Override
					public void success(byte[] result) {
						seekBar.setProgress((int) result[19]);
					}

					@Override
					public void failure(int stateCode) {

					}
				});
				new Thread(ccc).start();
				break;
			case R.id.control_timecollatin:
				byte[] time = BasicOrder.timecollatin();
				//System.out.println("时间校对" + SendPacket.bytes2HexString(time, time.length));
				Toast_UI.toast(getActivity(), getActivity().getString(R.string.control_time_change));
				ccc = new ConnectControlCard(0, time, time.length);
				new Thread(ccc).start();
				break;

		}

	}


	/*
	  获得亮度对话框View
	 */
	protected View getBrightDialogView() {
		View dialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_brightness, null);
		bright_text = (TextView) dialog.findViewById(R.id.bright_text);
		seekBar = (SeekBar) dialog.findViewById(R.id.bright_seekBar);
		bright_setting = (LinearLayout) dialog.findViewById(R.id.bright_setting);
		bright_setting.setOnClickListener(this);
		bright_time = (LinearLayout) dialog.findViewById(R.id.bright_time);
		bright_time.setOnClickListener(this);
		bright_load = (LinearLayout) dialog.findViewById(R.id.bright_load);
		bright_load.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				bright_text.setText(progress + "");
				brightness_int = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		return dialog;
	}


}
