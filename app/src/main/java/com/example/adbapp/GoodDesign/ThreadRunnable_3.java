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
    private boolean isComplete;

    public ThreadRunnable_3(Handler handler, Action action){
        this.handler = handler;
        this.action = action;
        TAG = getClass().getCanonicalName();

        nextRunnableList = new ArrayList<>();
        prevRunnableList = new ArrayList<>();
        isComplete = false;
    }

    public void goNext(){
        for (ThreadRunnable_3 nextRunnable:nextRunnableList) {
            nextRunnable.checkAndRun();
        }
    }

    public void run(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                runRaw();
            }
        };
        handler.post(runnable);
    }

    private void checkAndRun(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (ThreadRunnable_3 prevRunnable:prevRunnableList) {
                    if(!prevRunnable.getIsCompleted())
                        return;
                }
                runRaw();
            }
        };
        handler.post(runnable);
    }

    private void runRaw(){
        if(action!=null)
            action.doAction();
        isComplete = true;
        goNext();
    }

    public void setNextRunnable(ThreadRunnable_3 nextRunnable) {
        nextRunnableList.add(nextRunnable);
        nextRunnable.setPrevRunnable(this);
    }

    private void setPrevRunnable(ThreadRunnable_3 nextRunnable) {
        prevRunnableList.add(nextRunnable);
    }

    private void setIsComplete(boolean val){
        isComplete = val;
    }

    private boolean getIsCompleted(){
        return isComplete;
    }
}
