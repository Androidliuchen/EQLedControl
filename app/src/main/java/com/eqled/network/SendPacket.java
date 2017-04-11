package com.eqled.network;

/**
 * Created by hyman(fenghaiming) on 2016/5/27.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.eqled.bean.TimeDateBean;
import com.eqled.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SendPacket {

    // AC AC AC AC FE FF 00 80 01 00 0A 00 01 52 CE CA

    // 帧头 4 AC AC AC AC
    // 目标地址 2 FE FF
    // 源地址 2 00 80
    // 数据大小 2 01 00
    // 版本号 1 0A
    // 保留字节 1 00
    // 包数据 1024 01
    // 包校验 2 52 CE
    // 帧尾 1 CA
    // 1.帧头 目标地址 源地址 数据大小 版本号 保留字节 3.包数据 4.包校验 5.帧尾
    // 0xAC FE FF 00 80 01 0 0xCA0 0A 00 01 52 CE
    // 4 2 2 2 1 1 =<1024 2 1
    public static void sendPing1(int iDataSize, byte[] iData, byte[] b) {
        // 帧头
        b[0] = (byte) (0xAC);
        b[1] = (byte) (0xAC);
        b[2] = (byte) (0xAC);
        b[3] = (byte) (0xAC);
        // 目标地址
        b[4] = (byte) (0xFE);
        b[5] = (byte) (0xFF);
        // 源地址
        b[6] = (byte) (0x00);
        b[7] = (byte) (0x80);
        // 数据大小
        b[8] = (byte) (0x01);
        b[9] = (byte) (0x00);
        // 版本号
        b[10] = (byte) (0x0A);
        // 保留字节
        b[11] = (byte) (0x00);
        // 包数据
        for (int i = 0; i < iData.length; i++) {
            b[12 + i] = (byte) iData[i];
        }
        //包效验
        b[iData.length + 12] = (byte) 0x52;
        b[iData.length + 13] = (byte) 0xCE;
        //帧尾
        b[iData.length + 14] = (byte) 0xCA;
    }

    /**
     * 完整包 发送 ***********************************************************
     *
     * @param pkgData
     * @return
     */

    public static byte[] dataPkg(byte[] pkgData) {
        byte[] sendPkg = new byte[pkgData.length + 7];
        for (int i = 0; i < 4; i++) {
            sendPkg[i] = (byte) 0xAC;
        }
        for (int i = 4; i < sendPkg.length - 3; i++) {
            sendPkg[i] = pkgData[i - 4];
        }
        // 包效验
        sendPkg[pkgData.length + 4] = (byte) CodecUtil.crc16Bytes(pkgData,
                pkgData.length)[0];
        // (byte)0xD6;
        sendPkg[pkgData.length + 5] = (byte) CodecUtil.crc16Bytes(pkgData,
                pkgData.length)[1];
        // (byte)0xCE;
        sendPkg[pkgData.length + 6] = (byte) 0xCA;
        Log.d("校验码",
                SendPacket.bytes2HexString(
                        CodecUtil.crc16Bytes(pkgData, pkgData.length), 2));

        return sendPkg;
    }

    /**
     * 创建 包头 接口
     */
    public static byte[] pkgHeadInterface(int pkgSize) {
        byte[] b = new byte[8];
        b[0] = (byte) 0xFE; // 包头 8 字节
        b[1] = (byte) 0xFF;
        b[2] = (byte) 0x00;
        b[3] = (byte) 0x80;
        b[4] = (byte) ((pkgSize >> 0) & 0xFF);
        b[5] = (byte) ((pkgSize >> 8) & 0xFF);
        b[6] = (byte) 0x0A;
        b[7] = (byte) 0x00;
        return b;
    }

    /**
     * PING命令 0x00 **********************************************
     */
    public static byte[] pingPkg() {
        byte[] b = new byte[9];
        // 包头 8 字节
        for (int i = 0; i < 8; i++) {
            b[0] = pkgHeadInterface(1)[i];
        }
        b[8] = 0x00;
        return b;
    }

    /**
     * 获取控制卡信息 0x01 **************************************************
     *
     * @return
     */

    public static byte[] getCardInfo() {
        byte[] b = new byte[9];
        // 包头 8 字节
        for (int i = 0; i < 8; i++) {
            b[0] = pkgHeadInterface(1)[i];
        }
        b[8] = 0x01;
        return b;
    }

    /**
     * 设置屏参 0x02 *************************************************
     *
     * @param ScreenWidth
     * @param ScreenHeight
     * @param ColorStyle
     * @param ScreenLineSquence
     * @param DataDir
     * @return
     */
    public static byte[] setScreenParameterPkg(int ScreenWidth, int ScreenHeight, int ColorStyle, int ScreenLineSquence, int DataDir) {
        byte[] b = new byte[16];
        // 包头 8 字节
        for (int i = 0; i < 8; i++) {
            b[i] = pkgHeadInterface(8)[i];
        }
        b[8] = (byte) 0x02;
        b[9] = (byte) ((ScreenWidth >> 0) & 0xff);
        b[10] = (byte) ((ScreenWidth >> 8) & 0xff);
        b[11] = (byte) ((ScreenHeight >> 0) & 0xff);
        b[12] = (byte) ((ScreenHeight >> 8) & 0xff);
        b[13] = (byte) ColorStyle;

        b[14] = (byte) ScreenLineSquence;

        b[15] = (byte) DataDir;
        return b;
    }

    /**
     * 设置硬件参数 0x03 *************************************************
     * <p/>
     * AC AC AC AC FE FF 00 80 12 00 0A 00 03 C0 A8 01 EC 8D 13 FF FF FF 00 C0
     * A8 01 01 01 00 01 B3 56 CA
     */
    /*
     * Cmd 1 0x03 设置硬件参数 03 IP Address 4 0xC0A801EC 控制卡IP地址，默认值192.168.1.236。 C0
	 * A8 01 EC IP Port 2 0x138D 网络端口号，默认值5005。 8D 13 IPSubMask 4 0xFFFFFF00
	 * 子网掩码，默认值255.255.255.0。 FF FF FF 00 IPGateWay 4 0xC0A80100
	 * 默认网关，默认值192.168.1.0。 C0 A8 01 01 Card Address 2 0x0001 控制卡地址号（串口通信用）默认值1。
	 * 01 00 SerialBaud 1 0x02 串口波特率0--9600，1—57600； 01
	 */
    public static byte[] setHardwareParameterPkg(byte[] ip, int ipport,
                                                 byte[] IPSubMask, byte[] IPGateWay, int CardAddress, int SerialBaud) {
        byte[] b = new byte[18 + 8]; // 包头8+包数据18
        // 包头 8 字节
        for (int i = 0; i < 8; i++) {
            b[i] = pkgHeadInterface(18)[i];
        }
        b[8] = (byte) 0x03;
        for (int i = 0; i < 4; i++) {
            b[9 + i] = ip[i];
        }
        b[13] = (byte) ((ipport >> 0) & 0xff);
        b[14] = (byte) ((ipport >> 8) & 0xff);
        for (int i = 0; i < 4; i++) {
            b[15 + i] = IPSubMask[i];
        }
        for (int i = 0; i < 4; i++) {
            b[19 + i] = IPGateWay[i];
        }
        b[23] = (byte) ((CardAddress >> 0) & 0xff);
        b[24] = (byte) (byte) ((CardAddress >> 8) & 0xff);
        b[25] = (byte) SerialBaud;

        return b;
    }

    /**
     * 开始发送节目数据命令 0x04 *************************************************
     * pkgNum  节目数据总包数
     * byteNum  节目数据总字节数
     */
    public static byte[] prepareSendDataPkg(int pkgNum, int byteNum) {
        byte[] b = new byte[23];
        // 包头
        for (int i = 0; i < 8; i++) {
            b[i] = pkgHeadInterface(15)[i];
        }
        b[8] = (byte) 0x04;
        for (int i = 0; i < 6; i++) {
            b[9 + i] = (byte) 0x00;
        }
        b[15] = (byte) ((pkgNum >> 0) & 0xff);
        b[16] = (byte) ((pkgNum >> 8) & 0xff);
        b[17] = (byte) ((pkgNum >> 16) & 0xff);
        b[18] = (byte) ((pkgNum >> 24) & 0xff);

        b[19] = (byte) ((byteNum >> 0) & 0xff);
        b[20] = (byte) ((byteNum >> 8) & 0xff);
        b[21] = (byte) ((byteNum >> 16) & 0xff);
        b[22] = (byte) ((byteNum >> 24) & 0xff);
        return b;
    }

    /**
     * 节目数据传输 0x05 *************************************************
     * pkgNo 包序号
     * pkgSize 本包数据大小
     * pkgData  包数据
     */
    public static byte[] dataTransmissionPkg(int pkgNo, int pkgSize,
                                             byte[] pkgData) {
        byte[] b = new byte[8 + 9 + pkgSize]; // 包头 +节目包固定格式+报数据
        for (int i = 0; i < 8; i++) {
            b[i] = pkgHeadInterface(9 + pkgSize)[i];
        }
        b[8] = (byte) 0x05;

        b[9] = (byte) ((pkgNo >> 0) & 0xff); // 本包序号
        b[10] = (byte) ((pkgNo >> 8) & 0xff);
        b[11] = (byte) ((pkgNo >> 16) & 0xff);
        b[12] = (byte) ((pkgNo >> 24) & 0xff);

        b[13] = (byte) ((pkgSize >> 0) & 0xff); // 本包数据大小
        b[14] = (byte) ((pkgSize >> 8) & 0xff);
        b[15] = (byte) ((pkgSize >> 16) & 0xff);
        b[16] = (byte) ((pkgSize >> 24) & 0xff);

        for (int i = 17; i < b.length; i++) { // 包数据
            b[i] = pkgData[i - 17];
        }
        return b;
    }

    /**
     * 节目数据文件 格式 ***** 文件头 ,节目索引, 节目参数数据, 窗口区域参数数据 A B C D
     */
    public static byte[] programDataFilePkg(byte[] fileHeadPkg,
                                            byte[] programIndexPkg, byte[] programParameterDataPkg,
                                            byte[] windowAreaDataPkg, byte[] areaType_A_Pkg,
                                            byte[] areaType_B_Pkg, byte[] areaType_C_Pkg, byte[] areaType_D_Pkg) {
        int b1 = fileHeadPkg.length + programIndexPkg.length;
        int b2 = fileHeadPkg.length + programIndexPkg.length
                + programParameterDataPkg.length;
        int b3 = fileHeadPkg.length + programIndexPkg.length
                + programParameterDataPkg.length + windowAreaDataPkg.length
                + areaType_A_Pkg.length;
        int b4 = fileHeadPkg.length + programIndexPkg.length
                + programParameterDataPkg.length + windowAreaDataPkg.length
                + areaType_A_Pkg.length + areaType_B_Pkg.length;
        int b5 = fileHeadPkg.length + programIndexPkg.length
                + programParameterDataPkg.length + windowAreaDataPkg.length
                + areaType_A_Pkg.length + areaType_B_Pkg.length
                + areaType_C_Pkg.length;
        int b6 = fileHeadPkg.length + programIndexPkg.length
                + programParameterDataPkg.length + windowAreaDataPkg.length
                + areaType_A_Pkg.length + areaType_B_Pkg.length
                + areaType_C_Pkg.length + areaType_D_Pkg.length;
        byte[] b = new byte[1033];

        for (int i = 0; i < fileHeadPkg.length; i++) {
            b[0] = fileHeadPkg[i];
        }
        for (int i = 0; i < programIndexPkg.length; i++) {
            b[fileHeadPkg.length + i] = programIndexPkg[i];
        }
        for (int i = 0; i < programParameterDataPkg.length; i++) {
            b[b1 + i] = programParameterDataPkg[i];
        }
        for (int i = 0; i < windowAreaDataPkg.length; i++) {
            b[b2 + i] = windowAreaDataPkg[i];
        }
        for (int i = 0; i < areaType_A_Pkg.length; i++) {
            b[b3 + i] = areaType_A_Pkg[i];
        }
        for (int i = 0; i < areaType_B_Pkg.length; i++) {
            b[b4 + i] = areaType_B_Pkg[i];
        }
        for (int i = 0; i < areaType_C_Pkg.length; i++) {
            b[b5 + i] = areaType_C_Pkg[i];
        }
        for (int i = 0; i < areaType_D_Pkg.length; i++) {
            b[b6 + i] = areaType_D_Pkg[i];
        }

        return b;
    }

    // 节目数据文件头
    public static byte[] fileHeadPkg(int ColorStyle, int ScreenWidth,
                                     int ScreenHeight, int ProgramCount, int ProgramIndxCount,
                                     byte[] Indexs) {
        byte[] b = new byte[14]; // 2016/05/17 hyman 协议更新
        b[0] = (byte) 0xF6; // 文件标记常亮 " EQ " 2字节
        b[1] = (byte) 0x5A;
        b[2] = (byte) 0x00; // 节目版本号
        b[3] = (byte) ColorStyle; // 颜色类型：（0.单色，1.双色）。

        b[4] = (byte) ((ScreenWidth >> 0) & 0xff);
        b[5] = (byte) ((ScreenWidth >> 8) & 0xff);

        b[6] = (byte) ((ScreenHeight >> 0) & 0xff);
        b[7] = (byte) ((ScreenHeight >> 8) & 0xff);

        b[8] = (byte) ((ProgramCount >> 0) & 0xff); // 节目个数。
        b[9] = (byte) ((ProgramCount >> 8) & 0xff);

        b[10] = (byte) ((ProgramIndxCount >> 0) & 0xff); // 节目索引数量
        b[11] = (byte) ((ProgramIndxCount >> 8) & 0xff);

        b[12] = CodecUtil.ProgramIndexCRC16(Indexs)[0]; // 索引校验
        b[13] = CodecUtil.ProgramIndexCRC16(Indexs)[1];
        return b;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    // 节目索引
    public static byte[] programIndexPkg(int IndexType, int IndexParam,
                                         int DataStartAddress, int SubIndexCount, byte[] programIndex) {
        byte[] b = new byte[9];
        b[0] = (byte) IndexType; // 数据类型。0x01节目数据，0x02区域数据
        b[1] = (byte) IndexParam; // 索引参数。

        b[2] = (byte) ((DataStartAddress >> 0) & 0xff); // 节目数据对应的地址（节目数据开始地址1）
        b[3] = (byte) ((DataStartAddress >> 8) & 0xff);
        b[4] = (byte) ((DataStartAddress >> 16) & 0xff);
        b[5] = (byte) ((DataStartAddress >> 24) & 0xff);

        b[6] = (byte) SubIndexCount; // 子项个数，对节目索引来说是下面的区域数量

        b[7] = CodecUtil.ProgramIndexCRC16(programIndex)[0];
        b[8] = CodecUtil.ProgramIndexCRC16(programIndex)[1];
        return b;
    }

    // 节目参数数据
    public static byte[] programParameterDataPkg(int PlayModel, int IsTimer,
                                                 byte[] PlayTime, byte[] PlayDate, int PlayWeek, int BorderSpeed,
                                                 int BorderDirection, int BorderWidth, int BorderHeight,
                                                 int BorderDataLen, byte[] BorderData) {
        byte[] b = new byte[26 + BorderData.length]; // hyman 协议更新 2016/05/17
        // 17:27
        b[0] = (byte) ((PlayModel >> 0) & 0xff); // 播放模式
        b[1] = (byte) ((PlayModel >> 8) & 0xff);

        b[2] = (byte) IsTimer; // 定时播放

        b[3] = PlayTime[0]; // 起始时间结束时间
        b[4] = PlayTime[1];
        b[5] = PlayTime[2];
        b[6] = PlayTime[3];
        b[7] = PlayTime[4];
        b[8] = PlayTime[5];

        b[9] = PlayDate[0];// 起始日期结束日期
        b[10] = PlayDate[1];
        b[11] = PlayDate[2];
        b[12] = PlayDate[3];
        b[13] = PlayDate[4];
        b[14] = PlayDate[5];
        b[15] = PlayDate[6];
        b[16] = PlayDate[7];

        b[17] = (byte) PlayWeek; // 播放星期，该字段只有Timer=0x03时有效。
        b[18] = (byte) BorderSpeed; // 边框速度20,40,60,80,100,120,140毫秒/像素
        b[19] = (byte) BorderDirection; // 边框旋转方向0x00顺时针，0x01逆时针。
        b[20] = (byte) BorderWidth; // 边框图像宽度（像素）
        b[21] = (byte) BorderHeight; // 边框图像高度

        // b[22] = (byte) BorderDataLen; // 边框图像数据大小（字节），为0表示没有图像数据
        b[22] = (byte) ((BorderDataLen >> 0) & 0xff);
        b[23] = (byte) ((BorderDataLen >> 8) & 0xff);
        b[24] = (byte) ((BorderDataLen >> 16) & 0xff);
        b[25] = (byte) ((BorderDataLen >> 24) & 0xff);

        for (int i = 0; i < BorderData.length; i++) {// 边框图像数据
            b[23 + i] = BorderData[i];
        }
        return b;
    }

    // 窗口区域参数数据 hyman 协议更新 2016/05/17
    public static byte[] windowAreaDataPkg(int AreaX, int AreaY, int AreaWidth,
                                           int AreaHeight, int BorderSpeed, int BorderDirection,
                                           int BorderWidth, int BorderHeight, int BorderDataSize,
                                           byte[] BorderData) {
        byte[] b = new byte[16 + BorderData.length];
        b[0] = (byte) ((AreaX >> 0) & 0xff); // 区域 X坐标
        b[1] = (byte) ((AreaX >> 8) & 0xff);

        b[2] = (byte) ((AreaY >> 0) & 0xff); // 区域 Y坐标
        b[3] = (byte) ((AreaY >> 8) & 0xff);

        b[4] = (byte) ((AreaWidth >> 0) & 0xff); // 区域宽度
        b[5] = (byte) ((AreaWidth >> 8) & 0xff);

        b[6] = (byte) ((AreaHeight >> 0) & 0xff); // 区域高度
        b[7] = (byte) ((AreaHeight >> 8) & 0xff);

        b[8] = (byte) BorderSpeed; // 边框速度20,40,60,80,100,120,140毫秒/像素

        b[9] = (byte) BorderDirection; // 边框旋转方向0x00顺时针，0x01逆时针

        b[10] = (byte) BorderWidth; // 边框图像宽度（像素）

        b[11] = (byte) BorderHeight; // 边框图像高度

        b[12] = (byte) ((BorderDataSize >> 0) & 0xff); // 边框图像数据大小（字节），为0表示没有图像数据。
        b[13] = (byte) ((BorderDataSize >> 8) & 0xff);
        b[14] = (byte) ((BorderDataSize >> 16) & 0xff);
        b[15] = (byte) ((BorderDataSize >> 24) & 0xff);

        for (int i = 0; i < BorderData.length; i++) { // 边框图像数据
            b[14 + i] = BorderData[i];
        }
        return b;
    }

    // A．区域类型为（1.图文窗口，2.文本窗口，3.单行文本，4.静止文本，5. 表格窗口）的图像序列数据结构: 2016/05/18 hyman
    public static byte[] areaType_A_Pkg(int NextAreaAddress,
                                        int LoopStartAddress, int LoopNum, int InMode, int OutMode,
                                        int InSpeed, int OutSpeed, int StayTime, int ActiveLen,
                                        int ImageDataSize, Byte[] ImageData) {
        byte[] b = new byte[25 + ImageData.length];
        b[0] = (byte) ((NextAreaAddress >> 0) & 0xff); // 下一个图片的地址。
        b[1] = (byte) ((NextAreaAddress >> 8) & 0xff);
        b[2] = (byte) ((NextAreaAddress >> 16) & 0xff);
        b[3] = (byte) ((NextAreaAddress >> 24) & 0xff);

        b[4] = (byte) ((LoopStartAddress >> 0) & 0xff); // 下一个图片的地址。
        b[5] = (byte) ((LoopStartAddress >> 8) & 0xff);
        b[6] = (byte) ((LoopStartAddress >> 16) & 0xff);
        b[7] = (byte) ((LoopStartAddress >> 24) & 0xff);
        b[8] = (byte) LoopNum;
        b[9] = (byte) InMode; // 进场方式

        b[10] = (byte) OutMode; // 出场方式


        b[11] = (byte) ((InSpeed >> 0) & 0xff);
        b[12] = (byte) ((InSpeed >> 8) & 0xff);


        b[13] = (byte) ((OutSpeed >> 0) & 0xff);
        b[14] = (byte) ((OutSpeed >> 8) & 0xff);

        b[15] = (byte) ((StayTime >> 0) & 0xff);
        b[16] = (byte) ((StayTime >> 8) & 0xff);

        b[17] = (byte) ((ActiveLen >> 0) & 0xff); // 文本有效长度
        b[18] = (byte) ((ActiveLen >> 8) & 0xff);

        b[19] = (byte) ((ImageDataSize >> 0) & 0xff); // 图像数据长度
        b[20] = (byte) ((ImageDataSize >> 8) & 0xff);
        b[21] = (byte) ((ImageDataSize >> 16) & 0xff);
        b[22] = (byte) ((ImageDataSize >> 24) & 0xff);

        b[23] = CodecUtil.ProgramIndexCRC16(Utils.subBytes(b, 0, 23))[0]; // 数据和校验
        b[24] = CodecUtil.ProgramIndexCRC16(Utils.subBytes(b, 0, 23))[1];

        for (int i = 0; i < ImageData.length; i++) { // 图像数据
            b[25 + i] = ImageData[i];
        }

        return b;
    }

    // B．区域类型为（6. 时间日期）的数据结构:
    public static byte[] areaType_B_Pkg(Byte[] timedata, TimeDateBean timeDateBean) {

        byte[] b = new byte[17];
        b[0] = (byte) 'D';
        b[1] = (byte) 'A';
        b[2] = (byte) 'T';
        b[3] = (byte) 'E';
        if (timeDateBean.getM_nClockType() == 3) {  //0 1 2 为数字时钟，3 为模拟时钟
            b[4] = (byte) 1;   //时钟类型0,1	0:数字时钟 1:模拟时钟
        } else {
            b[4] = (byte) 0;
        }
        int offset = 0;                       //时差
        int[] times = timeDateBean.splitStrTimeLag(timeDateBean.getM_strTimeLag());
        offset = timeDateBean.getM_nDayLag() * 24 * 3600 + times[0] * 3600 + times[1] * 60;    //如果还有秒的话+上秒
        if (timeDateBean.getM_nOffset() == 1) {//0是 + 1是-；
            offset = 0 - offset;
        }
        b[5] = (byte) ((offset >> 0) & 0xff);
        b[6] = (byte) ((offset >> 8) & 0xff);
        b[7] = (byte) ((offset >> 16) & 0xff);
        b[8] = (byte) ((offset >> 24) & 0xff);
        //时钟指针颜色
        int handcolor = 0;
        int red = 0x0000FF;
        int green = 0x00FF00;
        int yellow = 0x00FFFF;
        int black = 0;
        int hourcolor = timeDateBean.getHourscolor(), minutecolor = timeDateBean.getMinutecolor(), secondcolor = timeDateBean.getSecondcolor();
        int[] handcolors = {hourcolor, minutecolor, secondcolor};
        for (int i = 0; i < handcolors.length; i++) {
            switch (handcolors[i]) {
                case 0:
                    handcolors[i] = yellow;
                    break;
                case 1:
                    handcolors[i] = green;
                    break;
                case 2:
                    handcolors[i] = red;
                    break;
                case 3:
                    handcolors[i] = black;
                    break;
            }
        }
        handcolor |= ((handcolors[0] & red) == red ? 0x01 : 0x00) | ((handcolors[0] & green) == green ? 0x02 : 0x00);
        handcolor |= ((handcolors[1] & red) == red ? 0x01 << 2 : 0x00) | ((handcolors[1] & green) == green ? 0x02 << 2 : 0x00);
        handcolor |= ((handcolors[2] & red) == red ? 0x01 << 4 : 0x00) | ((handcolors[2] & green) == green ? 0x02 << 4 : 0x00);
        b[9] = (byte) handcolor;
        EQ_DateFile_Asc[] m_gStuAsc = timeDateBean.getAsc();
        int byFontTypeCount = 0;   //字模板个数
        EQ_DateFile_TimeAsc[] sutAsc = new EQ_DateFile_TimeAsc[32];
        for (int i = 0; i < 100; i++) {
            if (m_gStuAsc[i].getM_byVaild() == 1) {
                sutAsc[byFontTypeCount] = new EQ_DateFile_TimeAsc();
                sutAsc[byFontTypeCount].setM_byAsc(m_gStuAsc[i].getM_byAsc());
                sutAsc[byFontTypeCount].setM_byColor(m_gStuAsc[i].getM_byColor());
                sutAsc[byFontTypeCount].setM_wx(m_gStuAsc[i].getM_wx());
                sutAsc[byFontTypeCount].setM_wy(m_gStuAsc[i].getM_wy());
                byFontTypeCount++;
            } else {
                continue;
            }
        }
        Log.d("test", byFontTypeCount + "");
        byte[] asc_array = new byte[6 * byFontTypeCount];  //asc
        int y = 0;
        for (int i = 0; i < byFontTypeCount; i++) {
            asc_array[y + 0] = (byte) sutAsc[i].getM_byAsc();
            asc_array[y + 1] = (byte) sutAsc[i].getM_byColor();
            asc_array[y + 2] = (byte) ((sutAsc[i].getM_wx() >> 0) & 0xff);
            asc_array[y + 3] = (byte) ((sutAsc[i].getM_wx() >> 8) & 0xff);
            asc_array[y + 4] = (byte) ((sutAsc[i].getM_wy() >> 0) & 0xff);
            asc_array[y + 5] = (byte) ((sutAsc[i].getM_wy() >> 8) & 0xff);
            y = y + 6;
        }
        //字模
        List<Byte> fonttype = new ArrayList<Byte>();
        String sWeekText = "日,一,二,三,四,五,六,";
        if (timeDateBean.getM_nClockType() == 3) {//模拟时钟

            if (WriteFontType(("0,1,2,3,4,5,6,7,8,9,"), timeDateBean, (ArrayList<Byte>) fonttype)) {
                WriteFontType(sWeekText, timeDateBean, (ArrayList<Byte>) fonttype);
            }
        } else {
            WriteFontType("0,1,2,3,4,5,6,7,8,9," + sWeekText, timeDateBean, (ArrayList<Byte>) fonttype);
        }
        Byte[] zimu = fonttype.toArray(new Byte[fonttype.size()]);
        //字模板个数
        b[10] = (byte) byFontTypeCount;

        int imagesize = timedata.length;
        b[11] = (byte) ((imagesize >> 0) & 0xff);
        b[12] = (byte) ((imagesize >> 8) & 0xff);
        b[13] = (byte) ((imagesize >> 16) & 0xff);
        b[14] = (byte) ((imagesize >> 24) & 0xff);
        // 数据和校验
        b[15] = CodecUtil.ProgramIndexCRC16(Utils.subBytes(b, 0, 15))[0];
        b[16] = CodecUtil.ProgramIndexCRC16(Utils.subBytes(b, 0, 15))[1];
        byte[] c = new byte[timedata.length + zimu.length];
        for (int i = 0; i < c.length; i++) {
            if (i <= timedata.length - 1) {
                c[i] = timedata[i];
            } else {
                c[i] = zimu[i - timedata.length];
            }
        }
        System.out.println("字模板数据." + SendPacket.bytes2HexString((ArrayList<Byte>) fonttype, fonttype.size()));

        byte[] a = SendPacket.mergeIndex(b, asc_array, c);


        return a;

    }

    // C．区域类型为（7. 计时窗口）的数据结构
    public static byte[] areaType_C_Pkg(int DateDec, int TimeDec, int ShowType,
                                        int BackImageSize, byte[] BackImageData, int FontTypeHeight,
                                        int FontTypeWidth, int FontPackDataSize, byte[] FontPackData) {
        int pkgSize = 19 + BackImageData.length + FontPackData.length;
        byte[] b = new byte[pkgSize];
        b[0] = (byte) ((DateDec >> 0) & 0xff); // 到达日期, 用BCD码表示，如23:50:59为
        // 0x23-50-53-00
        b[1] = (byte) ((DateDec >> 8) & 0xff);
        b[2] = (byte) ((DateDec >> 16) & 0xff);
        b[3] = (byte) ((DateDec >> 24) & 0xff);

        b[4] = (byte) ((TimeDec >> 0) & 0xff); // 到达时间, 用BCD码表示，如2010-05-20为
        // 0x20-10-05-20
        b[5] = (byte) ((TimeDec >> 8) & 0xff);
        b[6] = (byte) ((TimeDec >> 16) & 0xff);
        b[7] = (byte) ((TimeDec >> 24) & 0xff);

        b[8] = (byte) ShowType; // 显示格式

        b[9] = (byte) ((BackImageSize >> 0) & 0xff); // 背景图片大小
        b[10] = (byte) ((BackImageSize >> 8) & 0xff);
        b[11] = (byte) ((BackImageSize >> 16) & 0xff);
        b[12] = (byte) ((BackImageSize >> 24) & 0xff);

        for (int i = 0; i < BackImageData.length; i++) { // 背景图片数据
            b[13 + i] = BackImageData[i];
        }

        b[13 + BackImageData.length] = (byte) FontTypeHeight; // 字模板高度

        b[14 + BackImageData.length] = (byte) FontTypeWidth; // 字模板宽度

        // 字库数据大小
        b[15 + BackImageData.length] = (byte) ((FontPackDataSize >> 0) & 0xff);
        b[16 + BackImageData.length] = (byte) ((FontPackDataSize >> 8) & 0xff);
        b[17 + BackImageData.length] = (byte) ((FontPackDataSize >> 16) & 0xff);
        b[18 + BackImageData.length] = (byte) ((FontPackDataSize >> 24) & 0xff);

        for (int i = 0; i < FontPackData.length; i++) { // 字库数据
            b[19 + BackImageData.length + i] = FontPackData[i];
        }
        return b;
    }

    // D．区域类型为（8.温度窗口）的数据结构:
    public static byte[] areaType_D_Pkg(int ShowType, int BackImageSize,
                                        byte[] BackImageData, int FontTypeHeight, int FontTypeWidth,
                                        int FontPackDataSize, byte[] FontPackData) {
        int pkgSize = 11 + BackImageData.length + FontPackData.length;
        byte[] b = new byte[pkgSize];

        b[0] = (byte) ShowType; // 温度显示格式

        b[1] = (byte) ((BackImageSize >> 0) & 0xff); // 背景图片大小
        b[2] = (byte) ((BackImageSize >> 8) & 0xff);
        b[3] = (byte) ((BackImageSize >> 16) & 0xff);
        b[4] = (byte) ((BackImageSize >> 24) & 0xff);

        for (int i = 0; i < BackImageData.length; i++) { // 背景图片数据
            b[5 + i] = BackImageData[i];
        }

        b[5 + BackImageData.length] = (byte) FontTypeHeight; // 字模板高度

        b[6 + BackImageData.length] = (byte) FontTypeWidth; // 字模板宽度

        // 字库数据大小
        b[7 + BackImageData.length] = (byte) ((FontPackDataSize >> 0) & 0xff);
        b[8 + BackImageData.length] = (byte) ((FontPackDataSize >> 8) & 0xff);
        b[9 + BackImageData.length] = (byte) ((FontPackDataSize >> 16) & 0xff);
        b[10 + BackImageData.length] = (byte) ((FontPackDataSize >> 24) & 0xff);

        for (int i = 0; i < FontPackData.length; i++) { // 字库数据
            b[11 + BackImageData.length + i] = FontPackData[i];
        }
        return b;
    }

    /**
     * 节目数据发送结束命令
     */
    public static byte[] endPingPkg() {
        byte[] b = new byte[9];
        // 包头 8 字节
        for (int i = 0; i < 8; i++) {
            b[i] = pkgHeadInterface(1)[i];
        }
        b[8] = 0x06;
        return b;
    }

    /**
     * 关闭 打开 屏 幕
     *
     * @param pkgData
     * @return
     */
    public static byte[] open_closeScreen(byte[] pkgData) {
        byte[] sendPkg = new byte[pkgData.length + 7];
        for (int i = 0; i < 4; i++) {
            sendPkg[i] = (byte) 0xAC;
        }
        for (int i = 4; i < sendPkg.length - 3; i++) {
            sendPkg[i] = pkgData[i - 4];
        }
        // 包效验
        sendPkg[pkgData.length + 4] = (byte) CodecUtil.crc16Bytes(pkgData, 10)[0];
        sendPkg[pkgData.length + 5] = (byte) CodecUtil.crc16Bytes(pkgData, 10)[1];
        sendPkg[pkgData.length + 6] = (byte) 0xCA;
        return sendPkg;
    }

    /**
     * byte 转String
     *
     * @param b
     * @param len
     * @return
     */
    public static String bytes2HexString(byte[] b, int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase() + " ";
        }
        return ret;
    }

    public static String bytes2HexString(ArrayList<Byte> b, int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            //  Log.d("b.get(i)",""+b.get(i).toString());
            String hex = Integer.toHexString(b.get(i) & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            //  Log.d("test",""+hex.toUpperCase());
            ret += hex.toUpperCase() + " ";
        }
        return ret;
    }

    /**
     * 所有索引组成的 数据
     */
    public static byte[] mergeIndex(byte[]... index) {
        int count = 0;
        for (int i = 0; i < index.length; i++) {
            count += index[i].length;
        }
        byte[] indexs = new byte[count];
        int offset = 0;
        for (byte[] array : index) {
            System.arraycopy(array, 0, indexs, offset, array.length);
            offset += array.length;
        }

        return indexs;
    }

    public static byte[] mergeIndex(ArrayList<byte[]> index) {
        int count = 0;
        for (int i = 0; i < index.size(); i++) {
            count += index.get(i).length;
        }
        byte[] indexs = new byte[count];
        int offset = 0;
        for (byte[] array : index) {
            System.arraycopy(array, 0, indexs, offset, array.length);
            offset += array.length;
        }

        return indexs;
    }


    /*
       画字模
     */
    private static boolean WriteFontType(String strFontType, TimeDateBean timeDateBean, ArrayList<Byte> fonttype) {
        String strChar = "";
        int i = 0;
        EQ_DataFile_FontType DataFile_FontType = new EQ_DataFile_FontType();
        Paint pa = timeDateBean.getPaint();
        pa.setColor(Color.WHITE);
        Paint.FontMetrics fme = pa.getFontMetrics();
        while (i < strFontType.length()) {
            if (String.valueOf(strFontType.charAt(i)).equals(",")) {
                DataFile_FontType.m_wFontTypeAscii = strFontType.charAt(i);
                i++;
            } else {
                strChar = String.valueOf(strFontType.charAt(i));
                i++;
                continue;
            }

            Bitmap bitmap = Bitmap.createBitmap(512, 128, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap); // 创建画布
            canvas.drawColor(Color.BLACK); // 颜色黑色
            canvas.drawText(strChar, 0, -fme.ascent + 1, pa);
            int cx = (int) pa.measureText(strChar);
            int cy = Utils.getFontHeight(pa);
            strChar = "";   //重置
            DataFile_FontType.m_wFontTypeWidth = cx;
            DataFile_FontType.m_wFontTypeHeight = cy;
            if (cx % 8 > 0) {
                cx += 8 - cx % 8;
            }
            List<Byte> btAryBitmap = new ArrayList<Byte>();
            DataFile_FontType.m_dwFontTypeDataSize = DCToByte_ColorSpace_8(bitmap, (ArrayList<Byte>) btAryBitmap, cx, cy);
            if (DataFile_FontType.m_dwFontTypeDataSize <= 0) {

                return false;
            }
            //写参数
            fonttype.add((byte) ((DataFile_FontType.m_wFontTypeAscii >> 0) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_wFontTypeAscii >> 8) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_wFontTypeHeight >> 0) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_wFontTypeHeight >> 8) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_wFontTypeWidth >> 0) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_wFontTypeWidth >> 8) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_dwFontTypeDataSize >> 0) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_dwFontTypeDataSize >> 8) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_dwFontTypeDataSize >> 16) & 0xff));
            fonttype.add((byte) ((DataFile_FontType.m_dwFontTypeDataSize >> 24) & 0xff));
            //写图片
            for (Byte b : btAryBitmap
                    ) {
                fonttype.add(b);
            }
        }


        return true;
    }

    //字模参数结构
    static class EQ_DataFile_FontType {
        int m_wFontTypeAscii;        //字模板Ascii码     2
        int m_wFontTypeHeight;        //字模板高度      2
        int m_wFontTypeWidth;        //字模板宽度     2
        int m_dwFontTypeDataSize;    //字模板数据大小  4
    }

    //8位颜色间隔
    private static int DCToByte_ColorSpace_8(Bitmap bitmap, ArrayList<Byte> ByteArray, int nWidth, int nHeight) {
        ByteArray.clear();
        int dwColor = 0;
        for (int nY = 0; nY < nHeight; nY++) {
            int byBitPost = 0, byRedValue = 0;
            for (int nX = 0; nX < nWidth; nX++) {
                dwColor = bitmap.getPixel(nX, nY); //0x00bbggrr
                byRedValue >>= 1;
                if (Color.red(dwColor) > 128)    //红色
                    byRedValue |= 0x80;

                if (byBitPost >= 7) {
                    ByteArray.add((byte) byRedValue);
                    byRedValue = 0;
                    byBitPost = 0;
                } else {
                    byBitPost++;
                }
            }
        }
        return ByteArray.size();
    }


}
