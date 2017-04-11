package com.eqled.eqledcontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eqled.network.ConnectControlCard;
import com.eqled.utils.HttpUrlConn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WifiActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mProssed;
    private EditText mIp;
    private Button mButton;
    private TextView mTextView;
    private ImageView mImageView;
    private DhcpInfo dhcpInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        mName = (EditText) findViewById(R.id.edit_name);
        mProssed = (EditText) findViewById(R.id.edit_prossed);
        mIp = (EditText) findViewById(R.id.edit_ip);
        mButton = (Button) findViewById(R.id.btn_wifi);
        mTextView = (TextView) findViewById(R.id.text_wifi);
        mImageView = (ImageView) findViewById(R.id.iamge_btn);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取WiFi信息
        WifiManager my_wifiManager = ((WifiManager) getSystemService(WIFI_SERVICE));
        dhcpInfo = my_wifiManager.getDhcpInfo();
        Log.d(".........","dhcpInfo"+ dhcpInfo.toString());
        initHttp();
        initLisenter();

    }

    private void initHttp() {
        //动态获取IP地址
        String ip = mIp.getText().toString();
        String path = "http://" + ip + "/";
        demand mand = new demand();
        mand.execute(path);
    }

    //确认修改内容，提交数据
    private void initLisenter() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadAsyTask upload = new UploadAsyTask();
                //获取post地址
                String ip = mIp.getText().toString();
                String path = "http://" + ip + "/";
                upload.execute(path + "config");

                AlertDialog.Builder builder = new AlertDialog.Builder(WifiActivity.this);
                builder.setIcon(R.drawable.select);//设置图标
                builder.setTitle("WIFI修改成功！");//设置对话框的标题
                builder.setMessage("是否要重新连接WIFI？");//设置对话框的内容
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(WifiActivity.this, NewWorkActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
                AlertDialog b = builder.create();
                b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
            }
        });
    }


    class UploadAsyTask extends AsyncTask {
        String text = (String) mTextView.getText();
        String username = text + mName.getText().toString();
        String password = mProssed.getText().toString();
        String ip = mIp.getText().toString();

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String IP = intToIp(dhcpInfo.dns1);
                String path = "http://" + IP + "/";
                URL url = new URL(path + "config");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setReadTimeout(1000);
                connection.setConnectTimeout(1000);

                String serial = "baud=" + 57600 + ',' + "bits=" + 3 + ',' + "parity=" + 2 + ',' + "stop=" + 1 + ',';
                String apparm = "apE=" + 4 + ',' + "apH=" + 2 + ',' + "apN=" + username + ',' + "apP=" + password + ',';
                String staparm = "staE=" + 2 + ',' + "staN=" + "Network" + ',' + "staP=" + 12345678 + ',';
                String netpara = "ip=" + "192.168.1.255" + ',' + "port=" + 5005 + ',' + "sockttype=" + 1 + ',' + "transport=" + 2 + ',';
                String netpara2 = "apIP=" + ip + ',' + "apNM=" + "255.255.255.0" + ',' + "apGW=" + "192.168.4.1" + ',';
                String netpara3 = "dhcpE=" + 1 + ',' + "staIP=" + "192.168.1.200" + ',' + "staNM=" + "255.255.255.0" + ',' + "staGW=" + "192.168.1.1" + ',';
                String parm = serial + apparm + staparm + netpara + netpara2 + netpara3;
                byte[] entiy = parm.toString().getBytes();

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Length", entiy.length + "");
                connection.setRequestProperty("Charset", "zh-CN");

                OutputStream out = connection.getOutputStream();
                out.write(entiy);
                out.flush();
                out.close();

                if (connection.getResponseCode() == 200) ;

                InputStream is = connection.getInputStream();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object js) {

            super.onPostExecute(js);
        }
    }


    class demand extends HttpUrlConn {
        @Override
        protected void onPostExecute(Object json) {
            super.onPostExecute(json);
            Log.d("............","json............"+json);
            String[] jiezhiaa = json.toString().split(";");
            for (int i = 0; i < jiezhiaa.length; i++) {
                String jiequ = jiezhiaa[i];
                String jiequ1 = jiezhiaa[164];

                String[] a = jiequ1.split("=");
                for (int q = 0; q < a.length; q++) {
                    String z = a[1];
                    String[] g = z.split("'");
                    String u = g[1];
                    String username = u.toString().substring(3, u.length());
                    mName.setText(username);
                    Log.i("................", "username............................... " + username);
                }
                Log.i(".................", "jiezhi0.................... " + jiequ);
                Log.i(".................", "jiezhi1.................... " + jiequ1);
                String jiequ2 = jiezhiaa[165];
                String[] b = jiequ2.split("=");
                for (int w = 0; w < b.length; w++) {
                    String x = b[1];
                    String[] h = x.split("'");
                    String password = h[1];
                    mProssed.setText(password);
                    Log.i(".................", "jiezhi2.................... " + jiequ2);
                    Log.i("................", "username............................... " + password);
                }
                String jiequ3 = jiezhiaa[175];
                String[] c = jiequ3.split("=");
                for (int e = 0; e < c.length; e++) {
                    String y = c[1];
                    String[] j = y.split("'");
                    String IP = j[1];
                    mIp.setText(IP);
                    Log.i("................", "username............................... " + IP);
                }

                Log.i(".................", "jiezhi3.................... " + jiequ3);
            }
        }
    }


    //转换成DNS格式
    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }
}

