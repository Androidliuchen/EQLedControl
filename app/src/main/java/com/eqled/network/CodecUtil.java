package com.eqled.network;

/**
 * Created by Administrator on 2016/5/27.
 */


public class CodecUtil {
    static CRC16 crc16 = new CRC16();

    public static short bytes2short(byte[] bytes) {
        short s = (short) (bytes[1] & 0xFF);
        s |= (bytes[0] << 8) & 0xFF00;
        return s;
    }

    /*
     * 获取crc校验的byte形式
     */
    public static byte[] crc16Bytes(byte[] data, int dataSize) {
        return shortToByteArray(crc16Short(data, dataSize));
    }
    /*
     * 获取crc校验的short形式
     */
    public static short crc16Short(byte[] data, int dataSize) {
        return crc16.getCrcLen(data, dataSize);
    }

    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 节目数据文件头索引校验规则 ，所有的节目索引组成的数组
     *
     * @return
     */
    public static byte[] ProgramIndexCRC16(byte[] programIndexPkg) {
        int b = 0;
        for (int i = 0; i < programIndexPkg.length; i++) {
            b = b + (programIndexPkg[i] & 0xFF);
        }
        byte[] targets = new byte[2];
        targets[0] = (byte) shortToByteArray((short) b)[1];
        targets[1] = (byte) shortToByteArray((short) b)[0];

        return targets;
    }

}