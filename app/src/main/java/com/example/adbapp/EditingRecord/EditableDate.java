package com.example.adbapp.EditingRecord;

import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class EditableDate extends Editable{
    private TextView timeView;
    private DatePicker datePicker;

    public EditableDate(){
    }

    @Override
    void fillView() {
        timeView.setText("Последнее изменение: "+ContentView.getDateTime(record.getTime()));

        Calendar c = Calendar.getInstance();
        c.setTime(ContentView.getDateForDates(record.getName()));

        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                Date date = calendar.getTime();

                changing(ContentView.getDateForDates(date),record.getName());
            }
        });

    }

    @Override
    void findingViews() {
        datePicker = view.findViewById(R.id.datePicker);
        timeView = (TextView) view.findViewById(R.id.time);
    }

}
