package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.os.HandlerThread;

public abstract class ThreadRunnable implements Runnable{
    private Runnable nextRunnable;
    private Handler handler;

    public ThreadRunnable(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        operation();
        goNext();
    }

    abstract void operation();

    public void goNext(){
        if(nextRunnable!=null){
            handler.post(nextRunnable);
        }
    }

    public void start(){
        handler.post(this);
    }

    public void setNextRunnable(Runnable nextRunnable) {
        this.nextRunnable = nextRunnable;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

}
