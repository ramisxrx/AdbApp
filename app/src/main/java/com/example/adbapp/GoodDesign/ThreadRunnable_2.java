package com.example.adbapp.GoodDesign;

import android.os.Handler;

import java.util.List;

public class ThreadRunnable_2 implements Runnable{

    protected static String TAG;

    private ThreadRunnable_2 nextRunnable;
    private final Handler handler;
    private List<Action> actionList;
    private int pointerToAction;

    public ThreadRunnable_2(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        actionList.get(pointerToAction).doAction();
        goNext();
    }

    public void goNext(){
        pointerToAction--;
        if(nextRunnable!=null && pointerToAction>=0){
            nextRunnable.start();
        }
    }

    public void start(){
        pointerToAction = 0;
        handler.post(this);
    }

    public void setNextRunnable(ThreadRunnable_2 nextRunnable,Action nextAction) {
        this.nextRunnable = nextRunnable;
        nextRunnable.setAction(nextAction);
    }

    public void setAction(Action action){
        actionList.add(action);
    }

}
