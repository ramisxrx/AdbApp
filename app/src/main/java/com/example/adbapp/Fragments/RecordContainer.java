package com.example.adbapp.Fragments;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import java.util.Date;

public class RecordContainer {

    private final FrameLayout frameLayout;
    private View view;
    private final FrameLayout.LayoutParams layoutParams;
    private final LayoutInflater inflater;
    private Record record;
    private TextView nameView,timeView;

    public RecordContainer(Context context, FrameLayout frameLayout) {
        this.inflater = LayoutInflater.from(context);
        this.frameLayout = frameLayout;
        layoutParams = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
    }

    public void FillingContainer(Record record,int viewType){
        this.record = record;
        if(view!=null)
            frameLayout.removeView(view);

        view = ContentView.getView(inflater,frameLayout,ContentView.getListItemViewType(record,viewType));
        //view.setLayoutParams(layoutParams);

        nameView = (TextView) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);

        switch (ContentView.getListItemViewType(record,viewType)){
            case 0:
                nameView.setText(record.getName());
                break;
            default:
                nameView.setText(record.getName());
                timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
                break;
        }
        frameLayout.addView(view);
    }

    public Record getRecord(){
        return this.record;
    }
}
