package com.example.adbapp.Container;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.Interfaces.Fillable;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import java.util.Date;

public class ParentRecord implements Fillable {
    private TextView nameView,timeView;

    public ParentRecord(View view, FrameLayout frameLayout) {
        nameView = (TextView) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);
    }

    @Override
    public void fill(Record record) {
        nameView.setText(record.getName());
        timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
    }
}
