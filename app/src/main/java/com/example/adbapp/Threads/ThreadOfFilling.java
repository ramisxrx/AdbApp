package com.example.adbapp.Threads;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.adbapp.FillingOfList.OverviewListFilling;


public class ThreadOfFilling extends Thread{

    private String TAG = OverviewListFilling.class.getCanonicalName();

    public interface Operations{
        void BG_Operations();
        void UI_Operations();
    }

    final Operations operations;
    public Handler handler;

    public ThreadOfFilling(Operations operations){
        this.handler = new Handler(Looper.getMainLooper());
        this.operations = operations;
    }

    @Override
    public void run(){

        Log.d(TAG, "run: Current thread="+Thread.currentThread());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        operations.BG_Operations();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        handler.post(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run: Current thread="+Thread.currentThread());

                operations.UI_Operations();

            }
        });
    }


}
