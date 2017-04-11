package com.eqled.eqledcontrol;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eqled.bean.TimeDateBean;
import com.eqled.bean.UpdateInfoBean;
import com.eqled.bese.BaseActivity;
import com.eqled.custom.Toast_UI;
import com.eqled.fragment.ControlFragment;
import com.eqled.fragment.ProgramFragment;
import com.eqled.fragment.SettingFragment;
import com.eqled.network.ConnectControlCard;
import com.eqled.utils.AreaDrawUtilTool;
import com.eqled.utils.DownLoadManager;
import com.eqled.utils.HttpUrlConn;
import com.eqled.utils.StatusBarCompat;
import com.eqled.utils.UpdateInfoUtils;
import com.eqled.utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getName();
    private LinearLayout progarm_la = null;
    private LinearLayout control_la = null;
    private LinearLayout setting_la = null;
    private LinearLayout[] las = {progarm_la, control_la, setting_la};
    private int[] la_id = {R.id.main_la1, R.id.main_la2, R.id.main_la3};
    private int old_count = 0;  //记录上一次被选中的布局,默认选中0；
    private MyClick myClick;  //选中布局监听
    private ProgramFragment programFragment;
    private ControlFragment controlFragment;
    private SettingFragment settingFragment;
    private FragmentTransaction transaction;
    private Fragment mContent = null;
    private boolean isExit = false;
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int DOWN_ERROR = 4;
    private UpdateInfoBean info;
    private String localVersion;
    private ImageView add;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        StatusBarCompat.SetColor(this, this.getResources().getColor(R.color.color_bottom_background));
        myClick = new MyClick();
        for (int i = 0; i < las.length; i++
                ) {
            las[i] = (LinearLayout) findViewById(la_id[i]);
            las[i].setOnClickListener(myClick);
        }
        add = (ImageView) findViewById(R.id.main_add);
        add.setOnClickListener(this);
        //fragment 初始化
        if (programFragment == null) {
            programFragment = new ProgramFragment();
        }
        loadFragment(programFragment);

