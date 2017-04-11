package com.eqled.custom;

import com.eqled.eqledcontrol.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @Function: 自定义对话框
 */
public class CustomDialog extends Dialog {
	private EditText editText;
	private Button positiveButton, negativeButton;
	private TextView title;

	public CustomDialog(Context context) {
		super(context);
		setCustomDialog();
	}

	public CustomDialog(Context context, String title) {
		super(context);
		this.setTitle(title);
		setCustomDialog();
		//	this.title.setText(title);
	}

	private void setCustomDialog() {
		View mView = LayoutInflater.from(getContext()).inflate(
				R.layout.mdialog, null);
		title = (TextView) mView.findViewById(R.id.title);
		editText = (EditText) mView.findViewById(R.id.number);
		positiveButton = (Button) mView.findViewById(R.id.positiveButton);
		negativeButton = (Button) mView.findViewById(R.id.negativeButton);
		super.setContentView(mView);
	}

	public String getEditText() {
		return editText.getText().toString();
	}

	@Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}

	/**
	 * 确定键监听器
	 *
	 * @param listener
	 */
	public void setOnPositiveListener(View.OnClickListener listener) {
		positiveButton.setOnClickListener(listener);
	}

	/**
	 * 取消键监听器
	 *
	 * @param listener
	 */
	public void setOnNegativeListener(View.OnClickListener listener) {
		negativeButton.setOnClickListener(listener);
	}
}