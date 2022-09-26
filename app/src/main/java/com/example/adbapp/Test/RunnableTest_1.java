package com.example.adbapp.Test;

import android.os.Handler;
import android.util.Log;

import com.example.adbapp.GoodDesign.ThreadRunnable;

public class RunnableTest_1 extends ThreadRunnable {
    private int count;

    public RunnableTest_1(Handler handler, int count) {
        super(handler);
        TAG = getClass().getCanonicalName();
        this.count = count;
    }

    @Override
    public void operation() {
        count++;
        Log.d(TAG, "operation: ");
    }

}
