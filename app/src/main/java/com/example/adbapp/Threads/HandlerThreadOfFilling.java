package com.example.adbapp.Threads;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;


public class HandlerThreadOfFilling extends HandlerThread {

    private static final String TAG = "HandlerThreadOfFilling";
    final Handler BG_handler, UI_handler;

    public HandlerThreadOfFilling(String name) {
        super(name);
        start();
        BG_handler = new Handler(getLooper());
        UI_handler = new Handler(Looper.getMainLooper());
    }
    public HandlerThreadOfFilling bg_operations(Runnable task){
        BG_handler.removeCallbacksAndMessages(null);
        BG_handler.post(task);
        return this;
    }
    public HandlerThreadOfFilling ui_operations(Runnable task){
        UI_handler.post(task);
        return this;
    }
}
