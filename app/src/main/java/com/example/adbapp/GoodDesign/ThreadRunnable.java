package com.example.adbapp.GoodDesign;

import android.os.HandlerThread;

public abstract class ThreadRunnable implements Runnable{
    private HandlerThreads handlerThreads;

    public ThreadRunnable(HandlerThreads handlerThreads){
        this.handlerThreads = handlerThreads;
    }

    @Override
    public void run() {

    }

    abstract HandlerThreads operation();


    public HandlerThreads bg_operation(){
        handlerThreads.BG_handler.removeCallbacksAndMessages(null);
        handlerThreads.BG_handler.post(this);
        return handlerThreads;
    }

}
