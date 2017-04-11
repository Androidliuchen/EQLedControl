package com.eqled.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eqled.eqledcontrol.R;

public class SpinnerImageAdapter extends BaseAdapter {
	private int[] color_r;
	private Context context;

	public SpinnerImageAdapter(Context context, int[] mWifiList) {
		super();
		this.context = context;
		this.color_r = mWifiList;
	}



	@Override
	public int getCount() {
		if (color_r != null) {
			return color_r.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (color_r != null) {
			return color_r[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {

		public ImageView tv;
	}

	@Override
	public View getView(int position, View subview, ViewGroup arg2) {
		ViewHolder vh = null;
		if (subview == null) {
			vh = new ViewHolder();
			subview = LayoutInflater.from(context).inflate(
					R.layout.spinner_image_la, null);
			vh.tv = (ImageView) subview.findViewById(R.id.text_color_image);
			subview.setTag(vh);
		} else {
			vh = (ViewHolder) subview.getTag();
		}
		vh.tv.setImageResource(color_r[position]);
		return subview;
	}
}
