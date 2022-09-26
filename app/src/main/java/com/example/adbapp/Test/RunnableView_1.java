package com.example.adbapp.Test;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.adbapp.GoodDesign.ThreadRunnable;

public class RunnableView_1 extends ThreadRunnable {
    TextView textView;
    int count;

    public RunnableView_1(Handler handler, TextView textView,int count){
        super(handler);
        TAG = getClass().getCanonicalName();
        this.textView = textView;
        this.count = count;
    }

    @Override
    public void operation() {
        textView.setText(String.valueOf(count));
        Log.d(TAG, "operation: ");
    }
}
