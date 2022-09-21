package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HandlerThreads extends HandlerThread {

    final Handler BG_handler, UI_handler;

    public HandlerThreads(String name) {
        super(name);
        start();
        BG_handler = new Handler(getLooper());
        UI_handler = new Handler(Looper.getMainLooper());
    }
}
