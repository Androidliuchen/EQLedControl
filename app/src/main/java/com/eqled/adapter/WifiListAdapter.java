package com.eqled.adapter;

import java.util.List;

import com.eqled.eqledcontrol.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiListAdapter extends BaseAdapter {
	private List<ScanResult> mWifiList;
	private Context context;

	public WifiListAdapter(Context context, List<ScanResult> mWifiList) {
		super();
		this.context = context;
		this.mWifiList = mWifiList;
	}

	@Override
	public int getCount() {
		if (mWifiList != null) {
			return mWifiList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mWifiList != null) {
			return mWifiList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public ImageView jm;
		public TextView tv;
	}

	@Override
	public View getView(int position, View subview, ViewGroup arg2) {
		ViewHolder vh = null;
		if (subview == null) {
			vh = new ViewHolder();
			subview = LayoutInflater.from(context).inflate(
					R.layout.wifi_list_item, null);
			// vh.iv = (ImageView)
			// subview.findViewById(R.id.item_menu_imageview);
			vh.tv = (TextView) subview.findViewById(R.id.wifi_tv);
			vh.jm = (ImageView) subview.findViewById(R.id.wifi_jm);
			subview.setTag(vh);
		} else {
			vh = (ViewHolder) subview.getTag();
		}
		if(mWifiList.get(position).capabilities.indexOf("WPA")==-1){
			vh.jm.setVisibility(View.INVISIBLE);
		}else{
			vh.jm.setVisibility(View.VISIBLE);
		}
		vh.tv.setText(mWifiList.get(position).SSID);
		return subview;
	}
}
