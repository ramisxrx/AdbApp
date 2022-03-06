package com.example.adbapp.AddingRecord;

import android.os.Build;
import android.widget.DatePicker;


import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class AddDateFragment extends AddRecordFragment{

    DatePicker datePicker;

    public AddDateFragment(int _type) {
        super(_type);
    }

    @Override
    protected void setStringAddContent() {
        stringAddContent = "Добавить новую дату";
    }

    @Override
    protected void initContainer() {
        datePicker = viewAddidable.findViewById(R.id.datePicker);
        setStringAddContent();
        saveButton.setText(stringAddContent);
        saveButton.setEnabled(true);
    }

    @Override
    protected void setListener() {
        datePicker.init(2020, 02, 01, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                Date date = calendar.getTime();

                foundList.ActionOfSearch(ContentView.getDateForDates(date));

                name_ToAdd = ContentView.getDateForDates(date);
                name_ToAdd = "Здесь должно быть дата!";
            }
        });
    }
}
