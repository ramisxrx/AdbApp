package com.example.adbapp.Container;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import java.util.Date;

public class ParentZeroLevel extends ContainerRecord{
    private TextView nameView;

    public ParentZeroLevel(View view, FrameLayout frameLayout) {
        super(frameLayout,view);
    }

    @Override
    protected void FindingNewViews() {
        super.FindingNewViews();
        nameView = (TextView) view.findViewById(R.id.name);
    }


    @Override
    void fillView() {
        nameView.setText(record.getName());
    }
}
