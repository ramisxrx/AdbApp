package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.os.HandlerThread;

public class HandlerThreadBG extends HandlerThread {

    public final Handler handler;

    public HandlerThreadBG(String name) {
        super(name);
        start();
        handler = new Handler(getLooper());
    }
}
