package com.eqled.bese;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eqled.eqledcontrol.R;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

	public View v;

	protected abstract void initView(View v);

	protected abstract void initData();


}
