package com.eqled.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eqled.bean.ProgramBean;
import com.eqled.custom.Toast_UI;
import com.eqled.eqledcontrol.R;
import com.eqled.utils.InterfaceItemClick;

import java.util.List;

public class TestListAdapter extends BaseAdapter {
	private List<ProgramBean> programBeanrs;
	private Context context;
	private InterfaceItemClick interfaceItemClick ;

	public TestListAdapter(Context context, List<ProgramBean>  programBeanrs,InterfaceItemClick interfaceItemClick) {
		super();
		this.context = context;
		this. programBeanrs =programBeanrs;
		this.interfaceItemClick =interfaceItemClick;
	}

	@Override
	public int getCount() {
		if (programBeanrs != null) {
			return programBeanrs.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (programBeanrs != null) {
			return programBeanrs.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public ImageView about;
		public TextView name;
		public  TextView content;
	}

	@Override
	public View getView(int position, View subview, ViewGroup arg2) {
		ViewHolder vh;
		if (subview == null) {
			vh = new ViewHolder();
			subview = LayoutInflater.from(context).inflate(
					R.layout.program_listview_la, null);
			vh.name = (TextView) subview.findViewById(R.id.program_name);
			vh.content = (TextView) subview.findViewById(R.id.program_content);
			vh.about = (ImageView) subview.findViewById(R.id.program_about);
			subview.setTag(vh);
		} else {
			vh = (ViewHolder) subview.getTag();
		}
		vh.name.setText(programBeanrs.get(position).getName());
		vh.content.setText(programBeanrs.get(position).getText_content());
		vh.content.setText(programBeanrs.get(position).getTime_content());
		final  int count  =position;
		vh.about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				interfaceItemClick.ClickItem(count);
			}
		});

		return subview;
	}



}
