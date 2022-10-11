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
import com.example.adbapp.Test.ActionForTest;

public class TestActivity extends AppCompatActivity {

    String TAG = getClass().getCanonicalName();

    Handler handlerUI;

    ThreadRunnable runnableUI_start,runnableUI_finish,runnable_1_1,runnable_2_1,runnable_test;

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
        HandlerThreadBG handlerThreadBG_5 = new HandlerThreadBG("BG_THREAD_5");
        HandlerThreadBG handlerThreadBG_6 = new HandlerThreadBG("BG_THREAD_6");
        HandlerThreadBG handlerThreadBG_7 = new HandlerThreadBG("BG_THREAD_7");
        HandlerThreadBG handlerThreadBG_8 = new HandlerThreadBG("BG_THREAD_8");
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

        runnableUI_start = new ThreadRunnable(handlerUI,"runnableUI_start")
            .setActions(new ActionForTest("1.1",0));
        runnable_1_1 = new ThreadRunnable(handlerThreadBG_1.handler,"runnable_1_1")
                .setActions(new ActionForTest("2.1",10))
                .setActions(new ActionForTest("2.2",5));
        ThreadRunnable runnable_1_2 = new ThreadRunnable(handlerThreadBG_2.handler, "runnable_1_2")
                .setActions(new ActionForTest("3.1",4));
        runnable_2_1 = new ThreadRunnable(handlerThreadBG_3.handler,"runnable_2_1")
                .setActions(new ActionForTest("4.1",1))
                .setActions(new ActionForTest("4.2",5));
        ThreadRunnable runnable_2_2 = new ThreadRunnable(handlerThreadBG_4.handler,"runnable_2_2")
                .setActions(new ActionForTest("5.1",5))
                .setActions(new ActionForTest("5.2",6));
        ThreadRunnable runnable_2_3 = new ThreadRunnable(handlerThreadBG_5.handler,"runnable_2_3")
                .setActions(new ActionForTest("6.1",10));
        ThreadRunnable runnable_2_4 = new ThreadRunnable(handlerThreadBG_6.handler,"runnable_2_4")
                .setActions(new ActionForTest("7.1",7));
        ThreadRunnable runnable_3_1 = new ThreadRunnable(handlerThreadBG_7.handler,"runnable_3_1")
                .setActions(new ActionForTest("8.1",3))
                .setActions(new ActionForTest("8.2",6));
        ThreadRunnable runnable_3_2 = new ThreadRunnable(handlerThreadBG_8.handler,"runnable_3_2")
                .setActions(new ActionForTest("9.1",9));
        runnableUI_finish = new ThreadRunnable(handlerUI,"runnableUI_finish")
                .setActions(new ActionForTest("10.1",0));

        runnableUI_start.setNextRunnable(runnable_1_1)
            .setNextRunnable(runnable_1_2);

        runnable_1_1.setNextRunnable(runnable_2_1);
        runnable_1_2.setNextRunnable(runnable_2_2)
            .setNextRunnable(runnable_2_3)
            .setNextRunnable(runnable_2_4);

        runnable_2_1.setNextRunnable(runnable_3_1);
        runnable_2_2.setNextRunnable(runnable_3_1);
        runnable_2_3.setNextRunnable(runnable_3_2);
        runnable_2_4.setNextRunnable(runnable_3_2);

        runnable_3_1.setNextRunnable(runnableUI_finish);
        runnable_3_2.setNextRunnable(runnableUI_finish);
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
                runnableUI_start.cancelNext();
                //runnable_test.cancelNext();
            }
        });

    }
}