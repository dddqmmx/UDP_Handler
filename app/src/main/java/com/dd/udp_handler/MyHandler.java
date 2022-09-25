package com.dd.udp_handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

public class MyHandler extends Handler {
    public MyHandler(@NonNull Looper looper) {
        super(looper);
    }



    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        System.out.println(msg);
    }
}
