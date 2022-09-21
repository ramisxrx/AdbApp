package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HandlerThreadOfActions extends HandlerThread {
    private static final String TAG = "HandlerThreadOfActions";
    final Handler BG_handler, UI_handler;

    public HandlerThreadOfActions(String name) {
        super(name);
        start();
        BG_handler = new Handler(getLooper());
        UI_handler = new Handler(Looper.getMainLooper());
    }
    public HandlerThreadOfActions bg_operations(Runnable task){
        BG_handler.removeCallbacksAndMessages(null);
        BG_handler.post(task);
        return this;
    }
    public HandlerThreadOfActions ui_operations(Runnable task){
        UI_handler.post(task);
        return this;
    }
}
