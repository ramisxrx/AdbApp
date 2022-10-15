package com.example.adbapp.GoodDesign;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.GoodDesign.Threads.Action;
import com.example.adbapp.GoodDesign.Threads.HandlerThreadBG;
import com.example.adbapp.GoodDesign.Threads.ThreadRunnable;
import com.example.adbapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecordListAdapter extends RecyclerView.Adapter<TestViewHolder> {

    private final String TAG = "RecordListAdapter";

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final HandlerThreadBG handlerThreadBG;
    private final Handler handler;
    private final List<ThreadRunnable> listStartRunnable;
    private int count=0;
    private int countCreateHolder=0;
    private int posStartRunnable=0;

    public RecordListAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        handlerThreadBG = new HandlerThreadBG("ThreadRLAdapter");
        handler = new Handler(Looper.getMainLooper());
        listStartRunnable = new ArrayList<>();
        for (int i=0;i<10;i++){
            listStartRunnable.add(i,new ThreadRunnable(handlerThreadBG.handler,"StartRunnable"+String.valueOf(i)));
            listStartRunnable.get(i).setNextRunnable(new ThreadRunnable(handler,"NextRunnable"+String.valueOf(i)));
        }
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        countCreateHolder++;
        Log.d(TAG, "onCreateViewHolder: num="+String.valueOf(countCreateHolder));

        View view = layoutInflater.inflate(R.layout.test_item,parent,false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        listStartRunnable.get(position).setActions(new Action() {
            @Override
            public void doAction() {
                count++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        listStartRunnable.get(position).getNextRunnable(0).setActions(new Action() {
            @Override
            public void doAction() {
                holder.textView.setText("Позиция="+String.valueOf(holder.getAdapterPosition())+" count="+String.valueOf(count));
            }
        });
        listStartRunnable.get(position).run();
        /*
        ThreadRunnable threadRunnable_start = new ThreadRunnable(handlerThreadBG.handler,"threadRunnable_start")
                .setActions(new Action() {
                    @Override
                    public void doAction() {
                        count++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        ThreadRunnable threadRunnable_1 = new ThreadRunnable(handler,"threadRunnable_1")
                .setActions(new Action() {
                    @Override
                    public void doAction() {
                        holder.textView.setText("Позиция="+String.valueOf(holder.getAdapterPosition())+" count="+String.valueOf(count));
                    }
                });
        threadRunnable_start.setNextRunnable(threadRunnable_1);

        threadRunnable_start.run();

         */
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
