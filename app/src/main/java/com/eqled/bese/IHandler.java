package com.eqled.bese;

import android.os.Message;

public interface IHandler {
    /*
     * 	传递消息的接口
     */
    public void handleMessage(Message msg);
}