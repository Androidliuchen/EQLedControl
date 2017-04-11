package com.eqled.eqledcontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eqled.bean.ProgramBean;
import com.eqled.bean.TextBean;
import com.eqled.bean.TimeDateBean;
import com.eqled.bese.BaseActivity;
import com.eqled.custom.Toast_UI;
import com.eqled.databasemanagement.ProgramBeanDao;
import com.eqled.databasemanagement.TextBeanDao;
import com.eqled.databasemanagement.TimeDateBeanDao;
import com.eqled.utils.AnimationManager;
import com.eqled.utils.AreaDrawUtilTool;
import com.eqled.utils.Constant;
import com.eqled.utils.Utils;
import com.eqled.utils.WindowSizeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class PreviewActivity extends BaseActivity implements View.OnClickListener{
    private ProgramBean programBean;
    private TextBean textBean ;
    private TimeDateBean timeDateBean;
    private ImageView  back ;
    private TextView  program_name;
    private TextView  preview_text;
    private Button   preview_start ;
    private  Button  preview_stop ;
    private  boolean start = false;   // 启动动画
    private Animation animation = null  ;   //
    private List<String> texts = new ArrayList<String>();   //当文本内容超出显示范围时，分屏
    private int count=0  ;  //动画播放的文本内容

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_preview);
        back=(ImageView)findViewById(R.id.preview_back);
        back.setOnClickListener(this);
        program_name=(TextView)findViewById(R.id.preview_name);
        preview_text=(TextView)findViewById(R.id.preview_text);
        preview_start=(Button)findViewById(R.id.preview_start);
        preview_start.setOnClickListener(this);
        preview_stop=(Button)findViewById(R.id.preview_stop);
        preview_stop.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        //读取数据库数据
        //这一块缺少时间功能，以至于预览是数据报空指针异常
        int program_id=getIntent().getIntExtra(Constant.PROGRAM_ID,-1);
        programBean = new ProgramBeanDao(this).get(program_id);
        textBean = new TextBeanDao(this).listByUserId(program_id).get(0);  //暂时一个节目只有一个文本框
        program_name.setText(programBean.getName());
        WindowSizeManager  windowSizeManager =WindowSizeManager.getSahrePreference(this);
        Utils.SplitScreen(texts,programBean.getText_content(),windowSizeManager.getWindowWidth(),
        Utils.getPaint(this,Utils.getPaintSize(this,textBean.getText_size_position())));
        if(texts.size()>0){
            preview_text.setText(texts.get(0));

        }
    }

    @Override
    protected void handler(Message msg) {

    }
    /*
      //启动动画
     */
    private  void   startAnimation(){
        start=true;
        animation=AnimationManager.GetAnimation(this,textBean.getText_inmode_position());
        animation.setDuration(1000);//设置动画持续时间为1秒
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //   Toast_UI.toast(PreviewActivity.this,count+"");
                preview_text.setText(texts.get(count));
                count++;
                if(count==texts.size()){
                    count=0;
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                            try {
                                Thread.sleep(textBean.getText_duration_position()*100);  //文字停留时间就是休眠时间
                                startAnimation();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        preview_text.startAnimation(animation);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.preview_start:
                if(textBean.getText_inmode_position()== 0||textBean.getText_inmode_position()== 5){
                   Toast_UI.toast(this,getResources().getString(R.string.hint_no_preview));

                }else {
                    startAnimation();

                }
                break;

            case  R.id.preview_stop :
                if(start==true){
                    animation.setAnimationListener(null);
                    count=0;
                    preview_text.setText(texts.get(0));
                }

                break;

            case    R.id.preview_back :
                finish();
                break;

        }

    }
}
