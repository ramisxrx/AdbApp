package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.adbapp.GoodDesign.HandlerThreadBG;
import com.example.adbapp.GoodDesign.ThreadRunnable;
import com.example.adbapp.Test.RunnableTest_1;
import com.example.adbapp.Test.RunnableView_1;

public class TestActivity extends AppCompatActivity {

    HandlerThreadBG handlerThreadBG;
    Handler handlerUI;

    ThreadRunnable runnable_1,runnableView_1;

    TextView textView;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = (TextView) this.findViewById(R.id.textView);

        handlerThreadBG = new HandlerThreadBG("BG_THREAD");
        handlerUI = new Handler(Looper.getMainLooper());

        runnable_1 = new RunnableTest_1(handlerThreadBG.handler,count);
        runnableView_1 = new RunnableView_1(handlerUI,textView,count);

        runnable_1.setNextRunnable(runnableView_1);
        runnableView_1.setNextRunnable(runnable_1);
        runnable_1.setNextRunnable(runnableView_1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        runnable_1.start();
    }
}