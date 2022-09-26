package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.List;

public abstract class ThreadRunnable implements Runnable{

    protected static String TAG;

    private ThreadRunnable nextRunnable;
    private Handler handler;
    private List<Action> actionList;
    private int pointerToAction;

    public ThreadRunnable(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        operation();
        goNext();

    }

    public abstract void operation();

    public void goNext(){
        if(nextRunnable!=null){
            nextRunnable.start();
        }
    }

    public void start(){
        handler.post(this);
    }

    public void setNextRunnable(ThreadRunnable nextRunnable) {
        this.nextRunnable = nextRunnable;
    }

    private void setAction(Action action){
        actionList.add(action);
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

}
