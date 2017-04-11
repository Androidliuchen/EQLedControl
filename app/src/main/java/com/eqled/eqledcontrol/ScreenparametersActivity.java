package com.eqled.eqledcontrol;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eqled.adapter.MyFragmentPagerAdapter;
import com.eqled.bese.BaseActivity;
import com.eqled.custom.ViewPagerCompat;
import com.eqled.fragment.HardwareFragment;
import com.eqled.fragment.LedFragment;
import com.eqled.fragment.ScanFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyman on 2016/5/26.
 */
public class ScreenparametersActivity extends BaseActivity implements View.OnClickListener {
    private ViewPagerCompat viewPagerCompat;
    private List<Fragment> fragmentList;
    private LedFragment ledFragment;
    private HardwareFragment hardwareFragment;
    private ScanFragment scanFragment;
    private ImageView back;
    //当前选中的项
    private int currenttab = 0;
    //上一次选中的 ,也就是变色的item
    private int old_count = 0;
    private LinearLayout led;
    private LinearLayout hardware;
    private LinearLayout scan;
    //选项卡变色
    private LinearLayout la0;
    private LinearLayout la1;
    private LinearLayout la2;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private LinearLayout[] linearLayouts = {la0, la1, la2};
    private int[] las = {R.id.scr_la0, R.id.scr_la1, R.id.scr_la2};
    private TextView[] textViews = {tv0, tv1, tv2};
    private int[] tvs = {R.id.scr_text0, R.id.scr_text1, R.id.scr_text2};

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_screenparameters);
        back = (ImageView) findViewById(R.id.screen_back);
        back.setOnClickListener(this);
        led = (LinearLayout) findViewById(R.id.led_setting);
        led.setOnClickListener(this);
        hardware = (LinearLayout) findViewById(R.id.hardware_setting);
        hardware.setOnClickListener(this);
        scan = (LinearLayout) findViewById(R.id.scan_setting);
        scan.setOnClickListener(this);
        viewPagerCompat = (ViewPagerCompat) findViewById(R.id.Screenparameters_viewpager);
        fragmentList = new ArrayList<Fragment>();
        ledFragment = new LedFragment();
        hardwareFragment = new HardwareFragment();
        scanFragment = new ScanFragment();
        fragmentList.add(ledFragment);
        fragmentList.add(hardwareFragment);
        fragmentList.add(scanFragment);
        viewPagerCompat.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), (ArrayList<Fragment>) fragmentList));
        viewPagerCompat.addOnPageChangeListener(new MyOnPageChangeListener());
        //
        for (int i = 0; i < linearLayouts.length; i++) {
            linearLayouts[i] = (LinearLayout) findViewById(las[i]);
            textViews[i] = (TextView) findViewById(tvs[i]);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void handler(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.led_setting:
                currenttab = 0;
                break;
            case R.id.hardware_setting:
                currenttab = 1;
                //  viewPagerCompat.setCurrentItem(currenttab);
                break;
            case R.id.scan_setting:
                currenttab = 2;
                //  viewPagerCompat.setCurrentItem(currenttab);
                break;
        }
        if (v.getId() == R.id.screen_back) {
            this.finish();
        } else {

            ChangeColor();
        }

    }


    /**
     * 颜色切换
     */
    private void ChangeColor() {
        if (old_count != currenttab) {
            viewPagerCompat.setCurrentItem(currenttab);   //点击切换布局
            textViews[old_count].setTextColor(ContextCompat.getColor(this, R.color.color_text_blank2));
            linearLayouts[old_count].setVisibility(View.INVISIBLE);
            textViews[currenttab].setTextColor(ContextCompat.getColor(this, R.color.color_top_background));
            linearLayouts[currenttab].setVisibility(View.VISIBLE);
            old_count = currenttab;
        }

    }

    /*
        滑动监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currenttab = position;
            ChangeColor();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
