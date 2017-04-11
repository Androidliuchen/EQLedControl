package com.eqled.eqledcontrol;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eqled.adapter.RecyclerViewAdapter;
import com.eqled.bean.Areabean;
import com.eqled.bean.ProgramBean;
import com.eqled.bean.TextBean;
import com.eqled.bean.TimeDateBean;
import com.eqled.bese.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import com.eqled.custom.Toast_UI;
import com.eqled.databasemanagement.ProgramBeanDao;
import com.eqled.databasemanagement.TextBeanDao;
import com.eqled.databasemanagement.TimeDateBeanDao;
import com.eqled.network.ConnectControlCard;
import com.eqled.network.SendPacket;
import com.eqled.utils.AreaDrawUtilTool;
import com.eqled.utils.BasicOrder;
import com.eqled.utils.Constant;
import com.eqled.utils.InterfaceConnect;
import com.eqled.utils.Utils;
import com.eqled.utils.WindowSizeManager;
import com.eqled.utils.WindowTool;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ProgramActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<String> mDatas = new ArrayList<String>();   //顶部导航栏适配器数据
    private Button send;   //发送节目
    private Button preview; //预览
    private String program_name;
    private ImageView back;
    private ImageView add;
    private LinearLayout text_background; //文本窗背景
    private ImageView text_ima;  //文本窗区域
    private int windowWidth;//窗口宽度
    private int windowHeight; //窗口高度
    private ConnectControlCard ccc;
    private byte[] proBytes;   //节目完整封包数据
    private int color = 0;  //单双色，默认单色
    private List<byte[]> program_lists = null;   //当节目数据包需要分包的时候
    private int programPKg_count = 1;   //节目数据分包 包数量
    private int send_count = 0;          //节目分包发送的计数器
    private ProgramBean programBean;
    private ProgressDialog proDialog; // 进度条
    private List<Areabean> areaBeans = new ArrayList<Areabean>();   //区域数组

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_program);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        text_ima = (ImageView) findViewById(R.id.program_ima);
        send = (Button) findViewById(R.id.program_send);
        back = (ImageView) findViewById(R.id.program_back);
        text_background = (LinearLayout) findViewById(R.id.program_text_background);
        text_background.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        preview = (Button) findViewById(R.id.program_preview);
        preview.setOnClickListener(this);
        add = (ImageView) findViewById(R.id.program_area_add);
        add.setOnClickListener(this);
        snedTest();
        new TimeThread().start(); //启动新的线程
    }

    @Override
    protected void initData() {
        //读取数据库数据
        int program_id = getIntent().getIntExtra(Constant.PROGRAM_ID, -1);
        Log.d("..............." ,"program_id数据..........:" + program_id);
        programBean = new ProgramBeanDao(this).get(program_id);
        programBean.setArea_position(0);
        programBean.setType(Constant.AREA_TYPE_PROGRAM);   //type不存储，实时赋值
        areaBeans.add(programBean);
        Log.d("..............." ,"areaBeans数据..........:" + areaBeans);

        //读取宽高设置
        WindowSizeManager windowSizeManager = WindowSizeManager.getSahrePreference(this);
        windowHeight = windowSizeManager.getWindowHeight();
        windowWidth = windowSizeManager.getWindowWidth();
        List<TextBean> textBeans = new TextBeanDao(this).listByUserId(program_id);  //读取文本窗
        if (textBeans.size() >= 1) {
            for (TextBean t : textBeans
                    ) {
                Paint paint = Utils.getPaint(this, Utils.getPaintSize(this, t.getText_size_position()));//字体参数启动读取
                paint.setFakeBoldText(t.isText_size_bold());
                Utils.setTypeface(this, paint
                        , (this.getResources().getStringArray(R.array.typeface_path))[t.getText_typeface()]);
                t.setPaint(paint);
                areaBeans.add(t);
            }
        }
        List<TimeDateBean> timeDateBeens = new TimeDateBeanDao(this).listByUserId(program_id);  //读取时间窗
        if (timeDateBeens.size() >= 1) {
            for (TimeDateBean tb : timeDateBeens) {
                Paint paint = Utils.getPaint(this, Utils.getPaintSize(this, tb.getM_rgbClockTextSize()));//字体参数启动读取
                Utils.setTypeface(this, paint
                        , (this.getResources().getStringArray(R.array.typeface_path))[tb.getNumber_typeface()]);
                tb.setPaint(paint);
                areaBeans.add(tb);
            }
        }
        for (Areabean ab : areaBeans) {
            switch (ab.getType()) {
                case Constant.AREA_TYPE_IMAGE:
                    mDatas.add(getString(R.string.program_image));
                    break;
                case Constant.AREA_TYPE_TEXT:
                    mDatas.add(getString(R.string.program_text));
                    break;
                case Constant.AREA_TYPE_TIME:
                    mDatas.add(getString(R.string.program_time));
                    break;
                case Constant.AREA_TYPE_PROGRAM:
                    mDatas.add(getString(R.string.program_parameters));
                    break;
            }
        }
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this, mDatas);
        recyclerViewAdapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                openSelectView(position);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
        showText();

        //如果是从首页点击发送节目，进入本页面
        if (getIntent().getIntExtra(Constant.SEND_STR, 0) == Constant.SEND) {
            Sendprogram();
        }
    }

    /*
       显示第一屏内容
     */
    private void showText() {
        int times = 1;
        if (windowWidth < 100) {
            times = 5;
        } else if (times < 160) {
            times = 3;
        } else if (times < 200) {
            times = 2;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) text_background.getLayoutParams();
        layoutParams.weight = windowWidth * times;
        layoutParams.height = windowHeight * times;
        text_background.setLayoutParams(layoutParams);
        final Bitmap bt = Bitmap.createBitmap(windowWidth * times, windowHeight * times, Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bt); // 创建画布
        canvas1.drawColor(Color.BLACK); // 颜色黑色
        for (int i = 0; i < areaBeans.size(); i++) {  //绘制出所有区域
            AreaDrawUtilTool.DrawArea(this, canvas1, areaBeans.get(i), times);

        }
        text_ima.setImageBitmap(bt);
    }

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (programPKg_count == 1) {   //数据不分包
                        ccc = new ConnectControlCard(0, proBytes, proBytes.length, new InterfaceConnect() {

                            @Override
                            public void success(byte[] result) {
                                handler.sendEmptyMessage(2);
                            }

                            @Override
                            public void failure(int stateCode) {
                                handler.sendEmptyMessage(1);
                            }
                        });
                        new Thread(ccc).start();
                    } else {  //数据分包
                        ccc = new ConnectControlCard(0, program_lists.get(send_count), program_lists.get(send_count).length, new InterfaceConnect() {
                            @Override
                            public void success(byte[] result) {
                                if (send_count == programPKg_count) {
                                    handler.sendEmptyMessage(2);
                                } else {
                                    handler.sendEmptyMessage(0);
                                }
                            }
                            @Override
                            public void failure(int stateCode) {
                                handler.sendEmptyMessage(1);
                            }
                        });
                        new Thread(ccc).start();
                        send_count++;
                    }

                    break;
                case 1:
                    Toast_UI.toast(ProgramActivity.this, getResources().getString(R.string.program_send_failure));
                    proDialog.cancel();
                    break;
                case 4:
                    Toast_UI.toast(ProgramActivity.this, getResources().getString(R.string.program_send_success));
                    proDialog.cancel();
                    //如果是从首页点击发送节目，进入本页面，发送成功后退出
                    if (getIntent().getIntExtra(Constant.SEND_STR, 0) == Constant.SEND) {
                        ProgramActivity.this.finish();
                    }
                    break;

                case 2:
                    //  Toast_UI.toast(this,"节目发送完毕，发送节目传输结束指令");
                    byte[] endSend = SendPacket.dataPkg(SendPacket.endPingPkg());
                    ccc = new ConnectControlCard(0, endSend, endSend.length, new InterfaceConnect() {
                        @Override
                        public void success(byte[] result) {

                            handler.sendEmptyMessage(4);
                        }

                        @Override
                        public void failure(int stateCode) {
                            handler.sendEmptyMessage(1);
                        }
                    });
                    new Thread(ccc).start();

                    break;

                case 3:
                    Log.d("回读", "回读成功");  //回读控制卡信息后，发送节目准备发送指令
                    snedTest();
                    int dataLength = 0;
                    if (programPKg_count == 1) {  //分包的数据大小，和不分包的数据大小
                        dataLength = proBytes.length;
                    } else {
                        for (int i = 0; i < program_lists.size(); i++) {
                            dataLength += program_lists.get(i).length;
                            Log.d("每个包的大小", +program_lists.get(i).length + "");
                        }
                    }
                    byte[] startSend = SendPacket.prepareSendDataPkg(programPKg_count, dataLength);
                    byte[] startSend1 = SendPacket.dataPkg(startSend);
                    ccc = new ConnectControlCard(0, startSend1, startSend1.length, new InterfaceConnect() {
                        @Override
                        public void success(byte[] result) {
                            handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void failure(int stateCode) {
                            handler.sendEmptyMessage(1);
                        }
                    });
                    new Thread(ccc).start();
                    break;

            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void handler(Message msg) {
    }

    /*
       按type 弹出不同的  窗体 1.图文窗口，2.文本窗口，3.单行文本，4.静止文本，5. 表格窗口 0 节目参数
     */
    private void openSelectView( final int position) {
        switch (areaBeans.get(position).getType()) {
            case Constant.AREA_TYPE_IMAGE:

                break;
            case Constant.AREA_TYPE_TEXT:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProgramActivity.this);
                builder.setIcon(R.drawable.select);//设置图标
                builder.setMessage("请选择操作内容");//设置对话框的内容
                builder.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WindowTool.getTextWindow(ProgramActivity.this, (TextBean) areaBeans.get(position), windowWidth,
                                windowHeight, new WindowTool.InterfaceDismiss() {
                                    @Override
                                    public void dismiss(String content) {
                                        if (position == 1) {
                                            programBean.setText_content(content);
                                            new ProgramBeanDao(ProgramActivity.this).update(programBean);
                                        }
                                        showText();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextBean textBean = (TextBean) areaBeans.get(position);
                        mDatas.remove(position);
                        areaBeans.remove(position);
                        new TextBeanDao(ProgramActivity.this).delete(textBean);
                        if (areaBeans.size() == 1) { //说明没有文本窗了，暂时，多窗体则删除
                            programBean.setText_content("");
                            new ProgramBeanDao(ProgramActivity.this).update(programBean);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                        showText();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                android.app.AlertDialog b = builder.create();
                b.show();  //必须show一下才能看到对话框，跟Toast一样的道理

                break;
            case Constant.AREA_TYPE_TIME:
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(ProgramActivity.this);
                builder1.setIcon(R.drawable.select);//设置图标
                builder1.setMessage("请选择操作内容");//设置对话框的内容
                builder1.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WindowTool.getTimeWindow(ProgramActivity.this, (TimeDateBean) areaBeans.get(position), windowWidth, windowHeight,
                                new WindowTool.InterfaceDismiss() {
                                    @Override
                                    public void dismiss(String content) {
                                        showText();
                                    }
                                });
                    }
                });
                builder1.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimeDateBean timeDateBean = (TimeDateBean) areaBeans.get(position);
                        mDatas.remove(position);
                        areaBeans.remove(position);
                        new TimeDateBeanDao(ProgramActivity.this).delete(timeDateBean);
                        if (areaBeans.size() == 1) {
                            programBean.setTime_content("");
                            new ProgramBeanDao(ProgramActivity.this).update(programBean);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                        showText();
                    }
                });
                builder1.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                android.app.AlertDialog b1 = builder1.create();
                b1.show();  //必须show一下才能看到对话框，跟Toast一样的道理


                break;

            case Constant.AREA_TYPE_PROGRAM:
                break;
        }
    }

    /*
      获取节目参数窗口，
    */
    private View getProgramWindow() {
        View v = this.getLayoutInflater().inflate(R.layout.window_program, null);
        CheckBox play = (CheckBox) v.findViewById(R.id.window_pro_play);
        final LinearLayout layout = (LinearLayout) v.findViewById(R.id.window_pro_content);
        play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
            }
        });
        return v;
    }

    /**
     * 节目发送指令
     */
    public void Sendprogram() {
        proDialog = android.app.ProgressDialog.show(this, null,
                getResources().getString(R.string.program_sending));
        send_count = 0;  //发送包计数器重置
        programPKg_count = 1;//包数量重置
        byte[] controlCardInfo = BasicOrder.getControlCardInfo();
        ccc = new ConnectControlCard(0, controlCardInfo, controlCardInfo.length, new InterfaceConnect() {
            @Override
            public void success(byte[] result) {
                color = (result[27] & 0xFF);
                handler.sendEmptyMessage(3);

            }

            @Override
            public void failure(int stateCode) {
                handler.sendEmptyMessage(1);
            }
        });
        new Thread(ccc).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.program_send:
                Sendprogram();
                break;
            case R.id.program_preview:
                if (programBean.getText_content().equals("")) {
                    Toast_UI.toast(this, getResources().getString(R.string.program_edit));
                } else {
                    Intent intent1 = new Intent(this, PreviewActivity.class);
                    intent1.putExtra(Constant.PROGRAM_ID, programBean.getId());
                    startActivity(intent1);
                }
                break;

            case R.id.program_back:

                finish();
                break;
            case R.id.program_text_background:


                break;

            case R.id.program_area_add:
                final String[] arrayFruit = new String[]{getString(R.string.program_text),
                        getString(R.string.program_time)
//                        ,getString(R.string.program_image)
                };
                AlertDialog.Builder dia = new AlertDialog.Builder(this);
                dia.setTitle(getString(R.string.program_area_add))
                        .setIcon(R.drawable.eq_xuanzhe).setItems(arrayFruit,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                if (which == 0) {
                                    TextBean textBean = new TextBean();
                                    textBean.setProgramBean(programBean);
                                    //创建时画笔赋值
                                    Paint paint = Utils.getPaint(ProgramActivity.this, Utils.getPaintSize(ProgramActivity.this, 15));
                                    paint.setFakeBoldText(textBean.isText_size_bold());
                                    Utils.setTypeface(ProgramActivity.this, paint
                                            , (ProgramActivity.this.getResources().getStringArray(R.array.typeface_path))[textBean.getText_typeface()]);
                                    textBean.setPaint(paint);
                                    textBean.setType(Constant.AREA_TYPE_TEXT);
                                    textBean.setArea_position(areaBeans.get(areaBeans.size() - 1).getArea_position() + 1);
                                    new TextBeanDao(ProgramActivity.this).add(textBean);
                                    areaBeans.add(textBean);
                                    mDatas.add(getString(R.string.program_text));
                                    recyclerViewAdapter.notifyDataSetChanged();
                                } else if (which == 1) {
                                    Toast.makeText(ProgramActivity.this,
                                            arrayFruit[which],
                                            Toast.LENGTH_SHORT).show();
                                    TimeDateBean timeDateBean = new TimeDateBean();
                                    timeDateBean.setProgramBean(programBean);
                                    Paint paint = Utils.getPaint(ProgramActivity.this, Utils.getPaintSize(ProgramActivity.this, 15));
                                    Utils.setTypeface(ProgramActivity.this, paint
                                            , (ProgramActivity.this.getResources().getStringArray(R.array.typeface_path))[timeDateBean.getNumber_typeface()]);
                                    timeDateBean.setPaint(paint);
                                    timeDateBean.setType(Constant.AREA_TYPE_TIME);
                                    timeDateBean.setArea_position(areaBeans.get(areaBeans.size() - 1).getArea_position() + 1);
                                    new TimeDateBeanDao(ProgramActivity.this).add(timeDateBean);
                                    areaBeans.add(timeDateBean);
                                    mDatas.add(getString(R.string.program_time));
                                    recyclerViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }).setNegativeButton(getString(R.string.setting_cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        });
                dia.show();
                break;
        }

    }
    /**
     * 完整节目组织测试
     */
    public void snedTest() {
        //节目参数数据
        byte[] program = SendPacket.programParameterDataPkg(
                0, 0,//播放模式， 是否定时
                new byte[]{0x00, 0x00, 0x00, 0x23, 0x59, 0x59},   //起始时间 结束 时间
                new byte[]{0x20, 0x13, 0x01, 0x01, 0x20, 0x13, 0x01, 0x02}, //起始日期
                0,//播放星期
                18, 0, 0, 0, 0, new byte[0]    //边框
        );
        int address1 = program.length;
        //开始组织区域数据
        ArrayList<byte[]> program_indexs = new ArrayList<byte[]>(); //开始组织索引组数据
        final ArrayList<byte[]> area_bytes = new ArrayList<byte[]>();
        int index = 0;   //area_bytes，位置
        for (int i = 0; i < areaBeans.size(); i++) {  //区域数据组织， 从 1开始， 0是  program
            if (i == 0) { //用来创建节目索引
                byte[] programIndex = SendPacket.programIndexPkg(
                        1, 1,   //节目类型，   节目编号
                        0,   //坐标 ，第一个节目的数据起始值0
                        areaBeans.size() - 1,//  节目子项的个数
                        program);
                program_indexs.add(programIndex);
            } else { //组织区域数据
                if (i == 1) {
                    index = address1;
                } else if (i > 1) {

                    index += area_bytes.get(i - 2).length;
                }
                areaBeans.get(i).setIndex(index);
                //窗口区域参数 组织
                final byte[] windowData = SendPacket.windowAreaDataPkg(
                        areaBeans.get(i).getArea_X(), areaBeans.get(i).getArea_y(),   //窗口区域的坐标
                        areaBeans.get(i).getWindowWidth(), areaBeans.get(i).getWindowHeight(),   //宽高
                        18, 0, 0, 0, 0, new byte[0]        //边框数据 暂不考虑
                );
                switch (areaBeans.get(i).getType()) {
                    case Constant.AREA_TYPE_IMAGE:

                        break;
                    case Constant.AREA_TYPE_TIME:
                        TimeDateBean timeDateBean = (TimeDateBean) areaBeans.get(i);
//                        Utils.saveMyBitmap(AreaDrawUtilTool.getTime(ProgramActivity.this, timeDateBean), "moni");
                        ArrayList<Byte> timeImageData =  new ArrayList<Byte>();
                        timeImageData.add(AreaDrawUtilTool.getTime(ProgramActivity.this,timeDateBean));
//                        Utils.GetBitmapBytes(AreaDrawUtilTool.getTime(ProgramActivity.this,timeDateBean), timeImageData, color);
                        Byte[]  timeImageDatas = timeImageData.toArray(new Byte[timeImageData.size()]);
                        byte []  timeArea = SendPacket.areaType_B_Pkg(timeImageDatas,timeDateBean);
                        area_bytes.add(SendPacket.mergeIndex(windowData,timeArea));
                        break;
                    case Constant.AREA_TYPE_TEXT:
                        TextBean textBean = (TextBean) areaBeans.get(i);
                        int text_inedex = index + windowData.length;
                        ArrayList<byte[]> text_bytes = new ArrayList<byte[]>();
                        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        text_bytes.clear();
                        bitmaps.clear();
                        List<String> texts = textBean.getTexts();
                        ArrayList<Integer> BmpWidthList = new ArrayList<Integer>();
                        for (int y = 0; y < texts.size(); y++) {//创建bitmap组
                            bitmaps.add(AreaDrawUtilTool.getText(texts.get(y), textBean, BmpWidthList));
                        }
                        for (int z = 0; z < bitmaps.size(); z++) {
                            ArrayList<Byte> bytes = new ArrayList<Byte>();
                            Utils.saveMyBitmap(AreaDrawUtilTool.getText(texts.get(z),textBean,BmpWidthList),"001");
                            Utils.GetBitmapBytes(bitmaps.get(z), bytes, color);
                            Byte[] bytess = bytes.toArray(new Byte[bytes.size()]);
                            text_inedex = text_inedex + 25 + bytess.length;   // 这里25是文本窗的参数的固定长度
                            int next_address = text_inedex;
                            int loop_address = 0;
                            if (z == bitmaps.size() - 1) {
                                next_address = textBean.getIndex();
                                loop_address = next_address;
                            }
                            int runtime = Integer.parseInt(getResources().getStringArray(R.array.text_runtime_int)[textBean.getText_runtime_position()].toString());
                            byte[] tex = SendPacket.areaType_A_Pkg(
                                    next_address, loop_address, 0,//下个区域数据地址，当前区域数据地址，局部
                                    Integer.parseInt(getResources().getStringArray(R.array.text_inmode_int)[textBean.getText_inmode_position()].toString()),  // 进场方式
                                    Integer.parseInt(getResources().getStringArray(R.array.text_outmode_int)[textBean.getText_outmode_position()].toString()),   //出场方式51代表不清除的文字效果
                                    runtime, runtime,//进场时间 出场时间
                                    textBean.getText_duration_position(),        //停留时间 默认20个（0.1秒）
                                    BmpWidthList.get(z),
                                    bytes.size(),             //图像数据长度
                                    bytess);                 //图像数据
                            text_bytes.add(tex);
                        }
                        area_bytes.add(SendPacket.mergeIndex(windowData, SendPacket.mergeIndex(text_bytes)));

                        break;
                }
                //构建区域完成以后，构建索引
                byte[] areaIndex = SendPacket.programIndexPkg(
                        2, 2,   //节目类型：区域数据，区域数据类型为文本类型
                        areaBeans.get(i).getIndex(),   //坐标 ，第一个节目的数据起始值0
                        0,//  节目子项的个数
                        windowData
                );
                program_indexs.add(areaIndex);
            }
        }

        //节目文件头数据组织
        byte[] fileHeadPkg = SendPacket.fileHeadPkg(
                color,//单双色
                windowWidth, windowHeight,  //屏幕的宽高，不一定等同于 区域的宽高
                1, areaBeans.size(),   //节目个数，索引个数
                SendPacket.mergeIndex(program_indexs)  //索引组校验
        );
        //完整的节目数据包： 文件头,节目索引，区域索引，节目参数，区域参数，区域内容
        byte[] progarmData = SendPacket.mergeIndex(
                fileHeadPkg,                              //文件头
                SendPacket.mergeIndex(program_indexs),   //索引组
                program,                                //节目参数
                SendPacket.mergeIndex(area_bytes)       //区域
        );
        Log.d(" 节目数据包大小：", progarmData.length + "：是否超过1024 " + windowWidth + " " + windowHeight + " 颜色" + color);
        if (progarmData.length > Constant.DATA_SPLIT) { //设定为超过1000分包
            program_lists = new ArrayList<byte[]>();
            int count = progarmData.length / Constant.DATA_SPLIT + 1;
            int count_remainder = progarmData.length % Constant.DATA_SPLIT; //数据分包，最后一个数据包的大小
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    byte[] list = new byte[count_remainder];
                    int z = 0;
                    for (int y = i * Constant.DATA_SPLIT; y < progarmData.length; y++) {
                        list[z] = progarmData[y];
                        z++;
                    }
                    program_lists.add(SendPacket.dataPkg(SendPacket.dataTransmissionPkg(i, list.length, list)));
                } else {

                    byte[] list = new byte[Constant.DATA_SPLIT];
                    int z = 0;
                    for (int y = i * Constant.DATA_SPLIT; y < Constant.DATA_SPLIT * (i + 1); y++) {
                        list[z] = progarmData[y];
                        z++;
                    }
                    program_lists.add(SendPacket.dataPkg(SendPacket.dataTransmissionPkg(i, list.length, list)));
                }

            }
            if (program_lists != null && program_lists.size() > 1) {  //当数据分包时
                programPKg_count = program_lists.size();
            }
        }
        //添加帧头包校验帧尾
        proBytes = SendPacket.dataPkg(SendPacket.dataTransmissionPkg(0, progarmData.length, progarmData));

    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showText();
            }
        }
    };
}