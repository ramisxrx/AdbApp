package com.example.adbapp.Threads;

import android.os.Handler;
import android.os.Looper;


public class ThreadOfFilling extends Thread{

    public Handler handler;
    public Looper looperOfThread;

    @Override
    public void run(){
        Looper.prepare();

            looperOfThread  = Looper.myLooper();

        Looper.loop();
    }


}
