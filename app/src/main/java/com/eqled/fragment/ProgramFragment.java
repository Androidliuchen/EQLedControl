package com.eqled.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.eqled.adapter.TestListAdapter;
import com.eqled.bean.ProgramBean;
import com.eqled.bese.BaseFragment;
import com.eqled.custom.CustomPopWindow;
import com.eqled.custom.Toast_UI;
import com.eqled.databasemanagement.ProgramBeanDao;
import com.eqled.eqledcontrol.PreviewActivity;
import com.eqled.eqledcontrol.ProgramActivity;
import com.eqled.eqledcontrol.R;
import com.eqled.utils.Constant;
import com.eqled.utils.InterfaceItemClick;
import com.eqled.utils.ProgramNameItemManager;
import java.util.List;


public class ProgramFragment extends BaseFragment {
	private View v;
	private ListView listView;
	private List<ProgramBean> programBeen;
	private TestListAdapter testListAdapter;
	private CustomPopWindow customPopWindow;
	private TextView popu_pro_name;   //popuwindow布局
	private LinearLayout popu_pro_send;  //发送节目
	private LinearLayout popu_pro_show;  //预览节目
	private LinearLayout popu_pro_edit;  //编辑节目
	private LinearLayout popu_pro_delete;  //删除节目
	private int select;    //选中的节目item
	private int program_name_count;   //自动命名的数字


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_program, container, false);
		initView(v);
		initData();
		return v;
	}

	@Override
	protected void initView(View v) {

		listView = (ListView) v.findViewById(R.id.program_listView);
	}

	@Override
	protected void initData() {

	}

	@Override
	public void onResume() {
		super.onResume();
		new Thread(new Runnable() {
			@Override
			public void run() {
				programBeen = new ProgramBeanDao(getContext()).getListAll();
				handler.sendEmptyMessage(1);
			}
		}).start();


	}


	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			updateList();
		}

	};


	public void updateList() {
		if (programBeen.size() == 0) {
			program_name_count = 1;
		} else {
			program_name_count = ProgramNameItemManager.getSahrePreference(getActivity());

		}
		testListAdapter = new TestListAdapter(getActivity(), programBeen, new InterfaceItemClick() {
			@Override
			public void ClickItem(int position) {
				openMore(position);
			}
		});
		listView.setAdapter(testListAdapter);

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	  点击节目more,打开更多选项供用户选择
      */
	private void openMore(int position) {
		if (customPopWindow == null) {   //可以选择不复用，减少开销
			customPopWindow = new CustomPopWindow(getActivity(), R.id.main);
			customPopWindow.setView(getPopWindowView(), 1.0f, 0.3f);
		}
		select = position;
		popu_pro_name.setText(programBeen.get(select).getName());
		customPopWindow.show();
	}

	/*
	  添加节目
	 */
	public void addProgram() {
		ProgramBean bean = new ProgramBean();
		bean.setName(getActivity().getString(R.string.program) + "" + program_name_count);
		new ProgramBeanDao(getContext()).add(bean);
		/*TextBean textBean = new TextBean();
		textBean.setProgramBean(bean);
		new TextBeanDao(getContext()).add(textBean);
		*/
		programBeen.add(bean);
		testListAdapter.notifyDataSetChanged();
		program_name_count++;
		ProgramNameItemManager.setSharedPreference(getActivity(), program_name_count);

	}

	/*
	   加载弹出窗体布局
	 */
	private View getPopWindowView() {
		View view = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_program, null);
		popu_pro_name = (TextView) view.findViewById(R.id.popu_pro_name);
		popu_pro_send = (LinearLayout) view.findViewById(R.id.popu_pro_send);
		popu_pro_show = (LinearLayout) view.findViewById(R.id.popu_pro_show);
		popu_pro_edit = (LinearLayout) view.findViewById(R.id.popu_pro_edit);
		popu_pro_delete = (LinearLayout) view.findViewById(R.id.popu_pro_delete);
		popu_pro_send.setOnClickListener(this);
		popu_pro_show.setOnClickListener(this);
		popu_pro_edit.setOnClickListener(this);
		popu_pro_delete.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		customPopWindow.dismiss(); //当有其他的单击事件加入时，转移代码执行位置
		switch (v.getId()) {
			case R.id.popu_pro_send:
				Intent it = new Intent(getActivity(), ProgramActivity.class);
				it.putExtra(Constant.PROGRAM_ID, programBeen.get(select).getId());
				it.putExtra(Constant.SEND_STR, Constant.SEND);
				getActivity().startActivity(it);

				break;
			case R.id.popu_pro_show:
				if (programBeen.get(select).getText_content().equals("")) {
					Toast_UI.toast(getContext(), getActivity().getString(R.string.program_edit));
				} else {
					//Toast_UI.toast(getContext(),programBeen.get(select).getText_content());
					Intent intent1 = new Intent(getActivity(), PreviewActivity.class);
					intent1.putExtra(Constant.PROGRAM_ID, programBeen.get(select).getId());
					getActivity().startActivity(intent1);
				}
				break;
			case R.id.popu_pro_edit:
				Intent intent = new Intent(getActivity(), ProgramActivity.class);
				intent.putExtra(Constant.PROGRAM_ID, programBeen.get(select).getId());
				getActivity().startActivity(intent);
				break;
			case R.id.popu_pro_delete:
				new ProgramBeanDao(getContext()).delete(programBeen.get(select).getId());
				programBeen.remove(select);
				testListAdapter.notifyDataSetChanged();
				if (programBeen.size() == 0) {
					program_name_count = 1;
				}
				break;
		}

	}

}
