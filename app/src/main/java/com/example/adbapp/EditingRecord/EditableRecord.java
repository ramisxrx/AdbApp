package com.example.adbapp.EditingRecord;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import java.util.Date;

public class EditableRecord extends Editable{
    private TextView timeView;
    private EditText nameView;

    @Override
    void fillView() {
        nameView.setText(record.getName());
        timeView.setText("Последее изменение: "+ContentView.getDateTime(record.getTime()));
    }

    @Override
    void findingViews() {
        nameView = (EditText) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);
    }
}
