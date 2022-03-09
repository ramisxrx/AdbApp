package com.example.adbapp.EditingRecord;

import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

public class EditableDate extends Editable{
    private TextView timeView;
    private EditText nameView;

    public EditableDate(){
    }

    @Override
    void fillView() {
        nameView.setText(record.getName());
        timeView.setText("Последнее изменение: "+ContentView.getDateTime(record.getTime()));

        nameView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(android.text.Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changing(nameView.getText().toString(),record.getName());
            }
        });
    }

    @Override
    void findingViews() {
        nameView = (EditText) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);
    }

}
