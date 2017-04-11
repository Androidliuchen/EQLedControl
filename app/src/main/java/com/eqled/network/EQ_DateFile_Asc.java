package com.eqled.network;

/**
 * Created by Administrator on 2016/7/27.
 *
 * 我也不知道这是什么， 用于时间窗组织数据包时，所需的固定参数
 *
 */
public class EQ_DateFile_Asc
{
    private   char  m_byAsc;   //字符
    private   int   m_wx;   //x坐标
    private   int  m_wy;  // y坐标
    private   int  m_byColor;  //颜色
    private   int  m_byVaild;

    public char getM_byAsc() {
        return m_byAsc;
    }

    public void setM_byAsc(char m_byAsc) {
        this.m_byAsc = m_byAsc;
    }

    public int getM_wx() {
        return m_wx;
    }

    public void setM_wx(int m_wx) {
        this.m_wx = m_wx;
    }

    public int getM_wy() {
        return m_wy;
    }

    public void setM_wy(int m_wy) {
        this.m_wy = m_wy;
    }

    public int getM_byColor() {
        return m_byColor;
    }

    public void setM_byColor(int m_byColor) {
        this.m_byColor = m_byColor;
    }

    public int getM_byVaild() {
        return m_byVaild;
    }

    public void setM_byVaild(int m_byVaild) {
        this.m_byVaild = m_byVaild;
    }
}
