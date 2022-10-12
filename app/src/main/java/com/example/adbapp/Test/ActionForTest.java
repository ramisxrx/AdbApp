package com.example.adbapp.Test;

import android.util.Log;

import com.example.adbapp.GoodDesign.Threads.Action;

public class ActionForTest implements Action {
    private final String TAG = getClass().getCanonicalName();
    private final int pause;
    private final String number;

    public ActionForTest(String number, int pause) {
        this.number = number;
        this.pause = pause;
    }

    @Override
    public void doAction() {
        Log.d(TAG, "doAction: Start Action "+number);

        for(int i=1;i<=pause;i++){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Log.d(TAG, "Прошло "+String.valueOf(i)+" c "+" Action "+String.valueOf(number));
        }

        Log.d(TAG, "doAction: End Action "+number);
    }
}
