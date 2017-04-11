package com.eqled.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/10/28.
 */

public class StringUtils {
    /**
     * 将输入流转成字符串
     *
     * @param stream
     *            :传入的输入流
     * @param
     * @return
     */
    public static String getStringByInputStream(InputStream stream, String msg) {
        BufferedReader reader;
        StringBuilder response = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream, msg));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}

