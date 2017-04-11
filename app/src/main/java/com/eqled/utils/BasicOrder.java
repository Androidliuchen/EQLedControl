package com.eqled.utils;

import android.util.Log;

import com.eqled.network.SendPacket;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/6/16.
 * <p>
 * 基本指令。。。非动态控制命令
 */
public class BasicOrder {
    /**
     * 打开屏幕
     */

    public static byte[] openScreen() {
        byte[] b = new byte[]{(byte) 0x08, (byte) 0x01};
        return SendPacket.dataPkg(SendPacket.mergeIndex(SendPacket.pkgHeadInterface(b.length), b));
    }

    /**
     * 关闭屏幕
     */
    public static byte[] closeScreen() {
        byte[] b = new byte[]{(byte) 0x08, (byte) 0x02};
        return SendPacket.dataPkg(SendPacket.mergeIndex(SendPacket.pkgHeadInterface(b.length), b));
    }

    /*
    调节亮度 1-15
     */
    public static byte[] setbrightness(int brightness) {
        byte[] b = new byte[]{(byte) 0x0b, (byte) 0x01, (byte) brightness, (byte) 0x0d, (byte) 0x1b,
                (byte) 0x00, (byte) 0xb0, (byte) 0xe5, (byte) 0xce, (byte) 0x00,
                (byte) 0x28, (byte) 0xf0, (byte) 0xce, (byte) 0x00, (byte) 0x58,
                (byte) 0xe6, (byte) 0xce, (byte) 0x00, (byte) 0xf0, (byte) 0xed,
                (byte) 0x70, (byte) 0x74, (byte) 0x5e, (byte) 0x13, (byte) 0x2a,
                (byte) 0x00};
        return SendPacket.dataPkg(SendPacket.mergeIndex(SendPacket.pkgHeadInterface(b.length), b));
    }

    /**
     * 回读亮度
     */
    public static byte[] getbrightness() {
        byte[] b = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) 0x03, (byte) 0x20, (byte) 0x00};
        return SendPacket.dataPkg(SendPacket.mergeIndex(SendPacket.pkgHeadInterface(b.length), b));
    }

    /*
     时间校对
     */
    public static byte[] timecollatin() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH) + 1;//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        byte[] b = new byte[]{(byte) 0x07, Utils.str2Bcd(Integer.toString(year))[0]
                , Utils.str2Bcd(Integer.toString(year))[1]
                , Utils.str2Bcd(Integer.toString(month))[0]
                , Utils.str2Bcd(Integer.toString(day))[0]
                , Utils.str2Bcd(Integer.toString(hour))[0]
                , Utils.str2Bcd(Integer.toString(minute))[0]
                , Utils.str2Bcd(Integer.toString(second))[0]
                , Utils.str2Bcd(Integer.toString(WeekOfYear))[0]};
        return SendPacket.dataPkg(SendPacket.mergeIndex(SendPacket.pkgHeadInterface(b.length), b));
    }

    /*
    获取控制卡信息
    */
    public static byte[] getControlCardInfo() {
        byte[] b = new byte[]{(byte) 0x01};
        return SendPacket.dataPkg(SendPacket.mergeIndex(SendPacket.pkgHeadInterface(b.length), b));
    }

    /*
     发送室内3.75扫描方式
    */
    public static byte[] getScanType1() {
        String str =//"ACACACACFEFF008001000A0000920FCA" +
                //  "ACACACACFEFF008001000A000152CECA" +
                "ACACACACFEFF008037010A000C320100004C4DDA070" +
                        "4011000100040000000000000000000010002000300040005" +
                        "0006000700080009000A000B000C000D000E000F001000110012001300" +
                        "1400150016001700180019001A001B001C001D001E001F00000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000005002000" +
                        "0000000000000000000000000000000000000000000000000" +
                        "0000000000000100000055558822CA"
                //    +"ACACACAC0080FEFF02000A000C003C97CA03"
                ;
        byte[] b = HexString2Bytes(str);
        return b;
    }

    /*
    发送室外10扫描方式
   */
    public static byte[] getScanType2() {
        String str =//"ACACACACFEFF008001000A0000920FCA\n" +
                //"ACACACACFEFF008001000A000152CECA\n" +
                "ACACACACFEFF008037010A000C320100004C4DDA07040104" +
                        "001000500000000000000000000000000000000100010001" +
                        "000100020002000200020003000300030003000400040004000400050" +
                        "005000500050006000600060006000700070007000700030002000100000003" +
                        "0002000100000003000200010000000300020001000000030002000100000003000200" +
                        "010000000300020001000000030002000100000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000" +
                        "00000000000000000000000040100000000000000000000000000000000000" +
                        "00000000000000000000000000000030004025555122CCA"
                //  "ACACACAC0080FEFF02000A000C003C97CA03"
                ;
        byte[] b = HexString2Bytes(str);
        return b;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节；
     * 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

}
