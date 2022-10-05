package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunnable_3 {

    protected static String TAG;

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
        if(runnable2!=null)
            handler.removeCallbacks(runnable2);
         runnable2 = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                runRaw();
            }
        };
        handler.post(runnable2);
    }

    private void checkAndRun(){
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ThreadRunnable_3 prevRunnable:prevRunnableList) {
                    if(!prevRunnable.isComplete)
                        return;
                }
                runRaw();
            }
        };
        handler.post(runnable);
    }

    private void runRaw(){
        if(action!=null && !isComplete)
            action.doAction();
        isComplete = true;
        goNext();
    }

    public void cancelNext(){
        Log.d(TAG, "cancelNext:");
        //handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);
        cancelNextRaw();
    }

    private void cancelNextRaw(){
        Log.d(name, "cancelNextRaw: ");
        cancelNext = true;
        if(isComplete){
            for (ThreadRunnable_3 nextRunnable:nextRunnableList) {
                nextRunnable.cancelNextRaw();
            }
            setNotCompletedAndNotCancel();
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
