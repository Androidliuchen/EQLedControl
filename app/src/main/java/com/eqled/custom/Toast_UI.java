package com.eqled.custom;

import com.eqled.eqledcontrol.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Toast_UI {

    public static void toast(Context context, String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_style,
                null);
        TextView textView = (TextView) view.findViewById(R.id.toast_text);
        textView.setText(content);
        Toast toast = new Toast(context);
        toast.setDuration(50);
        toast.setGravity(Gravity.BOTTOM, 0, 80);
        toast.setView(view);
        toast.show();
    }
}
