package com.example.adbapp.Container;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Date;

public class ParentText extends ContainerRecord {
    private TextView nameView,timeView;

    public ParentText(View view, FrameLayout frameLayout) {
        super(frameLayout,view);
    }


    @Override
    protected void FindingNewViews() {
        super.FindingNewViews();
        nameView = (TextView) view.findViewById(R.id.text);
        timeView = (TextView) view.findViewById(R.id.time);
    }


    @Override
    void fillView() {
        nameView.setText(record.getName());
        timeView.setText(ContentView.getDateTime(record.getTime()));
    }
}
