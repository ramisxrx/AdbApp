package com.example.adbapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.RecordList.Record;

public class RecordContainer {

    private final FrameLayout frameLayout;
    private View view;
    private final FrameLayout.LayoutParams layoutParams;
    private final LayoutInflater inflater;
    private TextView nameView,timeView;

    RecordContainer(Context context,FrameLayout frameLayout) {
        this.inflater = LayoutInflater.from(context);
        this.frameLayout = frameLayout;
        layoutParams = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
    }

    public void FillingContainer(Record record,int viewType){
        if(view!=null)
            frameLayout.removeView(view);
        view = ContentView.getView(inflater,frameLayout,viewType);
        view.setLayoutParams(layoutParams);

        nameView = (TextView) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);

        switch (viewType){
            case 0:
                nameView.setText(record.getName());
                break;
            default:
                nameView.setText(record.getName());
                timeView.setText(String.valueOf(record.getTime()));
                break;
        }
        frameLayout.addView(view);
    }
}
