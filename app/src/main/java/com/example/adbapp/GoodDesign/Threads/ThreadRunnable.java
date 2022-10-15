package com.example.adbapp.GoodDesign.Threads;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ThreadRunnable {

    private static final byte READY = 0;
    private static final byte RUNNING = 1;
    private static final byte COMPLETE = 2;
    private static final byte CANCEL = 3;

    private byte state;
    private boolean toCancel;

    private final List<ThreadRunnable> nextRunnableList, prevRunnableList;
    private final List<Action> actionList;
    private final Handler handler;
    private Runnable runnable;
    private final String name;

    public ThreadRunnable(Handler handler, String name){
        this.handler = handler;
        this.name = name;

        nextRunnableList = new ArrayList<>();
        prevRunnableList = new ArrayList<>();
        actionList = new ArrayList<>();
        state = READY;
        toCancel = false;
    }

    private void goNext(){
        boolean flag=true;
        for (ThreadRunnable nextRunnable:nextRunnableList) {
            flag=false;
            nextRunnable.checkAndRun();
        }
        if(flag){
            setReadyAndNotToCancel();
        }
    }

    public void run(){
        Log.d(name, "run: ");
         runnable = () -> {
             if(state==READY) {
                 runRaw();
                 goNext();
             }
         };
         handler.post(runnable);
    }

    private void checkAndRun(){
        runnable = () -> {
            for (ThreadRunnable prevRunnable:prevRunnableList) {
                if(!(prevRunnable.state==COMPLETE || prevRunnable.state==CANCEL))
                    return;
            }
            if(toCancel)
                state = CANCEL;
            else
                runRaw();
            goNext();
        };
        handler.post(runnable);
    }

    private void runRaw(){
        state = RUNNING;
        for (Action action:actionList) {
            action.doAction();
            if(toCancel){
                state = CANCEL;
                return;
            }
        }
        state = COMPLETE;
    }

    public void cancelNext(){
        Log.d(name, "cancelNext:");
        Runnable runnable = () -> {
            if(!toCancel && state!=READY)
                cancelNextRaw();
        };
        handler.post(runnable);
    }

    private void cancelNextRaw(){
        Log.d(name, "cancelNextRaw: ");
        toCancel = true;
        for (ThreadRunnable nextRunnable:nextRunnableList) {
            if(!nextRunnable.toCancel)
                nextRunnable.cancelNextRaw();
        }
    }

    private void setReadyAndNotToCancel(){
        Runnable runnable = () -> {
            Log.d(name, "setReadyAndNotToCancel: ");
            toCancel = false;
            state = READY;
            for (ThreadRunnable prevRunnable:prevRunnableList) {
                if(prevRunnable.state!=READY || prevRunnable.toCancel)
                    prevRunnable.setReadyAndNotToCancel();
            }
        };
        handler.post(runnable);
    }

    public ThreadRunnable setActions(Action action){
        actionList.add(action);
        return this;
    }

    public ThreadRunnable setNextRunnable(ThreadRunnable nextRunnable) {
        nextRunnableList.add(nextRunnable);
        nextRunnable.setPrevRunnable(this);
        return this;
    }

    public ThreadRunnable getNextRunnable(int number){
        return nextRunnableList.get(number);
    }

    private void setPrevRunnable(ThreadRunnable nextRunnable) {
        prevRunnableList.add(nextRunnable);
    }
}
