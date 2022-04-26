package com.example.adbapp.EditingRecord;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class EditableDate extends Editable{
    private TextView timeView,nameView;
    private DatePicker datePicker;

    public EditableDate(){
    }

    @Override
    void fillView() {
        timeView.setText("Последнее изменение: "+ContentView.getDateTime(record.getTime()));

        Calendar c = Calendar.getInstance();
        c.setTime(ContentView.getDateForDates(record.getName()));
        /*
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                Date date = calendar.getTime();

                changing(ContentView.getDateForDates(date),record.getName());
            }
        });

         */
        nameView.setText(record.getName());
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                Date date = calendar.getTime();
                nameView.setText(ContentView.getDateForDates(date));
                changing(ContentView.getDateForDates(date),record.getName());
            }
        };


        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context,
                        dateSetListener,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    void findingViews() {
        nameView = (TextView) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);
    }

}
