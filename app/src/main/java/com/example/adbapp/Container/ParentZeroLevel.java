package com.example.adbapp.Container;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.Interfaces.Fillable;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import java.util.Date;

public class ParentZeroLevel extends ContainerRecord implements Fillable {
    private TextView nameView;
    private View view;

    public ParentZeroLevel(View view, FrameLayout frameLayout) {
        super(frameLayout,view);
        this.view = view;
        this.setFillable(this);
    }

    @Override
    public void fill(Record record) {
        nameView.setText(record.getName());
    }

    @Override
    void FindingNewViews() {
        nameView = (TextView) view.findViewById(R.id.name);
    }
}
