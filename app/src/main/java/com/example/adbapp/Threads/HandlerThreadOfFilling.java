package com.example.adbapp.Threads;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;


public class HandlerThreadOfFilling extends HandlerThread {

    private static final String TAG = "HandlerThreadOfFilling";

    private Handler BG_handler, UI_handler;

    public HandlerThreadOfFilling() {
        super(TAG);
        start();

        BG_handler = new Handler(getLooper());
        UI_handler = new Handler(Looper.getMainLooper());
    }

    public HandlerThreadOfFilling execute(Runnable task){
        BG_handler.post(task);
        return this;
    }

    public HandlerThreadOfFilling show(Runnable task){
        UI_handler.post(task);
        return this;
    }
}
