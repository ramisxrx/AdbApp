package com.example.adbapp.Container;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.Interfaces.JumpCommand;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import java.util.Date;

public class ParentRecord extends ContainerRecord {
    private TextView nameView,timeView;

    public ParentRecord(View view, FrameLayout frameLayout) {
        super(frameLayout,view);
    }


    @Override
    protected void FindingNewViews() {
        super.FindingNewViews();
        nameView = (TextView) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);
    }


    @Override
    void fillView() {
        nameView.setText(record.getName());
        timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
    }
}
