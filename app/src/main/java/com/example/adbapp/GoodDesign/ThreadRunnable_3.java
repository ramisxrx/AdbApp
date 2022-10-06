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
        isComplete = false;
        cancelNext = false;
        state = READY;
    }

    private void goNext(){
        boolean flag=true;
        if(cancelNext){
            setNotCompletedAndNotCancel();
            return;
        }
        for (ThreadRunnable_3 nextRunnable:nextRunnableList) {
            flag=false;
            nextRunnable.checkAndRun();
        }
        if(flag){
            setNotCompletedAndNotCancel();
        }
    }

    public void run(){
        Log.d(TAG, "run(out): ");

         runnable2 = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                runRaw();
                goNext();
            }
        };
        if(state==READY)
            handler.post(runnable2);
    }

    private void checkAndRun(){
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ThreadRunnable_3 prevRunnable:prevRunnableList) {
                    if(prevRunnable.state!=COMPLETE)
                        return;
                }
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
        //handler.removeCallbacks(runnable);
        //handler.removeCallbacks(runnable2);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
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

    private void setNotCompletedAndNotCancel(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(name, "setNotCompletedAndNotCancel: ");
                isComplete = false;
                cancelNext = false;
                for (ThreadRunnable_3 prevRunnable:prevRunnableList) {
                    prevRunnable.setNotCompletedAndNotCancel();
                }
            }
        };
        handler.post(runnable);
    }

}