//        try {
//            localVersion = getVersionName();
//            CheckVersionTask cv = new CheckVersionTask();
//            new Thread(cv).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
//    /**
//     * 获取当前程序的版本号
//     */
//    private String getVersionName() throws Exception {
//        //getPackageName()是你当前类的包名，0代表是获取版本信息
//        PackageManager packageManager = getPackageManager();
//        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
//                0);
//        return packInfo.versionName;
//    }
//    public class CheckVersionTask implements Runnable {
//        InputStream is;
//        public void run() {
//            try {
//                String path = getResources().getString(R.string.url_server);
//                URL url = new URL(path);
//                HttpURLConnection conn = (HttpURLConnection) url
//                        .openConnection();
//                conn.setConnectTimeout(5000);
//                conn.setRequestMethod("GET");
//                int responseCode = conn.getResponseCode();
//                if (responseCode == 200) {
//                    // 从服务器获得一个输入流
//                    is = conn.getInputStream();
//                }
//                info = UpdateInfoUtils.getUpdataInfo(is);
//                if (info.getVersion().equals(localVersion)) {
//                    Log.i(TAG, "版本号相同");
//                    Message msg = new Message();
//                    msg.what = UPDATA_NONEED;
//                    handler.sendMessage(msg);
//                    // LoginMain();
//                } else {
//                    Log.i(TAG, "版本号不相同 ");
//                    Message msg = new Message();
//                    msg.what = UPDATA_CLIENT;
//                    handler.sendMessage(msg);
//                }
//            } catch (Exception e) {
//                Message msg = new Message();
//                msg.what = GET_UNDATAINFO_ERROR;
//                handler.sendMessage(msg);
//                e.printStackTrace();
//            }
//        }
//    }
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case UPDATA_NONEED:
//                    Toast.makeText(getApplicationContext(), "不需要更新",
//                            Toast.LENGTH_SHORT).show();
//                case UPDATA_CLIENT:
//                    //对话框通知用户升级程序
//                    showUpdataDialog();
//                    break;
//                case GET_UNDATAINFO_ERROR:
//                    //服务器超时
//                    //Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show();
//                    break;
//                case DOWN_ERROR:
//                    //下载apk失败
//                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };
//    /**
//     *
//     * 弹出对话框通知用户更新程序
//     *
//     * 弹出对话框的步骤：
//     *  1.创建alertDialog的builder.
//     *  2.要给builder设置属性, 对话框的内容,样式,按钮
//     *  3.通过builder 创建一个对话框
//     *  4.对话框show()出来
//     */
//    protected void showUpdataDialog() {
//        AlertDialog.Builder builer = new AlertDialog.Builder(this);
//        builer.setTitle("版本升级");
//        builer.setMessage(info.getDescription());
//        //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
//        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Log.i(TAG, "下载apk,更新");
//                downLoadApk();
//            }
//        });
//        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                //do sth
//            }
//        });
//        AlertDialog dialog = builer.create();
//        dialog.show();
//    }
//    /**
//     * 从服务器中下载APK
//     */
//    protected void downLoadApk() {
//        final ProgressDialog pd;    //进度条对话框
//        pd = new  ProgressDialog(this);
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setMessage("正在下载更新");
//        pd.show();
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
//                    sleep(3000);
//                    installApk(file);
//                    pd.dismiss(); //结束掉进度条对话框
//                } catch (Exception e) {
//                    Message msg = new Message();
//                    msg.what = DOWN_ERROR;
//                    handler.sendMessage(msg);
//                    e.printStackTrace();
//                }
//            }}.start();
//    }
//
//    //安装apk
//    protected void installApk(File file) {
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
//    }

    @Override
    protected void onResume() {
        // StatusBarCompat.SetColor(this,this.getResources().getColor(R.color.color_bottom_background));//有bug暂时不启用
        super.onResume();
      /* if(mContent!=null &&mContent instanceof ProgramFragment){ //刷新节目列表
           programFragment.updateList();
       }*/

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void handler(Message msg) {

    }


    private class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_la1:
                    if (old_count != 0) {  //重复选中不刷新UI，节省开销,也可以选中的布局取消监听的方式来实现
                        las[old_count].setBackgroundResource(R.color.color_bottom_background);
                        las[0].setBackgroundResource(R.color.color_top_background);
                        if (programFragment == null) {
                            programFragment = new ProgramFragment();
                        }
                        loadFragment(programFragment);
                        old_count = 0;
                    }
                    break;
                case R.id.main_la2:
                    if (old_count != 1) {
                        las[old_count].setBackgroundResource(R.color.color_bottom_background);
                        las[1].setBackgroundResource(R.color.color_top_background);
                        if (controlFragment == null) {
                            controlFragment = new ControlFragment();
                        }
                        loadFragment(controlFragment);
                        old_count = 1;
                    }
                    break;
                case R.id.main_la3:
                    if (old_count != 2) {
                        las[old_count].setBackgroundResource(R.color.color_bottom_background);
                        las[2].setBackgroundResource(R.color.color_top_background);
                        if (settingFragment == null) {
                            settingFragment = new SettingFragment();
                        }
                        loadFragment(settingFragment);
                        old_count = 2;
                    }
                    break;
            }


        }
    }

    /*
       fragment 初始化
     */
    private void loadFragment(Fragment to) {
        if (to instanceof ProgramFragment) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.INVISIBLE);
        }

        transaction = getSupportFragmentManager().beginTransaction();
        //   if (mContent != to) {  //判断是否是重复加载fragment ，如果没有判断重复选中的情况，必须启用

        if (!to.isAdded()) { // 先判断是否被add过
            if (mContent != null) {  //初始化时调用，mContent为空

                transaction.hide(mContent).add(R.id.main_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {

                transaction.add(R.id.main_content, to).commit();
            }
        } else {
            transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
        mContent = to;


    }

    /**
     * ********************* 获取返回键监听 调用退出方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * ********************* 双击退出程序方法
     */

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            // 准备退出
            Toast_UI.toast(MainActivity.this, getResources().getString(R.string.hint_exit));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            MainActivity.this.finish();
            System.exit(0);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_add) {
            programFragment.addProgram();
        }
    }
}
