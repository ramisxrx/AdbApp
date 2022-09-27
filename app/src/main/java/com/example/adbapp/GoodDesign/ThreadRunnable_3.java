package com.example.adbapp.GoodDesign;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunnable_3 implements Runnable{

    protected static String TAG;

    private final List<ThreadRunnable_3> nextRunnableList;
    private final Handler handler;
    private final Action action;

    public ThreadRunnable_3(Handler handler, Action action){
        this.handler = handler;
        this.action = action;
        TAG = getClass().getCanonicalName();

        nextRunnableList = new ArrayList<>();
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        if(action!=null)
            action.doAction();
        goNext();
    }


    public void goNext(){
        for (ThreadRunnable_3 nextRunnable:nextRunnableList) {
            nextRunnable.start();
        }
    }

    public void start(){
        handler.post(this);
    }

    public void setNextRunnable(ThreadRunnable_3 nextRunnable) {
        nextRunnableList.add(nextRunnable);
    }
}
