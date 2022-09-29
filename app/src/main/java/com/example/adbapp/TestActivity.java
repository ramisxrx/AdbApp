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
import com.example.adbapp.Test.RunnableTest_1;
import com.example.adbapp.Test.RunnableView_1;

public class TestActivity extends AppCompatActivity {

    String TAG = getClass().getCanonicalName();

    Handler handlerUI;

    ThreadRunnable_3 runnableUI_start_1,runnableUI_2_1;

    TextView textView;
    int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = (TextView) this.findViewById(R.id.textView);

        HandlerThreadBG handlerThreadBG_1 = new HandlerThreadBG("BG_THREAD_1");
        HandlerThreadBG handlerThreadBG_2 = new HandlerThreadBG("BG_THREAD_2");
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

        runnableUI_start_1 = new ThreadRunnable_3(handlerUI,actionUI_1);
        ThreadRunnable_3 runnableBG_1_1 = new ThreadRunnable_3(handlerThreadBG_1.handler,actionBG_1);
        ThreadRunnable_3 runnableBG_1_2 = new ThreadRunnable_3(handlerThreadBG_2.handler,actionBG_2);
        runnableUI_2_1 = new ThreadRunnable_3(handlerUI,actionUI_1);
        ThreadRunnable_3 runnableUI_2_2 = new ThreadRunnable_3(handlerUI,actionUI_1);

        runnableUI_start_1.setNextRunnable(runnableBG_1_1);
        runnableUI_start_1.setNextRunnable(runnableBG_1_2);
        runnableBG_1_1.setNextRunnable(runnableUI_2_1);
        runnableBG_1_2.setNextRunnable(runnableUI_2_2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnableUI_start_1.run();
            }
        });
    }
}