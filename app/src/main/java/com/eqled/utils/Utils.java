package com.eqled.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.eqled.eqledcontrol.R;

public class Utils {


    // 获取屏幕宽度度
    public static int getUiwidth(Activity at) {
        WindowManager manager = at.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        // int height = outMetrics.heightPixels;
        return width;
    }

    // 屏幕高度-状态栏高度

    public static int getUiheight(Activity at) {
        WindowManager manager = at.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        // int width= outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return height - getStatusHeight(at);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /*
     * bitmap 转存 图片到本地
     */
    public static void saveMyBitmap(Bitmap mBitmap, String bitName) {
        File f = new File(Environment.getExternalStorageDirectory().getPath()
                + "/" + bitName + ".png");
        Log.d("存储路径", f.toString());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	/*
       将bitmap  转换成 byte[]    高清重制版
	     colorstyle= 1; 双色屏，2 七彩屏
	 */

    public static int BitmapToByte(Bitmap bitmap, ArrayList<Byte> bytes, int nLeft, int nBorderBmpHeight) {
        int m_nColorStyle = 0;
        if (bitmap == null)
            return 0;
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        //左侧需要填充的宽度
        int nAlignLeft = nLeft % 8;
        //右侧需要填充的宽度
        int nAlignRight = (nLeft + bmWidth + nBorderBmpHeight * 2) % 8 > 0 ? 8 - (nLeft + bmWidth + nBorderBmpHeight * 2) % 8 : 0;
        //需要调整
        if (nAlignLeft > 0 || nAlignRight > 0 || nBorderBmpHeight > 0) {
            int nAdjustWidth = nAlignLeft + bmWidth + nAlignRight + nBorderBmpHeight * 2;
            int nAdjustHeight = bmHeight + nBorderBmpHeight * 2;
            //根据补齐后的尺寸创建一个空白的位图
            Bitmap AdjustBitmap = Bitmap.createBitmap(nAdjustWidth, nAdjustHeight, Bitmap.Config.ARGB_8888);
            AdjustBitmap.eraseColor(Color.RED);
            //将节目图片拷贝的上面创建的位图上，注意去掉边框的宽度
            Canvas canvas = new Canvas(AdjustBitmap);
            Paint paint = new Paint();
            canvas.drawBitmap(bitmap,nAlignLeft + nBorderBmpHeight, nBorderBmpHeight, paint);
            int ilen = GetBitmapBytes(AdjustBitmap, bytes, m_nColorStyle);
            return ilen;
        } else {
            return GetBitmapBytes(bitmap, bytes, m_nColorStyle);
        }
    }

    /*
      通过指针提取图片数据
     */
    public static int GetBitmapBytes(Bitmap bitmap, ArrayList<Byte> bytes, int ColorStyle) {
        bytes.clear();
        int nWidth = bitmap.getWidth();
        int nHeight = bitmap.getHeight();
        int clrColor = 0;
        for (int nY = 0; nY < nHeight; nY++) {
            byte byBitPost = 0;
            int byRedValue = 0;
            int byGreenValue = 0;
            int byBlueValue = 0;
            for (int nX = 0; nX < nWidth; nX++) {
                clrColor = bitmap.getPixel(nX, nY);
                byRedValue = byRedValue >> 1;
                byGreenValue = byGreenValue >> 1;
                byBlueValue = byBlueValue >> 1;
                if (Color.red(clrColor) > 128) {
                    //  Log.d("red",""+byRedValue);
                    byRedValue = 0x80 | byRedValue;
                    //  Log.d("或运算后",byRedValue+"");
                }
                if (Color.green(clrColor) > 128) {
                    byGreenValue = 0x80 | byGreenValue;
                }
                if (Color.blue(clrColor) > 128) {
                    byBlueValue = 0x80 | byBlueValue;
                }
                if (byBitPost >= 7) {
                    bytes.add((byte) byRedValue);  //如果int 超过byte 255的取值范围将出现bug
                    if (ColorStyle == 1)//双色
                    {
                        bytes.add((byte) byGreenValue);
                    } else if (ColorStyle == 2)//7色
                    {
                        bytes.add((byte) byGreenValue);
                        bytes.add((byte) byBlueValue);
                    }
                    byRedValue = 0;
                    byGreenValue = 0;
                    byBlueValue = 0;
                    byBitPost = 0;
                } else {
                    byBitPost++;
                }
            }

        }
        return bytes.size();
    }

    /**
     * 从一个byte[]数组中截取一部分
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) bs[i - begin] = src[i];
        return bs;
    }


    /**
     * 获取像素点的 红绿蓝
     */
    public int[] getPixelRgb(int pixel) {
        int[] rgb = new int[3];
        rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);


        return rgb;
    }

	/*
	  针对英文单词的分屏方法
	 */
    public static void SplitScreen(List<String> texts, String text, int windowWidth, Paint paint) {
        texts.clear();
        int index = 0; //字符宽度之和
        StringBuffer str = new StringBuffer();  //单屏字符串
        StringBuffer word = new StringBuffer(); //英语单词
        int state = 0; // 枚举   0  代表非英语字符   1 代表 英语字符
        for (int i = 0; i < text.length(); i++) {
            if (((text.charAt(i) <= 'Z' && text.charAt(i) >= 'A')     //126 是' 号， 46是.号
                    || (text.charAt(i) <= 'z' && text.charAt(i) >= 'a'))
                    || text.charAt(i) == 126 || text.charAt(i) == 46
                    || (text.charAt(i) <= 57 && text.charAt(i) >= 48)
                    || text.charAt(i) == ','
                    ) {    //数字
                state = 1;
                String s = text.charAt(i) + "";
                word.append(s);
                index += paint.measureText(word.toString());
                str.append(word.toString());
                word.delete(0, word.length());
                if (index > windowWidth) {
                    str.deleteCharAt(str.length() - 1);
                    texts.add(str.toString());
                    str.delete(0, str.length());
                    str.append(text.charAt(i) + "");
                    index = (int) paint.measureText(text.charAt(i) + "");
                }
                if (i == (text.length() - 1)) {
                    if ((paint.measureText(str.toString()) + paint.measureText(word.toString())) > windowWidth) {
                        //如果最后2个字符串部分大于屏幕宽度
                        if (!str.toString().equals("")) {
                            texts.add(str.toString());
                        }
                        String ing = "";
                        for (int y = 0; y < word.length(); y++) {//如果文本只有一个单词，并没有收尾没有空格的情况下，并且超出一屏幕的时候的处理方案
                            if ((paint.measureText(ing) + paint.measureText(String.valueOf(word.toString().charAt(y)))) < windowWidth) {
                                ing += String.valueOf(word.toString().charAt(y));
                            } else {
                                texts.add(ing);
                                ing = "";
                            }
                        }
                        texts.add(ing);
                    } else {
                        str.append(word.toString());
                        texts.add(str.toString());
                    }
                    break;
                } else {
                    continue;
                }
            } else if (state == 1) {
                if ((index + paint.measureText(word.toString())) > windowWidth) { //当英文单词超过边框长度
                    if (!str.toString().equals("")) {
                        texts.add(str.toString());
                    }
                    str.delete(0, str.length());
                    //存在单词超过屏幕宽度的情况，暂不考虑
                    index = (int) paint.measureText(word.toString());
                    if (index > windowWidth) {  //当单词超过屏幕宽度
                        String ing = "";
                        for (int y = 0; y < word.length(); y++) {
                            if ((paint.measureText(ing) + paint.measureText(String.valueOf(word.toString().charAt(y)))) < windowWidth) {
                                ing += String.valueOf(word.toString().charAt(y));
                            } else {
                                texts.add(ing);
                                ing = "";
                            }
                        }
                        str.append(ing);
                        word.delete(0, word.length());
                    } else {
                        str.append(word.toString());
                        word.delete(0, word.length());
                    }
                } else {
                    index += (int) paint.measureText(word.toString());
                    str.append(word.toString());
                    word.delete(0, word.length());
                }
                state = 0;
            }
            index = (int) (index + paint.measureText(text.charAt(i) + ""));
            str.append(text.charAt(i) + "");
            if (index > windowWidth) {
                str.deleteCharAt(str.length() - 1);
                texts.add(str.toString());
                str.delete(0, str.length());
                str.append((text.charAt(i) + ""));
                index = (int) paint.measureText(text.charAt(i) + "");
            }
            if (i == text.length() - 1) {
                texts.add(str.toString());
            }
        }
        Log.d("分屏bug测试", tos(texts));
    }

    public static String tos(List<String> texts) {
        String s = "";
        for (String a : texts) {
            s += a + "\n";
        }
        return s;
    }

    /*
    获取文本画笔
     */
    public static Paint getPaint(Context context, int size) {
        Paint paint = new Paint();
        //设置画笔属性
        paint.setFakeBoldText(false); // 字体是否加粗
        paint.setTextSize(size); // 字体大小 进度条参数
        paint.setColor(Color.YELLOW); // 字体颜色 选择的 button **
        //paint.setStrokeWidth(10f); // 比划宽度
        //paint.setFakeBoldText(true);
        //Typeface face = Typeface.createFromAsset (context.getAssets() , "fonts/st.ttc");
        //paint.setTypeface(face);

        return paint;
    }
	/*
	  更换字体
	 */

    public static void setTypeface(Context context, Paint paint, String fontsPath) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), fontsPath);
        paint.setTypeface(face);
    }

    /*
      获取用户选择的画笔大小
     */
    public static int getPaintSize(Context context, int text_size_position) {
        return Integer.parseInt(context.getResources().getStringArray(R.array.text_size)[text_size_position]) + 3;
    }


    /**
     * 获取字体高度
     */
    public static int getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /*
      获取字符串宽度
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * string 转bcd码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
            System.out.format("%02X\n", bbt[p]);
        }
        return bbt;
    }

    // 工具类：在代码中使用dp的方法（因为代码中直接用数字表示的是像素）
    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


    /**
     * 将dialog 宽度调整为宽度覆盖手机屏幕宽度
     */
    public static void setDialogW(Context context, Dialog builder) {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
        lp.width = (int) (Utils.getUiwidth((Activity) context)); //设置宽度
        lp.height = (int) (Utils.getUiheight((Activity) context) * 0.5f);
        builder.getWindow().setAttributes(lp);
    }


    /*基本原理是将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）。这样就可以直接例用length方法获得字符串的字节长度了*/
    public static int getWordCountRegex(String s) {
        return s.replaceAll("[^\\x00-\\xff]", "**").length();
    }

}
