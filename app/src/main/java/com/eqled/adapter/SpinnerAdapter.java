package com.eqled.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eqled.eqledcontrol.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
	private String[] mWifiList;
	private Context context;

	public SpinnerAdapter(Context context, String[] mWifiList) {
		super();
		this.context = context;
		this.mWifiList = mWifiList;
	}



	@Override
	public int getCount() {
		if (mWifiList != null) {
			return mWifiList.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mWifiList != null) {
			return mWifiList[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {

		public TextView tv;
	}

	@Override
	public View getView(int position, View subview, ViewGroup arg2) {
		ViewHolder vh = null;
		if (subview == null) {
			vh = new ViewHolder();
			subview = LayoutInflater.from(context).inflate(
					R.layout.spinner_la, null);
			vh.tv = (TextView) subview.findViewById(R.id.wifi_tv);
			subview.setTag(vh);
		} else {
			vh = (ViewHolder) subview.getTag();
		}

		vh.tv.setText(mWifiList[position]);
		return subview;
	}
}
