package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunnable_3 {

    private static String TAG;

    private static final byte READY = 0;
    private static final byte RUNNING = 1;
    private static final byte COMPLETE = 2;
    private static final byte CANCEL = 3;

    private byte state;
    private boolean toCancel;

    private final List<ThreadRunnable_3> nextRunnableList, prevRunnableList;
    private final Handler handler;
    private final Action action;
    private boolean isComplete, cancelNext;
    private Runnable runnable,runnable2;
    private final String name;

    public ThreadRunnable_3(Handler handler, Action action, String name){
        this.handler = handler;
        this.action = action;
        this.name = name;

        nextRunnableList = new ArrayList<>();
        prevRunnableList = new ArrayList<>();
        state = READY;
        toCancel = false;
    }

    private void goNext(){
        boolean flag=true;
        for (ThreadRunnable_3 nextRunnable:nextRunnableList) {
            flag=false;
            nextRunnable.checkAndRun();
        }
        if(flag){
            setReadyAndNotToCancel();
        }
    }

    public void run(){
        Log.d(TAG, "run: ");
         runnable2 = () -> {
             if(state==READY) {
                 runRaw();
                 goNext();
             }
         };
         handler.post(runnable2);
    }

    private void checkAndRun(){
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ThreadRunnable_3 prevRunnable:prevRunnableList) {
                    if(!(prevRunnable.state==COMPLETE || prevRunnable.state==CANCEL))
                        return;
                }
                if(toCancel)
                    state = CANCEL;
                else
                    runRaw();
                goNext();
            }
        };
        handler.post(runnable);
    }

    private void runRaw(){
        state = RUNNING;
        if(action!=null)
            action.doAction();
        state = COMPLETE;
    }

    public void cancelNext(){
        Log.d(TAG, "cancelNext:");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!toCancel && state!=READY)
                    cancelNextRaw();
            }
        };
        handler.post(runnable);
    }

    private void cancelNextRaw(){
        Log.d(name, "cancelNextRaw: ");
        toCancel = true;
        for (ThreadRunnable_3 nextRunnable:nextRunnableList) {
            if(!nextRunnable.toCancel)
                nextRunnable.cancelNextRaw();
        }
    }

    public void setNextRunnable(ThreadRunnable_3 nextRunnable) {
        nextRunnableList.add(nextRunnable);
        nextRunnable.setPrevRunnable(this);
    }

    private void setPrevRunnable(ThreadRunnable_3 nextRunnable) {
        prevRunnableList.add(nextRunnable);
    }

    private void setReadyAndNotToCancel(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(name, "setReadyAndNotToCancel: ");
                toCancel = false;
                state = READY;
                for (ThreadRunnable_3 prevRunnable:prevRunnableList) {
                    if(prevRunnable.state!=READY || prevRunnable.toCancel)
                        prevRunnable.setReadyAndNotToCancel();
                }
            }
        };
        handler.post(runnable);
    }

}
