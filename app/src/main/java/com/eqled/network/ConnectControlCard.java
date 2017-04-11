package com.eqled.network;

/**
 * Created by Administrator on 2016/5/27.
 */


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

import com.eqled.utils.Constant;
import com.eqled.utils.InterfaceConnect;

public class ConnectControlCard implements Runnable {

    private static final String TAG = "ConnectControlCard";
    public static int PORT = 5005;                        // 端口
    public static String HOSTAddress = "192.168.1.236";    // 主机地址
    private DatagramSocket dataSocket = null;
    private DatagramPacket dataPacket = null;
    private int dataLength = 0;    //在当前类，竟然还要传个空值过来
    private byte[] sendByte = null;
    private int dataSize = 0;
    private String testStr = "";
    private boolean stop = true;
    private InterfaceConnect interfaceConnect = null;

    public ConnectControlCard(int dataLength, byte[] sendByte, int dataSize, InterfaceConnect interfaceConnect) {
        super();
        this.dataLength = dataLength;
        this.sendByte = sendByte;
        this.dataSize = dataSize;
        this.interfaceConnect = interfaceConnect;
        System.out.println("数据包.............." + SendPacket.bytes2HexString(sendByte, sendByte.length));
    }

    public ConnectControlCard(int dataLength, byte[] sendByte, int dataSize) {
        super();
        this.dataLength = dataLength;
        this.sendByte = sendByte;
        this.dataSize = dataSize;
        System.out.println("数据包.............." + SendPacket.bytes2HexString(sendByte, sendByte.length));
    }

    @Override
    public void run() {
        InetAddress local = null;
        try {
            local = InetAddress.getByName(HOSTAddress);
            Log.e(TAG, "正在检测连接地址...");
        } catch (UnknownHostException e) {
            Log.e(TAG, "未找到连接地址..." + e.toString());
            if (interfaceConnect != null) {
                interfaceConnect.failure(0);
            }
            e.printStackTrace();
        }
        try {
            Log.e(TAG, "正在连接服务器...");
            dataSocket = new DatagramSocket();
            Log.e(TAG, "正在准备数据...");

            dataLength = SendPacket.bytes2HexString(sendByte, sendByte.length).toString() == null ? 0 : sendByte.length;
            dataPacket = new DatagramPacket(sendByte, dataLength, local, PORT);
            try {
                dataSocket.send(dataPacket);
                Log.e(TAG, "发送成功...");
                stop = false;
                Log.e(TAG, "1");
                byte[] buf = new byte[100];
                Log.e(TAG, "2");
                dataPacket = new DatagramPacket(buf, buf.length);
                Log.e(TAG, "3");
                //  while (!stop) {
                try {
                    Log.e(TAG, "4");
                    dataSocket.setSoTimeout(Constant.UDP_WAIT);
                    dataSocket.receive(dataPacket); //	 获得输入流
                    Log.e(TAG, "5");
                    testStr = new String(buf, "GBK").trim();
                    Log.e(TAG, "6");
                    System.out.println("______________" + testStr.length());
                    Log.e(TAG, "7");
                    System.out.println("有数据.............." + SendPacket.bytes2HexString(buf, buf.length));
                    if (interfaceConnect != null) {
                        Log.e(TAG, "8");
                        interfaceConnect.success(buf); //传递返回值
                    }
                    //System.out.println("有数据1.............." + HexadecimalConver.decode((byte2HexStr(buf))));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "接收数据包异常...");
                    if (interfaceConnect != null) {
                        interfaceConnect.failure(0);
                    }
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, "接收数据包IO异常...");
                    if (interfaceConnect != null) {
                        interfaceConnect.failure(0);
                    }
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                    if (interfaceConnect != null) {
                        interfaceConnect.failure(0);
                    }
                }
                // }
            } catch (IOException e) {
                Log.e(TAG, "发送数据包异常...");
                if (interfaceConnect != null) {
                    interfaceConnect.failure(0);
                }
                e.printStackTrace();
            }
        } catch (SocketException e) {
            Log.e(TAG, "连接服务器失败...");
            if (interfaceConnect != null) {
                interfaceConnect.failure(0);
            }
            e.printStackTrace();
        }

    }
}
