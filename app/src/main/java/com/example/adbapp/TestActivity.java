package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.adbapp.GoodDesign.Action;
import com.example.adbapp.GoodDesign.HandlerThreadBG;
import com.example.adbapp.GoodDesign.ThreadRunnable;
import com.example.adbapp.GoodDesign.ThreadRunnable_3;
import com.example.adbapp.Test.ActionForTest;
import com.example.adbapp.Test.RunnableTest_1;
import com.example.adbapp.Test.RunnableView_1;

public class TestActivity extends AppCompatActivity {

    String TAG = getClass().getCanonicalName();

    Handler handlerUI;

    ThreadRunnable_3 runnableUI_start,runnableUI_finish,runnable_1_1,runnable_2_1,runnable_test;

    TextView textView,textView2;
    int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = (TextView) this.findViewById(R.id.textView);
        textView2 = (TextView) this.findViewById(R.id.textView2);

        HandlerThreadBG handlerThreadBG_1 = new HandlerThreadBG("BG_THREAD_1");
        HandlerThreadBG handlerThreadBG_2 = new HandlerThreadBG("BG_THREAD_2");
        HandlerThreadBG handlerThreadBG_3 = new HandlerThreadBG("BG_THREAD_3");
        HandlerThreadBG handlerThreadBG_4 = new HandlerThreadBG("BG_THREAD_4");
        handlerUI = new Handler(Looper.getMainLooper());

        Action actionBG_1 = new Action() {
            @Override
            public void doAction() {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = 2;
                Log.d(TAG, "doAction: count="+String.valueOf(count));
            }
        };
        Action actionBG_2 = new Action() {
            @Override
            public void doAction() {
                try {
                    Thread.sleep(25000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = 3;
                Log.d(TAG, "doAction: count="+String.valueOf(count));
            }
        };
        Action actionUI_1 = new Action() {
            @Override
            public void doAction() {
                textView.setText(String.valueOf(count));
                count = 1;
            }
        };

        runnableUI_start = new ThreadRunnable_3(handlerUI,new ActionForTest(1,0));
        runnable_1_1 = new ThreadRunnable_3(handlerThreadBG_1.handler,new ActionForTest(2,10));
        ThreadRunnable_3 runnable_1_2 = new ThreadRunnable_3(handlerThreadBG_2.handler,new ActionForTest(3,8));
        runnable_2_1 = new ThreadRunnable_3(handlerThreadBG_1.handler,new ActionForTest(4,2));
        ThreadRunnable_3 runnable_2_2 = new ThreadRunnable_3(handlerThreadBG_2.handler,new ActionForTest(5,1));
        ThreadRunnable_3 runnable_2_3 = new ThreadRunnable_3(handlerThreadBG_3.handler,new ActionForTest(6,8));
        ThreadRunnable_3 runnable_2_4 = new ThreadRunnable_3(handlerThreadBG_4.handler,new ActionForTest(7,18));
        ThreadRunnable_3 runnable_3_1 = new ThreadRunnable_3(handlerThreadBG_1.handler,new ActionForTest(8,15));
        ThreadRunnable_3 runnable_3_2 = new ThreadRunnable_3(handlerThreadBG_2.handler,new ActionForTest(9,4));
        runnableUI_finish = new ThreadRunnable_3(handlerUI,new ActionForTest(10,0));

        runnableUI_start.setNextRunnable(runnable_1_1);
        runnableUI_start.setNextRunnable(runnable_1_2);

        runnable_1_1.setNextRunnable(runnable_2_1);
        runnable_1_2.setNextRunnable(runnable_2_2);
        runnable_1_2.setNextRunnable(runnable_2_3);
        runnable_1_2.setNextRunnable(runnable_2_4);

        runnable_2_1.setNextRunnable(runnable_3_1);
        runnable_2_2.setNextRunnable(runnable_3_1);
        runnable_2_3.setNextRunnable(runnable_3_2);
        runnable_2_4.setNextRunnable(runnable_3_2);

        runnable_3_1.setNextRunnable(runnableUI_finish);
        runnable_3_2.setNextRunnable(runnableUI_finish);

        runnable_test = new ThreadRunnable_3(handlerThreadBG_1.handler,new ActionForTest(2,10));
        ThreadRunnable_3 runnable_test2 = new ThreadRunnable_3(handlerThreadBG_1.handler,new ActionForTest(3,10));
        runnable_test.setNextRunnable(runnable_test2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnableUI_start.run();
                //runnable_test.run();
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnable_1_1.cancelNext();
                //runnable_test.cancelNext();
            }
        });

    }
}